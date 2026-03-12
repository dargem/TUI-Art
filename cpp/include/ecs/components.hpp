#pragma once

#include <concepts>
#include <cstdint>
#include <ranges>
#include <tuple>
#include <type_traits>
#include <vector>

namespace ECS {

// components
enum class Ordering : uint8_t { HEALTH = 0, DEFENSE, ATTACK, COUNT };

struct Health {
    constexpr static Ordering RANK{Ordering::HEALTH};
    float hitPoints{};
};

struct Attack {
    constexpr static Ordering RANK{Ordering::ATTACK};
    float attack{};
};

template <typename T>
concept Component = std::same_as<T, Health> || std::same_as<T, Attack>;

}  // namespace ECS