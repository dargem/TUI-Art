package com.example.terminal;
import java.io.IOException;
import java.util.ArrayList;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * A publisher for the size of the terminal.
 * Tells each subscriber when terminal size changes.
 * Designed to be shared as a shared source of truth
 */
public class TerminalPublisher 
{
    private final Terminal terminal;
    private final ArrayList<TerminalSubscriber> subscribers = new ArrayList<>();
    private int width;
    private int height;

    public TerminalPublisher()
    {
        try
        {
            terminal = TerminalBuilder.terminal();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Terminal could not be built by the terminal builder: " + e);
        }

        width = terminal.getWidth();
        height = terminal.getHeight();
    }

    /**
     * Add a subscriber to the publisher's subscriber list
     * @param terminal_subscriber the subscriber to add
     * @throws IllegalArgumentException if subscriber is null
     */
    public void addTerminalSubscriber(TerminalSubscriber terminal_subscriber)
    {
        if (terminal_subscriber == null)
        {
            throw new IllegalArgumentException("A subscriber can't be null");
        }

        if (!subscribers.contains(terminal_subscriber))
        {
            subscribers.add(terminal_subscriber);
            terminal_subscriber.updateTerminalSize(width, height);
        }
    }

    /**
     * Removes a subscriber from the publisher's subscriber list
     * @param terminal_subscriber subscriber to remove
     * @return true if it was a subscriber, false if it wasn't a subscriber
     */
    public boolean removeTerminalSubscriber(TerminalSubscriber terminal_subscriber)
    {
        return subscribers.remove(terminal_subscriber);
    }

    /**
     * Checks the size of the terminal.
     * If it has changed vs prior, emits the new size out to subscribers.
     */
    public void checkEmitTerminalSizeNews()
    {
        int current_width = terminal.getWidth();
        int current_height = terminal.getHeight();

        if (width != current_width || height != current_height)
        {
            width = current_height;
            height = current_width;
            subscribers.forEach(subscriber -> subscriber.updateTerminalSize(width, height));
        }
    }
}
