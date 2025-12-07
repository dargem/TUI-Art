package com.example.terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class TerminalPublisher 
{
    private final Terminal screen_terminal;
    private final ArrayList<TerminalSubscriber> subscribers = new ArrayList<>();
    private int x_width;
    private int y_width;

    public TerminalPublisher()
    {
        try
        {
            screen_terminal = TerminalBuilder.terminal();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Terminal could not be built by the terminal builder: " + e);
        }

        x_width = screen_terminal.getWidth();
        y_width = screen_terminal.getHeight();
    }

    /**
     * 
     * @param terminal_subscriber
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
            if (subscriber_iterator.next() == terminal_subscriber)
            {
                subscriber_iterator.remove();
                return true;
            }
        }

        return false;
    }


    public void checkEmitTerminalSizeNews()
    {

    }
}
