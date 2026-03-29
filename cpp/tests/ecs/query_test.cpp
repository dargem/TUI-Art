#include "ecs/query.hpp"

#include <functional>
#include <tuple>

#include "ecs/components.hpp"
#include "gtest/gtest.h"

namespace ECS {

class A : public ComponentTag {};
class B : public ComponentTag {};

void testProcess(A, B){};

TEST(QueryTests, TemplateArgumentDeduction) {
    std::function<void(A, B)> function = testProcess;
    Query query1{function};
    Query query2(testProcess);
    Query query3{[](A componentA, B componentB) {}};

    EXPECT_TRUE((std::same_as<decltype(query1)::ArgsTuple, std::tuple<A, B>>));
    EXPECT_TRUE((std::same_as<decltype(query2)::ArgsTuple, std::tuple<A, B>>));
    EXPECT_TRUE((std::same_as<decltype(query3)::ArgsTuple, std::tuple<A, B>>));
}

}  // namespace ECS