package com.example;
import org.fusesource.jansi.AnsiConsole;
/**
 * Responsible for writing to the console
 */

public class Printer 
{
    private static final String HIDE_CURSOR = "\u001b[?25l";
    private static final String SHOW_CURSOR = "\u001b[?25h";
    private static final String CLEAR_SCREEN = "\u001b[2J";
    private static final String MOVE_TO_TOP_LEFT = "\u001b[1;1H";
    private static final String INSERT_LINE = "\u001b[L";

    /**
     * Install Jansi console and set it up
     */
    public Printer()
    {
        AnsiConsole.systemInstall();
        System.out.println(HIDE_CURSOR);
        System.out.println(CLEAR_SCREEN);
    }


    public void printLine(String contextString)
    {
    }
}
