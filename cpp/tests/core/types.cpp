#include <gtest/gtest.h>
#include "core/types.hpp"

TEST(TypesTest, ColourEquality) {
    tui::Colour c1{255, 0, 0};
    tui::Colour c2{255, 0, 0};
    tui::Colour c3{0, 255, 0};
    
    EXPECT_TRUE(c1 == c2);
    EXPECT_FALSE(c1 == c3);
    EXPECT_FALSE(c2 == c3);
}

TEST(TypesTest, StyleEquality) {
    tui::Colour c1{255, 0, 0};
    tui::Colour c2{255, 0, 0};
    tui::Colour c3{0, 255, 0};

    tui::Style style1{c1, c2, false};
    tui::Style style2{c2, c1, false};

    EXPECT_TRUE(style1 == style2);

    style2.bold = true;

    EXPECT_FALSE(style1 == style2);

    style1.bold = true;

    EXPECT_TRUE(style1 == style2);

    style1.fg = c3;

    EXPECT_FALSE(style1 == style2);

    style2.fg = c3;

    EXPECT_TRUE(style1 == style2);
}