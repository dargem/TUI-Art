#pragma once
#include <algorithm>
#include <cassert>
#include <concepts>
#include <cstddef>
#include <type_traits>
#include <vector>

#include "core/types.hpp"

namespace types {

template <typename T>
concept Item = requires(T item) { std::same_as<T, Cell> || std::same_as<T, Light>; };

template <Item T>
class Surface;

using CellSurface = Surface<Cell>;
using LightSurface = Surface<Light>;

template <typename T>
struct SurfaceTraits;

template <>
struct SurfaceTraits<Cell> {
    static constexpr Cell defaultValue() { return Cell{' ', {}}; }
};

template <>
struct SurfaceTraits<Light> {
    static constexpr Light defaultValue() { return Light{}; }
};

template <Item T>
class Surface {
   public:
    const size_t width, height;
    // keep track whether this surface has been drawn on
    bool drawnOn{false};

    Surface(size_t w, size_t h) :
            width{w}, height{h}, pixels{w * h, SurfaceTraits<T>::defaultValue()} {}

    // allow std::move to steal pixel vectors resource
    Surface<T>& operator=(Surface<T>&&) noexcept = default;

    // Sets a cell on the surface to be equal to another
    void setPixel(size_t x, size_t y, const T value) {
        assert(x < width && "Out of x bounds write");
        assert(y < height && "Out of y bounds write");
        pixels[y * width + x] = value;
    }

    // Returns a cell
    [[nodiscard]] const T& getCell(size_t x, size_t y) const {
        assert(x < width && "Out of x bounds read");
        assert(y < height && "Out of y bounds read");
        return pixels[y * width + x];
    }

    void reset() { pixels.assign(width * height, SurfaceTraits<T>::defaultValue()); }

    bool sameSize(const Surface<T>& other) const {
        return (width == other.width) && (height == other.height);
    }

   private:
    std::vector<T> pixels;
};

}  // namespace types
