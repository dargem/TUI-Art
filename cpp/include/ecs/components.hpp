#pragma once

#include <compare>
#include <concepts>
#include <cstdint>
#include <ranges>
#include <tuple>
#include <type_traits>
#include <vector>

namespace ECS {

// To be a component it must inherit a ComponentTag. Should have 0 overhead due to empty base
// optimisation and it not relying on polymorphism at runtime.
struct ComponentTag {
    auto operator<=>(const ComponentTag&) const = default;
};

// A simple utility for creating basic components, e.g. To have a archetype hold 2 doubles for
// something like x and y pos they cannot both be doubles since an archetype can't have multiple of
// the same type. A type alias does not solve this since its just an alias not a different type. So
// this allows creating a simple component like a X component that works like a given type (double)

template <typename Tag, typename T>
class BasicComponent : public ComponentTag {
   public:
    BasicComponent(T val) : val{val} {}
    BasicComponent() : val{T{}} {}
    operator T() const { return val; }
    operator T&() { return val; }
    operator const T&() const { return val; }
    // Returns a comparison category object
    auto operator<=>(const BasicComponent&) const = default;
    bool operator==(const BasicComponent&) const = default;

    friend auto operator<=>(const BasicComponent& lhs, const T& rhs)
        requires std::three_way_comparable<T>
    {
        return lhs.val <=> rhs;
    }

    friend auto operator<=>(const T& lhs, const BasicComponent& rhs)
        requires std::three_way_comparable<T>
    {
        return lhs <=> rhs.val;
    }

    friend bool operator==(const BasicComponent& lhs, const T& rhs)
        requires std::equality_comparable<T>
    {
        return lhs.val == rhs;
    }

    friend bool operator==(const T& lhs, const BasicComponent& rhs)
        requires std::equality_comparable<T>
    {
        return lhs == rhs.val;
    }

   private:
    T val;
};

template <typename T>
concept Component = std::derived_from<std::remove_cvref_t<T>, ComponentTag>;

}  // namespace ECS