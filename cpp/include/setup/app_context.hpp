#pragma once

#include "tui/terminal_status.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "utils/logger.hpp"
#include "setup/configs.hpp"

class AppContext
{
public:
    AppContext()
        : terminalStatus{}, printer{terminalStatus}, logger{SetupConstants::LOG_LOCATION}
    {
    }

    [[nodiscard]]
    tui::TerminalStatus &getTerminalStatus()
    {
        return terminalStatus;
    }

    [[nodiscard]]
    tui::ansi::Printer &getPrinter()
    {
        return printer;
    }

    [[nodiscard]]
    SetupConstants::AppLogger &getLogger()
    {
        return logger;
    }

private:
    tui::TerminalStatus terminalStatus;
    tui::ansi::Printer printer;
    SetupConstants::AppLogger logger;
};