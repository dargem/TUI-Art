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

    tui::Style base{red, green, false};
    tui::Style baseReversed{green, red, false};

    tui::Cell cell{};
}