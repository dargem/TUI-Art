#pragma once
#include <algorithm>
#include <cassert>
#include <cstddef>
#include <vector>

#include "core/types.hpp"

namespace tui {

class Surface {
   public:
    size_t width, height;
    // keep track whether this surface has been drawn on
    bool drawnOn;

    Surface(size_t w, size_t h) : width{w}, height{h}, drawnOn{false}, pixels{w * h} {}

    Surface& operator=(Surface&&) noexcept = default;

    // Sets a cell on the surface to be equal to another
    void setCell(size_t x, size_t y, const Cell cell) {
        assert(x < width && "Out of x bounds write");
        assert(y < height && "Out of y bounds write");
        pixels[y * width + x] = cell;
    }

    // Returns a cell
    [[nodiscard]] const Cell& getCell(size_t x, size_t y) const {
        assert(x < width && "Out of x bounds read");
        assert(y < height && "Out of y bounds read");
        return pixels[y * width + x];
    }

    void reset() {
        if (pixels.capacity() != width * height || pixels.size() != width * height) {
            pixels.resize(width * height);
        }
        pixels.clear();
        std::fill(pixels.begin(), pixels.end(), Cell{' ', {}});
    }

    bool sameSize(const Surface& other) {
        return (width == other.width) && (height == other.height);
    }

   private:
    std::vector<Cell> pixels;
};

}  // namespace tui
