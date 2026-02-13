#include <gtest/gtest.h>
#include <cstddef>
#include <optional>
#include "tui/terminal_status.hpp"

#if defined(__linux__) || defined(__unix__)
#include <unistd.h>
#elif defined(_WIN32) || defined(_WIN64)
#include <io.h>
#endif

using tui::TerminalDimension;
using tui::TerminalDimensionListener;
using tui::TerminalStatus;

static bool is_stdout_tty()
{
#if defined(__linux__) || defined(__unix__)
    return ::isatty(STDOUT_FILENO);
#elif defined(_WIN32) || defined(_WIN64)
    return ::_isatty(_fileno(stdout));
#else
    return false;
#endif
}

namespace {

    struct ExampleTerminalListener : public TerminalDimensionListener
    {
        void receiveTerminalSize(TerminalDimension dimension) override {
            terminalDimension.value() = dimension;
        }

        std::optional<TerminalDimension> terminalDimension{std::nullopt};
    };

}

TEST(TerminalStatus, TerminalDimensionAccessible)
{
    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }
    TerminalStatus &terminalStatus{TerminalStatus::getInstance()};
    ExampleTerminalListener listener;
    terminalStatus.addDimensionListener(&listener);
    
    EXPECT_NO_THROW(TerminalDimension terminalDimension{terminalStatus.getTerminalDimension()}) << "Querying terminal dimension throws an error";
}

TEST(TerminalStatus, TerminalDimensionSizesLogical)
{
    constexpr static size_t MAX_WIDTH{1000};
    constexpr static size_t MAX_HEIGHT{1000};

    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }

    TerminalStatus &terminalStatus{TerminalStatus::getInstance()};

    TerminalDimension terminalDimension{terminalStatus.getTerminalDimension()};

    // if its larger than these its probably outputting some nonsense
    EXPECT_LE(terminalDimension.x, MAX_WIDTH);
    EXPECT_LE(terminalDimension.y, MAX_HEIGHT);
}