package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityEldritchTree extends EntityMob
    implements IMob
{
    public int angerLevel;
    public int modelSeed;
    public int shudder;

    public EntityEldritchTree(World world)
    {
        super(world);
        angerLevel = 0;
        modelSeed = -1;
        shudder = 0;
        texture = "/thaumcraft/tree.png";
        experienceValue = 15;
        setSize(1.0F, 7F);
        modelSeed = world.rand.nextInt(10);
        naturalArmorRating = 1;
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (super.attackEntityFrom(damagesource, i))
        {
            Entity entity = damagesource.getEntity();
            entityToAttack = damagesource.getSourceOfDamage();
            angerLevel = 1000;
            if (worldObj.rand.nextInt(3) == 0)
            {
                spawnGrub();
                shudder = 6 + worldObj.rand.nextInt(5);
            }
        }
        return true;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)angerLevel);
        nbttagcompound.setShort("Seed", (short)modelSeed);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        angerLevel = nbttagcompound.getShort("Anger");
        modelSeed = nbttagcompound.getShort("Seed");
        if (modelSeed == -1)
        {
            modelSeed = (new Random()).nextInt(100);
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected void updateEntityActionState()
    {
        if (motionY > 0.0D)
        {
            motionY = 0.0D;
        }
        if (shudder > 0)
        {
            shudder--;
        }
        motionX = motionZ = moveStrafing = moveForward = 0.0F;
        rotationYaw = 0.0F;
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityArrow.class, AxisAlignedBB.getBoundingBoxFromPool(posX - 6D, posY - 6D, posZ - 6D, posX + 7D, posY + 7D, posZ + 7D));
        for (int i = 0; i < list.size(); i++)
        {
            ((EntityArrow)list.get(i)).motionX *= -1D;
            ((EntityArrow)list.get(i)).motionY *= -1D;
            ((EntityArrow)list.get(i)).motionZ *= -1D;
            ((EntityArrow)list.get(i)).rotationPitch *= -1F;
            ((EntityArrow)list.get(i)).rotationYaw *= -1F;
            worldObj.playSoundEffect(((EntityArrow)list.get(i)).posX, ((EntityArrow)list.get(i)).posY, ((EntityArrow)list.get(i)).posZ, "random.drr", 1.0F, 1.0F);
            ThaumCraftCore.poof(worldObj, (float)((EntityArrow)list.get(i)).posX, (float)((EntityArrow)list.get(i)).posY, (float)((EntityArrow)list.get(i)).posZ);
            if (((EntityArrow)list.get(i)).shootingEntity instanceof EntityLiving)
            {
                entityToAttack = ((EntityArrow)list.get(i)).shootingEntity;
                angerLevel = 500;
            }
        }

        if (angerLevel > 0 && entityToAttack != null)
        {
            angerLevel--;
            texture = "/thaumcraft/treeangry.png";
            if (attackTime <= 0 && (double)getDistanceToEntity(entityToAttack) < 10D && entityToAttack.boundingBox.maxY > boundingBox.minY && entityToAttack.boundingBox.minY < boundingBox.maxY)
            {
                attackTime = 25;
                if (worldObj.rand.nextInt(3) == 0)
                {
                    spawnGrub();
                    shudder = 6 + worldObj.rand.nextInt(5);
                }
            }
            if (entityToAttack.isDead)
            {
                entityToAttack = null;
                angerLevel = 5;
            }
        }
        else
        {
            texture = "/thaumcraft/tree.png";
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    protected String getLivingSound()
    {
        if (angerLevel > 0)
        {
            return "mob.blaze.breathe";
        }
        else
        {
            return "";
        }
    }

    protected String getDeathSound()
    {
        return "mob.blaze.death";
    }

    protected int getDropItemId()
    {
        return 0;
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        if (worldObj.getBlockId(i, j - 1, k) != Block.grass.blockID)
        {
            return false;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool(i - 64, j - 12, k - 64, i + 64, j + 12, k + 64));
        for (int l = 0; l < list.size(); l++)
        {
            if ((Entity)list.get(l) instanceof EntityEldritchTree)
            {
                return false;
            }
        }

        boolean flag = super.getCanSpawnHere();
        for (int i1 = -1; i1 < 2; i1++)
        {
            label0:
            for (int j1 = -1; j1 < 2; j1++)
            {
                int k1 = 0;
                do
                {
                    if (k1 >= 5)
                    {
                        continue label0;
                    }
                    if (!worldObj.isAirBlock(i + i1, j + k1, k + j1) && worldObj.getBlockId(i + i1, j + k1, k + j1) != Block.tallGrass.blockID)
                    {
                        flag = false;
                        continue label0;
                    }
                    k1++;
                }
                while (true);
            }
        }

        return flag;
    }

    protected float getSoundVolume()
    {
        return 0.5F;
    }

    public int getMaxHealth()
    {
        return 75;
    }

    protected void dropFewItems(boolean flag, int i)
    {
        int j = 2 + rand.nextInt(3 + i);
        for (int k = 0; k < j; k++)
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.eTreeItems, 1, 1), 3F);
        }

        if (i > 2)
        {
            i = 2;
        }
        j = rand.nextInt(3 - i);
        if (j == 0)
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.eTreeItems, 1, 2), 2.0F);
        }
    }

    private void spawnGrub()
    {
        World world = ModLoader.getMinecraftInstance().theWorld;
        EntityGrub entitygrub = new EntityGrub(world);
        entitygrub.setLocationAndAngles(posX + (double)((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F), posY + 4D, posZ + (double)((world.rand.nextFloat() - world.rand.nextFloat()) * 2.0F), world.rand.nextFloat() * 360F, 0.0F);
        world.spawnEntityInWorld(entitygrub);
        world.playSoundAtEntity(entitygrub, "mob.slimeattack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        entitygrub.spawnExplosionParticle();
    }
}
