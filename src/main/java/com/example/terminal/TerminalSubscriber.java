package com.example.terminal;

public interface TerminalSubscriber 
{
    /**
     * Updates a subscriber with the size of a terminal
     * An update will be given out when terminal size changes
     * If an update is not given it can be assumed terminal size is still the same
     * @param x number of characters that can fit in a row
     * @param y number of characters that can fit in a column
     */
    abstract void updateTerminalSize(int x, int y);
}
