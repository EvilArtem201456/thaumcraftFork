package thaumcraft;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class TileEntitySymbol extends TileEntityThaum
{
    final int delays[] =
    {
        3, 3, 3, 3, 30, 0, 25, 1, 1, 5,
        4, 0, 0, 0, 0
    };
    public int tickDelay;
    public int delay;
    public int orientation;
    public int rune;
    public float pistExt;
    public int pistSize;
    public int cooldown;
    public int shootRange;
    public boolean lastState;
    public int lastAura;
    public boolean isPowering;

    public TileEntitySymbol()
    {
        lastState = false;
        lastAura = 0;
        isPowering = false;
        shootRange = 12;
        delay = 0;
        orientation = -1;
        rune = -1;
        pistExt = 0.0F;
        pistSize = 0;
        currentVis = 0.0F;
        cooldown = 0;
    }

    public String getInvName()
    {
        return "Thaumic Symbol";
    }

    public void placeRune(int i)
    {
        lastState = false;
        int j = rune;
        rune = i;
        addDestination(new TileEntityThaum.TPDest(this, xCoord, yCoord, zCoord, rune, ModLoader.getMinecraftInstance().thePlayer.dimension));
        if (j != -1)
        {
            float f = 0.7F;
            double d = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.20000000000000001D + 0.59999999999999998D;
            double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(worldObj, (double)xCoord + d, (double)yCoord + d1, (double)zCoord + d2, new ItemStack(mod_ThaumCraft.thaumRune, 1, j));
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        if (orientation == -1)
        {
            int i = 0;
            do
            {
                if (i >= 6)
                {
                    break;
                }
                Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)].onBlockPlaced(worldObj, xCoord, yCoord, zCoord, i);
                if (orientation > -1)
                {
                    break;
                }
                i++;
            }
            while (true);
        }
        rotation += 0.5F;
        if (rotation > 360F)
        {
            rotation -= 360F;
        }
        tickDelay = delays[getBlockMetadata()];
        if (cooldown > 0 && getBlockMetadata() == 5 && getVis(0.125F, true))
        {
            cooldown--;
        }
        if (delay > 0)
        {
            delay--;
        }
        else
        {
            if (getBlockMetadata() == 0 && !gettingPower())
            {
                delay = tickDelay;
                if (pushEntity(true, false) && getVis(0.05F, true))
                {
                    delay = 1;
                }
            }
            else if (getBlockMetadata() == 1 && !gettingPower())
            {
                delay = tickDelay;
                if (pushEntity(false, false) && getVis(0.05F, true))
                {
                    delay = 1;
                }
            }
            else if (getBlockMetadata() == 2 && !gettingPower())
            {
                delay = tickDelay;
                if (pushEntity(true, true) && getVis(0.1F, true))
                {
                    delay = 1;
                }
            }
            else if (getBlockMetadata() == 3 && !gettingPower())
            {
                delay = tickDelay;
                if (pushEntity(false, true) && getVis(0.1F, true))
                {
                    delay = 1;
                }
            }
            else if (getBlockMetadata() == 4 && !gettingPower())
            {
                delay = tickDelay;
                if (hasGoldRune())
                {
                    delay *= 0.66F;
                }
                if (fertilize())
                {
                    rotation += 1.5F;
                }
            }
            else if (getBlockMetadata() == 5 && !gettingPower())
            {
                delay = tickDelay;
                if (cooldown == 0 && onArch())
                {
                    teleport();
                }
            }
            else if (getBlockMetadata() == 6 && !gettingPower())
            {
                delay = tickDelay;
                if (till() && getVis(0.2F, true))
                {
                    delay *= 0.66F;
                }
            }
            if (getBlockMetadata() == 7 && !gettingPower())
            {
                delay = tickDelay;
                freeze();
            }
            if (getBlockMetadata() == 8 && !gettingPower())
            {
                delay = tickDelay;
                burn();
            }
            if (getBlockMetadata() == 9)
            {
                delay = tickDelay;
                boolean flag = isPowering;
                isPowering = detect();
                if (flag != isPowering)
                {
                    for (int j = -1; j < 2; j++)
                    {
                        for (int k = -1; k < 2; k++)
                        {
                            for (int l = -1; l < 2; l++)
                            {
                                worldObj.markBlockAsNeedsUpdate(xCoord + j, yCoord + k, zCoord + l);
                                worldObj.notifyBlocksOfNeighborChange(xCoord + j, yCoord + k, zCoord + l, 0);
                            }
                        }
                    }
                }
            }
            if (getBlockMetadata() == 10 && !gettingPower())
            {
                delay = tickDelay;
                shock();
                if (hasGoldRune())
                {
                    delay *= 0.66F;
                }
            }
        }
    }

    protected boolean onArch()
    {
        if (orientation == 2 && worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 1, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 1, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 2, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 2, zCoord + 1) == Block.obsidian.blockID && !worldObj.isBlockOpaqueCube(xCoord, yCoord - 1, zCoord + 1) && !worldObj.isBlockOpaqueCube(xCoord, yCoord - 2, zCoord + 1))
        {
            return true;
        }
        if (orientation == 3 && worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 1, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 1, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 2, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 2, zCoord - 1) == Block.obsidian.blockID && !worldObj.isBlockOpaqueCube(xCoord, yCoord - 1, zCoord - 1) && !worldObj.isBlockOpaqueCube(xCoord, yCoord - 2, zCoord - 1))
        {
            return true;
        }
        if (orientation == 4 && worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 1, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 1, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 2, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord + 1, yCoord - 2, zCoord + 1) == Block.obsidian.blockID && !worldObj.isBlockOpaqueCube(xCoord + 1, yCoord - 1, zCoord) && !worldObj.isBlockOpaqueCube(xCoord + 1, yCoord - 2, zCoord))
        {
            return true;
        }
        return orientation == 5 && worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 1, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 1, zCoord + 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 2, zCoord - 1) == Block.obsidian.blockID && worldObj.getBlockId(xCoord - 1, yCoord - 2, zCoord + 1) == Block.obsidian.blockID && !worldObj.isBlockOpaqueCube(xCoord - 1, yCoord - 1, zCoord) && !worldObj.isBlockOpaqueCube(xCoord - 1, yCoord - 2, zCoord);
    }

    private boolean teleport()
    {
        byte byte0 = 0;
        byte byte1 = 0;
        switch (orientation)
        {
            case 2:
                byte1 = 1;
                break;

            case 3:
                byte1 = -1;
                break;

            case 4:
                byte0 = 1;
                break;

            case 5:
                byte0 = -1;
                break;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(xCoord + byte0, yCoord - 2, zCoord + byte1, xCoord + byte0 + 1, yCoord, zCoord + byte1 + 1));
        int i = 0;
        if (i < list.size())
        {
            Entity entity = (Entity)list.get(i);
            TileEntityThaum.TPDest tpdest = getDestination(new TileEntityThaum.TPDest(this, xCoord, yCoord, zCoord, rune, ModLoader.getMinecraftInstance().thePlayer.dimension));
            if (tpdest != null)
            {
                TileEntitySymbol tileentitysymbol = (TileEntitySymbol)worldObj.getBlockTileEntity(tpdest.x, tpdest.y, tpdest.z);
                if (tileentitysymbol == null)
                {
                    return false;
                }
                if (entity instanceof EntityItem)
                {
                    tileentitysymbol.cooldown = 10;
                    tileentitysymbol.delay = 5;
                    cooldown = 10;
                    delay = 10;
                }
                if (entity instanceof EntityLiving)
                {
                    tileentitysymbol.cooldown = 50;
                    tileentitysymbol.delay = 5;
                    cooldown = 50;
                    delay = 10;
                }
                if (entity instanceof EntityMinecart)
                {
                    tileentitysymbol.cooldown = 20;
                    tileentitysymbol.delay = 5;
                    cooldown = 20;
                    delay = 10;
                }
                if ((entity instanceof EntityMinecart) && ((EntityMinecart)entity).riddenByEntity != null && (((EntityMinecart)entity).riddenByEntity instanceof EntityLiving))
                {
                    tileentitysymbol.cooldown = 60;
                    tileentitysymbol.delay = 5;
                    cooldown = 60;
                    delay = 10;
                }
                if ((entity instanceof EntityMinecart) && ((EntityMinecart)entity).riddenByEntity != null && (((EntityMinecart)entity).riddenByEntity instanceof EntityPlayer))
                {
                    tileentitysymbol.cooldown = 110;
                    tileentitysymbol.delay = 5;
                    cooldown = 110;
                    delay = 10;
                }
                if (entity instanceof EntityPlayer)
                {
                    tileentitysymbol.cooldown = 100;
                    tileentitysymbol.delay = 5;
                    cooldown = 100;
                    delay = 10;
                }
                float f = entity.rotationYaw;
                switch (tileentitysymbol.orientation)
                {
                    case 2:
                        f = 180F;
                        break;

                    case 3:
                        f = 0.0F;
                        break;

                    case 4:
                        f = 90F;
                        break;

                    case 5:
                        f = 270F;
                        break;
                }
                float f1 = MathHelper.cos((f / 180F) * 3.141593F);
                float f2 = MathHelper.sin((f / 180F) * 3.141593F);
                double d = entity.motionX * (double)f1 + entity.motionZ * (double)f2;
                double d1 = entity.motionZ * (double)f1 - entity.motionX * (double)f2;
                entity.motionX = d;
                entity.motionZ = -d1;
                ThaumCraftCore.poof(worldObj, (float)entity.posX - 0.5F, (float)entity.posY - 0.5F, (float)entity.posZ - 0.5F);
                worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                if (tileentitysymbol.orientation < 2 || !tileentitysymbol.onArch())
                {
                    entity.setLocationAndAngles((double)tileentitysymbol.xCoord + 0.5D, (double)tileentitysymbol.yCoord + 0.5D, (double)tileentitysymbol.zCoord + 0.5D, f, entity.rotationPitch);
                }
                else
                {
                    entity.setLocationAndAngles((double)tileentitysymbol.xCoord + 0.5D, (double)tileentitysymbol.yCoord - 1.8999999999999999D, (double)tileentitysymbol.zCoord + 0.5D, f, entity.rotationPitch);
                }
                ThaumCraftCore.poof(worldObj, (float)entity.posX - 0.5F, (float)entity.posY - 0.5F, (float)entity.posZ - 0.5F);
                worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean detect()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        List list = getEntities(byte0, orientation);
        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);
            if (canEntityBeSeen(entity) && ((entity instanceof EntityPlayer) || !hasGoldRune()) && ((entity instanceof EntityMob) || !hasRedstoneRune()) && ((entity instanceof EntityAnimal) || !hasLapisRune()) && ((entity instanceof EntityItem) || !hasFlintRune()))
            {
                return true;
            }
        }

        return false;
    }

    private boolean freeze()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        List list = getEntities(byte0, orientation);
        for (int i = 0; i < list.size(); i++)
        {
            if (!(list.get(i) instanceof EntityLiving))
            {
                continue;
            }
            EntityLiving entityliving = (EntityLiving)list.get(i);
            if (!canEntityBeSeen(entityliving) || (entityliving instanceof EntityPlayer) || (entityliving instanceof EntityTravelingTrunk) || !(entityliving instanceof EntityMob) && hasRedstoneRune() || !(entityliving instanceof EntityAnimal) && hasLapisRune())
            {
                continue;
            }
            if (getVis(0.2F, true))
            {
                entityliving.motionX *= 0.050000000000000003D;
                entityliving.motionY *= 0.050000000000000003D;
                entityliving.motionZ *= 0.050000000000000003D;
            }
            else
            {
                entityliving.motionX *= 0.20000000000000001D;
                entityliving.motionY *= 0.20000000000000001D;
                entityliving.motionZ *= 0.20000000000000001D;
            }
            if (hasGoldRune())
            {
                entityliving.motionY += 0.059999998658895493D;
                entityliving.onGround = false;
            }
            for (int j = 0; j < 2; j++)
            {
                ThaumCraftCore.spawnParticleFreeze(this, entityliving);
            }

            rotation += 1.5F;
            if (!hasDiamondRune())
            {
                return true;
            }
        }

        return false;
    }

    private boolean burn()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        List list = getEntities(byte0, orientation);
        for (int i = 0; i < list.size(); i++)
        {
            if (!(list.get(i) instanceof EntityLiving))
            {
                continue;
            }
            EntityLiving entityliving = (EntityLiving)list.get(i);
            if (!canEntityBeSeen(entityliving) || (entityliving instanceof EntityPlayer) || (entityliving instanceof EntityTravelingTrunk) || !(entityliving instanceof EntityMob) && hasRedstoneRune() || !(entityliving instanceof EntityAnimal) && hasLapisRune())
            {
                continue;
            }
            float f = 0.25F;
            byte byte1 = 1;
            if (hasGoldRune())
            {
                f = 0.5F;
                byte1 = 2;
            }
            if (!getVis(f, true))
            {
                continue;
            }
            entityliving.setFire(1);
            entityliving.attackEntityFrom(DamageSource.onFire, byte1);
            for (int j = 0; j < 2 + byte1; j++)
            {
                ThaumCraftCore.spawnParticleScorch(this, entityliving);
            }

            worldObj.playSoundEffect((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "tcsound.fireloop", 0.1F, 0.8F + worldObj.rand.nextFloat() * 0.2F);
            rotation += 1.5F;
            if (!hasDiamondRune())
            {
                return true;
            }
        }

        return false;
    }

    private boolean shock()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        List list = getEntities(byte0, orientation);
        for (int i = 0; i < list.size(); i++)
        {
            if (!(list.get(i) instanceof EntityLiving))
            {
                continue;
            }
            EntityLiving entityliving = (EntityLiving)list.get(i);
            if (!canEntityBeSeen(entityliving) || (entityliving instanceof EntityPlayer) || (entityliving instanceof EntityTravelingTrunk) || !(entityliving instanceof EntityMob) && hasRedstoneRune() || !(entityliving instanceof EntityAnimal) && hasLapisRune())
            {
                continue;
            }
            float f = 0.35F;
            if (!getVis(f, true))
            {
                continue;
            }
            worldObj.playSoundEffect((float)xCoord + 0.5F, (float)yCoord + 0.5F, (float)zCoord + 0.5F, "tcsound.shock", 1.0F, 1.0F);
            LightningBolt lightningbolt = new LightningBolt(worldObj, (double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, entityliving.posX, entityliving.boundingBox.minY + (double)(entityliving.height / 2.0F), entityliving.posZ, worldObj.rand.nextLong(), 3, 0.8F, 4);
            lightningbolt.defaultFractal();
            lightningbolt.setType(6);
            lightningbolt.finalizeBolt();
            entityliving.attackEntityFrom(DamageSource.magic, 3);
            entityliving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
            rotation += 1.5F;
            if (!hasDiamondRune())
            {
                return true;
            }
        }

        return false;
    }

    private boolean pushEntity(boolean flag, boolean flag1)
    {
        boolean flag2 = false;
        int i = hasIronRune() ? 6 : 3;
        List list = getEntities(i, orientation);
        for (int j = 0; j < list.size(); j++)
        {
            Entity entity = (Entity)list.get(j);
            if ((entity instanceof EntityPlayer) || (entity instanceof EntityTravelingTrunk) || !flag1 && (entity instanceof EntityLiving) || flag1 && !(entity instanceof EntityLiving) || flag1 && (entity instanceof EntityMob) && hasLapisRune() || flag1 && (entity instanceof EntityAnimal) && hasRedstoneRune() || (entity instanceof EntityArrow))
            {
                continue;
            }
            double d = entity.getDistance(xCoord, yCoord, zCoord) / ((double)i * 3D);
            if (d > 1.0D)
            {
                continue;
            }
            double d1 = entity.posX - (double)xCoord - 0.5D;
            double d2 = entity.posY - (double)yCoord - 0.5D;
            double d3 = entity.posZ - (double)zCoord - 0.5D;
            double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
            d1 /= d4;
            d2 /= d4;
            d3 /= d4;
            double d5 = (1.0D - d) * 0.20000000000000001D;
            double d6 = d5;
            if (flag)
            {
                entity.motionX -= d1 * d6;
                if (hasGoldRune())
                {
                    entity.motionY -= d2 * d6;
                }
                entity.motionZ -= d3 * d6;
            }
            else
            {
                entity.motionX += d1 * d6;
                if (hasGoldRune())
                {
                    entity.motionY += d2 * d6;
                }
                entity.motionZ += d3 * d6;
            }
            flag2 = true;
        }

        if (flag2)
        {
            rotation += 1.5F;
        }
        return flag2;
    }

    protected void attemptItemPickup(EntityItem entityitem)
    {
        ItemStack itemstack = entityitem.item;
        label0:
        for (int i = -1; i <= 1; i++)
        {
            int j = -1;
            do
            {
                if (j > 1)
                {
                    continue label0;
                }
                label1:
                for (int k = -1; k <= 1; k++)
                {
                    if (i == 0 && j == 0 && k == 0)
                    {
                        continue;
                    }
                    TileEntity tileentity = worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if (!(tileentity instanceof TileEntityChest))
                    {
                        continue;
                    }
                    TileEntityChest tileentitychest = (TileEntityChest)tileentity;
                    do
                    {
                        int l = 0;
                        do
                        {
                            if (l >= tileentitychest.getSizeInventory())
                            {
                                break;
                            }
                            if (tileentitychest.getStackInSlot(l) == null)
                            {
                                if (getVis(0.1F, true))
                                {
                                    tileentitychest.setInventorySlotContents(l, itemstack);
                                    entityitem.setEntityDead();
                                    worldObj.playSoundAtEntity(entityitem, "random.pop", 0.5F, 2.0F + worldObj.rand.nextFloat() * 0.45F);
                                    ThaumCraftCore.poof(worldObj, (float)entityitem.posX - 0.5F, (float)entityitem.posY - 0.5F, (float)entityitem.posZ - 0.5F);
                                    break label0;
                                }
                            }
                            else if (tileentitychest.getStackInSlot(l).isItemEqual(itemstack) && tileentitychest.getStackInSlot(l).stackSize + itemstack.stackSize <= itemstack.getMaxStackSize() && getVis(0.1F, true))
                            {
                                itemstack.stackSize += tileentitychest.getStackInSlot(l).stackSize;
                                tileentitychest.setInventorySlotContents(l, itemstack);
                                entityitem.setEntityDead();
                                worldObj.playSoundAtEntity(entityitem, "random.pop", 0.5F, 2.0F + worldObj.rand.nextFloat() * 0.45F);
                                ThaumCraftCore.poof(worldObj, (float)entityitem.posX - 0.5F, (float)entityitem.posY - 0.5F, (float)entityitem.posZ - 0.5F);
                                break label0;
                            }
                            l++;
                        }
                        while (true);
                        if (tileentitychest != (TileEntityChest)tileentity)
                        {
                            continue label1;
                        }
                        if (tileentitychest.adjacentChestXNeg != null)
                        {
                            tileentitychest = tileentitychest.adjacentChestXNeg;
                        }
                        else if (tileentitychest.adjacentChestXPos != null)
                        {
                            tileentitychest = tileentitychest.adjacentChestXPos;
                        }
                        else if (tileentitychest.adjacentChestZNeg != null)
                        {
                            tileentitychest = tileentitychest.adjacentChestZNeg;
                        }
                        else if (tileentitychest.adjacentChestZPos != null)
                        {
                            tileentitychest = tileentitychest.adjacentChestZPos;
                        }
                        else
                        {
                            tileentitychest = null;
                        }
                    }
                    while (tileentitychest != (TileEntityChest)tileentity && tileentitychest != null);
                }

                j++;
            }
            while (true);
        }
    }

    private boolean fertilize()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = -1;
        try
        {
            Class class1 = Class.forName("RedPowerWorld");
            k1 = ((Block)class1.getField("blockCrops").get(class1)).blockID;
        }
        catch (Exception exception) { }
        if (orientation == 0)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            k = -(byte0 * 2);
        }
        else if (orientation == 1)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            l = byte0 * 2;
        }
        else if (orientation == 2)
        {
            i = k = -byte0;
            j = l = byte0;
            i1 = -(byte0 * 2);
        }
        else if (orientation == 3)
        {
            i = k = -byte0;
            j = l = byte0;
            j1 = byte0 * 2;
        }
        else if (orientation == 4)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            i = -(byte0 * 2);
        }
        else if (orientation == 5)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            j = byte0 * 2;
        }
        for (int l1 = i; l1 <= j; l1++)
        {
            for (int i2 = k; i2 <= l; i2++)
            {
                for (int j2 = i1; j2 <= j1; j2++)
                {
                    if (worldObj.getBlockLightValue(xCoord + l1, yCoord + i2 + 1, zCoord + j2) < 8)
                    {
                        continue;
                    }
                    int k2 = worldObj.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2);
                    int l2 = worldObj.getBlockId(xCoord + l1, yCoord + i2 + 1, zCoord + j2);
                    int i3 = worldObj.getBlockId(xCoord + l1, (yCoord + i2) - 1, zCoord + j2);
                    if ((k2 == Block.melonStem.blockID || k2 == Block.pumpkinStem.blockID) && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) < 7)
                    {
                        if (worldObj.rand.nextInt(10) == 0 && !worldObj.multiplayerWorld && getVis(0.2F, false))
                        {
                            worldObj.setBlockMetadataWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) + 1);
                            ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                            return true;
                        }
                        continue;
                    }
                    if ((k2 == Block.melonStem.blockID || k2 == Block.pumpkinStem.blockID) && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) == 7)
                    {
                        int j3 = Block.melon.blockID;
                        if (k2 != Block.melonStem.blockID)
                        {
                            j3 = Block.pumpkin.blockID;
                        }
                        if (worldObj.rand.nextInt(25) != 0)
                        {
                            continue;
                        }
                        int l3 = worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2);
                        if (l3 < 7)
                        {
                            l3++;
                            worldObj.setBlockMetadataWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, l3);
                            continue;
                        }
                        if (worldObj.getBlockId((xCoord + l1) - 1, yCoord + i2, zCoord + j2) == j3 || worldObj.getBlockId(xCoord + l1 + 1, yCoord + i2, zCoord + j2) == j3 || worldObj.getBlockId(xCoord + l1, yCoord + i2, (zCoord + j2) - 1) == j3 || worldObj.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2 + 1) == j3)
                        {
                            continue;
                        }
                        int j4 = worldObj.rand.nextInt(4);
                        int k4 = xCoord + l1;
                        int l4 = zCoord + j2;
                        if (j4 == 0)
                        {
                            k4--;
                        }
                        if (j4 == 1)
                        {
                            k4++;
                        }
                        if (j4 == 2)
                        {
                            l4--;
                        }
                        if (j4 == 3)
                        {
                            l4++;
                        }
                        if (worldObj.getBlockId(k4, yCoord + i2, l4) == 0 && worldObj.getBlockId(k4, (yCoord + i2) - 1, l4) == Block.tilledField.blockID && getVis(1.0F, false))
                        {
                            worldObj.setBlockWithNotify(k4, yCoord + i2, l4, j3);
                            ThaumCraftCore.poof(worldObj, k4, yCoord + i2, l4);
                            return true;
                        }
                        continue;
                    }
                    if (k2 == Block.crops.blockID && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) != 7)
                    {
                        if (worldObj.rand.nextInt(10) == 0 && !worldObj.multiplayerWorld && getVis(0.2F, false))
                        {
                            worldObj.setBlockMetadataWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) + 1);
                            ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                            return true;
                        }
                        continue;
                    }
                    if ((k2 == Block.reed.blockID || k2 == Block.cactus.blockID) && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) < 15 && worldObj.isAirBlock(xCoord + l1, yCoord + i2 + 1, zCoord + j2))
                    {
                        int k3;
                        for (k3 = 1; worldObj.getBlockId(xCoord + l1, (yCoord + i2) - k3, zCoord + j2) == k2; k3++) { }
                        if (k3 >= 3)
                        {
                            continue;
                        }
                        int i4 = worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2);
                        if (i4 < 15 && worldObj.rand.nextInt(10) == 0 && getVis(0.1F, false))
                        {
                            worldObj.setBlockMetadataWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, i4 + 1);
                            ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                            return true;
                        }
                        continue;
                    }
                    if (k1 > -1 && worldObj.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2) == k1 && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) < 4)
                    {
                        if (worldObj.rand.nextInt(10) != 0 || worldObj.multiplayerWorld || !getVis(0.2F, false))
                        {
                            continue;
                        }
                        worldObj.setBlockMetadataWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) + 1);
                        ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                        if (worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) == 4)
                        {
                            worldObj.setBlockAndMetadataWithNotify(xCoord + l1, yCoord + i2 + 1, zCoord + j2, k1, 5);
                            ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2 + 1, zCoord + j2);
                        }
                        return true;
                    }
                    if (worldObj.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2) == Block.cobblestone.blockID && worldObj.rand.nextInt(50) == 0 && getVis(0.5F, true))
                    {
                        worldObj.setBlockWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, Block.cobblestoneMossy.blockID);
                        ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean till()
    {
        byte byte0 = ((byte)(hasIronRune() ? 6 : 3));
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        if (orientation == 0)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            k = -(byte0 * 2);
        }
        else if (orientation == 1)
        {
            i = i1 = -byte0;
            j = j1 = byte0;
            l = byte0 * 2;
        }
        else if (orientation == 2)
        {
            i = k = -byte0;
            j = l = byte0;
            i1 = -(byte0 * 2);
        }
        else if (orientation == 3)
        {
            i = k = -byte0;
            j = l = byte0;
            j1 = byte0 * 2;
        }
        else if (orientation == 4)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            i = -(byte0 * 2);
        }
        else if (orientation == 5)
        {
            i1 = k = -byte0;
            j1 = l = byte0;
            j = byte0 * 2;
        }
        int k1 = -1;
        try
        {
            Class class1 = Class.forName("RedPowerWorld");
            k1 = ((Block)class1.getField("blockCrops").get(class1)).blockID;
        }
        catch (Exception exception) { }
        for (int l1 = i; l1 <= j; l1++)
        {
            for (int i2 = k; i2 <= l; i2++)
            {
                for (int j2 = i1; j2 <= j1; j2++)
                {
                    int k2 = worldObj.getBlockId(xCoord + l1, yCoord + i2, zCoord + j2);
                    int l2 = worldObj.getBlockId(xCoord + l1, yCoord + i2 + 1, zCoord + j2);
                    int i3 = worldObj.getBlockId(xCoord + l1, (yCoord + i2) - 1, zCoord + j2);
                    if ((k2 == Block.dirt.blockID || k2 == Block.grass.blockID) && l2 == 0 && worldObj.rand.nextInt(10) == 0)
                    {
                        worldObj.setBlockWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, Block.tilledField.blockID);
                        ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                        return true;
                    }
                    if ((hasDiamondRune() && worldObj.rand.nextInt(10) == 0 && (k2 == Block.crops.blockID && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) == 7 || k2 == Block.melon.blockID) || k2 == Block.tallGrass.blockID || k2 == Block.pumpkin.blockID || k2 == Block.plantYellow.blockID || k2 == Block.plantRed.blockID || k2 == k1 && worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2) == 4 || k2 == Block.plantRed.blockID || k2 == Block.reed.blockID && i3 == Block.reed.blockID || k2 == Block.cactus.blockID && i3 == Block.cactus.blockID) && getVis(0.25F, false))
                    {
                        Block.blocksList[k2].dropBlockAsItem(worldObj, xCoord + l1, yCoord + i2, zCoord + j2, worldObj.getBlockMetadata(xCoord + l1, yCoord + i2, zCoord + j2), 0);
                        worldObj.setBlockWithNotify(xCoord + l1, yCoord + i2, zCoord + j2, 0);
                        ThaumCraftCore.poof(worldObj, xCoord + l1, yCoord + i2, zCoord + j2);
                        rotation += 1.5F;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int getRune()
    {
        return rune;
    }

    protected boolean hasIronRune()
    {
        return rune == 0;
    }

    protected boolean hasGoldRune()
    {
        return rune == 1;
    }

    protected boolean hasDiamondRune()
    {
        return rune == 2;
    }

    protected boolean hasFlintRune()
    {
        return rune == 3;
    }

    protected boolean hasRedstoneRune()
    {
        return rune == 4;
    }

    protected boolean hasLapisRune()
    {
        return rune == 5;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        orientation = nbttagcompound.getShort("Orientation");
        rune = nbttagcompound.getShort("Rune");
        currentVis = nbttagcompound.getFloat("Thaum");
        cooldown = nbttagcompound.getShort("cooldown");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("Orientation", (short)orientation);
        nbttagcompound.setShort("Rune", (short)rune);
        nbttagcompound.setFloat("Thaum", currentVis);
        nbttagcompound.setShort("cooldown", (short)cooldown);
    }
}
