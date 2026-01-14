#pragma once
#include "renderer/surface.hpp"
#include <iostream>

namespace tui {

class TerminalBackend {
    Surface frontBuffer;
    Surface backBuffer;

public:
    TerminalBackend(int w, int h) : frontBuffer(w, h), backBuffer(w, h) {}

    Surface& getDrawSurface() { return backBuffer; }

    void present();
};

}
