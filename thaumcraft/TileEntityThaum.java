package thaumcraft;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class TileEntityThaum extends TileEntity
{
    static class TPDest
    {
        public int x;
        public int y;
        public int z;
        public int rune;
        public int dimension;

        public TPDest(int i, int j, int k, int l, int i1)
        {
            x = i;
            y = j;
            z = k;
            rune = l;
            dimension = i1;
        }

        public TPDest(TileEntitySymbol symbol, int i, int j, int k, int l, int i1)
        {
            this(i, j, k, l, i1);
        }
    }

    private boolean invalidConnections;
    private ArrayList connections;
    protected static ArrayList TPDestinations = new ArrayList();
    public float currentVis;
    public float maxVis;
    public boolean isTransmitting;
    float suckAmount;
    private boolean newlyPlaced;
    protected boolean inAura;
    protected boolean skip;
    public boolean isSource;
    public boolean isConnectable;
    public boolean isPipe;
    public boolean isNode;
    protected float rotation;
    public boolean valveOpen;

    public TileEntityThaum()
    {
        suckAmount = 0.5F;
        skip = false;
        isSource = false;
        isConnectable = false;
        invalidConnections = true;
        newlyPlaced = true;
        inAura = false;
        isPipe = false;
        valveOpen = true;
    }

    public void invalidateConnections()
    {
        invalidConnections = true;
        connections = null;
    }

    public boolean addDestination(TPDest tpdest)
    {
        if (TPDestinations.size() > 0)
        {
            for (int i = 0; i < TPDestinations.size(); i++)
            {
                if (tpdest.x == ((TPDest)TPDestinations.get(i)).x && tpdest.y == ((TPDest)TPDestinations.get(i)).y && tpdest.z == ((TPDest)TPDestinations.get(i)).z && tpdest.dimension == ((TPDest)TPDestinations.get(i)).dimension)
                {
                    removeDestination(tpdest);
                }
            }
        }
        TPDestinations.add(tpdest);
        return true;
    }

    public void removeDestination(TPDest tpdest)
    {
        for (int i = 0; i < TPDestinations.size(); i++)
        {
            if (tpdest.x == ((TPDest)TPDestinations.get(i)).x && tpdest.y == ((TPDest)TPDestinations.get(i)).y && tpdest.z == ((TPDest)TPDestinations.get(i)).z && tpdest.dimension == ((TPDest)TPDestinations.get(i)).dimension)
            {
                TPDestinations.remove(i);
            }
        }
    }

    public TPDest getDestination(TPDest tpdest)
    {
        ArrayList arraylist = new ArrayList();
        for (int i = 0; i < TPDestinations.size(); i++)
        {
            if ((tpdest.x != ((TPDest)TPDestinations.get(i)).x || tpdest.y != ((TPDest)TPDestinations.get(i)).y || tpdest.z != ((TPDest)TPDestinations.get(i)).z || tpdest.dimension != ((TPDest)TPDestinations.get(i)).dimension) && tpdest.rune == ((TPDest)TPDestinations.get(i)).rune && worldObj.getBlockTileEntity(((TPDest)TPDestinations.get(i)).x, ((TPDest)TPDestinations.get(i)).y, ((TPDest)TPDestinations.get(i)).z) != null && !((TileEntityThaum)worldObj.getBlockTileEntity(((TPDest)TPDestinations.get(i)).x, ((TPDest)TPDestinations.get(i)).y, ((TPDest)TPDestinations.get(i)).z)).gettingPower() && ((TileEntitySymbol)worldObj.getBlockTileEntity(((TPDest)TPDestinations.get(i)).x, ((TPDest)TPDestinations.get(i)).y, ((TPDest)TPDestinations.get(i)).z)).cooldown == 0)
            {
                arraylist.add(TPDestinations.get(i));
            }
        }

        if (arraylist.size() > 0)
        {
            return (TPDest)arraylist.get(worldObj.rand.nextInt(arraylist.size()));
        }
        else
        {
            return null;
        }
    }

    public void invalidateNeighbourConnections()
    {
        int i = mod_ThaumCraft.thaumRange;
        for (int j = -i; j < i + 1; j++)
        {
            for (int k = -i; k < i + 1; k++)
            {
                for (int l = -i; l < i + 1; l++)
                {
                    if (k + yCoord < 0 || k + yCoord > 127)
                    {
                        continue;
                    }
                    TileEntity tileentity = worldObj.getBlockTileEntity(j + xCoord, k + yCoord, l + zCoord);
                    float f = 0.0F;
                    if (!(tileentity instanceof TileEntityThaum))
                    {
                        continue;
                    }
                    f = MathHelper.sqrt_double(tileentity.getDistanceFrom((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D));
                    if (f <= (float)i)
                    {
                        ((TileEntityThaum)tileentity).invalidateConnections();
                    }
                }
            }
        }
    }

    protected List getEntities(int i, int j)
    {
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        if (j == 0)
        {
            k = k1 = -i;
            l = l1 = i;
            i1 = -(i * 2);
        }
        else if (j == 1)
        {
            k = k1 = -i;
            l = l1 = i;
            j1 = i * 2;
        }
        else if (j == 2)
        {
            k = i1 = -i;
            l = j1 = i;
            k1 = -(i * 2);
        }
        else if (j == 3)
        {
            k = i1 = -i;
            l = j1 = i;
            l1 = i * 2;
        }
        else if (j == 4)
        {
            k1 = i1 = -i;
            l1 = j1 = i;
            k = -(i * 2);
        }
        else if (j == 5)
        {
            k1 = i1 = -i;
            l1 = j1 = i;
            l = i * 2;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(xCoord + k, yCoord + i1, zCoord + k1, (double)xCoord + 1.0D + (double)l, (double)yCoord + 1.0D + (double)j1, (double)zCoord + 1.0D + (double)l1));
        return list;
    }

    protected List getEntitiesSorted(int i, int j)
    {
        List list = getEntities(i, j);
        boolean flag = false;
        do
        {
            flag = false;
            int k = 0;
            do
            {
                if (k >= list.size() - 1)
                {
                    break;
                }
                Entity entity = (Entity)list.get(k);
                double d = getDistanceFrom(entity.posX, entity.posY, entity.posZ);
                Entity entity1 = (Entity)list.get(k + 1);
                double d1 = getDistanceFrom(entity1.posX, entity1.posY, entity1.posZ);
                if (d > d1)
                {
                    list.remove(k);
                    list.add(entity);
                    flag = true;
                    break;
                }
                k++;
            }
            while (true);
        }
        while (flag);
        return list;
    }

    private void calculateConnections()
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        int i = mod_ThaumCraft.thaumRange;
        for (int j = -i; j < i + 1; j++)
        {
            for (int k = -i; k < i + 1; k++)
            {
                for (int i1 = -i; i1 < i + 1; i1++)
                {
                    if (k + yCoord < 0 || k + yCoord > 127)
                    {
                        continue;
                    }
                    TileEntity tileentity1 = worldObj.getBlockTileEntity(j + xCoord, k + yCoord, i1 + zCoord);
                    float f1 = 0.0F;
                    if (!(tileentity1 instanceof TileEntityThaum))
                    {
                        continue;
                    }
                    f1 = MathHelper.sqrt_double(tileentity1.getDistanceFrom((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D));
                    if (f1 <= (float)i)
                    {
                        arraylist.add(tileentity1);
                        arraylist1.add(Float.valueOf(f1));
                    }
                }
            }
        }

        boolean flag = false;
        do
        {
            flag = false;
            int l = 0;
            do
            {
                if (l >= arraylist.size() - 1)
                {
                    break;
                }
                TileEntity tileentity = (TileEntity)arraylist.get(l);
                float f = ((Float)arraylist1.get(l)).floatValue();
                if (f > ((Float)arraylist1.get(l + 1)).floatValue())
                {
                    arraylist.remove(l);
                    arraylist1.remove(l);
                    arraylist.add(tileentity);
                    arraylist1.add(Float.valueOf(f));
                    flag = true;
                    break;
                }
                l++;
            }
            while (true);
        }
        while (flag);
        connections = arraylist;
        invalidConnections = false;
    }

    public boolean getVis(float f, boolean flag)
    {
        if (invalidConnections)
        {
            calculateConnections();
        }
        if (connections == null)
        {
            return false;
        }
        for (int i = 0; i < connections.size(); i++)
        {
            TileEntityThaum tileentitythaum = (TileEntityThaum)connections.get(i);
            if ((tileentitythaum.isNode || !flag) && tileentitythaum.currentVis >= f)
            {
                tileentitythaum.subtractVis(f, this);
                return true;
            }
        }

        return false;
    }

    public boolean checkVis(float f, boolean flag)
    {
        if (invalidConnections)
        {
            calculateConnections();
        }
        if (connections == null)
        {
            return false;
        }
        for (int i = 0; i < connections.size(); i++)
        {
            TileEntityThaum tileentitythaum = (TileEntityThaum)connections.get(i);
            if ((tileentitythaum.isNode || !flag) && tileentitythaum.currentVis >= f)
            {
                return true;
            }
        }

        return false;
    }

    public void addVis(float f)
    {
        currentVis += f;
        if (currentVis > maxVis)
        {
            currentVis = maxVis;
        }
    }

    public float subtractVis(float f, TileEntity tileentity)
    {
        if (currentVis < f)
        {
            f = currentVis;
        }
        currentVis -= f;
        if (f > 0.0F)
        {
            isTransmitting = true;
            byte byte0 = 4;
            if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
            {
                byte0 = 8;
            }
            if (worldObj.rand.nextInt(byte0) == 0)
            {
                ThaumCraftCore.spawnParticleTransfer(this, tileentity, false);
            }
        }
        return f;
    }

    public float subtractVis(float f)
    {
        if (currentVis < f)
        {
            f = currentVis;
        }
        currentVis -= f;
        if (f > 0.0F)
        {
            isTransmitting = true;
        }
        return f;
    }

    public float subtractVis(float f, EntityLiving entityliving)
    {
        if (currentVis < f)
        {
            f = currentVis;
        }
        currentVis -= f;
        if (f > 0.0F)
        {
            isTransmitting = true;
            byte byte0 = 4;
            if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
            {
                byte0 = 8;
            }
            if (worldObj.rand.nextInt(byte0) == 0)
            {
                ThaumCraftCore.spawnParticleTransfer(this, entityliving, false);
            }
        }
        return f;
    }

    protected void equalizeWithNeighbours()
    {
        if (!isConnectable || !valveOpen)
        {
            return;
        }
        ArrayList arraylist = new ArrayList();
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int l = -1; l <= 1; l++)
                {
                    if (i == 0 && j == 0 && l == 0 || i != 0 && l != 0 || i != 0 && j != 0 || j != 0 && l != 0 || j + yCoord < 0 || j + yCoord > 127)
                    {
                        continue;
                    }
                    TileEntity tileentity = worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + l);
                    if (!(tileentity instanceof TileEntityThaum))
                    {
                        continue;
                    }
                    TileEntityThaum tileentitythaum = (TileEntityThaum)tileentity;
                    if (tileentitythaum.isSource && !tileentitythaum.isPipe && tileentitythaum.currentVis >= suckAmount && currentVis + suckAmount <= maxVis && (!isPipe || currentVis / maxVis <= 0.33F || !tileentitythaum.isNode))
                    {
                        currentVis += tileentitythaum.subtractVis(suckAmount, this);
                    }
                    if (tileentitythaum.isPipe && tileentitythaum.valveOpen)
                    {
                        arraylist.add(tileentitythaum);
                    }
                }
            }
        }

        if (arraylist.size() > 0)
        {
            float f = currentVis;
            for (int k = 0; k < arraylist.size(); k++)
            {
                f += ((TileEntityThaum)arraylist.get(k)).currentVis;
            }

            f /= arraylist.size() + 1;
            byte byte0 = 4;
            if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
            {
                byte0 = 8;
            }
            for (int i1 = 0; i1 < arraylist.size(); i1++)
            {
                if ((double)(((TileEntityThaum)arraylist.get(i1)).currentVis - f) > 0.01D && worldObj.rand.nextInt(byte0) == 0)
                {
                    ThaumCraftCore.spawnParticleTransfer((TileEntity)arraylist.get(i1), this, false);
                }
                if ((double)(((TileEntityThaum)arraylist.get(i1)).currentVis - f) < -0.01D && worldObj.rand.nextInt(byte0) == 0)
                {
                    ThaumCraftCore.spawnParticleTransfer(this, (TileEntity)arraylist.get(i1), false);
                }
                ((TileEntityThaum)arraylist.get(i1)).currentVis = f;
            }

            currentVis = f;
        }
    }

    protected float emptyNeighbours(float f)
    {
        if (!isConnectable)
        {
            return 0.0F;
        }
        if (currentVis == maxVis)
        {
            return 0.0F;
        }
        float f1 = f;
        if (currentVis + f1 > maxVis)
        {
            f1 = maxVis - currentVis;
        }
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int k = -1; k <= 1; k++)
                {
                    if (j + yCoord < 0 || j + yCoord > 127 || i == 0 && j == 0 && k == 0 || i != 0 && k != 0 || i != 0 && j != 0 || j != 0 && k != 0)
                    {
                        continue;
                    }
                    TileEntity tileentity = worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if (!(tileentity instanceof TileEntityThaum))
                    {
                        continue;
                    }
                    TileEntityThaum tileentitythaum = (TileEntityThaum)tileentity;
                    if (!tileentitythaum.isSource || tileentitythaum.currentVis < f1 || isNode && tileentitythaum.isPipe && tileentitythaum.valveOpen && (double)(tileentitythaum.currentVis / tileentitythaum.maxVis) < 0.5D)
                    {
                        continue;
                    }
                    float f2 = tileentitythaum.subtractVis(f1, this);
                    if (f2 > 0.0F)
                    {
                        return f2;
                    }
                }
            }
        }

        return 0.0F;
    }

    public boolean canEntityBeSeen(Entity entity)
    {
        return worldObj.rayTraceBlocks_do(Vec3D.createVector((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), true) == null;
    }

    protected boolean gettingPower()
    {
        return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord + 1, zCoord);
    }

    public void updateEntity()
    {
        if (newlyPlaced)
        {
            newlyPlaced = false;
            if (ThaumCraftCore.inAura(xCoord, yCoord, zCoord))
            {
                inAura = true;
            }
            if ((this instanceof TileEntitySymbol) && getBlockMetadata() == 5)
            {
                addDestination(new TPDest(xCoord, yCoord, zCoord, ((TileEntitySymbol)this).getRune(), ModLoader.getMinecraftInstance().thePlayer.dimension));
                ThaumCraftCore.AddChunkToList(xCoord, zCoord);
                ((TileEntitySymbol)this).delay = worldObj.rand.nextInt(10) + 1;
            }
            else if ((this instanceof TileEntityConduit) && getBlockMetadata() == 0)
            {
                isNode = true;
                valveOpen = true;
            }
            else if ((this instanceof TileEntityConduit) && getBlockMetadata() == 1)
            {
                maxVis = 4F;
                isPipe = true;
                valveOpen = true;
            }
            else if ((this instanceof TileEntityConduit) && getBlockMetadata() == 2)
            {
                maxVis = 4F;
                isPipe = true;
            }
            else if ((this instanceof TileEntityConduit) && getBlockMetadata() == 3)
            {
                isNode = true;
                valveOpen = false;
                isConnectable = false;
                isSource = false;
                maxVis = 250F;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 1)
            {
                maxVis = 600F;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 8)
            {
                maxVis = 750F;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 2)
            {
                maxVis = 50F;
                isConnectable = false;
                isSource = false;
                isNode = true;
            }
            else if ((this instanceof TileEntityProcessor) && (getBlockMetadata() == 2 || getBlockMetadata() == 3))
            {
                isConnectable = false;
                isSource = false;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 9)
            {
                isConnectable = false;
                isNode = false;
                isSource = false;
                maxVis = 500F;
                valveOpen = false;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 6)
            {
                isConnectable = false;
                isNode = true;
                isSource = false;
                maxVis = 1.0F;
                valveOpen = false;
            }
            else if ((this instanceof TileEntityCrucible) && getBlockMetadata() < 6 && getBlockMetadata() > 2)
            {
                isConnectable = false;
                isNode = false;
                isSource = false;
                maxVis = 0.0F;
                valveOpen = false;
            }
            if ((this instanceof TileEntityCrucible) && getBlockMetadata() == 7)
            {
                isConnectable = false;
                isSource = false;
                valveOpen = false;
                isNode = false;
                maxVis = 0.0F;
            }
            else
            {
                rotation = worldObj.rand.nextFloat() * 360F;
            }
        }
    }
}