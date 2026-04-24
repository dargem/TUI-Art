#include "ecs/concepts.hpp"

#include "gtest/gtest.h"

namespace ECS {

TEST(ECSConceptTests, UniqueTypesFilters) {
    EXPECT_TRUE((IsUniquePackTypes<TypePack<int, bool, double, long, short, float>>))
        << "Should be true with many types if all unique";
    EXPECT_TRUE((IsUniquePackTypes<TypePack<double>>))
        << "Should be true with just one type as its unique then";

    // // EXPECT_FALSE((IsUniquePackTypes<TypePack<int, bool, double, long, double>>))
    // //     << "Should be false with repeated types";
    // // EXPECT_FALSE((IsUniquePackTypes<TypePack<int, long, int, double, double>>))
    // //     << "Should be false with repeated types";
}

TEST(ECSConceptTests, OneOfFilters) {
    EXPECT_TRUE((OneOfPack<int, TypePack<bool, double, long, int, float>>));
    EXPECT_TRUE((OneOfPack<float, TypePack<float, double>>));
    EXPECT_FALSE((OneOfPack<float, TypePack<int, double>>));
}

TEST(ECSConceptTests, OneOfPackFilters) {
    EXPECT_TRUE((OneOfPack<int, TypePack<bool, double, long, int, float>>));
    EXPECT_TRUE((OneOfPack<float, TypePack<float, double>>));
    EXPECT_FALSE((OneOfPack<float, TypePack<int, double>>));
}

TEST(ECSConceptTests, BoundPackFilters) {
    // Can also be used with more explicit type pack syntax
    EXPECT_TRUE((OneOfPack<int, TypePack<bool, double, long, int, float>>));
    EXPECT_TRUE((OneOfPack<float, TypePack<float, double>>));
    EXPECT_FALSE((OneOfPack<short, TypePack<float, double>>));
    EXPECT_TRUE((OneOfPack<short, TypePack<short>>));
}

// Helper class for a "realistic" example
template <typename... Ts>
class Foo {
   public:
    template <typename... Us>
    bool sameSize() {
        return SameSizePacks<TypePack<Ts...>, TypePack<Us...>>;
    }
};

TEST(ECSConceptTests, SameSize) {
    Foo<int, double, long> foo;
    EXPECT_TRUE((foo.sameSize<double, double, float>()));
    EXPECT_FALSE((foo.sameSize<float>()));

    EXPECT_TRUE((SameSizePacks<TypePack<float, bool, double>, TypePack<double, double, double>>));
    EXPECT_TRUE((SameSizePacks<TypePack<double>, TypePack<double>>));
    EXPECT_FALSE((SameSizePacks<TypePack<double, float>, TypePack<double>>));
}

TEST(ECSConceptTests, IsPermutationPack) {
    EXPECT_TRUE((IsPermutationPacks<TypePack<float, double>, TypePack<float, double>>));
    EXPECT_TRUE((IsPermutationPacks<TypePack<float, double>, TypePack<double, float>>));
    EXPECT_TRUE((IsPermutationPacks<TypePack<double, Foo<int>, float, short>,
                                    TypePack<Foo<int>, short, float, double>>));

    EXPECT_FALSE((IsPermutationPacks<TypePack<float, long, double>, TypePack<double, float>>));
    EXPECT_FALSE((IsPermutationPacks<TypePack<float, double, long>, TypePack<double, float, int>>));
    EXPECT_FALSE((IsPermutationPacks<TypePack<float, double, long>, TypePack<float, int>>));
    EXPECT_FALSE((IsPermutationPacks<TypePack<float>, TypePack<float, int>>));
}

TEST(ECSConceptTests, BoundedPack) {
    EXPECT_TRUE((BoundedPacks<TypePack<int, double, long>, TypePack<int, short, long, double>>));
    EXPECT_TRUE((BoundedPacks<TypePack<int>, TypePack<long, int, double, short>>));
    EXPECT_TRUE((BoundedPacks<TypePack<int, double>, TypePack<double, float, int>>));
    EXPECT_FALSE((BoundedPacks<TypePack<int, double, long>, TypePack<int, short, double, float>>));
    EXPECT_FALSE((BoundedPacks<TypePack<float, double>, TypePack<double>>));
}

TEST(ECSConceptTests, IsRead) {
    // checks IsRead properly filters
    EXPECT_TRUE((IsRead<int>)) << "Copy can't modify OG data so should be read";
    EXPECT_TRUE((IsRead<const int>)) << "Const is superfluous but should also be a read";
    EXPECT_FALSE((IsRead<int&>)) << "Non const reference is obv not just a read";
    EXPECT_TRUE((IsRead<const int&>)) << "you wouldn't use const cast would you";
}

TEST(ECSConceptTests, IsWrite) {
    // checks IsRead properly filters
    EXPECT_FALSE((IsWrite<int>)) << "Copy can't modify OG data so not a write";
    EXPECT_FALSE((IsWrite<const int>)) << "Const is superfluous, is also be not a write";
    EXPECT_TRUE((IsWrite<int&>)) << "Non const reference is a write";
    EXPECT_FALSE((IsWrite<const int&>)) << "you wouldn't use const cast would you";
}

}  // namespace ECS