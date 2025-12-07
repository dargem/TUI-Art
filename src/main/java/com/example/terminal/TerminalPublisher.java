package com.example.terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

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
     */
    public void addTerminalSubscriber(TerminalSubscriber terminal_subscriber)
    {
        subscribers.add(terminal_subscriber);
    }

    /**
     * Removes a subscriber from the publisher's subscriber list
     * @param terminal_subscriber
     * @return true if it was a subscriber, false if it wasn't a subscriber
     */
    public boolean removeTerminalSubscriber(TerminalSubscriber terminal_subscriber)
    {
        Iterator<TerminalSubscriber> subscriber_iterator = subscribers.iterator();
        while (subscriber_iterator.hasNext())
        {
            if (subscriber_iterator.next().equals(terminal_subscriber))
            {
                subscriber_iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the size of the terminal.
     * If it has changed vs prior, emits the new size out to subscribers.
     */
    public void checkEmitTerminalSizeNews()
    {
        if (width != terminal.getWidth() || height != terminal.getHeight())
        {
            width = terminal.getWidth();
            height = terminal.getHeight();
            subscribers.forEach(subscriber -> subscriber.updateTerminalSize(width, height));
        }
    }
}
