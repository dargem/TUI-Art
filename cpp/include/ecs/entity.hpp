#pragma once

#include "cstdint"
#include "ecs/components.hpp"
#include "utils/sparse_set.hpp"

namespace ECS {

struct Entity {
    utils::ID index;     // used to lookup entity in entityMap
    uint32_t version{};  // incremented when an entity is destroyed
};

struct EntityLocation {
    // the archetype table this location refers to
    ArchetypeTable* table;
    // the row index in that referred archetype table
    uint32_t row;
};

struct Scene {
    utils::SparseSet<EntityLocation> entityMap;
};

}  // namespace ECS
