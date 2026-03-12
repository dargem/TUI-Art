#pragma once

#include <concepts>
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
struct ArchetypeTable {
    // tuple of sparse sets, where each sparse set holds a component type, SOA style
    std::tuple<utils::SparseSet<Cs>...> cols;

    // get an unordered vector of a specific component type
    template <Component C>
        requires(std::same_as<C, Cs> || ...)  // table must actually have it
    std::vector<C>& column() {
        return std::get<utils::SparseSet<C>>(cols).getData();
    }

    // get a zipped view of multiple components
    template <Component... Csearch>
        requires UniqueTypes<Csearch...> && Bounded<Csearch..., Cs...>
    auto columns() {
        return std::views::zip(std::get<utils::SparseSet<Csearch>>(cols).getData()...);
    }
};

struct AccessManager {};

}  // namespace ECS