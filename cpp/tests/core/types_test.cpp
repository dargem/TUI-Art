#include "core/types.hpp"

#include <gtest/gtest.h>

#include <ostream>
#include <utility>

using types::Cell;
using types::GridLocation;
using types::Light;
using types::RGB;
using types::Style;
using types::ToneShift;

namespace types {

void PrintTo(const RGB& rgb, std::ostream* os) {
    *os << "RGB{";
    *os << "r=" << static_cast<int>(rgb.r);
    *os << ", g=" << static_cast<int>(rgb.g);
    *os << ", b=" << static_cast<int>(rgb.b);
    *os << "}";
}

void PrintTo(const ToneShift& ts, std::ostream* os) {
    *os << "ToneShift{";
    *os << "tone=";
    PrintTo(ts.getTone(), os);
    *os << ", shiftStrength=" << static_cast<int>(ts.getShiftStrength());
    *os << ", accumulatedWeight=" << ts.getAccumulatedWeight();
    *os << "}";
}

}  // namespace types

TEST(TypesTest, RGB_Equality) {
    RGB red1{255, 0, 0};
    RGB red2{255, 0, 0};
    RGB green{0, 255, 0};

    EXPECT_EQ(red1, red2)
        << "Colours with the same value even with different identity should be equal";
    EXPECT_NE(red1, green) << "Colours with different value should be unequal";
}

TEST(TypesTest, RGB_Iterable) {
    RGB colour{5, 25, 16};
    EXPECT_EQ(colour.colours.size(), 3);
    // shouldn't rely on [0] to access red but they should be overlaying since it uses a union
    EXPECT_EQ(colour.colours[0], colour.r);
    EXPECT_EQ(colour.colours[1], colour.g);
    EXPECT_EQ(colour.colours[2], colour.b);

    // check updates works
    colour.g = 160;
    EXPECT_EQ(colour.colours[1], colour.g);
    colour.colours[0] = 116;
    EXPECT_EQ(colour.colours[0], colour.r);
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

    // A cell should be exactly eight bytes for good alignment
    EXPECT_EQ(sizeof(std::declval<Cell>()), EIGHT_BYTES);
}

TEST(TypesTest, LightSize) {
    constexpr static size_t FOUR_BYTES{4};

    // A light should be exactly 4 bytes for good alignment
    EXPECT_EQ(sizeof(std::declval<Light>()), FOUR_BYTES);
}

TEST(TypesTest, LightPremultiplied) {
    RGB RGB_Initializer{50, 60, 70};

    {
        Light light(RGB_Initializer, 100);
        RGB RGB_Premultiplied = light.getPremultipliedRGB();
        EXPECT_NE(RGB_Premultiplied, RGB_Initializer)
            << "Shouldn't be equal if alpha is premultiplied and alpha != 255";

        for (size_t i{}; i < RGB_Premultiplied.colours.size(); ++i) {
            // should be smaller as alpha != 1 (when scaled 255 effectively)
            EXPECT_LT(RGB_Premultiplied.colours[i], RGB_Initializer.colours[i]);
        }
    }

    {
        Light light(RGB_Initializer, 255);
        RGB RGB_Premultiplied = light.getPremultipliedRGB();
        EXPECT_EQ(RGB_Premultiplied, RGB_Initializer)
            << "Should be equal since alpha == 255, premultiplied should be same as input";
    }

    {
        Light light(RGB_Initializer, 0);
        RGB RGB_Premultiplied = light.getPremultipliedRGB();
        EXPECT_EQ(RGB_Premultiplied, (RGB{0, 0, 0}))
            << "Should be equal since alpha = 0 its premultiplied to zero";
    }
}

TEST(TypesTest, LightBlends) {
    RGB RGB_Initializer{100, 100, 100};
    RGB RGB_Initializer_2{50, 70, 95};

    Light base{{}, 0};  // a blank base
    Light light{RGB_Initializer, 86};
    base.blend(light);  // blend the light with this

    EXPECT_EQ(light.getPremultipliedRGB(), base.getPremultipliedRGB())
        << "Should be additively put together, the base was originally empty so should be same";

    RGB oldRGB = base.getPremultipliedRGB();
    Light blankLight{RGB_Initializer, 0};  // should be blank since 0 alpha
    base.blend(blankLight);  // Shouldn't do anything since its a blank light being blended in
    EXPECT_EQ(oldRGB, base.getPremultipliedRGB())
        << "Should be equivalent since a blank light was added";
    // not really needed but to ensure tests are independent
    oldRGB = base.getPremultipliedRGB();
    base.blend({{RGB_Initializer_2}, 30});  // blend it with another light
    EXPECT_NE(oldRGB, base.getPremultipliedRGB()) << "Should be different after a blend";
}

TEST(TypesTest, LightBlendsOrderless) {
    Light A({60, 50, 60}, 75);
    Light B({30, 30, 0}, 120);
    Light C({7, 120, 1}, 5);

    Light AB = A;
    AB.blend(B);

    Light BA = B;
    BA.blend(A);

    EXPECT_EQ(AB, BA) << "Light blending should be commutative at least 2 ways";

    Light ABCC = A;
    ABCC.blend(B);
    ABCC.blend(C);
    ABCC.blend(C);

    Light CBCA = C;
    CBCA.blend(B);
    CBCA.blend(C);
    CBCA.blend(A);

    EXPECT_EQ(ABCC, CBCA) << "Light blending should be commutative 3+ ways";
}

TEST(TypesTest, LightApplies) {
    Light nothingLight({30, 30, 30}, 0);  // has no alpha so should do nothing

    Cell cell{};
    RGB lastFG = cell.style.fg;
    RGB lastBG = cell.style.bg;
    nothingLight.applyOn(cell);
    for (size_t i{}; i < lastFG.colours.size(); ++i) {
        EXPECT_EQ(cell.style.fg.colours[i], lastFG.colours[i]) << "Light should do nothing";
        EXPECT_EQ(cell.style.bg.colours[i], lastBG.colours[i]) << "Light should do nothing";
    }

    Light somethingLight({50, 50, 50}, 120);
    somethingLight.applyOn(cell);
    EXPECT_NE(lastFG, cell.style.fg);
    EXPECT_NE(lastFG, cell.style.bg);

    // the light applied should brighten it as its an additive proccess
    for (size_t i{}; i < lastFG.colours.size(); ++i) {
        EXPECT_GT(cell.style.fg.colours[i], lastFG.colours[i]);
        EXPECT_GT(cell.style.bg.colours[i], lastBG.colours[i]);
    }
}

TEST(TypesTest, ToneShiftSize) {
    static constexpr size_t TWENTY_BYTES{20};
    EXPECT_EQ(sizeof(std::declval<ToneShift>()), TWENTY_BYTES)
        << "ToneShift size should remain stable";
}

TEST(TypesTest, ToneShiftBlends) {
    constexpr uint8_t initialShift{100};

    ToneShift base{{50, 50, 50}, initialShift};
    ToneShift other{{50, 50, 50}, 100};

    RGB oldTone = base.tone;
    base.blend(other);
    EXPECT_EQ(oldTone, base.tone)
        << "Blending two toneshifts of equal tone should still have the same tone output";

    oldTone = base.tone;
    base.blend({{100, 200, 37}, 0});
    EXPECT_EQ(oldTone, base.tone)
        << "Blending with another tone of shiftStrength 0 should have no impacts";

    // midpoint blend should only assume equal weights if both ToneShifts are fresh.
    ToneShift midpointBase{{50, 50, 50}, initialShift};
    RGB midpointOldTone = midpointBase.tone;
    ToneShift modifier{{200, 200, 200}, initialShift};

    midpointBase.blend(modifier);
    for (size_t i{}; i < midpointOldTone.colours.size(); ++i) {
        int expectedMeet = (int(midpointOldTone.colours[i]) + modifier.tone.colours[i]) / 2;
        EXPECT_EQ(midpointBase.tone.colours[i], expectedMeet);
    }
}

TEST(TypesTest, ToneShiftOrderless) {
    ToneShift A{{50, 40, 55}, 73};
    ToneShift B{{52, 56, 51}, 120};
    ToneShift C{{152, 26, 121}, 90};

    ToneShift AB = A;
    AB.blend(B);
    ToneShift BA = B;
    BA.blend(A);

    EXPECT_EQ(AB, BA) << "Should be commutative two way";

    ToneShift ABC = A;
    ABC.blend(B);
    ABC.blend(C);

    ToneShift CAB = C;
    CAB.blend(A);
    CAB.blend(B);

    EXPECT_EQ(ABC, CAB) << "Should be three way commutative";
}

TEST(TypesTest, GridLocationEquality) {
    GridLocation base{5, 5};
    GridLocation copy{5, 5};
    GridLocation altered{5, 10};

    EXPECT_EQ(base, copy) << "Identical locations by value should be equal";
    EXPECT_NE(base, altered) << "Different locations should be not equal";
}