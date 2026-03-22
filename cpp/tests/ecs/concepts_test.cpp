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

}  // namespace ECS