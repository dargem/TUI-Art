#pragma once

#include <concepts>
#include <cstdint>
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

// To check if variadic templates are unique (no duplicate types)
template <class... Ts>
inline constexpr bool are_unique_v = are_unique<Ts...>::value;

template <typename T>
concept Component = std::same_as<T, Health> || std::same_as<T, Attack>;

// Use at call site to require variadic templalte to be unique
template <typename... Ts>
concept UniqueTypes = are_unique_v<Ts...>;

template <Component... Cs>
    requires UniqueTypes<Cs...>
struct ArchetypeTable {
    // tuple of vectors, where each vector holds a component type
    std::tuple<std::vector<Cs>...> cols;

    // get a specific component type
    template <Component C>
    std::vector<C>& column() {
        return std::get<std::vector<C>>(cols);
    }
};

}  // namespace ECS