#include "ecs/concepts.hpp"

#include "gtest/gtest.h"

namespace ECS {

TEST(ECSConceptTests, UniqueTypesFilters) {
    EXPECT_TRUE((UniqueTypes<int, bool, double, long, short, float>))
        << "Should be true with many types if all unique";
    EXPECT_TRUE((UniqueTypes<double>)) << "Should be true with just one type as its unique then";

    EXPECT_FALSE((UniqueTypes<int, bool, double, long, double>))
        << "Should be false with repeated types";
    EXPECT_FALSE((UniqueTypes<int, long, int, double, double>))
        << "Should be false with repeated types";
}

TEST(ECSConceptTests, OneOfFilters) {
    EXPECT_TRUE((OneOf<int, bool, double, long, int, float>));
    EXPECT_TRUE((OneOf<float, float, double>));
    EXPECT_FALSE((OneOf<float, int, double>));
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
    EXPECT_FALSE((OneOfPack<short, short>)) << "Should fail if you forget to use TypePack syntax";
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
    EXPECT_FALSE((SameSizePacks<double, double>))
        << "Should be falsy if accidentally not providing a typepack for argument";
    EXPECT_TRUE((SameSizePacks<TypePack<double>, TypePack<double>>));
    EXPECT_FALSE((SameSizePacks<TypePack<double, float>, TypePack<double>>));
}

TEST(ECSConceptTests, SameCompositionPacks) {
    EXPECT_TRUE((SameCompositionPacks<TypePack<float, double>, TypePack<float, double>>));
    EXPECT_TRUE((SameCompositionPacks<TypePack<float, double>, TypePack<double, float>>));
    EXPECT_TRUE((SameCompositionPacks<TypePack<double, Foo<int>, float, short>,
                                      TypePack<Foo<int>, short, float, double>>));

    EXPECT_FALSE((SameCompositionPacks<TypePack<float, long, double>, TypePack<double, float>>));
    EXPECT_FALSE(
        (SameCompositionPacks<TypePack<float, double, long>, TypePack<double, float, int>>));
    EXPECT_FALSE((SameCompositionPacks<TypePack<float, double, long>, TypePack<float, int>>));
    EXPECT_FALSE((SameCompositionPacks<TypePack<float>, TypePack<float, int>>));
}

TEST(ECSConceptTests, BoundedPack) {
    EXPECT_TRUE((BoundedPacks<TypePack<int, double, long>, TypePack<int, short, long, double>>));
    EXPECT_TRUE((BoundedPacks<TypePack<int>, TypePack<long, int, double, short>>));
    EXPECT_TRUE((BoundedPacks<TypePack<int, double>, TypePack<double, float, int>>));
    EXPECT_FALSE((BoundedPacks<TypePack<int, double, long>, TypePack<int, short, double, float>>));
    EXPECT_FALSE((BoundedPacks<TypePack<float, double>, TypePack<double>>));
}

}  // namespace ECS