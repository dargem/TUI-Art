#pragma once

#include <concepts>

namespace ECS {

// this basically takes in a variadic template, the first argument binds to T while the rest are
// packed in Td. Then it compares checking T isn't the same as anything in types through the fold
// expression. Then it ands this with a recursive call to itself using Ts to they're all unique.
template <typename T, typename... Ts>
inline constexpr bool are_unique_v = (!std::is_same_v<T, Ts> && ...) && are_unique_v<Ts...>;

// The base case for just one type
template <typename T>
constexpr bool are_unique_v<T> = true;

// Use at call site to require variadic template to be unique
template <typename... Ts>
concept UniqueTypes = are_unique_v<Ts...>;

// Checks if type T is one of Ts
template <typename T, typename... Ts>
concept OneOf = (std::same_as<T, Ts> || ...);

// Checks if element of types in first pack are a subset of types in second pack
template <typename... Elements, typename... Set>
concept Bounded = (OneOf<Elements, Set...> && ...);

}  // namespace ECS