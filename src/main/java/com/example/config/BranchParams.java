package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record BranchParams(
    double initial_length,
    double length_decay,
    double width_decay,
    //int max_depth,
    double angle_variance,
    int branches_per_node
) 
{
    public static BranchParams fromFile()
    {
        return BranchParams.fromFile("/branchparams.properties");
    }

    public static BranchParams fromFile(String path)
    {
        Properties p = new Properties();

        try (InputStream in = BranchParams.class.getResourceAsStream(path)) 
        {
            if (in != null) 
            {
                p.load(in);
                double initial_length = Double.parseDouble(p.getProperty("initial_length"));
                double length_decay = Double.parseDouble(p.getProperty("length_decay"));
                double width_decay = Double.parseDouble(p.getProperty("width_decay"));
                double angle_variance = Double.parseDouble(p.getProperty("angle_variance"));
                int branches_per_node = Integer.parseInt(p.getProperty("branches_per_node"));
                return new BranchParams(initial_length, length_decay, width_decay, angle_variance, branches_per_node);
            }
            else
            {
                System.out.println("Input Stream Null for " + path + ", quitting");
                System.exit(0);
            }
        }
        catch (IOException e) 
        {
            System.out.println("File for path not found");
            System.out.println(e);
            System.exit(0);
        }
        return null;
    }
}
