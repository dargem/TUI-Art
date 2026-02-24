#include "core/types.hpp"

#include <gtest/gtest.h>

using types::Cell;
using types::GridLocation;
using types::RGB;
using types::Style;

TEST(TypesTest, ColourEquality) {
    RGB red1{255, 0, 0};
    RGB red2{255, 0, 0};
    RGB green{0, 255, 0};

    EXPECT_EQ(red1, red2)
        << "Colours with the same value even with different identity should be equal";
    EXPECT_NE(red1, green) << "Colours with different value should be unequal";
}

TEST(TypesTest, StyleEquality) {
    const RGB red{255, 0, 0};
    const RGB green{0, 255, 0};

    Style base{red, green, false};
    Style duplicate{red, green, false};
    Style diffBold{red, green, true};
    Style diffFg{green, green, false};
    Style diffBg{red, red, false};

    EXPECT_EQ(base, duplicate) << "Identical styles should be equal";
    EXPECT_NE(base, diffBold) << "Different bold attribute should make styles unequal";
    EXPECT_NE(base, diffFg) << "Different foreground constant should make styles unequal";
    EXPECT_NE(base, diffBg) << "Different background constant should make styles unequal";
}

TEST(TypesTest, CellEquality) {
    RGB red{255, 0, 0};
    RGB green{0, 255, 0};

    Style style{red, green, false};
    Style diffStyleProps{green, red, false};

    Cell base{style, ' '};
    Cell duplicate{style, ' '};
    Cell cellDiffStyle{diffStyleProps, ' '};
    Cell cellDiffChar{style, 'X'};

    EXPECT_EQ(base, duplicate) << "Identical cells should be equal";
    EXPECT_NE(base, cellDiffStyle) << "Different styles should make them cells unequal";
    EXPECT_NE(base, cellDiffChar) << "Different chars should make them need overwriting";
}

TEST(TypesTest, CellSize) {
    constexpr static size_t EIGHT_BYTES{8};
    Cell cell;

    // A cell should be exactly eight bytes for good alignment
    EXPECT_EQ(sizeof(cell), EIGHT_BYTES);
}

TEST(TypesTest, GridLocationEquality) {
    GridLocation base{5, 5};
    GridLocation copy{5, 5};
    GridLocation altered{5, 10};

    EXPECT_EQ(base, copy) << "Identical locations by value should be equal";
    EXPECT_NE(base, altered) << "Different locations should be not equal";
}