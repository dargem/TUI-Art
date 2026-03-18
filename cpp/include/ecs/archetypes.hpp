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

using ID = uint32_t;

// Used to hold archetypes (component groupings)
template <Component... Cs>
    requires UniqueTypes<Cs...>
class ArchetypeTable {
   public:
    // Add the components of an entity into the archetype table
    ID add(auto& holderEntity) { return 5; }  // placeholder

    // Remove a component by index, does a swap and pop
    void remove(ID entityID) {
        auto swapPop = [](auto& vec, size_t index) {
            assert(index < vec.size());
            vec[index] = std::move(vec.back());
            vec.pop_back();
        };

        size_t dataIndex = dataIndices[entityID];
        // For each component (each nested in its own vector) pop and swap it with the last
        // component
        std::apply([&, dataIndex](auto&... vecs) { (swapPop(vecs, dataIndex), ...); }, data);

        // Swap around the IDIndices of the back and [dataIndices] to keep consistency
        size_t backIDIndex = IDIndices.back();
        IDIndices.back() = IDIndices[dataIndex];
        IDIndices[dataIndex] = backIDIndex;
    }

    // get an unordered vector of a specific component type
    template <Component C>
        requires OneOf<C, Cs...>
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
    // sparse set keying ID's to the dense vectors's index (component data)
    std::vector<ID> dataIndices;

    // maps an index in data back to the id
    std::vector<ID> IDIndices;

    // tuple of dense vectors, where each vector holds a component type, SOA style
    std::tuple<std::vector<Cs>...> data;
};

struct AccessManager {};

}  // namespace ECS