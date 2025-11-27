package com.example;
import org.fusesource.jansi.AnsiConsole;

import com.example.config.BranchParams;
import com.example.config.TrunkParams;
import com.example.models.tree_model.TreeFactory;


public class Main 
{
    // Define our ANSI escape codes as constants
    private static final String SHOW_CURSOR = "\u001b[?25h";

    public static void main(String[] args)
    {
        TreeFactory tree_factory = new TreeFactory(
            BranchParams.fromFile(),
            TrunkParams.fromFile()
        );

        // 1. Make a shutdown hook to uninstall jansi and show cursor
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            print(SHOW_CURSOR);
            AnsiConsole.systemUninstall();
        }));

        Controller controller = new Controller();
        
        controller.addModelFactory(tree_factory);
        controller.startUp();
        while (true)
        {
            controller.runRound();
        }
    }

    /**
     * Helper method to print and immediately flush the output.
     */
    private static void print(String s) {
        System.out.print(s);
        System.out.flush();
    }
}
