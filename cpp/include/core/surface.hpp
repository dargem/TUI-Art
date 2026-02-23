#pragma once
#include <algorithm>
#include <cassert>
#include <concepts>
#include <cstddef>
#include <type_traits>
#include <vector>

#include "core/types.hpp"

namespace types {

using CellSurface = SurfaceBase<Cell>;
using LightSurface = SurfaceBase<Light>;

template <typename T>
concept Item = requires(T) { std::same_as<T, Cell> || std::same_as<T, Light>; };

template <Item T>
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
class SurfaceBase {
   public:
    const size_t width, height;
    // keep track whether this surface has been drawn on
    bool drawnOn{false};

    SurfaceBase(size_t w, size_t h) :
            width{w}, height{h}, elements{w * h, SurfaceTraits<T>::defaultValue()} {}

    // allow std::move to steal other surface base's vector resource
    SurfaceBase<T>& operator=(SurfaceBase<T>&&) noexcept = default;

    // Writes an element to the grid location
    void writeElement(const T value, GridLocation gridLocation) {
        assert(x < width && "Out of x bounds write");
        assert(y < height && "Out of y bounds write");

        if constexpr (std::same_as<T, Light>) {
            // if its a light, writing to it does a colour blend action
            elements[gridLocation.y * width + gridLocation.x];
        } else {
            elements[gridLocation.y * width + gridLocation.x] = value;
        }
    }

    // Returns the element at the grid location
    [[nodiscard]] const T& getElement(GridLocation gridLocation) const {
        assert(x < width && "Out of x bounds read");
        assert(y < height && "Out of y bounds read");
        return elements[gridLocation.y * width + gridLocation.x];
    }

    void reset() { elements.assign(width * height, SurfaceTraits<T>::defaultValue()); }

    bool sameSize(const SurfaceBase<T>& other) const {
        return (width == other.width) && (height == other.height);
    }

   private:
    std::vector<T> elements;
};

}  // namespace types
