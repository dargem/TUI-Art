#pragma once

#include <tuple>

#include "cstdint"
#include "ecs/archetypes.hpp"
#include "ecs/components.hpp"
#include "ecs/concepts.hpp"

namespace ECS {

template <Component... Cs>
    requires IsUniquePackTypes<TypePack<Cs...>>
struct Entity {
    ID index;            // used to lookup entity in entityMap
    uint32_t version{};  // incremented when an entity is destroyed

    template <Component C>
        requires OneOfPack<C, TypePack<Cs...>>
    C get() {
        // some holder logic
    }

    template <Component... Csearch>
        requires IsUniquePackTypes<TypePack<Csearch...>> &&
                 BoundedPacks<TypePack<Csearch...>, TypePack<Cs...>>
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
