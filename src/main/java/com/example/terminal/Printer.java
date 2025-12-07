package com.example.terminal;
import java.util.ArrayList;

import org.fusesource.jansi.AnsiConsole;

import com.example.representations.Board;
import com.example.representations.Tile;
/**
 * Responsible for writing to the console
 */
public class Printer implements TerminalSubscriber
{
    private static final String HIDE_CURSOR = "\u001b[?25l";
    private static final String CLEAR_SCREEN = "\u001b[2J";
    private static final String MOVE_TO_TOP_LEFT = "\u001b[1;1H";
    private static final String INSERT_LINE = "\u001b[L";
    private int terminal_width;

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
        StringBuilder output = new StringBuilder();
        ArrayList<Tile> output_row = board.getRow(round);

        if (output_row == null)
        {
            //System.out.print(MOVE_TO_TOP_LEFT);
            //System.out.print(INSERT_LINE);
            return;
        }

        int num_iterations = Math.min(terminal_width, output_row.size());

        for (int i = 0; i < num_iterations; i++)
        {
            Tile tile = output_row.get(i);

            if (tile == null)
            {
                // empty space for tiles not containing anything
                output.append(" ");
                continue;
            }

            output.append(tile.getForeground().foreground());
            output.append(tile.getCharacter());
        }
        // Reset colors at the end of the line
        output.append("\u001b[0m");
        
        //System.out.println("printed line");
        System.out.print(MOVE_TO_TOP_LEFT);
        System.out.print(INSERT_LINE);
        System.out.print(output.toString());
        // flush output immediately once written
        System.out.flush();
    }

    /**
     * Printer implements TerminalSubscriber
     * It will receive notifications when terminal size changes
     */
    @Override
    public void updateTerminalSize(int x, int y) 
    {
        // printer has no need for terminal height
        terminal_width = x;
    }
}
