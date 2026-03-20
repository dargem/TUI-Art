#pragma once

#include <cassert>
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
    [[nodiscard]]
    ID add() {
        // Emplace each vector with a new component
        auto emplaceBack = [](auto& vec) { vec.emplace_back(); };
        std::apply([&](auto&... vecs) { (emplaceBack(vecs), ...); }, data);

        return metadata[getDataSize() - 1].rid;
    }

    // Remove a component by index, does a swap and pop
    void erase(ID entityID) {
        assert(entityID < dataIndices.size() && "Out of bounds index");

        // Get relevant info
        const ID dataID = dataIndices[entityID];
        const ID lastDataID = getDataSize() - 1;
        const ID lastID = metadata[lastDataID].rid;
        // Update validity ID
        ++metadata[dataID].validityID;

        // Overwrite the object with object at the end, then pop it
        auto swapPop = [](auto& vec, size_t index) {
            assert(index < vec.size());
            vec[index] = std::move(vec.back());
            vec.pop_back();
        };

        // For each component (each nested in a vector) pop and swap it with the last component
        std::apply([&, dataID](auto&... vecs) { (swapPop(vecs, dataID), ...); }, data);
        // Then realign the data indices and metadata
        std::swap(metadata[dataID], metadata[lastDataID]);
        std::swap(dataIndices[entityID], dataIndices[lastID]);
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
    inline size_t getDataSize() { return std::get<0>(data).size(); }

    /**
     * @brief Makes a free slot returning an ID for use.
     *
     * @return ID
     */
    inline ID getFreeSlot() {
        const ID id = getFreeID();
        dataIndices[id] = getDataSize();
        return id;
    }

    inline ID getFreeID() {
        // Available from the free list
        if (metadata.size() > getDataSize()) {
            ++metadata[getDataSize()].validityID;
            return metadata[getDataSize()].rid;
        }

        // ID needs creation
        const ID newID = getDataSize();
        metadata.push_back({newID, {}});
        dataIndices.push_back(newID);
    }

    struct Metadata {
        // The reverse ID allowing retrieval of ID of the object from data vector
        ID rid{};
        // An identifier that is changed when object is erased, ensures a handle is still valid
        ID validityID{};
    };

    // sparse set keying ID's to the dense vectors's index (component data)
    std::vector<ID> dataIndices;

    // maps an index in data back to the id
    std::vector<Metadata> metadata;

    // tuple of dense vectors, where each vector holds a component type, SOA style
    std::tuple<std::vector<Cs>...> data;
};

}  // namespace ECS