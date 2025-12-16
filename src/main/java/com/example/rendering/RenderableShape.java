package com.example.rendering;

import com.example.rendering.tile_providing.TileProvider;
import com.example.representations.shapes.Shape;

// a basic data carrier
public record RenderableShape 
(
    Shape shape,
    TileProvider provider
)
{}
