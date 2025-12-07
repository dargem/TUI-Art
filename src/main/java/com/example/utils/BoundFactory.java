package com.example.utils;

import com.example.terminal.TerminalSubscriber;

public class BoundFactory implements TerminalSubscriber
{
    private final int MIN_X = 0;
    private int terminal_width;

    /**
     * As a TerminalSubscriber, 
     * it will receive notifications when terminal size changes
     */
    @Override
    public void updateTerminalSize(int x, int y) {
        // doesn't need terminal height
        terminal_width = x;
    }

    /**
     * Create a new bound
     * @param min_y the lower y bound inclusive
     * @param max_y the upper y bound inclusive
     * @return the Bound object for the requirements
     */
    public Bound createBound(int min_y, int max_y)
    {
        return new Bound(MIN_X, terminal_width, min_y, max_y);
    }
}
