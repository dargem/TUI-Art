package com.example.utils;
import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public final class TerminalStatus {

    private static Terminal terminal;

    // private constructor just to prevent instances
    private TerminalStatus() {}

    /**
     * Lazy loads a terminal, can't do this with a final 
     * As checked exception needs handling
     * @return Terminal object
     */
    private static Terminal getTerminal()
    {
        if (terminal == null)
        {
            try 
            {
                terminal = TerminalBuilder.terminal();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Failed to create terminal: ", e);
            }
        }
        return terminal;
    }
    public static int getWidth() throws RuntimeException
    {
        return getTerminal().getWidth();
    }

    public static int getHeight() throws RuntimeException
    {
        return getTerminal().getHeight();
    }
}
