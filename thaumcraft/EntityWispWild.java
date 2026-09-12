package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class EntityWispWild extends EntityFlying
    implements IMob
{
    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;
    public int type;

    public EntityWispWild(World world)
    {
        super(world);
        courseChangeCooldown = 0;
        targetedEntity = null;
        aggroCooldown = 0;
        prevAttackCounter = 0;
        attackCounter = 0;
        setSize(0.9F, 0.9F);
        isImmuneToFire = true;
        experienceValue = 5;
        type = 0;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (damagesource.getSourceOfDamage() instanceof EntityLiving)
        {
            targetedEntity = (EntityLiving)damagesource.getSourceOfDamage();
            aggroCooldown = 200;
        }
        return super.attackEntityFrom(damagesource, i);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public int getMaxHealth()
    {
        return 22;
    }

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateEntityActionState()
    {
        if (!worldObj.multiplayerWorld && worldObj.difficultySetting == 0)
        {
            setEntityDead();
        }
        despawnEntity();
        float f = 0.5F + worldObj.rand.nextFloat() * 0.5F;
        float f1 = 0.5F + worldObj.rand.nextFloat() * 0.5F;
        float f2 = 0.5F + worldObj.rand.nextFloat() * 0.5F;
        if (type == 1)
        {
            f = 0.7F + worldObj.rand.nextFloat() * 0.3F;
            f1 = 0.2F;
            f2 = 0.2F;
        }
        else if (type == 2)
        {
            f = 0.2F;
            f1 = 0.7F + worldObj.rand.nextFloat() * 0.3F;
            f2 = 0.2F;
        }
        else if (type == 5)
        {
            f = 0.2F;
            f1 = 0.2F;
            f2 = 0.7F + worldObj.rand.nextFloat() * 0.3F;
        }
        EntityWispFX entitywispfx = new EntityWispFX(worldObj, (posX + (double)worldObj.rand.nextFloat()) - (double)worldObj.rand.nextFloat(), (posY + (double)worldObj.rand.nextFloat()) - (double)worldObj.rand.nextFloat(), (posZ + (double)worldObj.rand.nextFloat()) - (double)worldObj.rand.nextFloat(), 0.6F, f, f1, f2);
        ModLoader.getMinecraftInstance().effectRenderer.addEffect(entitywispfx);
        prevAttackCounter = attackCounter;
        double d = 16D;
        double d1 = waypointX - posX;
        double d2 = waypointY - posY;
        double d3 = waypointZ - posZ;
        double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2 + d3 * d3);
        if (targetedEntity == null)
        {
            if (d4 < 1.0D || d4 > 60D)
            {
                waypointX = posX + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
                waypointY = posY + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
                waypointZ = posZ + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
            }
        }
        else if (!canEntityBeSeen(targetedEntity))
        {
            waypointX = posX + (double)((rand.nextFloat() * 2.0F - 1.0F) * 4F);
            waypointY = posY + (double)((rand.nextFloat() * 2.0F - 1.0F) * 4F);
            waypointZ = posZ + (double)((rand.nextFloat() * 2.0F - 1.0F) * 4F);
        }
        else if (targetedEntity.getDistanceSqToEntity(this) > d * d * 0.75D)
        {
            waypointX = targetedEntity.posX;
            waypointY = targetedEntity.posY + 1.0D;
            waypointZ = targetedEntity.posZ;
        }
        else
        {
            waypointX = posX;
            waypointY = posY;
            waypointZ = posZ;
        }
        if (courseChangeCooldown-- <= 0)
        {
            worldObj.playSoundAtEntity(this, "random.orb", 0.05F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.6F + 2.0F));
            courseChangeCooldown += rand.nextInt(5) + 2;
            if (isCourseTraversable(waypointX, waypointY, waypointZ, d4))
            {
                motionX += (d1 / d4) * 0.10000000000000001D;
                motionY += (d2 / d4) * 0.10000000000000001D;
                motionZ += (d3 / d4) * 0.10000000000000001D;
            }
            else
            {
                waypointX = posX;
                waypointY = posY;
                waypointZ = posZ;
            }
        }
        if (targetedEntity != null && targetedEntity.isDead)
        {
            targetedEntity = null;
        }
        aggroCooldown--;
        if (targetedEntity != null && targetedEntity.getDistanceSqToEntity(this) < d * d)
        {
            double d5 = targetedEntity.posX - posX;
            double d6 = (targetedEntity.boundingBox.minY + (double)(targetedEntity.height / 2.0F)) - (posY + (double)(height / 2.0F));
            double d7 = targetedEntity.posZ - posZ;
            renderYawOffset = rotationYaw = (-(float)Math.atan2(d5, d7) * 180F) / 3.141593F;
            if (canEntityBeSeen(targetedEntity))
            {
                attackCounter++;
                if (attackCounter == 20)
                {
                    LightningBolt lightningbolt = new LightningBolt(worldObj, this, targetedEntity, worldObj.rand.nextLong(), 4);
                    lightningbolt.defaultFractal();
                    lightningbolt.setWrapper(this);
                    lightningbolt.setType(type);
                    lightningbolt.finalizeBolt();
                    attackCounter = -30 + worldObj.rand.nextInt(30);
                }
            }
            else if (attackCounter > 0)
            {
                attackCounter--;
            }
        }
        else
        {
            renderYawOffset = rotationYaw = (-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F;
            if (attackCounter > 0)
            {
                attackCounter--;
            }
        }
    }

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (waypointX - posX) / d3;
        double d5 = (waypointY - posY) / d3;
        double d6 = (waypointZ - posZ) / d3;
        AxisAlignedBB axisalignedbb = boundingBox.copy();
        for (int i = 1; (double)i < d3; i++)
        {
            axisalignedbb.offset(d4, d5, d6);
            if (worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0)
            {
                return false;
            }
        }

        int j = (int)waypointX;
        int k = (int)waypointY;
        int l = (int)waypointZ;
        if (worldObj.getBlockId(j, k, l) == Block.waterMoving.blockID || worldObj.getBlockId(j, k, l) == Block.waterStill.blockID || worldObj.getBlockId(j, k, l) == Block.lavaMoving.blockID || worldObj.getBlockId(j, k, l) == Block.lavaStill.blockID)
        {
            return false;
        }
        for (int i1 = 0; i1 < 11; i1++)
        {
            if (!worldObj.isAirBlock(j, k - i1, l))
            {
                return true;
            }
        }

        return false;
    }

    protected String getLivingSound()
    {
        return "random.levelup";
    }

    protected String getHurtSound()
    {
        return "random.fizz";
    }

    protected String getDeathSound()
    {
        return "random.breath";
    }

    protected void dropFewItems(boolean flag, int i)
    {
        byte byte0 = 0;
        switch (type)
        {
            case 1:
                byte0 = 1;
                break;

            case 2:
                byte0 = 2;
                break;

            case 5:
                byte0 = 3;
                break;
        }
        if (rand.nextInt(4) + i >= 2)
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.thaumReagent, 1, 11 + byte0), 0.0F);
        }
        else
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6), 0.0F);
        }
        if (rand.nextInt(3) < i)
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6), 0.0F);
        }
    }

    protected float getSoundVolume()
    {
        return 0.25F;
    }

    private boolean nearLava()
    {
        for (int i = -5; i <= 5; i++)
        {
            for (int j = -5; j <= 5; j++)
            {
                for (int k = -5; k <= 5; k++)
                {
                    if (worldObj.getBlockId((int)posX + i, (int)posY + j, (int)posZ + k) == Block.lavaMoving.blockID || worldObj.getBlockId((int)posX + i, (int)posY + j, (int)posZ + k) == Block.lavaStill.blockID)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean getCanSpawnHere()
    {
        if (worldObj.rand.nextBoolean())
        {
            return false;
        }
        int i = worldObj.getWorldChunkManager().getBiomeGenAt((int)posX, (int)posZ).biomeID;
        if (i == BiomeGenBase.desert.biomeID || i == BiomeGenBase.hell.biomeID || nearLava())
        {
            type = 1;
        }
        else if (i == BiomeGenBase.swampland.biomeID || i == BiomeGenBase.taiga.biomeID || i == BiomeGenBase.mushroomIsland.biomeID || i == BiomeGenBase.mushroomIslandShore.biomeID)
        {
            type = 2;
        }
        else if (i == BiomeGenBase.iceMountains.biomeID || i == BiomeGenBase.icePlains.biomeID || i == BiomeGenBase.frozenRiver.biomeID || worldObj.canSnowAt((int)posX, (int)posY, (int)posZ) || worldObj.isRaining())
        {
            type = 5;
        }
        Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
        return worldObj.difficultySetting > 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Type", (short)type);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        type = nbttagcompound.getShort("Type");
    }
}
