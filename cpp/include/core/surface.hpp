#pragma once
#include <algorithm>
#include <cassert>
#include <concepts>
#include <cstddef>
#include <execution>
#include <ranges>
#include <type_traits>
#include <vector>

#include "core/types.hpp"

namespace types {

template <typename T>
concept Item = std::same_as<T, Cell> || std::same_as<T, Light> || std::same_as<T, ToneShift>;

template <Item T>
class SurfaceBase;  // forward declare surface base

using LightSurface = SurfaceBase<Light>;
using ToneSurface = SurfaceBase<ToneShift>;

template <Item T>
struct SurfaceTraits;

template <>
struct SurfaceTraits<Cell> {
    static constexpr Cell defaultValue() { return Cell{{}, ' '}; }
};

template <>
struct SurfaceTraits<Light> {
    static constexpr Light defaultValue() { return Light{}; }
};

template <>
struct SurfaceTraits<ToneShift> {
    // defaults to a blank RGB value with 0 shift
    static constexpr ToneShift defaultValue() { return ToneShift{RGB{}, 0}; }
};

template <Item T>
class SurfaceBase {
   public:
    size_t width, height;
    // keep track whether this surface has been drawn on
    bool drawnOn{false};

    SurfaceBase(size_t w, size_t h) :
            width{w}, height{h}, elements{w * h, SurfaceTraits<T>::defaultValue()} {}

    // Writes an element to the grid location
    void writeElement(const T element, GridLocation gridLocation) {
        assert(gridLocation.x < width && "Out of x bounds write");
        assert(gridLocation.y < height && "Out of y bounds write");

        if constexpr (std::same_as<T, Light> || std::same_as<T, ToneShift>) {
            // if its a light, writing to it does a colour blend action
            elements[gridLocation.y * width + gridLocation.x].blend(element);
        } else {
            elements[gridLocation.y * width + gridLocation.x] = element;
        }
    }

    // Returns the element at the grid location
    [[nodiscard]] const T& getElement(GridLocation gridLocation) const {
        assert(gridLocation.x < width && "Out of x bounds read");
        assert(gridLocation.y < height && "Out of y bounds read");
        return elements[gridLocation.y * width + gridLocation.x];
    }

    // Get element by index, dangerous so ensure indexing is correct
    [[nodiscard]] const T& getElement(size_t index) const {
        assert(index < elements.size() && "Out of bounds indexing");

        return elements[index];
    }

    void reset() { elements.assign(width * height, SurfaceTraits<T>::defaultValue()); }

    bool sameSize(const SurfaceBase<T>& other) const {
        return (width == other.width) && (height == other.height);
    }

    // return a modifiable view of its elements, avoid using when possible
    auto getView() { return std::views::all(elements); }

    // returns a read only view of its elements
    auto getReadView() const { return std::views::all(elements); }

   private:
    std::vector<T> elements;
};

// cell surface is an implementation of a surface base that holds cells
class CellSurface : public SurfaceBase<Cell> {
   public:
    CellSurface(size_t w, size_t h) : SurfaceBase<Cell>(w, h) {}

    void applyLight(const LightSurface& lightSurface) {
        assert((height == lightSurface.height) && (width == lightSurface.width) &&
               "The applied light surface should have the same dimensions as the surface");

        // Create a view which will effectively zip the relevant tile with its light
        auto zip = std::views::zip(SurfaceBase::getView(), lightSurface.getReadView());
        // Apply the lights, can be done in parallel and non sequentially
        std::for_each(std::execution::par_unseq, zip.begin(), zip.end(), [](auto&& overlay) {
            auto& cell = std::get<0>(overlay);
            auto& light = std::get<1>(overlay);
            light.applyOn(cell);
        });
    }
};

}  // namespace types
