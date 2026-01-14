#pragma once
#include "core/types.hpp"
#include <vector>
#include <algorithm>

namespace tui {

class Surface {
public:
    int width, height;
    std::vector<Cell> pixels;

    Surface(int w, int h) : width(w), height(h), pixels(w * h) {}

    void set(int x, int y, const Cell& cell) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y * width + x] = cell;
        }
    }

    Cell& get(int x, int y) {
        // Simple bounds check or unsafe for speed (using at() vs [])
        // For a renderer, usually we ensure indices are valid beforehand or clamp
        if (x < 0 || x >= width || y < 0 || y >= height) {
             static Cell empty;
             return empty; 
        }
        return pixels[y * width + x];
    }

    void clear() {
        std::fill(pixels.begin(), pixels.end(), Cell{' ', {}});
    }
};

}
