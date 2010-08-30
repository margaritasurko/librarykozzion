package kozzion.csv;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import kozzion.file.FileTools;

public class CSVReader
{

    public String [] [] readCSVFile(File csvfile)
    {
        Vector<String> lines = FileTools.readFile(csvfile);
       
        return null;
    }
}
