#pragma once

#include <tuple>

#include "cstdint"
#include "ecs/archetypes.hpp"
#include "ecs/components.hpp"
#include "ecs/concepts.hpp"
#include "utils/sparse_set.hpp"

namespace ECS {

template <Component... Cs>
    requires UniqueTypes<Cs...>
struct Entity {
    utils::ID index;     // used to lookup entity in entityMap
    uint32_t version{};  // incremented when an entity is destroyed

    template <Component C>
        requires OneOf<C, Cs>
    C get() {
        // some holder logic
    }

    template <Component... Csearch>
        requires UniqueTypes<Csearch> && Bounded<Csearch, Cs>
    std::tuple<CSearch...> getComponents() {
        // get a tuple of its values
    }
};

/*
struct EntityLocation {
    // the archetype table this location refers to
    Table* table;
    // the row index in that referred archetype table
    uint32_t row;
};

struct Scene {
    utils::SparseSet<EntityLocation> entityMap;
};
*/

}  // namespace ECS
