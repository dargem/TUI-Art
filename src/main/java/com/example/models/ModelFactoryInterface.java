package com.example.models;

import com.example.rendering.Renderable;

/**
 * An interface for model factories
 * Used so the controller can store many factories
 * knowing these factories produce renderables
 */
public interface ModelFactoryInterface {

    /**
     * Used to request a renderable object
     * @return the factories renderable model
     */
    public abstract Renderable getRenderable();

    /**
     * Checks if a factory wants to render something
     * Should be implemented through RNG
     * @return true if it should render, false if not
     */
    public abstract boolean checkShouldRender();

    /*
     * Finds the number of renderables that should be created on startup
     * @return int number of items to render on startup
     */
    public abstract int numStartupRenders();
}
