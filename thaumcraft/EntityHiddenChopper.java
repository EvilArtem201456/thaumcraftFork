package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityHiddenChopper extends Entity
{
    private int startX;
    private int startY;
    private int startZ;
    private int blockid;
    private int meta;
    private static World world;
    public int delay;

    public EntityHiddenChopper(World world1)
    {
        super(world1);
        preventEntitySpawning = true;
        world = world1;
        setSize(0.0F, 0.0F);
    }

    public EntityHiddenChopper(World world1, int i, int j, int k, int l, int i1)
    {
        this(world1);
        startX = i;
        startY = j;
        startZ = k;
        posX = ModLoader.getMinecraftInstance().thePlayer.posX;
        posY = ModLoader.getMinecraftInstance().thePlayer.posY;
        posZ = ModLoader.getMinecraftInstance().thePlayer.posZ;
        blockid = l;
        meta = i1;
    }

    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        posX = ModLoader.getMinecraftInstance().thePlayer.posX;
        posY = ModLoader.getMinecraftInstance().thePlayer.posY;
        posZ = ModLoader.getMinecraftInstance().thePlayer.posZ;
        if (delay > 0)
        {
            delay--;
            return;
        }
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                for (int k = -1; k < 2; k++)
                {
                    if (world.getBlockId(startX + i, startY + j, startZ + k) == blockid && world.getBlockMetadata(startX + i, startY + j, startZ + k) == meta)
                    {
                        Block.blocksList[blockid].dropBlockAsItem(world, startX + i, startY + j, startZ + k, meta, 0);
                        world.setBlockWithNotify(startX + i, startY + j, startZ + k, 0);
                        ThaumCraftCore.poof(world, startX + i, startY + j, startZ + k);
                        world.playSoundEffect((double)(startX + i) + 0.5D, (double)(startY + j) + 0.5D, (double)(startZ + k) + 0.5D, "step.wood", 0.4F, 1.0F);
                        EntityHiddenChopper entityhiddenchopper = new EntityHiddenChopper(world, startX + i, startY + j, startZ + k, blockid, meta);
                        entityhiddenchopper.delay = 2;
                        world.spawnEntityInWorld(entityhiddenchopper);
                        delay = 6;
                        return;
                    }
                }
            }
        }

        setEntityDead();
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
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    }
}
