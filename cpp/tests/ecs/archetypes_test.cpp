#include "ecs/archetypes.hpp"

#include "ecs/components.hpp"
#include "gtest/gtest.h"

namespace ECS {

struct IntComponent : public ComponentTag {
    int value{5};
};

struct BoolComponent : public ComponentTag {
    bool value{true};
};

TEST(ArchetypesTest, pushBackEntities) {
    ArchetypeTable<IntComponent, BoolComponent> table;
    IntComponent a{};
    BoolComponent b{};
    ID id = table.pushBack(a, b);
}

}  // namespace ECS