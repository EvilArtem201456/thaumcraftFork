package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;
import thaumcraft.codechicken.WRVector3;

public class TileEntityCrucible extends TileEntityThaum
{
    private float oldVis;
    public int delay;
    public int spilldelay;
    public float rota;
    public float rotb;
    public int orientation;
    int chargeup;
    public int moonphase;
    public boolean isPowering;

    public TileEntityCrucible()
    {
        oldVis = 0.0F;
        rota = 0.0F;
        rotb = 360F;
        chargeup = 0;
        isPowering = false;
        delay = 5;
        spilldelay = 0;
        currentVis = 0.0F;
        maxVis = 500F;
        isSource = true;
        isConnectable = true;
        orientation = -1;
    }

    public String getInvName()
    {
        return "Thaumic Crucible";
    }

    public void updateEntity()
    {
        super.updateEntity();
        rota += 2.0F;
        if (rota > 360F)
        {
            rota -= 360F;
        }
        rotb -= 4F;
        if (rotb < 0.0F)
        {
            rotb += 360F;
        }
        if (getBlockMetadata() < 2 || getBlockMetadata() == 8)
        {
            delay--;
            spilldelay--;
            if (currentVis > maxVis && spilldelay <= 0)
            {
                worldObj.playSoundEffect((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "liquid.water", 0.8F, worldObj.rand.nextFloat() * 1.0F + 0.5F);
                spilldelay = 30;
                if (currentVis >= maxVis + 50F && ThaumCraftCore.createThaumPuddle(worldObj, xCoord, yCoord, zCoord, 2))
                {
                    currentVis -= 50F;
                }
            }
            if (currentVis > maxVis)
            {
                currentVis -= 0.25F;
            }
            if (oldVis != currentVis && delay % 5 == 0)
            {
                worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
                oldVis = currentVis;
                if (getBlockMetadata() == 1 || getBlockMetadata() == 8)
                {
                    boolean flag = isPowering;
                    if ((double)currentVis >= (double)maxVis * 0.90000000000000002D)
                    {
                        isPowering = true;
                    }
                    else
                    {
                        isPowering = false;
                    }
                    if (flag != isPowering)
                    {
                        for (int k = -1; k < 2; k++)
                        {
                            for (int i1 = -1; i1 < 2; i1++)
                            {
                                for (int j2 = -1; j2 < 2; j2++)
                                {
                                    worldObj.markBlockAsNeedsUpdate(xCoord + k, yCoord + i1, zCoord + j2);
                                    worldObj.notifyBlocksOfNeighborChange(xCoord + k, yCoord + i1, zCoord + j2, 0);
                                }
                            }
                        }
                    }
                }
            }
            if (delay <= 0)
            {
                delay = 5;
                List list = getContents();
                if (list.size() > 0)
                {
                    EntityItem entityitem = (EntityItem)list.get(worldObj.rand.nextInt(list.size()));
                    ItemStack itemstack = entityitem.item;
                    if (canCook(itemstack))
                    {
                        float f = RecipesCrucible.smelting().getSmeltingResult(itemstack, true, false);
                        if (getBlockMetadata() != 8 || currentVis + f <= maxVis)
                        {
                            if (getBlockMetadata() == 8)
                            {
                                currentVis += f * 0.98F;
                            }
                            else
                            {
                                currentVis += f;
                            }
                            delay = 5 + Math.round(f / 10F);
                            itemstack.stackSize--;
                            if (itemstack.stackSize <= 0)
                            {
                                entityitem.setEntityDead();
                            }
                            worldObj.spawnParticle("largesmoke", entityitem.posX, entityitem.posY, entityitem.posZ, 0.0D, 0.0D, 0.0D);
                            worldObj.playSoundAtEntity(entityitem, "tcsound.bubbling", 0.2F, 1.0F + worldObj.rand.nextFloat() * 0.2F);
                        }
                    }
                    else
                    {
                        entityitem.motionX = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F;
                        entityitem.motionY = 0.2F + worldObj.rand.nextFloat() * 0.3F;
                        entityitem.motionZ = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F;
                        worldObj.playSoundAtEntity(entityitem, "random.pop", 0.5F, 2.0F + worldObj.rand.nextFloat() * 0.45F);
                        entityitem.delayBeforeCanPickup = 10;
                        entityitem.age = 0;
                    }
                }
            }
        }
        else if (getBlockMetadata() == 2)
        {
            moonphase = Math.abs(worldObj.func_40475_d(currentVis) - 4) + 1;
            int i = inAura ? moonphase : 0;
            byte byte2 = 6;
            for (int j1 = -byte2; j1 < byte2 + 1; j1++)
            {
                for (int k2 = -byte2; k2 < byte2 + 1; k2++)
                {
                    for (int k3 = -byte2; k3 < byte2 + 1; k3++)
                    {
                        TileEntity tileentity1 = worldObj.getBlockTileEntity(j1 + xCoord, k2 + yCoord, k3 + zCoord);
                        float f2 = 0.0F;
                        if (!(tileentity1 instanceof TileEntityCrucible) || tileentity1.getBlockMetadata() != 6 || !isObeliskComplete((TileEntityCrucible)tileentity1))
                        {
                            continue;
                        }
                        TileEntityCrucible tileentitycrucible = (TileEntityCrucible)tileentity1;
                        if ((double)tileentitycrucible.currentVis <= 0.01D)
                        {
                            continue;
                        }
                        tileentitycrucible.currentVis -= 0.01D;
                        i++;
                        char c1 = '\310';
                        if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
                        {
                            c1 = '\u0190';
                        }
                        if (worldObj.rand.nextInt(c1) == 0)
                        {
                            LightningBolt lightningbolt3 = new LightningBolt(worldObj, (double)tileentity1.xCoord + 0.5D, (double)tileentity1.yCoord + 0.80000000000000004D, (double)tileentity1.zCoord + 0.5D, (double)xCoord + 0.5D, (double)yCoord + 0.59999999999999998D, (double)zCoord + 0.5D, worldObj.rand.nextLong(), 9, 1.5F);
                            lightningbolt3.defaultFractal();
                            lightningbolt3.setType(3);
                            lightningbolt3.finalizeBolt();
                        }
                    }
                }
            }

            if (currentVis < maxVis)
            {
                currentVis += 0.001D * (double)i;
            }
            if (currentVis > maxVis)
            {
                currentVis -= maxVis;
                dispenseCrystal();
            }
        }
        else if (getBlockMetadata() == 6 && isObeliskComplete(this))
        {
            moonphase = Math.abs(worldObj.func_40475_d(currentVis) - 4) + 1;
            int j = inAura ? moonphase : 0;
            byte byte3 = 6;
            for (int k1 = -byte3; k1 < byte3 + 1; k1++)
            {
                for (int l2 = -byte3; l2 < byte3 + 1; l2++)
                {
                    for (int l3 = -byte3; l3 < byte3 + 1; l3++)
                    {
                        TileEntity tileentity2 = worldObj.getBlockTileEntity(k1 + xCoord, l2 + yCoord, l3 + zCoord);
                        float f3 = 0.0F;
                        if (!(tileentity2 instanceof TileEntityCrucible) || tileentity2.getBlockMetadata() != 6 || !isObeliskComplete((TileEntityCrucible)tileentity2))
                        {
                            continue;
                        }
                        j++;
                        char c = '\310';
                        if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
                        {
                            c = '\u0190';
                        }
                        if (worldObj.rand.nextInt(c) == 0)
                        {
                            LightningBolt lightningbolt2 = new LightningBolt(worldObj, (double)xCoord + 0.5D, (double)yCoord + 0.80000000000000004D, (double)zCoord + 0.5D, (double)tileentity2.xCoord + 0.5D, (double)tileentity2.yCoord + 0.80000000000000004D, (double)tileentity2.zCoord + 0.5D, worldObj.rand.nextLong(), 10, 2.0F, 5);
                            lightningbolt2.defaultFractal();
                            lightningbolt2.setType(3);
                            lightningbolt2.finalizeBolt();
                            worldObj.playSoundEffect((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, "tcsound.zap", 0.05F, 1.0F);
                        }
                    }
                }
            }

            if (currentVis < maxVis)
            {
                currentVis += 0.02D * (double)j;
            }
            if (currentVis > maxVis)
            {
                currentVis = maxVis;
            }
        }
        else if (getBlockMetadata() == 9 && isCircleComplete(this))
        {
            delay--;
            if (worldObj.getBlockId(xCoord, yCoord + 1, zCoord) == 0)
            {
                chargeup = 0;
            }
            if (currentVis < maxVis)
            {
                byte byte0 = 6;
                for (int l = -byte0; l < byte0 + 1; l++)
                {
                    for (int l1 = -1; l1 < 2; l1++)
                    {
                        for (int i3 = -byte0; i3 < byte0 + 1; i3++)
                        {
                            TileEntity tileentity = worldObj.getBlockTileEntity(l + xCoord, l1 + yCoord + 3, i3 + zCoord);
                            if (tileentity == null || !(tileentity instanceof TileEntityCrucible) || tileentity.getBlockMetadata() != 6)
                            {
                                continue;
                            }
                            float f1 = ((TileEntityCrucible)tileentity).subtractVis(1.0F);
                            if (f1 <= 0.0F)
                            {
                                continue;
                            }
                            currentVis += f1 / 10F;
                            if (currentVis > maxVis)
                            {
                                currentVis = maxVis;
                            }
                            byte byte5 = 25;
                            if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
                            {
                                byte5 = 50;
                            }
                            if (worldObj.rand.nextInt(byte5) == 0)
                            {
                                LightningBolt lightningbolt1 = new LightningBolt(worldObj, (double)tileentity.xCoord + 0.5D, (double)tileentity.yCoord + 0.80000000000000004D, (double)tileentity.zCoord + 0.5D, (float)xCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F, (float)yCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F, (float)zCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F, worldObj.rand.nextLong(), 9, 1.5F);
                                lightningbolt1.defaultFractal();
                                lightningbolt1.setType(3);
                                lightningbolt1.finalizeBolt();
                            }
                        }
                    }
                }
            }
            else if (isValidAltarBlock())
            {
                byte byte1 = 50;
                if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
                {
                    byte1 = 100;
                }
                if (chargeup < 90)
                {
                    byte byte4 = 6;
                    for (int i2 = -byte4; i2 < byte4 + 1; i2++)
                    {
                        for (int j3 = -1; j3 < 2; j3++)
                        {
                            for (int i4 = -byte4; i4 < byte4 + 1; i4++)
                            {
                                if (worldObj.getBlockId(xCoord + i2, yCoord + j3 + 3, zCoord + i4) == mod_ThaumCraft.thaumCrucible.blockID && worldObj.getBlockMetadata(xCoord + i2, yCoord + j3 + 3, zCoord + i4) == 6 && worldObj.rand.nextInt(byte1) <= (int)(0.4F * (float)chargeup))
                                {
                                    LightningBolt lightningbolt = new LightningBolt(worldObj, (float)xCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F + (float)i2, (float)(yCoord + j3 + 1) + worldObj.rand.nextFloat() * 3F, (float)zCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F + (float)i4, (float)xCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F, (float)yCoord + 1.3F + worldObj.rand.nextFloat() * 0.4F, (float)zCoord + 0.3F + worldObj.rand.nextFloat() * 0.4F, worldObj.rand.nextLong(), 9, 1.5F);
                                    lightningbolt.defaultFractal();
                                    lightningbolt.setType(3);
                                    lightningbolt.finalizeBolt();
                                }
                            }
                        }
                    }
                }
                if (chargeup == 0)
                {
                    worldObj.playSoundEffect((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "portal.trigger", 1.0F, 0.66F);
                }
                chargeup++;
                if (chargeup > 100)
                {
                    chargeup = 0;
                    if (processAltarBlock())
                    {
                        currentVis = 0.0F;
                    }
                }
            }
        }
    }

    boolean isValidAltarBlock()
    {
        int i = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);
        int j = worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord);
        if (i == Block.blockSteel.blockID || i == Block.slowSand.blockID)
        {
            return true;
        }
        if (i == mod_ThaumCraft.thaumEffects.blockID && j == 2)
        {
            TileEntityEffects tileentityeffects = (TileEntityEffects)worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
            if (tileentityeffects.contains == 86 || tileentityeffects.contains == 186)
            {
                return true;
            }
        }
        return i == mod_ThaumCraft.thaumCrucible.blockID && j == 7;
    }

    boolean processAltarBlock()
    {
        int i = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);
        int j = worldObj.getBlockMetadata(xCoord, yCoord + 1, zCoord);
        worldObj.playSoundEffect((double)xCoord + 0.5D, yCoord + 1, (double)zCoord + 0.5D, "ambient.weather.thunder", 10000F, 0.8F + worldObj.rand.nextFloat() * 0.2F);
        worldObj.playSoundEffect((double)xCoord + 0.5D, yCoord + 1, (double)zCoord + 0.5D, "random.explode", 1.0F, 0.5F + worldObj.rand.nextFloat() * 0.2F);
        ThaumCraftCore.poof(worldObj, xCoord, yCoord + 1, zCoord);
        byte byte0 = 20;
        if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
        {
            byte0 = 10;
        }
        for (int k = 0; k < byte0; k++)
        {
            LightningBolt lightningbolt = new LightningBolt(worldObj, new WRVector3((double)xCoord + 0.5D, (double)yCoord + 1.5D, (double)zCoord + 0.5D), new WRVector3(((double)xCoord + 0.5D + (double)(10F * worldObj.rand.nextFloat())) - 5D, ((double)yCoord + 1.5D + (double)(6F * worldObj.rand.nextFloat())) - 1.0D, ((double)zCoord + 0.5D + (double)(10F * worldObj.rand.nextFloat())) - 5D), worldObj.rand.nextLong());
            lightningbolt.defaultFractal();
            lightningbolt.setType(3);
            lightningbolt.finalizeBolt();
        }

        double d = (double)xCoord + 0.5D;
        double d1 = (double)yCoord + 1.5D;
        double d2 = (double)zCoord + 0.5D;
        if (i == Block.blockSteel.blockID)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord + 1, zCoord, 0);
            int l = 5 + worldObj.rand.nextInt(5);
            for (int i1 = 0; i1 < l; i1++)
            {
                EntityItem entityitem1 = new EntityItem(worldObj, d, d1, d2, new ItemStack(mod_ThaumCraft.thaumiumIngot, 1));
                entityitem1.motionX = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.25F;
                entityitem1.motionY = 0.10000000000000001D;
                entityitem1.motionZ = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.25F;
                worldObj.spawnEntityInWorld(entityitem1);
            }

            return true;
        }
        if (i == Block.slowSand.blockID)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord + 1, zCoord, 0);
            EntityGhast entityghast = new EntityGhast(worldObj);
            entityghast.setLocationAndAngles(d, d1 + 3D, d2, 0.0F, 0.0F);
            worldObj.spawnEntityInWorld(entityghast);
            return true;
        }
        if (i == mod_ThaumCraft.thaumCrucible.blockID && j == 7)
        {
            worldObj.setBlockWithNotify(xCoord, yCoord + 1, zCoord, 0);
            EntityThaumSlime entitythaumslime = new EntityThaumSlime(worldObj);
            entitythaumslime.setLocationAndAngles(d, d1 + 1.0D, d2, 0.0F, 0.0F);
            entitythaumslime.setSlimeSize(20);
            worldObj.spawnEntityInWorld(entitythaumslime);
            return true;
        }
        if (i == mod_ThaumCraft.thaumEffects.blockID && j == 2)
        {
            TileEntityEffects tileentityeffects = (TileEntityEffects)worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
            if (tileentityeffects.contains == 86)
            {
                worldObj.setBlockWithNotify(xCoord, yCoord + 1, zCoord, 0);
                EntityWispWild entitywispwild = new EntityWispWild(worldObj);
                entitywispwild.setLocationAndAngles(d, d1 + 1.0D, d2, 0.0F, 0.0F);
                switch (worldObj.rand.nextInt(4))
                {
                    case 0:
                        entitywispwild.type = 0;
                        break;

                    case 1:
                        entitywispwild.type = 1;
                        break;

                    case 2:
                        entitywispwild.type = 2;
                        break;

                    case 3:
                        entitywispwild.type = 5;
                        break;
                }
                worldObj.spawnEntityInWorld(entitywispwild);
                return true;
            }
            if (tileentityeffects.contains == 186)
            {
                worldObj.setBlockWithNotify(xCoord, yCoord + 1, zCoord, 0);
                EntityItem entityitem = new EntityItem(worldObj, d, d1, d2, new ItemStack(mod_ThaumCraft.talismanBlank, 1, 1));
                worldObj.spawnEntityInWorld(entityitem);
                return true;
            }
        }
        return false;
    }

    boolean isCircleComplete(TileEntityCrucible tileentitycrucible)
    {
        int i = 0;
        byte byte0 = 6;
        for (int j = -byte0; j < byte0 + 1; j++)
        {
            for (int k = -1; k < 2; k++)
            {
                for (int l = -byte0; l < byte0 + 1; l++)
                {
                    TileEntity tileentity = worldObj.getBlockTileEntity(j + tileentitycrucible.xCoord, k + tileentitycrucible.yCoord + 3, l + tileentitycrucible.zCoord);
                    if (tileentity != null && (tileentity instanceof TileEntityCrucible) && tileentity.getBlockMetadata() == 6)
                    {
                        i++;
                    }
                }
            }
        }

        return i > 3;
    }

    boolean isObeliskComplete(TileEntityCrucible tileentitycrucible)
    {
        boolean flag = true;
        for (int i = 1; i < 4; i++)
        {
            TileEntity tileentity = worldObj.getBlockTileEntity(tileentitycrucible.xCoord, tileentitycrucible.yCoord - i, tileentitycrucible.zCoord);
            if (tileentity == null || !(tileentity instanceof TileEntityCrucible))
            {
                flag = false;
            }
        }

        return flag;
    }

    private void dispenseCrystal()
    {
        int i = 0;
        int j = 0;
        byte byte0 = 0;
        if (orientation == 2)
        {
            j = 1;
            byte0 = 2;
        }
        else if (orientation == 0)
        {
            j = -1;
            byte0 = 3;
        }
        else if (orientation == 1)
        {
            i = 1;
            byte0 = 4;
        }
        else
        {
            i = -1;
            byte0 = 5;
        }
        double d = (double)xCoord + (double)i * 0.59999999999999998D + 0.5D;
        double d1 = (double)yCoord + 0.5D;
        double d2 = (double)zCoord + (double)j * 0.59999999999999998D + 0.5D;
        EntityItem entityitem = new EntityItem(worldObj, d, d1 - 0.29999999999999999D, d2, new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6));
        worldObj.spawnEntityInWorld(entityitem);
        TileEntity tileentity1 = worldObj.getBlockTileEntity(xCoord + i, yCoord, zCoord + j);
        if (!(tileentity1 instanceof IInventory) || !ThaumCraftCore.putIntoChest(xCoord + i, yCoord, zCoord + j, (IInventory)tileentity1, entityitem))
        {
            double d3 = worldObj.rand.nextDouble() * 0.025000000000000001D + 0.050000000000000003D;
            entityitem.motionX = (double)i * d3;
            entityitem.motionY = 0.075000000298023228D;
            entityitem.motionZ = (double)j * d3;
            entityitem.motionX += worldObj.rand.nextGaussian() * 0.0074999998323619366D * 6D;
            entityitem.motionY += worldObj.rand.nextGaussian() * 0.0074999998323619366D * 6D;
            entityitem.motionZ += worldObj.rand.nextGaussian() * 0.0074999998323619366D * 6D;
            worldObj.playAuxSFX(2000, xCoord, yCoord, zCoord, i + 1 + (j + 1) * 3);
        }
        worldObj.playSoundEffect((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, "random.levelup", 0.3F, 0.75F + worldObj.rand.nextFloat() * 0.5F);
    }

    private List getContents()
    {
        float f = 0.0F;
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, AxisAlignedBB.getBoundingBoxFromPool(xCoord, (float)yCoord + f, zCoord, (double)xCoord + 1.0D, ((double)yCoord + 1.0D) - (double)f, (double)zCoord + 1.0D));
        return list;
    }

    public boolean ejectContents(EntityPlayer entityplayer)
    {
        boolean flag = false;
        List list = getContents();
        for (int i = 0; i < list.size(); i++)
        {
            ((EntityItem)list.get(i)).setPosition(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
            flag = true;
        }

        return flag;
    }

    private boolean canCook(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return false;
        }
        float f = RecipesCrucible.smelting().getSmeltingResult(itemstack, true, false);
        return f != 0.0F;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        orientation = nbttagcompound.getShort("Orientation");
        currentVis = nbttagcompound.getFloat("Thaum");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("Thaum", currentVis);
        nbttagcompound.setShort("Orientation", (short)orientation);
    }
}
