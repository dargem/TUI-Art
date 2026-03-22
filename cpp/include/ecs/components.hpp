#pragma once

#include <concepts>
#include <cstdint>
#include <ranges>
#include <tuple>
#include <type_traits>
#include <vector>

namespace ECS {

// To be a component it must inherit a ComponentTag. Should have 0 overhead due to empty base
// optimisation and it not relying on polymorphism at runtime.
struct ComponentTag {};

template <typename T>
concept Component = std::derived_from<std::remove_cvref_t<T>, ComponentTag>;

}  // namespace ECS