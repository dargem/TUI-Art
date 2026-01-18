#pragma once

#include <string_view>

namespace tui {
    // Resets the colour of the next output
    constexpr std::string_view RESET_COLOUR{ "\033[0m" };
    // Moves the cursor to home
    constexpr std::string_view GO_HOME{ "\033[H" };
    // Use before sending RGB of foreground colour
    constexpr std::string_view SET_FOREGROUND_RGB{ "\033[38;2;" };
    // Use before sending RGB of background colour
    constexpr std::string_view SET_BACKGROUND_RGB{ "\033[48;2;" };
    // Clears the whole screen
    constexpr std::string_view CLEAR_SCREEN{ "\033[2J" };
};