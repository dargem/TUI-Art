#include "ecs/query.hpp"

#include <functional>

#include "ecs/components.hpp"
#include "gtest/gtest.h"

namespace ECS {

class A : public ComponentTag {};
class B : public ComponentTag {};

void testProcess(A, B){};

TEST(QueryTests, TemplateArgumentDeduction) {
    std::function<void(A, B)> function = testProcess;
    Query query{function};
    // Query query(testProcess);
}

}  // namespace ECS