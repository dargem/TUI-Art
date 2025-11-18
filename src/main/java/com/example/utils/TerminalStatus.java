package com.example.utils;
import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public final class TerminalStatus {
    // private constructor just to prevent instances
    private TerminalStatus() {}

    public static int getWidth() throws IOException, InterruptedException
    {
        try (Terminal terminal = TerminalBuilder.terminal()) {
            return terminal.getWidth();
        }
        /*
        Process p = new ProcessBuilder("sh", "-c", "tput cols").start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        int w = Integer.parseInt(br.readLine().trim());
        p.waitFor();
        return w;
        */
    }
}
