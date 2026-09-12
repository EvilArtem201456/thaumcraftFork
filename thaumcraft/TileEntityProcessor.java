package thaumcraft;

import forge.ISidedInventory;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class TileEntityProcessor extends TileEntityThaum
    implements IInventory, ISidedInventory
{
    private int delay;
    private int minedelay;
    private float oldCook;
    public boolean repeat;
    public int orientation;
    public int singValUnst;
    public int singValStbl;
    public int storedEnergy;
    public int energyMax;
    private ItemStack infuserItemStacks[];
    public boolean isReceiving;
    public float infuserCookTime;
    public float currentItemCookCost;
    private int genloop;

    public TileEntityProcessor()
    {
        singValUnst = 200;
        singValStbl = 300;
        storedEnergy = 0;
        energyMax = 5000;
        infuserItemStacks = new ItemStack[11];
        infuserCookTime = 0.0F;
        currentVis = 0.0F;
        maxVis = 20F;
        isReceiving = false;
        isConnectable = true;
        isNode = false;
        isSource = false;
        repeat = false;
        orientation = -1;
    }

    public int getSizeInventory()
    {
        return infuserItemStacks.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return infuserItemStacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (infuserItemStacks[i] != null)
        {
            if (infuserItemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = infuserItemStacks[i];
                infuserItemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = infuserItemStacks[i].splitStack(j);
            if (infuserItemStacks[i].stackSize == 0)
            {
                infuserItemStacks[i] = null;
            }
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        infuserItemStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Thaumic Infuser";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        infuserItemStacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotThaumInfuser");
            if (byte0 >= 0 && byte0 < infuserItemStacks.length)
            {
                infuserItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        infuserCookTime = nbttagcompound.getFloat("CookTime");
        currentItemCookCost = nbttagcompound.getFloat("CookCost");
        currentVis = nbttagcompound.getFloat("Thaum");
        repeat = nbttagcompound.getBoolean("Repeat");
        orientation = nbttagcompound.getShort("Orientation");
        storedEnergy = nbttagcompound.getShort("Energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("CookTime", infuserCookTime);
        nbttagcompound.setFloat("CookCost", currentItemCookCost);
        nbttagcompound.setFloat("Thaum", currentVis);
        nbttagcompound.setBoolean("Repeat", repeat);
        nbttagcompound.setShort("Orientation", (short)orientation);
        nbttagcompound.setShort("Energy", (short)storedEnergy);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < infuserItemStacks.length; i++)
        {
            if (infuserItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotThaumInfuser", (byte)i);
                infuserItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getCookProgressScaled(int i)
    {
        return Math.round((infuserCookTime / currentItemCookCost) * (float)i);
    }

    public boolean isCooking()
    {
        return infuserCookTime > 0.0F;
    }

    public boolean isReceiving()
    {
        return isReceiving;
    }

    public void updateEntity()
    {
        boolean flag;
        super.updateEntity();
        flag = false;
        boolean flag1 = infuserCookTime > 0.0F;
        delay--;
        if (delay <= 0 && oldCook != infuserCookTime)
        {
            worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            oldCook = infuserCookTime;
        }
        if (delay <= 0)
        {
            delay = 5;
        }
        if (worldObj.multiplayerWorld)
        {
            return;
        }
        if (getBlockMetadata() < 2)
        {
            if (canProcess() && currentItemCookCost > 0.0F && !gettingPower())
            {
                float f = suckAmount;
                if (getBlockMetadata() == 1)
                {
                    f = (f * 2.0F) / 3F;
                }
                float f1 = emptyNeighbours(f);
                if (f1 > 0.0F)
                {
                    isReceiving = true;
                }
                infuserCookTime += f1;
            }
            if (infuserCookTime >= currentItemCookCost && flag1)
            {
                addProcessedItem();
                infuserCookTime = 0.0F;
                currentItemCookCost = 0.0F;
                isReceiving = false;
                worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            }
            if (currentItemCookCost != 0.0F && currentItemCookCost != getCookCost())
            {
                infuserCookTime = 0.0F;
                currentItemCookCost = 0.0F;
                isReceiving = false;
                worldObj.playSoundEffect((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, "random.fizz", 1.0F, 1.6F);
            }
            if (infuserCookTime == 0.0F && canProcess())
            {
                currentItemCookCost = getCookCost();
                worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            }
            if (flag1 != (infuserCookTime > 0.0F))
            {
                flag = true;
            }
            return;
        }
        if (getBlockMetadata() == 2)
        {
            if (!checkFocus())
            {
                orientation = -1;
            }
            else
            {
                minedelay++;
                if (minedelay > 4 && (int)currentItemCookCost == singValUnst)
                {
                    minedelay = 0;
                }
                if (minedelay > 3 && (int)currentItemCookCost == singValStbl)
                {
                    minedelay = 0;
                }
                if (infuserCookTime > 0.0F && gettingPower() && minedelay == 0)
                {
                    for (int i = 0; i < 4 && !minedBlock(); i++) { }
                    worldObj.playSoundEffect(xCoord, yCoord, zCoord, "mob.slimeattack", 0.4F, 0.1F + worldObj.rand.nextFloat() * 0.3F);
                    infuserCookTime--;
                }
                if (infuserCookTime == 0.0F && infuserItemStacks[9] != null && gettingPower() && (infuserItemStacks[9].isItemEqual(new ItemStack(mod_ThaumCraft.thaumGrenade, 1, 0)) || infuserItemStacks[9].isItemEqual(new ItemStack(mod_ThaumCraft.thaumGrenade, 1, 1))))
                {
                    if (infuserItemStacks[9].isItemEqual(new ItemStack(mod_ThaumCraft.thaumGrenade, 1, 0)))
                    {
                        currentItemCookCost = singValUnst;
                    }
                    else
                    {
                        currentItemCookCost = singValStbl;
                    }
                    infuserCookTime = currentItemCookCost;
                    infuserItemStacks[9].stackSize--;
                    if (infuserItemStacks[9].stackSize == 0)
                    {
                        infuserItemStacks[9] = null;
                    }
                }
                if (flag1 != (infuserCookTime < currentItemCookCost))
                {
                    flag = true;
                }
            }
            return;
        }
        if (getBlockMetadata() == 3)
        {
            TileEntityProcessor tileentityprocessor = null;
            switch (orientation)
            {
                case 0:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
                    break;

                case 1:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
                    break;

                case 2:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);
                    break;

                case 3:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);
                    break;

                case 4:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);
                    break;

                case 5:
                    tileentityprocessor = (TileEntityProcessor)worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);
                    break;
            }
            if (tileentityprocessor != null && tileentityprocessor.gettingPower() && tileentityprocessor.infuserCookTime > 0.0F)
            {
                rotation++;
                if (rotation > 360F)
                {
                    rotation -= 360F;
                }
                suckItems(tileentityprocessor.currentItemCookCost);
                isReceiving = true;
            }
            else
            {
                isReceiving = false;
            }
            return;
        }
        if (getBlockMetadata() != 4)
        {
            return;
        }
        rotation++;
        if (rotation > 360F)
        {
            rotation -= 360F;
        }
        if (!gettingPower())
        {
            if (currentVis < maxVis)
            {
                float f2 = suckAmount;
                if (currentVis + f2 > maxVis)
                {
                    f2 = maxVis - currentVis;
                }
                float f3 = emptyNeighbours(f2);
                if (f3 > 0.0F)
                {
                    isReceiving = true;
                    currentVis += f3;
                }
                else if (getVis(0.005F, true))
                {
                    currentVis += 0.005F;
                }
            }
            int j = 11 + Math.abs(worldObj.func_40475_d(currentVis) - 4);
            float f4 = 0.0006666667F * (float)j;
            if (currentVis >= f4 && storedEnergy + j <= energyMax)
            {
                storedEnergy += j;
                currentVis -= f4;
            }
            if (storedEnergy > energyMax)
            {
                storedEnergy = energyMax;
            }
        }
        if (genloop == 0 && false)
        {
            worldObj.playSoundEffect((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "tcsound.elecloop", 0.15F, 1.0F);
        }
        genloop++;
        if (genloop >= 60)
        {
            genloop = 0;
        }
        if (flag)
        {
            onInventoryChanged();
        }
    }

    private void suckItems(float f)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        byte byte0 = 3;
        if (f == (float)singValStbl)
        {
            byte0 = 5;
        }
        if (orientation == 0)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            k = -128;
        }
        else if (orientation == 1)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            l = 128;
        }
        else if (orientation == 2)
        {
            i = k = -byte0;
            j = l = byte0;
            i1 = -66;
        }
        else if (orientation == 3)
        {
            i = k = -byte0;
            j = l = byte0;
            j1 = 66;
        }
        else if (orientation == 4)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            i = -66;
        }
        else if (orientation == 5)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            j = 66;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, AxisAlignedBB.getBoundingBoxFromPool(xCoord + i, yCoord + k, zCoord + i1, (double)xCoord + 1.0D + (double)j, (double)yCoord + 1.0D + (double)l, (double)zCoord + 1.0D + (double)j1));
        for (int k1 = 0; k1 < list.size(); k1++)
        {
            EntityItem entityitem = (EntityItem)list.get(k1);
            if (!(entityitem instanceof EntityItem))
            {
                continue;
            }
            double d = entityitem.posX - (double)xCoord - 0.5D;
            double d1 = entityitem.posY - (double)yCoord - 0.5D;
            double d2 = entityitem.posZ - (double)zCoord - 0.5D;
            double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
            d /= d3;
            d1 /= d3;
            d2 /= d3;
            double d4 = 0.20000000000000001D;
            entityitem.motionX -= d * d4;
            entityitem.motionY -= d1 * d4;
            entityitem.motionZ -= d2 * d4;
            if (entityitem.motionX > 0.34999999999999998D)
            {
                entityitem.motionX = 0.34999999999999998D;
            }
            if (entityitem.motionX < -0.34999999999999998D)
            {
                entityitem.motionX = -0.34999999999999998D;
            }
            if (entityitem.motionY > 0.34999999999999998D)
            {
                entityitem.motionY = 0.34999999999999998D;
            }
            if (entityitem.motionY < -0.34999999999999998D)
            {
                entityitem.motionY = -0.34999999999999998D;
            }
            if (entityitem.motionZ > 0.34999999999999998D)
            {
                entityitem.motionZ = 0.34999999999999998D;
            }
            if (entityitem.motionZ < -0.34999999999999998D)
            {
                entityitem.motionZ = -0.34999999999999998D;
            }
            entityitem.motionX += (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.01F;
            entityitem.motionY += (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.01F;
            entityitem.motionZ += (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.01F;
            ThaumCraftCore.createPurpleSpellFX(worldObj, (float)entityitem.lastTickPosX, (float)entityitem.lastTickPosY + 0.1F, (float)entityitem.lastTickPosZ);
        }

        list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, AxisAlignedBB.getBoundingBoxFromPool((double)xCoord - 0.14999999999999999D, (double)yCoord - 0.14999999999999999D, (double)zCoord - 0.14999999999999999D, (double)xCoord + 1.1499999999999999D, (double)yCoord + 1.1499999999999999D, (double)zCoord + 1.1499999999999999D));
        for (int l1 = 0; l1 < list.size(); l1++)
        {
            EntityItem entityitem1 = (EntityItem)list.get(l1);
            if (entityitem1 instanceof EntityItem)
            {
                entityitem1.motionX = 0.0D;
                entityitem1.motionY = 0.0D;
                entityitem1.motionZ = 0.0D;
                ThaumCraftCore.createPurpleDustFX(worldObj, (float)entityitem1.posX, (float)entityitem1.posY, (float)entityitem1.posZ);
                switch (orientation)
                {
                    case 0:
                        ejectBoreItems(entityitem1, xCoord, yCoord + 2, zCoord, 0.0F, 0.1F, 0.0F);
                        break;

                    case 1:
                        ejectBoreItems(entityitem1, xCoord, yCoord - 2, zCoord, 0.0F, -0.1F, 0.0F);
                        break;

                    case 2:
                        ejectBoreItems(entityitem1, xCoord, yCoord, zCoord + 2, 0.0F, 0.0F, 0.1F);
                        break;

                    case 3:
                        ejectBoreItems(entityitem1, xCoord, yCoord, zCoord - 2, 0.0F, 0.0F, -0.1F);
                        break;

                    case 4:
                        ejectBoreItems(entityitem1, xCoord + 2, yCoord, zCoord, 0.1F, 0.0F, 0.0F);
                        break;

                    case 5:
                        ejectBoreItems(entityitem1, xCoord - 2, yCoord, zCoord, -0.1F, 0.0F, 0.0F);
                        break;
                }
            }
        }
    }

    private void ejectBoreItems(EntityItem entityitem, int i, int j, int k, float f, float f1, float f2)
    {
        TileEntity tileentity = worldObj.getBlockTileEntity(i, j, k);
        boolean placed = false;
        if (tileentity instanceof IInventory)
        {
            placed = ThaumCraftCore.putIntoChest(i, j, k, (IInventory)tileentity, entityitem);
        }
        if (!placed)
        {
            entityitem.setLocationAndAngles(((double)i + 0.5D) - (double)(f * 3F), ((double)j + 0.5D) - (double)(f1 * 3F), ((double)k + 0.5D) - (double)(f2 * 3F), 0.0F, 0.0F);
            entityitem.motionX = f;
            entityitem.motionY = f1;
            entityitem.motionZ = f2;
            worldObj.spawnParticle("smoke", entityitem.posX, entityitem.posY, entityitem.posZ, 0.0D, 0.10000000000000001D * (double)worldObj.rand.nextFloat(), 0.0D);
        }
    }

    private boolean minedBlock()
    {
        int i = 0;
        int j = 0;
        int k = 0;
        double d = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        byte byte0 = 2;
        if (currentItemCookCost == (float)singValStbl)
        {
            byte0 = 4;
        }
        if (orientation == 0 || orientation == 1)
        {
            i = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            k = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            if (orientation == 0)
            {
                j = -2;
                d1 = -1.5D;
            }
            else
            {
                j = 2;
                d1 = 1.5D;
            }
        }
        else if (orientation == 2 || orientation == 3)
        {
            i = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            j = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            if (orientation == 2)
            {
                k = -2;
                d2 = -1.5D;
            }
            else
            {
                k = 2;
                d2 = 1.5D;
            }
        }
        else if (orientation == 4 || orientation == 5)
        {
            k = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            j = worldObj.rand.nextInt(byte0) - worldObj.rand.nextInt(byte0);
            if (orientation == 4)
            {
                i = -2;
                d = -1.5D;
            }
            else
            {
                i = 2;
                d = 1.5D;
            }
        }
        do
        {
            int l = worldObj.getBlockId(xCoord + i, yCoord + j, zCoord + k);
            int i1 = worldObj.getBlockMetadata(xCoord + i, yCoord + j, zCoord + k);
            if (l != 0 && l != Block.bedrock.blockID && l != Block.waterStill.blockID && l != Block.waterMoving.blockID && l != mod_ThaumCraft.thaumEffects.blockID)
            {
                Block.blocksList[l].dropBlockAsItem(worldObj, xCoord + i, yCoord + j, zCoord + k, i1, 0);
                worldObj.setBlockWithNotify(xCoord + i, yCoord + j, zCoord + k, 0);
                ThaumCraftCore.poof(worldObj, xCoord + i, yCoord + j, zCoord + k);
                worldObj.playSoundEffect((double)(xCoord + i) + 0.5D, (double)(yCoord + j) + 0.5D, (double)(zCoord + k) + 0.5D, "step.gravel", 1.0F, 1.0F);
                if (ModLoader.getMinecraftInstance().gameSettings.fancyGraphics && !mod_ThaumCraft.lowGfx)
                {
                    double d3 = (double)(xCoord + i) + 0.5D;
                    double d4 = (double)(yCoord + j) + 0.5D;
                    double d5 = (double)(zCoord + k) + 0.5D;
                    if (getDistanceFrom((double)(xCoord + i) + 0.5D, (double)(yCoord + j) + 0.5D, (double)(zCoord + k) + 0.5D) > 100D)
                    {
                        if (orientation == 0)
                        {
                            d4 = (double)(yCoord - 10) + 0.5D;
                        }
                        if (orientation == 1)
                        {
                            d4 = (double)(yCoord + 10) + 0.5D;
                        }
                        if (orientation == 2)
                        {
                            d5 = (double)(zCoord - 10) + 0.5D;
                        }
                        if (orientation == 3)
                        {
                            d5 = (double)(zCoord + 10) + 0.5D;
                        }
                        if (orientation == 4)
                        {
                            d3 = (double)(xCoord - 10) + 0.5D;
                        }
                        if (orientation == 5)
                        {
                            d3 = (double)(xCoord + 10) + 0.5D;
                        }
                    }
                    LightningBolt lightningbolt = new LightningBolt(worldObj, d3, d4, d5, (double)xCoord + d + 0.5D, (double)yCoord + d1 + 0.5D, (double)zCoord + d2 + 0.5D, worldObj.rand.nextLong(), 3);
                    lightningbolt.defaultFractal();
                    lightningbolt.setType(3);
                    lightningbolt.finalizeBolt();
                }
                return true;
            }
            if (orientation == 0)
            {
                j--;
            }
            else if (orientation == 1)
            {
                j++;
            }
            else if (orientation == 2)
            {
                k--;
            }
            else if (orientation == 3)
            {
                k++;
            }
            else if (orientation == 4)
            {
                i--;
            }
            else if (orientation == 5)
            {
                i++;
            }
        }
        while (yCoord + j >= 0 && yCoord + j <= 127 && i <= 64 && i >= -64 && k <= 64 && k >= -64);
        return false;
    }

    private float getCookCost()
    {
        if (getBlockMetadata() == 0)
        {
            if (infuserItemStacks[10] == null)
            {
                return 0.0F;
            }
            ItemStack itemstack = new ItemStack(infuserItemStacks[10].getItem(), 1);
            if (infuserItemStacks[10].getMaxDamage() == 0)
            {
                itemstack = infuserItemStacks[10];
            }
            return (float)RecipesProcessor.infusing().getInfusingCost(infuserItemStacks[9], itemstack);
        }
        if (getBlockMetadata() == 1)
        {
            return (float)RecipesProcessor.infusing().getCopyCost(infuserItemStacks[9]);
        }
        else
        {
            return 0.0F;
        }
    }

    private void legacyFix()
    {
        if (infuserItemStacks[10] == null)
        {
            return;
        }
        if (infuserItemStacks[10].isItemEqual(new ItemStack(mod_ThaumCraft.thaumTemplate, 1, 2)))
        {
            ItemStack itemstack = infuserItemStacks[10];
            itemstack.stackSize--;
            if (itemstack.stackSize == 0)
            {
                itemstack = null;
            }
            if (itemstack != null)
            {
                net.minecraft.src.EntityPlayerSP entityplayersp = ModLoader.getMinecraftInstance().thePlayer;
                EntityItem entityitem = new EntityItem(worldObj, ((EntityPlayer) (entityplayersp)).posX, ((EntityPlayer) (entityplayersp)).posY, ((EntityPlayer) (entityplayersp)).posZ, itemstack);
                entityitem.delayBeforeCanPickup = 0;
                worldObj.spawnEntityInWorld(entityitem);
            }
            infuserItemStacks[10] = new ItemStack(mod_ThaumCraft.thaumTinker, 1);
        }
        if (infuserItemStacks[10].isItemEqual(new ItemStack(mod_ThaumCraft.thaumTemplate, 1, 3)))
        {
            ItemStack itemstack1 = infuserItemStacks[10];
            itemstack1.stackSize--;
            if (itemstack1.stackSize == 0)
            {
                itemstack1 = null;
            }
            if (itemstack1 != null)
            {
                net.minecraft.src.EntityPlayerSP entityplayersp1 = ModLoader.getMinecraftInstance().thePlayer;
                EntityItem entityitem1 = new EntityItem(worldObj, ((EntityPlayer) (entityplayersp1)).posX, ((EntityPlayer) (entityplayersp1)).posY, ((EntityPlayer) (entityplayersp1)).posZ, itemstack1);
                entityitem1.delayBeforeCanPickup = 0;
                worldObj.spawnEntityInWorld(entityitem1);
            }
            infuserItemStacks[10] = new ItemStack(mod_ThaumCraft.thaumMold, 1);
        }
    }

    private ItemStack getResultStack()
    {
        legacyFix();
        if (getBlockMetadata() == 0)
        {
            if (infuserItemStacks[10] == null)
            {
                return null;
            }
            ItemStack itemstack = new ItemStack(infuserItemStacks[10].getItem(), 1);
            if (infuserItemStacks[10].getMaxDamage() == 0)
            {
                itemstack = infuserItemStacks[10];
            }
            return RecipesProcessor.infusing().getInfusingResult(infuserItemStacks[9], itemstack);
        }
        if (getBlockMetadata() == 1)
        {
            return RecipesProcessor.infusing().getCopyResult(infuserItemStacks[9]);
        }
        else
        {
            return null;
        }
    }

    private boolean canProcess()
    {
        ItemStack itemstack = getResultStack();
        if (itemstack == null)
        {
            return false;
        }
        if (getBlockMetadata() == 0 && infuserItemStacks[10] != null && infuserItemStacks[10].getMaxDamage() != 0)
        {
            ItemStack itemstack1 = new ItemStack(infuserItemStacks[10].getItem(), 1);
            int j = RecipesProcessor.infusing().getTemplateDamage(infuserItemStacks[9], itemstack1);
            if (j + infuserItemStacks[10].getItemDamage() > infuserItemStacks[10].getMaxDamage())
            {
                return false;
            }
        }
        for (int i = 0; i < 9; i++)
        {
            if (infuserItemStacks[i] == null)
            {
                return true;
            }
            if (!infuserItemStacks[i].isItemEqual(itemstack))
            {
                continue;
            }
            int k = infuserItemStacks[i].stackSize + itemstack.stackSize;
            if (k <= getInventoryStackLimit() && k <= itemstack.getMaxStackSize())
            {
                return true;
            }
        }

        return false;
    }

    private void addProcessedItem()
    {
        int i = 0;
        if (!canProcess())
        {
            return;
        }
        if (getBlockMetadata() == 0 && infuserItemStacks[10] != null)
        {
            ItemStack itemstack = new ItemStack(infuserItemStacks[10].getItem(), 1);
            if (infuserItemStacks[10].getMaxDamage() == 0)
            {
                itemstack = infuserItemStacks[10];
            }
            i = RecipesProcessor.infusing().getTemplateDamage(infuserItemStacks[9], itemstack);
        }
        ItemStack itemstack1 = getResultStack();
        byte byte0 = 1;
        if (getBlockMetadata() == 1 && !repeat)
        {
            byte0 = 2;
        }
        label0:
        for (int j = 0; j < byte0; j++)
        {
            int k = 0;
            do
            {
                if (k >= 9)
                {
                    continue label0;
                }
                if (infuserItemStacks[k] == null)
                {
                    infuserItemStacks[k] = itemstack1.copy();
                    continue label0;
                }
                if (infuserItemStacks[k].isItemEqual(itemstack1) && infuserItemStacks[k].stackSize < itemstack1.getMaxStackSize())
                {
                    infuserItemStacks[k].stackSize += itemstack1.stackSize;
                    continue label0;
                }
                k++;
            }
            while (true);
        }

        if (getBlockMetadata() == 0 || getBlockMetadata() == 1 && !repeat)
        {
            infuserItemStacks[9].stackSize--;
            if (infuserItemStacks[9].stackSize <= 0)
            {
                infuserItemStacks[9] = null;
            }
        }
        if (getBlockMetadata() == 0)
        {
            if (i == 9999)
            {
                infuserItemStacks[10].stackSize--;
                if (infuserItemStacks[10].stackSize <= 0)
                {
                    infuserItemStacks[10] = null;
                }
            }
            else
            {
                infuserItemStacks[10].damageItem(i, ModLoader.getMinecraftInstance().thePlayer);
                if (infuserItemStacks[10].getItemDamage() >= infuserItemStacks[10].getMaxDamage())
                {
                    infuserItemStacks[10].stackSize--;
                }
                if (infuserItemStacks[10].stackSize <= 0)
                {
                    infuserItemStacks[10] = null;
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        else
        {
            return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
        }
    }

    public void closeChest()
    {
    }

    public void openChest()
    {
    }

    public int getStartInventorySide(int i)
    {
        if (getBlockMetadata() > 1)
        {
            return 0;
        }
        if (i == 0 && getBlockMetadata() == 0)
        {
            return 10;
        }
        return i != 1 ? 0 : 9;
    }

    public int getSizeInventorySide(int i)
    {
        if (getBlockMetadata() == 4)
        {
            return 0;
        }
        if (getBlockMetadata() > 1)
        {
            return 1;
        }
        return i != 0 && i != 1 ? 9 : 1;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    private boolean checkFocus()
    {
        if (orientation >= 0)
        {
            switch (orientation)
            {
                default:
                    break;

                case 0:
                    if (worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord) == 3)
                    {
                        return true;
                    }
                    break;

                case 1:
                    if (worldObj.getBlockId(xCoord, yCoord + 1, zCoord) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord) == 3)
                    {
                        return true;
                    }
                    break;

                case 2:
                    if (worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1) == 3)
                    {
                        return true;
                    }
                    break;

                case 3:
                    if (worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1) == 3)
                    {
                        return true;
                    }
                    break;

                case 4:
                    if (worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord) == 3)
                    {
                        return true;
                    }
                    break;

                case 5:
                    if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == mod_ThaumCraft.thaumProcessor.blockID && worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord) == 3)
                    {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}