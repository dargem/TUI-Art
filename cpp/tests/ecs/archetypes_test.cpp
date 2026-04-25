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

struct LongComponent : public ComponentTag {
    long value{5l};
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

TEST(ArchetypesTest, fetchComponents) {}

TEST(ArchetypeTest, hasComponentTypePack) {
    ArchetypeTable<IntComponent, BoolComponent> archetype;
    ASSERT_TRUE((std::same_as<decltype(archetype)::ComponentTypePack,
                              TypePack<IntComponent, BoolComponent>>));
    ASSERT_TRUE(HasComponentTypePack<decltype(archetype)>);
}

TEST(ArchetypeTest, archetypeRegistryConstructs) {
    // clang-format off
    ArchetypeRegistry<
        ArchetypeTable<IntComponent, BoolComponent>,
        ArchetypeTable<BoolComponent>,
        ArchetypeTable<IntComponent>
    > registry{};
    // clang-format on
}

TEST(ArchetypeTest, registryFindsRelevantTables) {
    // clang-format off
    ArchetypeRegistry<
        ArchetypeTable<IntComponent, BoolComponent>,
        ArchetypeTable<BoolComponent>,
        ArchetypeTable<IntComponent>
    > registry{};
    // clang-format on

    using MatchingIntTables = decltype(registry)::RelevantTablesComponents<IntComponent>;
    ASSERT_TRUE(
        (std::same_as<MatchingIntTables,
                      std::tuple<TypePack<IntComponent, BoolComponent>, TypePack<IntComponent>>>));

    using MatchingBoolTables = decltype(registry)::RelevantTablesComponents<BoolComponent>;
    ASSERT_TRUE(
        (std::same_as<MatchingBoolTables,
                      std::tuple<TypePack<IntComponent, BoolComponent>, TypePack<BoolComponent>>>));

    using MatchingLongTables = decltype(registry)::RelevantTablesComponents<LongComponent>;
    ASSERT_TRUE((std::same_as<MatchingLongTables, std::tuple<>>));
}

TEST(ArchetypeTest, registryGetsTables) {
    // clang-format off
    ArchetypeRegistry<
        ArchetypeTable<IntComponent, BoolComponent>,
        ArchetypeTable<BoolComponent>,
        ArchetypeTable<IntComponent>
    > registry{};
    // clang-format on

    ASSERT_TRUE((std::same_as<decltype(registry.getRelevantTables<IntComponent>()),
                              std::tuple<ArchetypeTable<IntComponent, BoolComponent>&,
                                         ArchetypeTable<IntComponent>&>>));

    ASSERT_TRUE((std::same_as<decltype(registry.getRelevantTables<BoolComponent>()),
                              std::tuple<ArchetypeTable<IntComponent, BoolComponent>&,
                                         ArchetypeTable<BoolComponent>&>>));

    ASSERT_TRUE(
        (std::same_as<decltype(registry.getRelevantTables<LongComponent>()), std::tuple<>>));
}

}  // namespace ECS