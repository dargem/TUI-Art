#pragma once

#include <concepts>
#include <functional>
#include <type_traits>

#include "ecs/components.hpp"

namespace ECS {

template <typename T>
struct callable_traits;

// Function pointers: Ret(*)(Args...)
template <typename Ret, typename... Args>
struct callable_traits<Ret (*)(Args...)> {
    using signature = Ret(Args...);
    using returnType = Ret;
    using argsTuple = std::tuple<Args...>;
};

// Member function pointer (lambda/functor operator())
template <typename Ret, typename Class, typename... Args>
struct callable_traits<Ret (Class::*)(Args...) const> : callable_traits<Ret (*)(Args...)> {};

// functor / lambda
template <class F>
struct callable_traits : callable_traits<decltype(&std::remove_reference_t<F>::operator())> {};

template <typename F>
    requires std::same_as<typename callable_traits<F>::returnType, void>
class Query {
   public:
    explicit Query(F f) : func(std::move(f)) {}
    using ArgsTuple = callable_traits<F>::argsTuple;

    
    void execute(ArgsTuple args) { std::apply(func, args); }

   private:
    F func;
};

/*
template <Component... Cs>
class Query {
   public:
    Query(std::function<void(Cs...)> process) : process{process} {}

   private:
    std::function<void(Cs...)> process;
};
*/

}  // namespace ECS