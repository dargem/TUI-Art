#pragma once

#include <concepts>
#include <type_traits>

namespace ECS {

template <typename... Ts>
struct TypePack {};

namespace detail {
template <typename T>
struct IsTypePackImpl : std::false_type {
    // calling ::value type trait forces full instantiation, SFINAE only catches error in immediate
    // context so the parameter substitution itself, once evaluating the members its past that
    // point. So this hard fails if it isn't a type pack which is wanted
    static_assert(false, "Must be a TypePack");
};

template <typename... Ts>
struct IsTypePackImpl<TypePack<Ts...>> : std::true_type {};
}  // namespace detail

template <typename T>
concept IsTypePack = detail::IsTypePackImpl<T>::value;

namespace detail {

template <typename T>
struct Base {};

// direct multi-inheritance from the same class is ill formed so we use a default lambda to abstract
// away the true base (the type) another layer down, so it will be falsy but not compile error
template <typename T, auto Differentiator = [] {}>
struct BaseClass : Base<T> {};

template <typename T>
struct UniqueTypesImpl {};

template <typename... Ts>
struct UniqueTypesImpl<TypePack<Ts...>> : BaseClass<Ts>... {};
}  // namespace detail

template <typename TypePack>
concept IsUniqueTypes =
    IsTypePack<TypePack> && std::is_standard_layout_v<detail::UniqueTypesImpl<TypePack>>;

template <typename T, typename... Ts>
concept OneOf = (std::is_same_v<T, Ts> || ...);

namespace detail {
template <typename T, typename Pack>
struct OneOfPackImpl : std::false_type {};

template <typename T, typename... Ts>
struct OneOfPackImpl<T, TypePack<Ts...>> : std::bool_constant<OneOf<T, Ts...>> {};
}  // namespace detail

// OneOf<T, TypePack<...>>, checks if T is in  the TypePack
template <typename T, typename Pack>
concept OneOfPack = IsTypePack<Pack> && detail::OneOfPackImpl<T, Pack>::value;

namespace detail {
template <typename ElementsPack, typename SetPack>
struct BoundedPacksImpl : std::false_type {};

template <typename... Elements, typename... Set>
struct BoundedPacksImpl<TypePack<Elements...>, TypePack<Set...>>
        : std::bool_constant<(OneOf<Elements, Set...> && ...)> {};
}  // namespace detail

// BoundedPacks<TypePack<...>, TypePack<...>>
// Checks if element of types in first pack are a subset of types in second pack
template <typename ElementsPack, typename SetPack>
concept BoundedPacks = IsTypePack<ElementsPack> && IsTypePack<SetPack> &&
                       detail::BoundedPacksImpl<ElementsPack, SetPack>::value;

namespace detail {
template <typename PackA, typename PackB>
struct SameSizePacksImpl : std::false_type {};

template <typename... A, typename... B>
struct SameSizePacksImpl<TypePack<A...>, TypePack<B...>>
        : std::bool_constant<(sizeof...(A) == sizeof...(B))> {};
}  // namespace detail

// SameSizePacks<TypePack<...>, TypePack<...>>
// Checks if the template packings are the same size
template <typename PackA, typename PackB>
concept SameSizePacks =
    IsTypePack<PackA> && IsTypePack<PackB> && detail::SameSizePacksImpl<PackA, PackB>::value;

namespace detail {
template <typename PackA, typename PackB>
struct IsPermutationPacksImpl : std::false_type {};

template <typename... A, typename... B>
struct IsPermutationPacksImpl<TypePack<A...>, TypePack<B...>>
        : std::bool_constant<SameSizePacks<TypePack<A...>, TypePack<B...>> && UniqueTypes<A...> &&
                             UniqueTypes<B...> && BoundedPacks<TypePack<A...>, TypePack<B...>>> {};
}  // namespace detail

// IsPermutationPacks<TypePack<...>, TypePack<...>>. Checks if both both template packings are
// made from the same templates, not necessarily in order If A and B are same size, both are unique,
// and all members of A are bounded in B it must be made from exact same components
template <typename PackA, typename PackB>
concept IsPermutationPacks =
    IsTypePack<PackA> && IsTypePack<PackB> && detail::IsPermutationPacksImpl<PackA, PackB>::value;

namespace detail {}

// template <typename PackA, typename PackB>
// concept IsUniqueTypes =

// If its a const reference or just a copy its just a read
// std::is_const_v would not work for const int&
// this is because its a non const reference, pointing to a const object
// aka it is low level const, but not high level const
// is_const_v only checks high level const
template <typename T>
concept IsRead = std::is_const_v<std::remove_reference_t<T>> ||
                 (!std::is_reference_v<T> && !std::is_rvalue_reference_v<T>);

// A non const reference is a write
template <typename T>
concept IsWrite = std::is_lvalue_reference_v<T> && !std::is_const_v<std::remove_reference_t<T>>;

template <typename T>
concept HasQueryInterface = requires(T t) {
    T::FuncDecayedReadArgTuple;
    T::FuncDecayedWriteArgTuple;
};

}  // namespace ECS