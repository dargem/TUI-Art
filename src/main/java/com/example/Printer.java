package com.example;
import java.util.ArrayList;

import org.fusesource.jansi.AnsiConsole;

import com.example.representations.Board;
import com.example.representations.Tile;
import com.example.utils.TerminalStatus;
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
        System.out.print(HIDE_CURSOR);
        System.out.print(CLEAR_SCREEN);
        
    }


    public void printLine(int round, Board board)
    {
        //System.out.println("getting ready to print");
        String output = "";
        ArrayList<Tile> output_row = board.getRow(round);

        if (output_row == null)
        {
            //System.out.print(MOVE_TO_TOP_LEFT);
            //System.out.print(INSERT_LINE);
            return;
        }

        int num_iterations = Math.min(TerminalStatus.getWidth(), output_row.size());

        for (int i = 0; i < num_iterations; i++)
        {
            Tile tile = output_row.get(i);

            if (tile == null)
            {
                // empty space for tiles not containing anything
                output += " ";
                continue;
            }

            output += tile.getForeground().foreground();
            output += tile.getCharacter();
        }
        //System.out.println("printed line");
        System.out.print(MOVE_TO_TOP_LEFT);
        System.out.print(INSERT_LINE);
        System.out.print(output);
    }
}
