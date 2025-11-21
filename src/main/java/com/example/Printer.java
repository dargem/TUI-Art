package com.example;
import org.fusesource.jansi.AnsiConsole;
import com.example.game_board.Board;
import java.util.ArrayList;
import com.example.game_board.Tile;
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
        String output = "";
        ArrayList<Tile> output_row = board.getRow(round);

        if (output_row == null)
        {
            System.out.print(MOVE_TO_TOP_LEFT);
            System.out.print(INSERT_LINE);
            return;
        }

        for (Tile tile : output_row)
        {
            if (tile == null)
            {
                // empty space for tiles not containing anything
                output += " ";
                continue;
            }

            output += tile.getForeground().foreground();
            output += tile.getCharacter();
        }
        System.out.print(MOVE_TO_TOP_LEFT);
        System.out.print(INSERT_LINE);
        System.out.print(output);
    }
}
