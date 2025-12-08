package com.example.rendering;

import com.example.representations.shapes.Shape;

public record TaggedShape(Shape shape, RenderContext render_context){}
