#pragma once

#include <concepts>
#include <cstdint>
#include <ranges>
#include <tuple>
#include <vector>

#include "ecs/components.hpp"
#include "ecs/concepts.hpp"
#include "utils/sparse_set.hpp"

namespace ECS {

// Used to hold archetypes (component groupings)
template <Component... Cs>
    requires UniqueTypes<Cs...>
class ArchetypeTable {
   public:
    // sparse set mapping ID's to the dense set (component data)
    // std::vector<size_t> sparseArray; // maybe do this another way actually

    // tuple of dense sets, where each vector holds a component type, SOA style
    std::tuple<std::vector<Cs>...> data;

    // Remove a component by index, does a swap and pop
    void remove(size_t index) {
        // For each vector in the components tuple do a swap and pop of the data
        swapPop<Cs...>(index);
    }

    // get an unordered vector of a specific component type
    template <Component C>
        requires OneOf<C, Cs>
    std::vector<C>& column() {
        return std::get<std::vector<C>>(data);
    }

    // get a zipped view of multiple components
    template <Component... Csearch>
        requires UniqueTypes<Csearch...> && Bounded<Csearch..., Cs...>
    auto columns() {
        return std::views::zip(std::get<std::vector<Csearch>>(data)...) | std::views::reverse;
    }

   private:
    template <Component C>
        requires OneOf<C, Cs...>
    void swapPop(size_t index) {
        std::vector<C>& matchedVector = std::get<std::vector<C>>(data);
        // Pops + moves the last element into this new index, overwriting the element there
        matchedVector[index] = std::move(matchedVector.pop_back());
    }
};

struct AccessManager {};

}  // namespace ECS