package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class EntityHiddenTG extends Entity
{
    private float currentVis;
    public int duration;
    public int startDuration;
    public float rotation;
    private int explosionSize;
    private int auraSize;

    public EntityHiddenTG(World world, int i, double d, double d1, double d2)
    {
        super(world);
        preventEntitySpawning = true;
        duration = i;
        startDuration = i;
        posX = d;
        posY = d1;
        posZ = d2;
        explosionSize = startDuration / 4;
        auraSize = startDuration / 10;
        if (ThaumCraftCore.inAura((int)posX, (int)posY, (int)posZ))
        {
            auraSize += 4;
        }
        ignoreFrustumCheck = true;
    }

    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        if (duration > 0)
        {
            float f = (float)duration / ((float)startDuration / 3F);
            if (f > 1.0F)
            {
                f = 1.0F;
            }
            rotation += f;
            if (rotation > 180F)
            {
                rotation = rotation - 360F;
            }
            doSuckage();
            duration--;
        }
        else
        {
            doSpawnResult();
            setEntityDead();
        }
    }

    private void doSuckage()
    {
        int i = 999;
        int j = 0;
        int k = 0;
        int l = 0;
        byte byte0 = ((byte)(worldObj.rand.nextBoolean() ? 1 : -1));
        byte byte1 = ((byte)(worldObj.rand.nextBoolean() ? 1 : -1));
        byte byte2 = ((byte)(worldObj.rand.nextBoolean() ? 1 : -1));
        for (int i1 = -auraSize; i1 < auraSize + 1; i1++)
        {
            for (int l1 = -auraSize; l1 < auraSize + 1; l1++)
            {
                for (int k2 = -auraSize; k2 < auraSize + 1; k2++)
                {
                    if (worldObj.getBlockId((int)posX + i1 * byte0, (int)posY + l1 * byte1, (int)posZ + k2 * byte2) == 0)
                    {
                        continue;
                    }
                    int j3 = (int)getDistance(posX + (double)(i1 * byte0) + 0.5D, posY + (double)(l1 * byte1) + 0.5D, posZ + (double)(k2 * byte2) + 0.5D);
                    if (j3 < i && j3 <= auraSize)
                    {
                        i = j3;
                        j = (int)posX + i1 * byte0;
                        k = (int)posY + l1 * byte1;
                        l = (int)posZ + k2 * byte2;
                    }
                }
            }
        }

        if (i < 999)
        {
            int j1 = j;
            int i2 = k;
            int l2 = l;
            int k3 = worldObj.getBlockId(j1, i2, l2);
            if (k3 != Block.bedrock.blockID && Block.blocksList[k3].getExplosionResistance(this) < 100F)
            {
                Block.blocksList[k3].dropBlockAsItemWithChance(worldObj, j, k, l, worldObj.getBlockMetadata(j, k, l), 0.9F, 0);
                worldObj.setBlockWithNotify(j, k, l, 0);
                if (ModLoader.getMinecraftInstance().gameSettings.fancyGraphics && !mod_ThaumCraft.lowGfx)
                {
                    LightningBolt lightningbolt = new LightningBolt(worldObj, (double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, posX, posY, posZ, worldObj.rand.nextLong(), 6);
                    lightningbolt.defaultFractal();
                    lightningbolt.setType(3);
                    lightningbolt.finalizeBolt();
                }
            }
        }
        int k1 = MathHelper.floor_double(posX - (double)auraSize - 1.0D);
        int j2 = MathHelper.floor_double(posX + (double)auraSize + 1.0D);
        int i3 = MathHelper.floor_double(posY - (double)auraSize - 1.0D);
        int l3 = MathHelper.floor_double(posY + (double)auraSize + 1.0D);
        int i4 = MathHelper.floor_double(posZ - (double)auraSize - 1.0D);
        int j4 = MathHelper.floor_double(posZ + (double)auraSize + 1.0D);
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(k1, i3, i4, j2, l3, j4));
        for (int k4 = 0; k4 < list.size(); k4++)
        {
            Entity entity = (Entity)list.get(k4);
            double d = entity.getDistance(posX, posY, posZ) / (double)auraSize;
            double d1 = entity.posX - posX;
            double d2 = entity.posY - posY;
            double d3 = entity.posZ - posZ;
            double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
            d1 /= d4;
            d2 /= d4;
            d3 /= d4;
            double d5 = (1.0D - d) * 0.10000000000000001D;
            if (d5 < 0.0D)
            {
                d5 = 0.0D;
            }
            double d6 = d5;
            entity.motionX -= d1 * d6;
            entity.motionY -= d2 * d6;
            entity.motionZ -= d3 * d6;
        }

        k1 = MathHelper.floor_double(posX - 0.75D);
        j2 = MathHelper.floor_double(posX + 0.75D);
        i3 = MathHelper.floor_double(posY - 0.75D);
        l3 = MathHelper.floor_double(posY + 0.75D);
        i4 = MathHelper.floor_double(posZ - 0.75D);
        j4 = MathHelper.floor_double(posZ + 0.75D);
        list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(k1, i3, i4, j2, l3, j4));
        for (int l4 = 0; l4 < list.size(); l4++)
        {
            Entity entity1 = (Entity)list.get(l4);
            if (entity1 instanceof EntityThaumGrenade)
            {
                continue;
            }
            if (entity1 instanceof EntityItem)
            {
                int i5 = (int)RecipesCrucible.smelting().getSmeltingResult(((EntityItem)entity1).item, true, false);
                currentVis += i5;
                entity1.setEntityDead();
                worldObj.playSoundEffect(entity1.posX, entity1.posY, entity1.posZ, "random.fizz", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
                worldObj.spawnParticle("explode", entity1.posX, entity1.posY, entity1.posZ, 0.0D, 0.0D, 0.0D);
                continue;
            }
            entity1.attackEntityFrom(DamageSource.magic, 3);
            if (entity1 instanceof EntityLiving)
            {
                currentVis++;
            }
        }
    }

    private void doSpawnResult()
    {
        for (int i = 0; (float)i < currentVis / 100F; i++)
        {
            float f = 0.7F;
            double d = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.20000000000000001D;
            double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d1, posZ + d2, new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6));
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
            worldObj.playSoundAtEntity(entityitem, "random.pop", 0.4F, 2.0F + worldObj.rand.nextFloat() * 0.4F);
        }
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        return false;
    }

    protected void entityInit()
    {
    }

    public float getShadowSize()
    {
        return 0.1F;
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    }

    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    public boolean isInRangeToRenderVec3D(Vec3D vec3d)
    {
        return true;
    }
}
