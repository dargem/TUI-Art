#pragma once

#include "tui/terminal_status.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "utils/logger.hpp"

using tui::TerminalStatus;
using tui::ansi::Printer;
using utils::Logger;
using utils::LogLevel;

template <LogLevel logLevel = LogLevel::INFO>
class AppContext
{
public:
    AppContext()
        : terminalStatus{}, printer{terminalStatus}, logger{}
    {
    }

    [[nodiscard]]
    TerminalStatus &getTerminalStatus()
    {
        return terminalStatus;
    }

    [[nodiscard]]
    Printer &getPrinter()
    {
        return printer;
    }

    [[nodiscard]]
    Logger<logLevel> &getLogger()
    {
        return logger;
    }

private:
    TerminalStatus terminalStatus;
    Printer printer;
    Logger<logLevel> logger;
};