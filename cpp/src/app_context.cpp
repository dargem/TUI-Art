#include "app_context.hpp"
#include "tui/terminal_status.hpp"
#include "tui/ansi/ansi_printer.hpp"

AppContext::AppContext()
    : terminalStatus{}
    , printer{terminalStatus}
{}

TerminalStatus& AppContext::getTerminalStatus()
{
    return terminalStatus;
}

Printer& AppContext::getPrinter()
{
    return printer;
}