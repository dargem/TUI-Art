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
    operator T&() const { return val; }
    // Returns a comparison category object
    auto operator<=>(const BasicComponent<Tag, T>&) const = default;
    auto operator<=>(const T& other) const { return val <=> BasicComponent<Tag, T>(other); }

    friend auto operator<=>(const BasicComponent<Tag, T>& lhs, const auto& rhs)
        requires std::three_way_comparable<T>
    {
        return lhs.val == rhs;
    }

    friend auto operator<=>(const auto& lhs, const BasicComponent<Tag, T>& rhs)
        requires std::three_way_comparable<T>
    {
        return lhs == rhs.val;
    }

    friend auto operator==(const BasicComponent<Tag, T>& lhs, const auto& rhs)
        requires std::equality_comparable<T>
    {
        return lhs.val == rhs;
    }

    friend auto operator==(const auto& lhs, const BasicComponent<Tag, T>& rhs)
        requires std::equality_comparable<T>
    {
        return lhs == rhs.val;
    }

    friend auto operator==(const BasicComponent<Tag, T>& lhs, const BasicComponent<Tag, T>& rhs)
        requires std::equality_comparable<T>
    {
        return lhs.val == rhs.val;
    }

   private:
    T val;
};

template <typename T>
concept Component = std::derived_from<std::remove_cvref_t<T>, ComponentTag>;

}  // namespace ECS