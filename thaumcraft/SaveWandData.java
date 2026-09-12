package thaumcraft;

import java.io.Serializable;

public class SaveWandData
    implements Serializable
{
    public int apportBlock;
    public int apportMeta;
    public String spawnerMob;

    public SaveWandData(int i, int j, String s)
    {
        apportBlock = -1;
        apportMeta = -1;
        apportBlock = i;
        apportMeta = j;
        spawnerMob = s;
    }
}
