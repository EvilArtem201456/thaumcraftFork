package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class TileEntityEffects extends TileEntity
{
    public int delay;
    private float oldThaum;
    public float rota;
    public int bob;
    public int contains;

    public TileEntityEffects()
    {
        rota = 0.0F;
        bob = 0;
        delay = 20;
        contains = -1;
    }

    public String getInvName()
    {
        return "Thaumic Effects";
    }

    private void spawnCrystal()
    {
        EntityItem entityitem = new EntityItem(worldObj, (double)xCoord + 0.5D, yCoord, (double)zCoord + 0.5D, new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6));
        entityitem.delayBeforeCanPickup = 10;
        worldObj.spawnEntityInWorld(entityitem);
        worldObj.spawnParticle("largesmoke", entityitem.posX, entityitem.posY, entityitem.posZ, 0.0D, 0.0D, 0.0D);
        worldObj.playSoundAtEntity(entityitem, "random.pop", 0.4F, 2.0F + worldObj.rand.nextFloat() * 0.4F);
    }

    private void spawnSlime()
    {
        EntityThaumSlime entitythaumslime = new EntityThaumSlime(worldObj);
        entitythaumslime.setLocationAndAngles((double)xCoord + 0.5D, (double)yCoord + 1.0D, (double)zCoord + 0.5D, worldObj.rand.nextFloat() * 360F, 0.0F);
        worldObj.spawnEntityInWorld(entitythaumslime);
        entitythaumslime.spawnExplosionParticle();
    }

    private void spawnPuddleBelow(int i)
    {
        if (yCoord - i < 0)
        {
            return;
        }
        int j = worldObj.getBlockId(xCoord, yCoord - i, zCoord);
        int k = worldObj.getBlockId(xCoord, yCoord - i - 1, zCoord);
        if (j == mod_ThaumCraft.thaumEffects.blockID || k == mod_ThaumCraft.thaumEffects.blockID)
        {
            return;
        }
        if (j == Block.bedrock.blockID || Block.blocksList[j].getExplosionResistance(null) > 50F)
        {
            return;
        }
        if (j == 0 || k == 0)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - i, zCoord, 0);
            for (int l = 0; l < 20; l++)
            {
                double d = (double)xCoord + (double)worldObj.rand.nextFloat();
                double d2 = ((double)yCoord - (double)i) + (double)worldObj.rand.nextFloat();
                double d4 = (double)zCoord + (double)worldObj.rand.nextFloat();
                worldObj.spawnParticle("largesmoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
            }

            spawnPuddleBelow(i - 1);
            return;
        }
        if (!worldObj.isBlockSolidOnSide(xCoord, yCoord - i, zCoord, 1))
        {
            return;
        }
        worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord - i, zCoord, mod_ThaumCraft.thaumEffects.blockID, 0);
        ((TileEntityEffects)worldObj.getBlockTileEntity(xCoord, yCoord - i, zCoord)).delay += 100 + worldObj.rand.nextInt(100);
        worldObj.playSoundEffect(xCoord, yCoord - i, zCoord, "random.splash", 0.1F, 1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.4F);
        for (int i1 = 0; i1 < 20; i1++)
        {
            double d1 = (double)xCoord + (double)worldObj.rand.nextFloat();
            double d3 = ((double)yCoord - (double)i) + (double)worldObj.rand.nextFloat();
            double d5 = (double)zCoord + (double)worldObj.rand.nextFloat();
            worldObj.spawnParticle("largesmoke", d1, d3, d5, 0.0D, 0.0D, 0.0D);
        }
    }

    private void changeBlockBelow()
    {
        if (yCoord - 1 < 0 || worldObj.getBlockId(xCoord, yCoord - 1, zCoord) != Block.bedrock.blockID)
        {
            return;
        }
        int i = worldObj.rand.nextInt(100);
        if (i < 1)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.blockGold.blockID);
        }
        else if (i < 3)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.blockLapis.blockID);
        }
        else if (i < 6)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.blockSteel.blockID);
        }
        else if (i < 12)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.whiteStone.blockID);
        }
        else if (i < 24)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.glowStone.blockID);
        }
        else if (i < 48)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.cobblestoneMossy.blockID);
        }
        else if (i < 96)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.mycelium.blockID);
        }
        else
        {
            worldObj.setBlockWithNotify(xCoord, yCoord - 1, zCoord, Block.obsidian.blockID);
        }
        for (int j = 0; j < 20; j++)
        {
            double d = (double)xCoord + (double)worldObj.rand.nextFloat();
            double d1 = ((double)yCoord - 1.0D) + (double)worldObj.rand.nextFloat();
            double d2 = (double)zCoord + (double)worldObj.rand.nextFloat();
            worldObj.spawnParticle("largesmoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public void updateEntity()
    {
        rota += 2.0F;
        if (rota > 360F)
        {
            rota -= 360F;
        }
        bob++;
        if (bob > 30)
        {
            bob -= 30;
        }
        if (getBlockMetadata() == 0)
        {
            delay--;
            if (delay <= 0)
            {
                worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.fizz", 0.4F, 2.0F + worldObj.rand.nextFloat() * 0.4F);
                int i = worldObj.rand.nextInt(100);
                if (i < 5)
                {
                    spawnCrystal();
                }
                else if (i < 30)
                {
                    spawnSlime();
                }
                else if (i < 50)
                {
                    spawnPuddleBelow(1);
                }
                else if (i >= 85)
                {
                    if (i < 95)
                    {
                        worldObj.createExplosion(null, (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 1.0F);
                    }
                    else
                    {
                        changeBlockBelow();
                    }
                }
                worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
                invalidate();
            }
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        contains = nbttagcompound.getShort("Contains");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("Contains", (short)contains);
    }
}
