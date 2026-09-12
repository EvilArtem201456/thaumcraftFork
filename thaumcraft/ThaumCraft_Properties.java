package thaumcraft;

import java.io.*;
import java.util.*;

public class ThaumCraft_Properties extends Properties
{
    public ThaumCraft_Properties()
    {
    }

    public synchronized Enumeration keys()
    {
        Enumeration enumeration = super.keys();
        Vector vector = new Vector();
        for (; enumeration.hasMoreElements(); vector.add(enumeration.nextElement())) { }
        Collections.sort(vector);
        return vector.elements();
    }

    public String safeGetProperty(String s, File file, String s1)
    {
        if (getProperty(s) == null)
        {
            try
            {
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                setProperty(s, s1);
                store(fileoutputstream, "ThaumCraft Properties File");
                fileoutputstream.close();
                return s1;
            }
            catch (IOException ioexception)
            {
                ioexception.getMessage();
            }
        }
        else
        {
            return getProperty(s);
        }
        return "";
    }
}
