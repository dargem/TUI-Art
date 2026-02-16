#include <gtest/gtest.h>
#include <cstddef>
#include <optional>
#include "tui/terminal_status.hpp"
#include "app_context.hpp"

#if defined(__linux__) || defined(__unix__)
#include <unistd.h>
#elif defined(_WIN32) || defined(_WIN64)
#include <io.h>
#endif

using tui::TerminalDimension;
using tui::TerminalDimensionListener;
using tui::TerminalDimensionToken;
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

namespace
{

    struct ExampleTerminalListener : public TerminalDimensionListener
    {
        void receiveTerminalSize(TerminalDimension dimension) override
        {
            terminalDimension = dimension;
        }

        std::optional<TerminalDimension> terminalDimension{std::nullopt};
    };

}

TEST(TerminalStatus, TerminalDimensionQueryable)
{
    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }

    AppContext appContext;
    TerminalStatus &terminalStatus{appContext.getTerminalStatus()};

    EXPECT_NO_THROW(terminalStatus.queryTerminalSize()) << "Querying terminal dimension throws an error";
}

TEST(TerminalStatus, StatusAcceptsAndRemovesSubscribers)
{
    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }

    AppContext appContext;
    TerminalStatus &terminalStatus{appContext.getTerminalStatus()};
    // may not start at 0 due to printer subbing at startup of appcontext
    const size_t startingListeners{terminalStatus.getNumberListeners()};

    // add a listener
    ExampleTerminalListener listener;

    {
        TerminalDimensionToken token{terminalStatus.addDimensionListener(&listener)};
        EXPECT_TRUE(terminalStatus.getNumberListeners() == (startingListeners + 1)) << "Terminal status should gain exactly one listener after adding just one";

    } // token gets destroyed, unsubscribing it fom listeners
    EXPECT_TRUE(terminalStatus.getNumberListeners() == startingListeners) << "Terminal status should have original number of listeners after deleting access token";
}

TEST(TerminalStatus, StatusUpdatesOnlySubscribers)
{
    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }

    AppContext appContext;
    TerminalStatus &terminalStatus{appContext.getTerminalStatus()};
    ExampleTerminalListener listener;
    {
        TerminalDimensionToken token{terminalStatus.addDimensionListener(&listener)};
        EXPECT_FALSE(listener.terminalDimension.has_value()) << "Listener should start with no value";

        // check updates work
        terminalStatus.publishTerminalSize(); // should update listener with a terminal size
        EXPECT_TRUE(listener.terminalDimension.has_value()) << "Listener should have a value";

        // check unsubscribing stops updates, remove the listeners value
        listener.terminalDimension.reset();
    } // tokens lifetime ends
    terminalStatus.publishTerminalSize();
    EXPECT_FALSE(listener.terminalDimension.has_value()) << "Listener shouldn't be updated after unsubscribing";
}

TEST(TerminalStatus, TerminalDimensionSizesLogical)
{
    constexpr static size_t MAX_WIDTH{1000};
    constexpr static size_t MAX_HEIGHT{1000};

    if (!is_stdout_tty())
    {
        GTEST_SKIP() << "stdout is not a TTY; terminal size query is expected to fail";
    }

    AppContext appContext;
    TerminalStatus &terminalStatus{appContext.getTerminalStatus()};

    TerminalDimension terminalDimension{terminalStatus.queryTerminalSize()};
}