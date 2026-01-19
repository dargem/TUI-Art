#pragma once
#include "tui/surface.hpp"
#include <iostream>
#include <cstddef>

namespace tui {

class TerminalBackend {
public:
    TerminalBackend(size_t w, size_t h, int frameShift);

    // gets the draw surface (the back buffer)
    [[nodiscard]] Surface& getDrawSurface();

    // renders the back buffer
    // y_shift is used to inform the renderer if the new back buffer
    // is at least partially a y shifted version of the prior front buffer
    // this allows large optimizations as the current displayed front buffer with a newline
    void present(int y_shift);
private:
    // the front buffer is the buffer that is currently displayed
    Surface frontBuffer;

    // the back buffer is where the next frame is being rendered
    Surface backBuffer;

    // frame shift
    const int frameShift;
};

}
