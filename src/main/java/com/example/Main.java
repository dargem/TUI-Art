package com.example;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.example.config.BranchParams;
import com.example.utils.TerminalStatus;

public class Main 
{
    // Define our ANSI escape codes as constants
    private static final String HIDE_CURSOR = "\u001b[?25l";
    private static final String SHOW_CURSOR = "\u001b[?25h";
    private static final String CLEAR_SCREEN = "\u001b[2J";
    private static final String MOVE_TO_TOP_LEFT = "\u001b[1;1H";
    private static final String INSERT_LINE = "\u001b[L";
    
    private static final String GREEN = "\u001b[32m";
    private static final String BROWN = "\u001b[33m";
    private static final String RESET = "\u001b[0m";

    public static void main(String[] args)
    {
        BranchParams params = BranchParams.fromFile();
        System.out.println(params.angle_variance());
        //TreeFactory tree_factory = new TreeFactory(null, null)




        // 1. Install Jansi to make ANSI codes work
        while (true)
        {
            try
            {
                int x = TerminalStatus.getWidth();
                for (int i = 0; i < x; i++)
                {
                    System.out.print(
                        Ansi.ansi()
                        .fg(Ansi.Color.GREEN)
                        .bg(Ansi.Color.RED)
                        .bold()
                        .a("A")
                        .reset()
                    );
                }
                System.out.print("\n");
                Thread.sleep(100);
            } 
            catch(Exception e)
            {
                System.out.println(e);
                break;
            }
        }
        System.exit(0);
        AnsiConsole.systemInstall();

        // 2. Use a try...finally block to ensure we always restore the terminal
        try {
            // Setup the terminal for animation
            print(HIDE_CURSOR);
            print(CLEAR_SCREEN);

            // 3. The main animation loop
            for (int i = 0; i < 1000; i++) {
                
                // 4. Move to top and insert a new line. This pushes all
                // existing content down one row.
                print(MOVE_TO_TOP_LEFT);
                print(INSERT_LINE);

                // 5. Draw the "new growth" on the new line
                // This just alternates to simulate a growing tree
                String art;
                if (i % 4 == 0) {
                    art = "     " + BROWN + "|" + GREEN + "Test "+  RESET;
                } else if (i % 4 == 1) {
                    art = "    " + GREEN + "/ \\" + RESET;
                } else if (i % 4 == 2) {
                    art = "   " + GREEN + "// \\\\" + RESET;
                } else {
                    art = "     " + BROWN + "|" + RESET;
                }

                if (i % 20 == 0)
                {
                    art += "dakfal";
                }
                
                print(art);

                // 6. Wait for a moment to make the animation visible
                Thread.sleep(200); // 200 milliseconds
            }

        } catch (InterruptedException e) {
            // Handle thread sleep interruption
            Thread.currentThread().interrupt();
        } finally {
            // 7. ALWAYS restore the cursor when done or if an error occurs
            print(SHOW_CURSOR);
            AnsiConsole.systemUninstall();
        }
    }

    /**
     * Helper method to print and immediately flush the output.
     * This is crucial for terminal animation.
     */
    private static void print(String s) {
        System.out.print(s);
        System.out.flush();
    }
}
