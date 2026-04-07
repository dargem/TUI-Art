#pragma once

#include <cassert>
#include <concepts>
#include <cstdint>
#include <exception>
#include <ranges>
#include <tuple>
#include <type_traits>
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
    using ComponentTypePack = TypePack<Cs...>;

    // Add the components of an entity into the archetype table. Requirements statement ensures that
    // there must be one component passed in for each of the archetype table's components
    template <typename... Args>
        requires(Component<Args> && ...) &&
                IsPermutationPacks<TypePack<Cs...>, TypePack<std::remove_cvref_t<Args>...>>
    [[nodiscard]] ID pushBack(Args&&... components) {
        ID id = getFreeSlot();

        auto pushBack = [&](auto&& component) {
            using C = std::remove_cvref_t<decltype(component)>;
            std::get<std::vector<C>>(data).push_back(std::forward<decltype(component)>(component));
        };
        (pushBack(std::forward<Args>(components)), ...);

        return id;
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
        requires UniqueTypes<Csearch...> && BoundedPacks<TypePack<Csearch...>, TypePack<Cs...>>
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
        return newID;
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

template <typename T>
concept TableQueryable = requires(T t) {
    t.findRelevantTables();
    t.getTable();
};

template <typename T>
concept HasComponentTypePack = requires { typename std::remove_cvref_t<T>::ComponentTypePack; };

template <typename... Ts>
    requires(HasComponentTypePack<Ts> && ...)
class ArchetypeRegistry {
   public:
    /**
     * @brief Query the return type (tuple of tables) to find types. Don't call the function, find
     * the returned type to figure out which tables are relevant.
     *
     * @tparam ComponentQuery, (components the table must contain)
     * @return auto, tuple of archetype tables.
     */
    template <Component... ComponentQuery>
    auto findRelevantTables() {
        // nuclear option shouldn't be actually called, just used to find a type
        assert(false);
        std::terminate();
        return std::tuple_cat(
            std::conditional_t<
                BoundedPacks<TypePack<ComponentQuery...>, typename Ts::ComponentTypePack>,
                std::tuple<typename Ts::ComponentTypePack>, std::tuple<>>{}...);
    }

    template <typename Table>
        requires OneOfPack<Table, TypePack<Ts...>>
    auto getTable() {
        return std::get<Table>(tables);
    }

   private:
    std::tuple<Ts...> tables;
};

}  // namespace ECS