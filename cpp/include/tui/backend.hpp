#pragma once

#include "tui/surface.hpp"
#include "core/types.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "app_context.hpp"
#include "tui/terminal_status.hpp"
#include <iostream>
#include <cstddef>

namespace tui
{   
    using ansi::Printer;

    class TerminalBackend : TerminalDimensionListener
    {
    public:
        TerminalBackend(AppContext& context);

        // gets the draw surface (the back buffer)
        [[nodiscard]] Surface &getDrawSurface();

        // renders the back buffer
        // usedCamera is the camera that was used in drawing to the back buffer
        // after presenting a new draw surface should be retrieved for use
        void present(const Camera backBufferCamera);

        void receiveTerminalSize(TerminalDimension dimension) override;
    private:
        // subscription for receiving terminal dimensions
        TerminalDimensionToken dimensionSubscriptionToken;

        // current size of the terminal
        TerminalDimension currentDimension;

        // the front buffer is the buffer that is currently displayed
        Surface frontBuffer;

        // the back buffer is where the next frame is being rendered
        Surface backBuffer;

        // the camera that was used in rendering the front buffer
        Camera frontBufferCamera;

        Printer &printer;
    };

}
