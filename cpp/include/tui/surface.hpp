#pragma once
#include "core/types.hpp"
#include <vector>
#include <algorithm>
#include <cassert>

namespace tui {

class Surface {
public:
    int width, height;

    Surface(int w, int h) : width(w), height(h), pixels(w * h) {}

    void setCell(int x, int y, const Cell& cell) {
        assert(x>=0 && x < width && "Out of x bounds write");
        assert(y>=0 && y < height && "Out of y bounds write");
        pixels[y * width + x] = cell;
    }

    [[nodiscard]] const Cell& getCell(int x, int y) const {
        assert(x>=0 && x < width && "Out of x bounds read");
        assert(y>=0 && y < height && "Out of y bounds read");
        return pixels[y * width + x];
    }

    void clear() {
        std::fill(pixels.begin(), pixels.end(), Cell{' ', {}});
    }

private:
    std::vector<Cell> pixels;
};

}
