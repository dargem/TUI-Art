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

    ID sink{};
    // Should be able to do it in an orderless fashion since types unique
    // And should be happy to use template argument type deduction
    sink = table.pushBack(a, b);
    sink = table.pushBack(b, a);
    // Should take an rvalue as well as an lvalue happily
    sink = table.pushBack(BoolComponent{}, a);
}

}  // namespace ECS