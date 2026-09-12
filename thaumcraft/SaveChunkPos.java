package thaumcraft;

import java.io.Serializable;

public class SaveChunkPos
    implements Serializable
{
    public int x;
    public int z;

    public SaveChunkPos(int i, int j)
    {
        x = i;
        z = j;
    }
}
