package com.example.rendering.tile_providing;

import com.example.representations.shapes.Shape;

public record TaggedShape(Shape shape, RenderContext render_context){}
