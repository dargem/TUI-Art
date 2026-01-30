#pragma once

#include "tui/surface.hpp"
#include "core/types.hpp"

#include <iostream>
#include <cstddef>

namespace tui
{

    class TerminalBackend
    {
    public:
        TerminalBackend(size_t w, size_t h);

        // gets the draw surface (the back buffer)
        [[nodiscard]] Surface &getDrawSurface();

        // renders the back buffer
        // usedCamera is the camera that was used in drawing to the back buffer
        // after presenting a new draw surface should be retrieved for use
        void present(const Camera backBufferCamera);

    private:
        // the front buffer is the buffer that is currently displayed
        Surface frontBuffer;

        // the back buffer is where the next frame is being rendered
        Surface backBuffer;

        // the camera that was used in rendering the front buffer
        Camera frontBufferCamera;
    };

}
