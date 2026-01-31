#include <gtest/gtest.h>
#include "core/types.hpp"

TEST(TypesTest, ColourEquality)
{
    tui::Colour red1{255, 0, 0};
    tui::Colour red2{255, 0, 0};
    tui::Colour green{0, 255, 0};

    EXPECT_EQ(red1, red2) << "Colours with the same value even with different identity should be equal";
    EXPECT_NE(red1, green) << "Colours with different value should be unequal";
}

TEST(TypesTest, StyleEquality)
{
    const tui::Colour red{255, 0, 0};
    const tui::Colour green{0, 255, 0};

    tui::Style base{red, green, false};
    tui::Style duplicate{red, green, false};
    tui::Style diffBold{red, green, true};
    tui::Style diffFg{green, green, false};
    tui::Style diffBg{red, red, false};

    EXPECT_EQ(base, duplicate) << "Identical styles should be equal";
    EXPECT_NE(base, diffBold) << "Different bold attribute should make styles unequal";
    EXPECT_NE(base, diffFg) << "Different foreground constant should make styles unequal";
    EXPECT_NE(base, diffBg) << "Different background constant should make styles unequal";
}

TEST(TypesTest, CellEquality)
{
    tui::Colour red{255, 0, 0};
    tui::Colour green{0, 255, 0};

    tui::Style style{red, green, false};
    tui::Style diffStyleProps{green, red, false};

    tui::Cell base{' ', style};
    tui::Cell duplicate{' ', style};
    tui::Cell cellDiffStyle{' ', diffStyleProps};
    tui::Cell cellDiffChar{'X', style};

    EXPECT_EQ(base, duplicate) << "Identical cells should be equal";
    EXPECT_NE(base, cellDiffStyle) << "Different styles should make them cells unequal";
    EXPECT_NE(base, cellDiffChar) << "Different chars should make them need overwriting";
}

TEST(TypesTest, CellSize)
{
    constexpr static size_t EIGHT_BYTES{8};
    tui::Cell cell;

    // A cell should be exactly eight bytes for good alignment
    EXPECT_EQ(sizeof(cell), EIGHT_BYTES);
}

TEST(TypesTest, GridLocationEquality)
{
    tui::GridLocation base{5, 5};
    tui::GridLocation copy{5, 5};
    tui::GridLocation altered{5, 10};

    EXPECT_EQ(base, copy) << "Identical locations by value should be equal";
    EXPECT_NE(base, altered) << "Different locations should be not equal";
}