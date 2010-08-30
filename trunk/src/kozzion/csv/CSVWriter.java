package kozzion.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter
{
    public static void writeCSVFile(File file, String [] [] content)
    {
        char  [] chatab   ={ (char)9};
        String strtab   = new String(chatab);
        if(content.length    == 0)
            return;
        if(content[0].length == 0)
            return;
        try
        {
            FileWriter writer = new FileWriter(file);
            for(int rowindex = 0; rowindex < content.length; rowindex++)
            {
                writer.write(content[rowindex][0]);
                for(int collumindex = 1; collumindex < content[0].length; collumindex++)
                    writer.write(strtab + content[rowindex][collumindex]);
                writer.write(System.getProperty("line.separator"));
            }
            
            writer.close();
        } 
        catch (IOException e)
        {
            System.out.println("Error: IOError while writing " + file.getAbsolutePath());
            e.printStackTrace();
        }
        
    } 
}
