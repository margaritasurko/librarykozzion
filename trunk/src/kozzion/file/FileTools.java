package kozzion.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class FileTools
{

    public static Vector<String> readFile(File file)
    {
        Vector<String> lines = new Vector<String>();
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(file);
        } 
        catch (FileNotFoundException e)
        {
            System.err.println("Error: File not found: " + file.getAbsolutePath());
            e.printStackTrace();
            return lines;
        }
        while(scanner.hasNext())
            lines.add(scanner.nextLine());
        return lines;
    }
}
