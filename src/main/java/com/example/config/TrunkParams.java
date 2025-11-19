package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record TrunkParams(
    double section_length,
    double angle_variance,
    double centralness,
    double width
) 
{
    public static TrunkParams fromFile()
    {
        return TrunkParams.fromFile("/trunkparams.properties");
    }

    public static TrunkParams fromFile(String path)
    {
        Properties p = new Properties();

        try (InputStream in = BranchParams.class.getResourceAsStream(path))
        {
            if (in!=null)
            {
                p.load(in);
                double section_length = Double.parseDouble(p.getProperty("section_length"));
                double angle_variance = Double.parseDouble(p.getProperty("angle_variance"));
                double centralness = Double.parseDouble(p.getProperty("centralness"));
                double width = Double.parseDouble(p.getProperty("width"));
                return new TrunkParams(section_length, angle_variance, centralness, width);
            }
            else
            {
                System.out.println("Input stream null for " + path + " quitting");
                System.exit(0);
            }
        }
        catch (IOException e)
        {
            System.out.println("IO exception for " + path + " quitting\n" + e);
            System.exit(0);
        }
        return null;
    }
}
