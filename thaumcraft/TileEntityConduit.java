package thaumcraft;

import net.minecraft.src.*;

public class TileEntityConduit extends TileEntityThaum
{
    private int delay;
    private float oldVis;
    private boolean prevPower;

    public TileEntityConduit()
    {
        delay = 20;
        currentVis = 0.0F;
        maxVis = 200F;
        isSource = true;
        isConnectable = true;
        isPipe = false;
        isNode = false;
    }

    public String getInvName()
    {
        return "Thaumic Conduit";
    }

    public void updateEntity()
    {
        super.updateEntity();
        delay--;
        if (delay <= 0 && oldVis != currentVis && getBlockMetadata() == 0)
        {
            worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            oldVis = currentVis;
        }
        if (delay <= 0)
        {
            delay = 10;
        }
        if (getBlockMetadata() == 0)
        {
            float f = emptyNeighbours(suckAmount);
            if (f > 0.0F)
            {
                currentVis += f;
            }
        }
        else if (getBlockMetadata() == 1 || getBlockMetadata() == 2)
        {
            equalizeWithNeighbours();
        }
        if (getBlockMetadata() == 2 && gettingPower())
        {
            prevPower = true;
            valveOpen = false;
            worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
        }
        if (getBlockMetadata() == 2 && !gettingPower() && prevPower)
        {
            valveOpen = true;
            prevPower = false;
            worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        currentVis = nbttagcompound.getFloat("Thaum");
        valveOpen = nbttagcompound.getBoolean("ValveOpen");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("Thaum", currentVis);
        nbttagcompound.setBoolean("ValveOpen", valveOpen);
    }
}
