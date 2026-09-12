package thaumcraft;

import forge.ISpecialResistance;
import java.util.*;
import net.minecraft.src.*;

public class EntityThaumGrenade extends EntityItem
{
    double bounceFactor;
    int fuse;
    int fuse2;
    int fusemax;
    boolean exploded;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set destroyedBlockPositions;
    float thaumGathered;
    boolean overcharged;

    public EntityThaumGrenade(World world)
    {
        super(world);
        thaumGathered = 0.0F;
        setSize(0.5F, 0.5F);
        yOffset = height / 2.0F;
        bounceFactor = 0.25D;
        exploded = false;
        fuse = 0;
        fuse2 = 0;
        destroyedBlockPositions = new HashSet();
        worldObj = world;
        explosionSize = 2.0F;
    }

    public EntityThaumGrenade(World world, Entity entity, boolean flag)
    {
        this(world);
        setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * 3.141593F) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * 3.141593F) / 180F);
        motionX = 0.5D * d * (double)MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);
        motionY = -0.5D * (double)MathHelper.sin((entity.rotationPitch / 180F) * 3.141593F);
        motionZ = 0.5D * d1 * (double)MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);
        setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        overcharged = flag;
        if (overcharged)
        {
            bounceFactor = 0.5D;
        }
        item = new ItemStack(Item.itemsList[ItemThaumGrenade.grenadeID], 1, overcharged ? 1 : 0);
        if (overcharged)
        {
            explosionSize = 4F;
        }
        fuse = 40;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void onUpdate()
    {
        if (fuse-- == 0)
        {
            explode();
        }
        if (fuse > 0)
        {
            double d = motionX;
            double d1 = motionY;
            double d2 = motionZ;
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            moveEntity(motionX, motionY, motionZ);
            if (motionX != d)
            {
                motionX = -bounceFactor * d;
            }
            if (motionY != d1)
            {
                motionY = -bounceFactor * d1;
            }
            else
            {
                motionY -= 0.040000000000000001D;
            }
            if (motionZ != d2)
            {
                motionZ = -bounceFactor * d2;
            }
            motionX *= 0.98999999999999999D;
            motionY *= 0.98999999999999999D;
            motionZ *= 0.98999999999999999D;
        }
    }

    public void doExplosion()
    {
        worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        worldObj.spawnParticle("largeexplode", explosionX, explosionY, explosionZ, 0.0D, 0.0D, 0.0D);
        float f = explosionSize;
        int i = 16;
        for (int j = 0; j < i; j++)
        {
            for (int l = 0; l < i; l++)
            {
                label0:
                for (int j1 = 0; j1 < i; j1++)
                {
                    if (j != 0 && j != i - 1 && l != 0 && l != i - 1 && j1 != 0 && j1 != i - 1)
                    {
                        continue;
                    }
                    double d = ((float)j / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d1 = ((float)l / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d2 = ((float)j1 / ((float)i - 1.0F)) * 2.0F - 1.0F;
                    double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
                    d /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    float f1 = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                    double d5 = explosionX;
                    double d7 = explosionY;
                    double d10 = explosionZ;
                    float f2 = 0.3F;
                    do
                    {
                        if (f1 <= 0.0F)
                        {
                            continue label0;
                        }
                        int i4 = MathHelper.floor_double(d5);
                        int j4 = MathHelper.floor_double(d7);
                        int k4 = MathHelper.floor_double(d10);
                        int l4 = worldObj.getBlockId(i4, j4, k4);
                        if (l4 > 0)
                        {
                            if (Block.blocksList[l4] instanceof ISpecialResistance)
                            {
                                ISpecialResistance ispecialresistance = (ISpecialResistance)Block.blocksList[l4];
                                f1 -= (ispecialresistance.getSpecialExplosionResistance(worldObj, i4, j4, k4, explosionX, explosionY, explosionZ, this) + 0.3F) * f2;
                            }
                            else
                            {
                                f1 -= (Block.blocksList[l4].getExplosionResistance(this) + 0.3F) * f2;
                            }
                        }
                        if (f1 > 0.0F)
                        {
                            destroyedBlockPositions.add(new ChunkPosition(i4, j4, k4));
                        }
                        d5 += d * (double)f2;
                        d7 += d1 * (double)f2;
                        d10 += d2 * (double)f2;
                        f1 -= f2 * 0.75F;
                    }
                    while (true);
                }
            }
        }

        explosionSize *= 2.0F;
        int k = MathHelper.floor_double(explosionX - (double)explosionSize - 1.0D);
        int i1 = MathHelper.floor_double(explosionX + (double)explosionSize + 1.0D);
        int k1 = MathHelper.floor_double(explosionY - (double)explosionSize - 1.0D);
        int l1 = MathHelper.floor_double(explosionY + (double)explosionSize + 1.0D);
        int i2 = MathHelper.floor_double(explosionZ - (double)explosionSize - 1.0D);
        int j2 = MathHelper.floor_double(explosionZ + (double)explosionSize + 1.0D);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(k, k1, i2, i1, l1, j2));
        Vec3D vec3d = Vec3D.createVector(explosionX, explosionY, explosionZ);
        for (int k2 = 0; k2 < list.size(); k2++)
        {
            Entity entity = (Entity)list.get(k2);
            double d4 = entity.getDistance(explosionX, explosionY, explosionZ) / (double)explosionSize;
            if (d4 <= 1.0D)
            {
                double d6 = entity.posX - explosionX;
                double d8 = entity.posY - explosionY;
                double d11 = entity.posZ - explosionZ;
                double d13 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d11 * d11);
                d6 /= d13;
                d8 /= d13;
                d11 /= d13;
                double d15 = worldObj.func_675_a(vec3d, entity.boundingBox);
                double d17 = (1.0D - d4) * d15;
                entity.attackEntityFrom(DamageSource.explosion, (int)(((d17 * d17 + d17) / 2D) * 8D * (double)explosionSize + 1.0D));
                double d19 = d17;
                entity.motionX += d6 * d19;
                entity.motionY += d8 * d19;
                entity.motionZ += d11 * d19;
            }
        }

        explosionSize = f;
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(destroyedBlockPositions);
        for (int l2 = arraylist.size() - 1; l2 >= 0; l2--)
        {
            ChunkPosition chunkposition = (ChunkPosition)arraylist.get(l2);
            int i3 = chunkposition.x;
            int j3 = chunkposition.y;
            int k3 = chunkposition.z;
            int l3 = worldObj.getBlockId(i3, j3, k3);
            double d9 = (float)i3 + worldObj.rand.nextFloat();
            double d12 = (float)j3 + worldObj.rand.nextFloat();
            double d14 = (float)k3 + worldObj.rand.nextFloat();
            double d16 = d9 - explosionX;
            double d18 = d12 - explosionY;
            double d20 = d14 - explosionZ;
            double d21 = MathHelper.sqrt_double(d16 * d16 + d18 * d18 + d20 * d20);
            d16 /= d21;
            d18 /= d21;
            d20 /= d21;
            double d22 = 0.5D / (d21 / (double)explosionSize + 0.10000000000000001D);
            d22 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
            d16 *= -d22;
            d18 *= -d22;
            d20 *= -d22;
            worldObj.spawnParticle("smoke", d9, d12, d14, d16, d18, d20);
            if (l3 > 0)
            {
                Block.blocksList[l3].dropBlockAsItemWithChance(worldObj, i3, j3, k3, worldObj.getBlockMetadata(i3, j3, k3), 0.9F, 0);
                worldObj.setBlockWithNotify(i3, j3, k3, 0);
                Block.blocksList[l3].onBlockDestroyedByExplosion(worldObj, i3, j3, k3);
            }
        }
    }

    private void explode()
    {
        if (!exploded)
        {
            exploded = true;
            explosionX = posX;
            explosionY = posY;
            explosionZ = posZ;
            doExplosion();
            fuse2 = 80;
            if (overcharged)
            {
                fuse2 = 160;
            }
            EntityHiddenTG entityhiddentg = new EntityHiddenTG(worldObj, fuse2, posX, posY, posZ);
            worldObj.spawnEntityInWorld(entityhiddentg);
            setEntityDead();
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        super.attackEntityFrom(damagesource, i);
        explode();
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setByte("Fuse", (byte)fuse);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        fuse = nbttagcompound.getByte("Fuse");
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }
}
