#pragma once

#include <cstddef>
#include <iostream>

#include "core/surface.hpp"
#include "core/types.hpp"
#include "setup/app_context.hpp"
#include "setup/configs.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "tui/terminal_status.hpp"

namespace tui {

class TerminalBackend : TerminalDimensionListener {
   public:
    TerminalBackend(AppContext& context);

    // gets the draw surface (the back buffer)
    [[nodiscard]] Surface& getDrawSurface();

    // renders the back buffer
    // usedCamera is the camera that was used in drawing to the back buffer
    // after presenting a new draw surface should be retrieved for use
    void present(const Camera backBufferCamera);

    void receiveTerminalSize(TerminalDimension dimension) override;

   private:
    // Should be called when presentation ends
    void cleanupPresentation(Camera backBufferCamera);

    // logger
    setup::AppLogger& logger;

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

    ansi::Printer& printer;
};

}  // namespace tui
