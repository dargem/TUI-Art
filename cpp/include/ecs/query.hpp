#pragma once

#include <concepts>
#include <functional>
#include <tuple>
#include <type_traits>
#include <utility>

#include "ecs/components.hpp"
#include "ecs/concepts.hpp"

namespace ECS {

namespace detail {
template <std::size_t I, typename Wanted, typename... Ts>
struct TupleIndexByDecayedTypeImpl;

template <std::size_t I, typename Wanted, typename First, typename... Rest>
struct TupleIndexByDecayedTypeImpl<I, Wanted, First, Rest...>
        : std::conditional_t<std::is_same_v<std::remove_cvref_t<First>, Wanted>,
                             std::integral_constant<std::size_t, I>,
                             TupleIndexByDecayedTypeImpl<I + 1, Wanted, Rest...>> {};

template <std::size_t I, typename Wanted>
struct TupleIndexByDecayedTypeImpl<I, Wanted> {
    static_assert(sizeof(Wanted) == 0,
                  "Type not found in tuple when reordering Query::execute args");
};

template <typename Wanted, typename Tuple>
struct TupleIndexByDecayedType;

template <typename Wanted, typename... Ts>
struct TupleIndexByDecayedType<Wanted, std::tuple<Ts...>>
        : TupleIndexByDecayedTypeImpl<0, Wanted, Ts...> {};

template <typename Wanted, typename Tuple>
constexpr decltype(auto) getByDecayedType(Tuple&& tuple) {
    using BareTuple = std::remove_reference_t<Tuple>;
    constexpr std::size_t index = TupleIndexByDecayedType<Wanted, BareTuple>::value;
    return std::get<index>(std::forward<Tuple>(tuple));
}
}  // namespace detail

template <typename T>
struct callable_traits;

// Function pointers: Ret(*)(Args...)
template <typename Ret, typename... Args>
struct callable_traits<Ret (*)(Args...)> {
    using signature = Ret(Args...);
    using returnType = Ret;
    using argsTuple = std::tuple<Args...>;
    using typePack = TypePack<Args...>;
    // remove const value and references qualifiers, not really std::decay but yk
    using decayedTypePack = TypePack<std::remove_cvref_t<Args>...>;
};

// Member function pointer (lambda/functor operator())
template <typename Ret, typename Class, typename... Args>
struct callable_traits<Ret (Class::*)(Args...) const> : callable_traits<Ret (*)(Args...)> {};

// functor / lambda
template <typename F>
struct callable_traits : callable_traits<decltype(&std::remove_reference_t<F>::operator())> {};

template <typename F>
    requires std::same_as<typename callable_traits<F>::returnType, void>
class Query {
   public:
    explicit Query(F f) : func(std::move(f)) {}
    using FuncArgTuple = callable_traits<F>::argsTuple;
    using FuncArgTypePack = callable_traits<F>::typePack;
    using FuncDecayedArgTypePack = callable_traits<F>::decayedTypePack;

    template <typename... Args>
        requires IsPermutationPacks<TypePack<std::remove_cvref_t<Args>...>, FuncDecayedArgTypePack>
    void execute(Args&&... args) {
        executeImpl(std::forward_as_tuple(std::forward<Args>(args)...), FuncArgTypePack{});
    }

   private:
    template <typename Tuple, typename... FuncArgs>
    void executeImpl(Tuple&& tuple, TypePack<FuncArgs...>) {
        func(
            detail::getByDecayedType<std::remove_cvref_t<FuncArgs>>(std::forward<Tuple>(tuple))...);
    }

    F func;
};

}  // namespace ECS