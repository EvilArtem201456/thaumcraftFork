package thaumcraft;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class EntityThaumSlime extends EntityLiving
    implements IMob
{
    int angerLevel;
    protected float currentVis;
    private ArrayList connections;
    public float field_768_a;
    public float field_767_b;
    private int slimeJumpDelay;
    private EntityLiving entityToAttack;

    public EntityThaumSlime(World world)
    {
        super(world);
        angerLevel = 0;
        connections = null;
        slimeJumpDelay = 0;
        texture = "/thaumcraft/tslime.png";
        int i = 1;
        yOffset = 0.0F;
        slimeJumpDelay = rand.nextInt(20) + 10;
        setSlimeSize(i);
        experienceValue = i / 2;
        currentVis = 0.0F;
    }

    public float getShadowSize()
    {
        return (float)(getSlimeSize() / 5);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)1));
    }

    public void setSlimeSize(int i)
    {
        dataWatcher.updateObject(16, new Byte((byte)i));
        setSize(0.2F * (float)i, 0.2F * (float)i);
        health = i * i;
        setPosition(posX, posY, posZ);
        experienceValue = i;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (damagesource.getSourceOfDamage() instanceof EntityLiving)
        {
            entityToAttack = (EntityLiving)damagesource.getSourceOfDamage();
            angerLevel = 300;
        }
        return super.attackEntityFrom(damagesource, i);
    }

    public int getSlimeSize()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", getSlimeSize() - 1);
        nbttagcompound.setFloat("Thaum", currentVis);
        nbttagcompound.setShort("Anger", (short)angerLevel);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setSlimeSize(nbttagcompound.getInteger("Size") + 1);
        currentVis = nbttagcompound.getFloat("Thaum");
        angerLevel = nbttagcompound.getShort("Anger");
    }

    public void onUpdate()
    {
        field_767_b = field_768_a;
        boolean flag = onGround;
        super.onUpdate();
        if (onGround && !flag)
        {
            int i = getSlimeSize();
            if (i > 5)
            {
                worldObj.playSoundAtEntity(this, "mob.slime", getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }
            if (i > 15)
            {
                worldObj.createExplosion(this, posX, posY + 0.5D, posZ, 1.0F);
            }
            field_768_a = -0.5F;
        }
        field_768_a = field_768_a * 0.6F;
        if (connections == null)
        {
            calculateConnections();
        }
        if (getSlimeSize() < 10 && currentVis >= (float)(50 * getSlimeSize()))
        {
            currentVis -= 50 * getSlimeSize();
            setSlimeSize(getSlimeSize() + 1);
        }
    }

    protected void updateEntityActionState()
    {
        if (angerLevel > 0)
        {
            angerLevel--;
        }
        despawnEntity();
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 16D);
        boolean flag = false;
        if (connections != null && getSlimeSize() < 10 && angerLevel == 0)
        {
            int i = 0;
            do
            {
                if (i >= connections.size())
                {
                    break;
                }
                if (((TileEntityThaum)connections.get(i)).currentVis >= 1.0F)
                {
                    faceTileEntity((TileEntity)connections.get(i), 10F, 20F);
                    flag = true;
                    float f = MathHelper.sqrt_double(((TileEntityThaum)connections.get(i)).getDistanceFrom(posX + 0.5D, posY + 0.5D, posZ + 0.5D));
                    if (f <= 3F)
                    {
                        currentVis += ((TileEntityThaum)connections.get(i)).subtractVis(0.1F * (float)getSlimeSize(), this);
                    }
                    break;
                }
                i++;
            }
            while (true);
            if (!flag && connections.size() > 0 && slimeJumpDelay <= 0)
            {
                calculateConnections();
            }
        }
        if (getSlimeSize() < 10)
        {
            currentVis += 0.029999999999999999D;
        }
        if (angerLevel > 0 && entityToAttack != null && entityToAttack != entityplayer && !flag)
        {
            faceEntity(entityToAttack, 10F, 20F);
        }
        if (entityplayer != null && !flag)
        {
            faceEntity(entityplayer, 10F, 20F);
        }
        if (onGround && slimeJumpDelay-- <= 0)
        {
            slimeJumpDelay = rand.nextInt(20) + 10;
            if (entityplayer != null)
            {
                slimeJumpDelay /= 3;
            }
            isJumping = true;
            if (getSlimeSize() > 2)
            {
                worldObj.playSoundAtEntity(this, "mob.slime", getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }
            field_768_a = 1.0F;
            moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
            moveForward = 1 * getSlimeSize();
        }
        else
        {
            isJumping = false;
            if (onGround)
            {
                moveStrafing = moveForward = 0.0F;
            }
        }
    }

    public void faceTileEntity(TileEntity tileentity, float f, float f1)
    {
        double d = ((double)tileentity.xCoord - posX) + 0.5D;
        double d1 = ((double)tileentity.zCoord - posZ) + 0.5D;
        double d2 = 0.5D - (posY + (double)getEyeHeight());
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1);
        float f2 = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float)(-((Math.atan2(d2, d3) * 180D) / 3.1415927410125732D));
        rotationPitch = -updateRotation(rotationPitch, f3, f1);
        rotationYaw = updateRotation(rotationYaw, f2, f);
    }

    private float updateRotation(float f, float f1, float f2)
    {
        float f3;
        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }
        for (; f3 >= 180F; f3 -= 360F) { }
        if (f3 > f2)
        {
            f3 = f2;
        }
        if (f3 < -f2)
        {
            f3 = -f2;
        }
        return f + f3;
    }

    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        int i = getSlimeSize();
        if (!worldObj.multiplayerWorld && i > 1)
        {
            for (int j = 0; j < 2; j++)
            {
                int k = worldObj.rand.nextInt(4);
                if (k == 0)
                {
                    float f = 0.7F;
                    double d = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.20000000000000001D;
                    double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d1, posZ + d2, new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6));
                    entityitem.delayBeforeCanPickup = 10;
                    worldObj.spawnEntityInWorld(entityitem);
                    worldObj.spawnParticle("largesmoke", entityitem.posX, entityitem.posY, entityitem.posZ, 0.0D, 0.0D, 0.0D);
                    worldObj.playSoundAtEntity(entityitem, "random.pop", 0.4F, 2.0F + worldObj.rand.nextFloat() * 0.4F);
                    continue;
                }
                if (k < 3)
                {
                    float f1 = (((float)(j % 2) - 0.5F) * (float)i) / 4F;
                    float f2 = (((float)(j / 2) - 0.5F) * (float)i) / 4F;
                    EntityThaumSlime entitythaumslime = new EntityThaumSlime(worldObj);
                    entitythaumslime.setSlimeSize(Math.max(1, i / 2));
                    entitythaumslime.setLocationAndAngles(posX + (double)f1, posY + 0.5D, posZ + (double)f2, rand.nextFloat() * 360F, 0.0F);
                    worldObj.spawnEntityInWorld(entitythaumslime);
                }
            }
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        int i = getSlimeSize();
        int j = Math.min(10, i);
        if (i > 1 && canEntityBeSeen(entityplayer) && (double)getDistanceToEntity(entityplayer) < 0.59999999999999998D * (double)i && entityplayer.attackEntityFrom(DamageSource.causeMobDamage(this), j))
        {
            worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String getHurtSound()
    {
        return "mob.slime";
    }

    protected String getDeathSound()
    {
        return "mob.slime";
    }

    protected int getDropItemId()
    {
        return 0;
    }

    public boolean getCanSpawnHere()
    {
        Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
        return (getSlimeSize() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 100D;
    }

    protected float getSoundVolume()
    {
        return 0.3F;
    }

    private void calculateConnections()
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        int i = 10;
        for (int j = -i; j < i + 1; j++)
        {
            for (int k = -i; k < i + 1; k++)
            {
                for (int i1 = -i; i1 < i + 1; i1++)
                {
                    try
                    {
                        TileEntity tileentity = worldObj.getBlockTileEntity(j + (int)posX, k + (int)posY, i1 + (int)posZ);
                        float f1 = 0.0F;
                        if (!(tileentity instanceof TileEntityThaum))
                        {
                            continue;
                        }
                        f1 = MathHelper.sqrt_double(tileentity.getDistanceFrom(posX, posY, posZ));
                        if (f1 <= (float)i)
                        {
                            arraylist.add((TileEntityThaum)tileentity);
                            arraylist1.add(Float.valueOf(f1));
                        }
                    }
                    catch (Exception exception) { }
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
                TileEntityThaum tileentitythaum = (TileEntityThaum)arraylist.get(l);
                float f = ((Float)arraylist1.get(l)).floatValue();
                if (f > ((Float)arraylist1.get(l + 1)).floatValue())
                {
                    arraylist.remove(l);
                    arraylist1.remove(l);
                    arraylist.add(tileentitythaum);
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
    }

    public int getMaxHealth()
    {
        int i = getSlimeSize();
        return i * i;
    }
}
