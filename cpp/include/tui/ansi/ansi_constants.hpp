#pragma once

#include <string_view>

namespace tui::ansi {

// ---------------------------------------------
// COLOURS

// Resets the colour of the next output
constexpr std::string_view RESET_COLOUR{ "\033[0m" };

// Use before sending RGB of foreground colour
constexpr std::string_view SET_FOREGROUND_RGB{ "\033[38;2;" };

// Use before sending RGB of background colour
constexpr std::string_view SET_BACKGROUND_RGB{ "\033[48;2;" };
// ---------------------------------------------
// CURSOR

// Moves the cursor to home
constexpr std::string_view GO_HOME{ "\033[H" };

// Hides the cursor
constexpr std::string_view HIDE_CURSOR{ "\u001b[?25l" };

// Shows the cursor
constexpr std::string_view SHOW_CURSOR{ "\u001b[?25h" };
// ---------------------------------------------
// OTHERS

// Clears the whole screen
constexpr std::string_view CLEAR_SCREEN{ "\033[2J" };

// Inserts a blank line, moving everything down a line
constexpr std::string_view INSERT_LINE{ "\u001b[L" };
};