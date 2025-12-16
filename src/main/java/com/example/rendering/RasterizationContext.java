package com.example.rendering;

import com.example.rendering.tile_providing.TileProvider;
import com.example.representations.Board;

public record RasterizationContext(Board board, TileProvider provider) {}
