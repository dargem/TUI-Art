package com.example.rendering.tile_providing;

import java.util.HashMap;
import java.util.Map;

public class TileProviderRegistry 
{
    private final Map<RenderableKind, TileProvider> providers = new HashMap<>();

    /**
     * Register a rendrable kind with a tile provider
     * @param k the renderable kind
     * @param p the tile provider
     * @return a registry useful for method chaining    
     */
    public TileProviderRegistry register(RenderableKind k, TileProvider p) { providers.put(k,p); return this; }

    /**
     * Finds a provider for a given renderable kind
     * @param k the renderable kind type
     * @return a provider corresponding to it
     */
    public TileProvider forKind(RenderableKind k) { return providers.get(k); }
    
    
}
