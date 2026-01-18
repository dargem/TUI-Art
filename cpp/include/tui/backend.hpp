#pragma once
#include "tui/surface.hpp"
#include <iostream>

namespace tui {

class TerminalBackend {
public:
    TerminalBackend(int w, int h, int frameShift);

    // gets the draw surface (the back buffer)
    [[nodiscard]] Surface& getDrawSurface();

    // renders the back buffer
    void present();
private:
    // the front buffer is the buffer that is currently displayed
    Surface frontBuffer;

    // the back buffer is where the next frame is being rendered
    Surface backBuffer;

    // frame shift
    const int frameShift;
};

}
