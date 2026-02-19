#pragma once

#include "setup/configs.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "tui/terminal_status.hpp"
#include "utils/logger.hpp"

class AppContext {
   public:
    AppContext() : terminalStatus{}, printer{terminalStatus}, logger{setup::LOG_LOCATION} {}

    [[nodiscard]]
    tui::TerminalStatus& getTerminalStatus() {
        return terminalStatus;
    }

    [[nodiscard]]
    tui::ansi::Printer& getPrinter() {
        return printer;
    }

    [[nodiscard]]
    Setup::AppLogger& getLogger() {
        return logger;
    }

   private:
    tui::TerminalStatus terminalStatus;
    tui::ansi::Printer printer;
    setup::AppLogger logger;
};