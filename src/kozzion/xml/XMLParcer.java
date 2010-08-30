package kozzion.xml;

import java.util.Vector;

public class XMLParcer
{

    public static Vector<XMLNode> ParceNodes(Vector<String> xmllines, String tag)
    {

        Vector<XMLNode> nodes = new Vector<XMLNode>();        
        
        int lineindex         = 0;
        int startindex        = 0;
        String opentag        = "<"  + tag + ">";
        String closetag       = "</" + tag + ">";
        
        while(lineindex < xmllines.size())
        {      
            startindex  = xmllines.get(lineindex).indexOf(opentag,startindex);
            
            if(startindex == -1)
            {
                ++lineindex;
                startindex = 0;
            }
            else
            {
                XMLNode node = new XMLNode(tag); 
                int endindex = xmllines.get(lineindex).indexOf(closetag);                
                if(endindex != -1)            
                {    
                    node.addLine(xmllines.get(lineindex).substring(startindex + opentag.length(), endindex));
                    startindex = endindex + closetag.length();
                    nodes.add(node); 
                }
                else
                {
                    node.addLine(xmllines.get(lineindex).substring(startindex + opentag.length()));
                    ++lineindex;
                    startindex = 0;
                    while(lineindex < xmllines.size())
                    {
                        endindex = xmllines.get(lineindex).indexOf(closetag); 
                        if(endindex != -1)            
                        {    
                            node.addLine(xmllines.get(lineindex).substring(0, endindex - 1));
                            startindex = endindex + closetag.length();
                            nodes.add(node);
                            break;
                        }
                        else
                        {
                            node.addLine(xmllines.get(lineindex)); 
                            ++lineindex;
                            startindex = 0;
                        }                        
                    } 
                }
            }    
        }
        return nodes;
    }
    
    public static String getElement(Vector<String> lines, String tag)
    {
        Vector<XMLNode>  node  = XMLParcer.ParceNodes(lines, tag); 
        return XMLParcer.makeElement(node.get(0).getLines());        
    }
    
    

    
    public static String makeElement(Vector<String> lines)
    {
        String element = "";
        for(int index = 0; index < lines.size(); index++)
            element += " " +  lines.get(index);       
        return element.trim();
    }

}
