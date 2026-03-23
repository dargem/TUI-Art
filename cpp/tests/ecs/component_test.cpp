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
}

}  // namespace ECS