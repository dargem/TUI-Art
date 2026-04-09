#include "ecs/query.hpp"

#include <format>
#include <functional>
#include <tuple>

#include "ecs/components.hpp"
#include "ecs/concepts.hpp"
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

    EXPECT_TRUE((std::same_as<decltype(query1)::FuncArgTuple, std::tuple<A, B>>));
    EXPECT_TRUE((std::same_as<decltype(query1)::FuncArgTypePack, TypePack<A, B>>));

    EXPECT_TRUE((std::same_as<decltype(query2)::FuncArgTuple, std::tuple<A, B>>));
    EXPECT_TRUE((std::same_as<decltype(query2)::FuncArgTypePack, TypePack<A, B>>));

    EXPECT_TRUE((std::same_as<decltype(query3)::FuncArgTuple, std::tuple<A, B>>));
    EXPECT_TRUE((std::same_as<decltype(query3)::FuncArgTypePack, TypePack<A, B>>));
}

TEST(QueryTests, TemplateArgumentDeductionForDecay) {
    Query query{[](A a, B& b) {}};

    EXPECT_TRUE((std::same_as<decltype(query)::FuncDecayedArgTypePack, TypePack<A, B>>))
        << "Should decay the reference";
}

class PosTag;
class VelTag;
class Position : public BasicComponent<PosTag, float> {};
class Velocity : public BasicComponent<VelTag, float> {};

TEST(QueryTests, TemplateArgumentDeductionForExecutionDecay) {
    Query query{[](A a, B& b) {}};

    EXPECT_TRUE((std::same_as<decltype(query)::FuncDecayedArgTypePack, TypePack<A, B>>))
        << "Should decay the reference";
}

TEST(QueryTests, QueryReadArgExtracts) {
    Query query{[](A a, B& b, const Position& pos, const Velocity v) {}};

    EXPECT_TRUE((
        std::same_as<decltype(query)::FuncDecayedReadArgTuple, std::tuple<A, Position, Velocity>>));
}

TEST(QueryTests, QueryWriteArgsExtracts) {
    Query query{[](A a, B& b, const Position& pos, Velocity& v) {}};
    EXPECT_TRUE((std::same_as<decltype(query)::FuncDecayedWriteArgTuple, std::tuple<B, Velocity>>));
}

TEST(QueryTests, RunningQueryWorks) {
    auto updatePosition = [](Velocity v, Position& p) { p += v; };
    Query query{updatePosition};
    Position pos{5.0f};
    Position ogPos{5.0f};
    Velocity vel{1.0f};

    EXPECT_EQ(pos, 5.0f);
    query.execute(vel, pos);

    EXPECT_NE(pos, 5.0f);

    EXPECT_EQ(pos, (float)ogPos + (float)vel);

    EXPECT_NE(pos, ogPos) << "Query execution should modify original position";
    updatePosition(vel, ogPos);
    EXPECT_EQ(pos, ogPos);  // It should be running the given lambda
}

TEST(QueryTests, UnorderedQueryRunning) {
    auto updatePosition = [](Velocity v, Position& p) { p += v; };
    Query query{updatePosition};
    Position pos{5.0f};
    Velocity vel{1.0f};

    query.execute(pos, vel);
    EXPECT_EQ(pos, 6.0f);

    query.execute(vel, pos);
    EXPECT_EQ(pos, 7.0f);
}

}  // namespace ECS