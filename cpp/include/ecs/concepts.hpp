#pragma once

#include <concepts>
#include <type_traits>

namespace ECS {

template <typename... Ts>
struct TypePack {};

namespace detail {
template <typename T>
struct IsTypePack : std::false_type {};

template <typename... Ts>
struct IsTypePack<TypePack<Ts...>> : std::true_type {};
}  // namespace detail

template <typename T>
concept TypePackType = detail::IsTypePack<T>::value;

// this basically takes in a variadic template, the first argument binds to T while the rest are
// packed in Td. Then it compares checking T isn't the same as anything in types through the fold
// expression. Then it ands this with a recursive call to itself using Ts to they're all unique.
template <typename T, typename... Ts>
inline constexpr bool areUniqueV = (!std::is_same_v<T, Ts> && ...) && areUniqueV<Ts...>;

// The base case for just one type
template <typename T>
constexpr bool areUniqueV<T> = true;

// Use at call site to require variadic template to be unique
template <typename... Ts>
concept UniqueTypes = areUniqueV<Ts...>;

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
concept OneOfPack = TypePackType<Pack> && detail::OneOfPackImpl<T, Pack>::value;

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
concept BoundedPacks = TypePackType<ElementsPack> && TypePackType<SetPack> &&
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
    TypePackType<PackA> && TypePackType<PackB> && detail::SameSizePacksImpl<PackA, PackB>::value;

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
concept IsPermutationPacks = TypePackType<PackA> && TypePackType<PackB> &&
                             detail::IsPermutationPacksImpl<PackA, PackB>::value;
}  // namespace ECS