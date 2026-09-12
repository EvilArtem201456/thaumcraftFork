package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityHiddenPH extends Entity
{
    private int direction;
    public byte blocks[];
    public byte blocksMeta[];
    public int length;
    private int startX;
    private int startY;
    private int startZ;
    private static World world;
    private double time;

    public EntityHiddenPH(World world1)
    {
        super(world1);
        preventEntitySpawning = true;
        world = world1;
        setSize(0.0F, 0.0F);
    }

    public EntityHiddenPH(World world1, int i, int j, int k, int l, int i1)
    {
        this(world1);
        startX = i;
        startY = j;
        startZ = k;
        posX = ModLoader.getMinecraftInstance().thePlayer.posX;
        posY = ModLoader.getMinecraftInstance().thePlayer.posY;
        posZ = ModLoader.getMinecraftInstance().thePlayer.posZ;
        direction = i1;
        length = l;
        int j1 = l * 500;
        if (j1 < 5000)
        {
            j1 = 5000;
        }
        time = System.currentTimeMillis() + (long)j1;
    }

    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        posX = ModLoader.getMinecraftInstance().thePlayer.posX;
        posY = ModLoader.getMinecraftInstance().thePlayer.posY;
        posZ = ModLoader.getMinecraftInstance().thePlayer.posZ;
        if ((double)System.currentTimeMillis() > time)
        {
            int i = 0;
            do
            {
                if (i >= 2)
                {
                    break;
                }
                int j = startX;
                int k = startY;
                int l = startZ;
                for (int i1 = 0; i1 < length; i1++)
                {
                    switch (direction)
                    {
                        case 0:
                            k++;
                            break;

                        case 1:
                            k--;
                            break;

                        case 2:
                            l++;
                            break;

                        case 3:
                            l--;
                            break;

                        case 4:
                            j++;
                            break;

                        case 5:
                            j--;
                            break;
                    }
                    if (k - i < 0 || k - i > 127)
                    {
                        continue;
                    }
                    world.setBlockAndMetadataWithNotify(j, k - i, l, blocks[i1 + length * i] + 128, blocksMeta[i1 + length * i]);
                    if (i == 0 && (i1 == 0 || i1 == length - 1))
                    {
                        world.playSoundEffect((double)j + 0.5D, k, (double)l + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
                    }
                }

                if (direction < 2)
                {
                    break;
                }
                i++;
            }
            while (true);
            setEntityDead();
        }
    }

    protected void entityInit()
    {
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        return false;
    }

    public boolean isInRangeToRenderVec3D(Vec3D vec3d)
    {
        return true;
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        time = nbttagcompound.getDouble("time") + (double)System.currentTimeMillis();
        blocks = nbttagcompound.getByteArray("blocks");
        blocksMeta = nbttagcompound.getByteArray("blocksMeta");
        direction = nbttagcompound.getShort("direction");
        length = nbttagcompound.getShort("length");
        startX = nbttagcompound.getShort("startX");
        startY = nbttagcompound.getShort("startY");
        startZ = nbttagcompound.getShort("startZ");
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setDouble("time", time - (double)System.currentTimeMillis());
        nbttagcompound.setByteArray("blocks", blocks);
        nbttagcompound.setByteArray("blocksMeta", blocksMeta);
        nbttagcompound.setShort("direction", (short)direction);
        nbttagcompound.setShort("length", (short)length);
        nbttagcompound.setShort("startX", (short)startX);
        nbttagcompound.setShort("startY", (short)startY);
        nbttagcompound.setShort("startZ", (short)startZ);
    }
}
