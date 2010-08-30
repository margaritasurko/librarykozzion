package kozzion.xml;

import java.util.Vector;

public class XMLNode
{
    String          d_tag;
    Vector<String>  d_lines;
    
    public XMLNode(String tag)
    {
        d_tag   = tag;
        d_lines = new Vector<String>();
    }
    
    public void addLine(String line)
    {
        d_lines.add(line);
    }
    
    public void print()
    {
        for(int index = 0; index < d_lines.size(); index++)
            System.out.println(d_lines.get(index));
    }

    public Vector<String> getLines()
    {
        return d_lines;
    }

}
