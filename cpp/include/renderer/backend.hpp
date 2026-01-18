#pragma once
#include "renderer/surface.hpp"
#include <iostream>

namespace tui {

class TerminalBackend {
public:
    TerminalBackend(int w, int h, int frameShift);

    [[nodiscard]] Surface& getDrawSurface();

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
