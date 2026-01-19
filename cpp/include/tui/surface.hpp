#pragma once
#include "core/types.hpp"
#include <vector>
#include <algorithm>
#include <cassert>
#include <cstddef>

namespace tui {

class Surface {
public:
    size_t width, height;

    Surface(size_t w, size_t h) : width{ w }, height{ h }, pixels{ w * h } {}

    // Sets a cell on the surface to be equal to another
    void setCell(size_t x, size_t y, const Cell& cell) {
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

    void clear() {
        std::fill(pixels.begin(), pixels.end(), Cell{' ', {}});
    }

private:
    std::vector<Cell> pixels;
};

}
