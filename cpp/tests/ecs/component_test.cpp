#include "ecs/components.hpp"
#include "gtest/gtest.h"

namespace ECS {

struct XTag {};
struct YTag {};
struct ZTag {};
using X = BasicComponent<XTag, float>;
using Y = BasicComponent<YTag, float>;
using Z = BasicComponent<ZTag, float>;

TEST(ComponentTests, BasicComponentFunctions) {
    // Should construct properly using floats
    X locX{3.7f};
    ASSERT_EQ(locX, 3.7f);

    Y locY{};
    ASSERT_EQ(locY, 0.0f);  // Should default construct fine
    ASSERT_EQ(locY, float{});

    Z loc{5.0f};
    Z loc_z{loc};
    ASSERT_EQ(loc_z, loc);

    loc = 7.0f;  // should be able to reassign like this
    loc_z = 3.0f;
    ASSERT_EQ(loc, 7.0f);
    ASSERT_EQ(loc, 3.0f);
    ASSERT_GT(loc, loc_z);
    ASSERT_LT(loc_z, loc);
}

struct TesterTagged : public ComponentTag {};
struct TesterUntagged {};

TEST(ComponentTests, ComponentConceptFilters) {
    ASSERT_TRUE(Component<TesterTagged>) << "Should be a component if given a component tag";
    ASSERT_FALSE(Component<TesterUntagged>) << "Tagless should not be considered a component";

    ASSERT_TRUE(Component<X>)
        << "Should be considered a component if a BasicComponent specialization";
}

}  // namespace ECS