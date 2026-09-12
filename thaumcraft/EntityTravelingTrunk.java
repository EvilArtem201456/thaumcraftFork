package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityTravelingTrunk extends EntityAnimal
    implements IMob
{
    public int slotCount;
    public ILN_InventoryMob inventory;
    public float lidrot;
    public boolean open;
    public float field_768_a;
    public float field_767_b;
    private int jumpDelay;
    private int eatDelay;
    public int angerLevel;
    public byte trunkType;
    public boolean stay;

    public EntityTravelingTrunk(World world)
    {
        super(world);
        slotCount = 18;
        inventory = new ILN_InventoryMob(this, slotCount);
        eatDelay = 0;
        angerLevel = 0;
        jumpDelay = 0;
        texture = "/thaumcraft/trunk.png";
        preventEntitySpawning = true;
        jumpDelay = rand.nextInt(20) + 10;
        experienceValue = 7;
        naturalArmorRating = 1;
        lidrot = 0.0F;
        stay = false;
        setSize(0.8F, 0.8F);
    }

    public EntityTravelingTrunk(World world, byte byte0)
    {
        this(world);
        trunkType = byte0;
        health = getMaxHealth();
        if (trunkType == 1)
        {
            setInvSize(27);
            setSize(0.9F, 0.9F);
            texture = "/thaumcraft/trunkgreedy.png";
        }
        if (trunkType == 2)
        {
            setInvSize(36);
            setSize(1.0F, 1.0F);
            texture = "/thaumcraft/trunkroomy.png";
        }
        if (trunkType == 3)
        {
            setInvSize(18);
            texture = "/thaumcraft/trunkangry.png";
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(17, "");
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (super.attackEntityFrom(damagesource, i))
        {
            Entity entity = damagesource.getEntity();
            if ((entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwner()))
            {
                return true;
            }
            if (damagesource.getSourceOfDamage() instanceof EntityLiving)
            {
                entityToAttack = damagesource.getSourceOfDamage();
                angerLevel = 300;
                return true;
            }
            if ((damagesource.getSourceOfDamage() instanceof EntityArrow) && ((EntityArrow)damagesource.getSourceOfDamage()).shootingEntity != null)
            {
                entityToAttack = ((EntityArrow)damagesource.getSourceOfDamage()).shootingEntity;
                angerLevel = 300;
                return true;
            }
        }
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)angerLevel);
        nbttagcompound.setBoolean("Stay", stay);
        nbttagcompound.setByte("Type", trunkType);
        if (getOwner() == null)
        {
            nbttagcompound.setString("Owner", "");
        }
        else
        {
            nbttagcompound.setString("Owner", getOwner());
        }
        nbttagcompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        angerLevel = nbttagcompound.getShort("Anger");
        stay = nbttagcompound.getBoolean("Stay");
        trunkType = nbttagcompound.getByte("Type");
        if (trunkType == 1)
        {
            setInvSize(27);
            texture = "/thaumcraft/trunkgreedy.png";
        }
        if (trunkType == 2)
        {
            setInvSize(36);
            setSize(1.0F, 1.0F);
            texture = "/thaumcraft/trunkroomy.png";
        }
        if (trunkType == 3)
        {
            setInvSize(18);
            setSize(0.8F, 0.8F);
            texture = "/thaumcraft/trunkangry.png";
        }
        String s = nbttagcompound.getString("Owner");
        if (s.length() > 0)
        {
            setOwner(s);
        }
        NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
        inventory.readFromNBT(nbttaglist);
    }

    public String getOwner()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void setOwner(String s)
    {
        dataWatcher.updateObject(17, s);
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (health <= getMaxHealth() / 2 && eatDelay == 0)
        {
            int i = 0;
            do
            {
                if (i >= slotCount)
                {
                    break;
                }
                if (inventory.getStackInSlot(i) != null && (Item.itemsList[inventory.getStackInSlot(i).itemID] instanceof ItemFood))
                {
                    ItemFood itemfood = (ItemFood)Item.itemsList[inventory.getStackInSlot(i).itemID];
                    inventory.getStackInSlot(i).stackSize--;
                    heal(itemfood.getHealAmount());
                    eatDelay = 10 + worldObj.rand.nextInt(15);
                    if (health == getMaxHealth())
                    {
                        worldObj.playSoundAtEntity(this, "random.burp", 0.5F, worldObj.rand.nextFloat() * 0.5F + 0.5F);
                    }
                    else
                    {
                        worldObj.playSoundAtEntity(this, "random.eat", 0.5F, worldObj.rand.nextFloat() * 0.5F + 0.5F);
                    }
                    showHeartsOrSmokeFX(true);
                    lidrot = 0.15F;
                    if (inventory.getStackInSlot(i).stackSize <= 0)
                    {
                        inventory.setInventorySlotContents(i, null);
                    }
                    break;
                }
                i++;
            }
            while (true);
        }
        if (trunkType == 1)
        {
            pullItems();
        }
    }

    public void onUpdate()
    {
        if (getOwner().length() < 1)
        {
            setOwner(ModLoader.getMinecraftInstance().thePlayer.username);
        }
        field_767_b = field_768_a;
        boolean flag = onGround;
        super.onUpdate();
        if (onGround && !flag)
        {
            field_768_a = -0.5F;
        }
        field_768_a = field_768_a * 0.6F;
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected void updateEntityActionState()
    {
        if (angerLevel > 0)
        {
            angerLevel--;
        }
        if (eatDelay > 0)
        {
            eatDelay--;
        }
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
        if (entityplayer != null)
        {
            if (!stay && entityplayer != null && (getDistanceToEntity(entityplayer) > 20F || inWater && getDistanceToEntity(entityplayer) > 8F && !entityplayer.isInWater()))
            {
                int i = MathHelper.floor_double(entityplayer.posX) - 2;
                int j = MathHelper.floor_double(entityplayer.posZ) - 2;
                int k = MathHelper.floor_double(entityplayer.boundingBox.minY);
                for (int l = 0; l <= 4; l++)
                {
                    for (int i1 = 0; i1 <= 4; i1++)
                    {
                        if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1))
                        {
                            worldObj.playSoundEffect((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, "mob.endermen.portal", 0.5F, 1.0F);
                            setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, rotationYaw, rotationPitch);
                            showHeartsOrSmokeFX(false);
                            entityToAttack = null;
                            angerLevel = 0;
                            return;
                        }
                    }
                }
            }
            if ((angerLevel == 0 || entityToAttack == null) && trunkType == 3)
            {
                List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
                if (!list.isEmpty())
                {
                    Entity entity = (Entity)list.get(worldObj.rand.nextInt(list.size()));
                    if ((entity instanceof EntityMob) && canEntityBeSeen(entity))
                    {
                        angerLevel = 600;
                        setEntityToAttack(entity);
                    }
                }
            }
            boolean flag = false;
            if (angerLevel > 0 && entityToAttack != null && entityToAttack != entityplayer)
            {
                faceEntity(entityToAttack, 10F, 20F);
                flag = true;
                if (attackTime <= 0 && (double)getDistanceToEntity(entityToAttack) < 1.5D && entityToAttack.boundingBox.maxY > boundingBox.minY && entityToAttack.boundingBox.minY < boundingBox.maxY)
                {
                    attackTime = 10 + worldObj.rand.nextInt(5);
                    byte byte0 = (byte)(2 + trunkType);
                    entityToAttack.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
                    lidrot += 0.015F;
                    worldObj.playSoundAtEntity(this, "mob.blaze.hit", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
                }
                if (entityToAttack.isDead)
                {
                    entityToAttack = null;
                    angerLevel = 5;
                }
            }
            if (entityplayer != null && getDistanceToEntity(entityplayer) > 5F && angerLevel == 0 && !stay)
            {
                faceEntity(entityplayer, 10F, 20F);
                flag = true;
            }
            if (onGround && jumpDelay-- <= 0 && flag)
            {
                jumpDelay = rand.nextInt(10) + 5;
                jumpDelay /= 3;
                isJumping = true;
                field_768_a = 1.0F;
                moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
                moveForward = 6F;
                jumpMovementFactor = 0.03F;
                worldObj.playSoundAtEntity(this, "random.chestclosed", 0.15F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            else
            {
                isJumping = false;
                if (motionY < 0.0D || open)
                {
                    lidrot += 0.015F;
                }
                if ((double)lidrot > 0.5D)
                {
                    lidrot = 0.5F;
                }
                if (onGround)
                {
                    moveStrafing = moveForward = 0.0F;
                    if (!open)
                    {
                        lidrot -= 0.1F;
                        if (lidrot < 0.0F)
                        {
                            lidrot = 0.0F;
                        }
                    }
                }
            }
            if (open)
            {
                lidrot += 0.035F;
            }
            if ((double)lidrot > 0.5D)
            {
                lidrot = 0.5F;
            }
        }
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

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    protected String getHurtSound()
    {
        return Block.wood.stepSound.stepSoundDir2();
    }

    protected String getDeathSound()
    {
        return "random.break";
    }

    protected int getDropItemId()
    {
        return 0;
    }

    public boolean getCanSpawnHere()
    {
        return true;
    }

    protected float getSoundVolume()
    {
        return 0.5F;
    }

    public int getMaxHealth()
    {
        return 40 + trunkType * 10;
    }

    public void setInvSize(int i)
    {
        slotCount = i;
        inventory = new ILN_InventoryMob(this, slotCount);
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood) && health < getMaxHealth())
        {
            ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];
            itemstack.stackSize--;
            heal(itemfood.getHealAmount());
            if (health == getMaxHealth())
            {
                worldObj.playSoundAtEntity(this, "random.burp", 0.5F, worldObj.rand.nextFloat() * 0.5F + 0.5F);
            }
            else
            {
                worldObj.playSoundAtEntity(this, "random.eat", 0.5F, worldObj.rand.nextFloat() * 0.5F + 0.5F);
            }
            showHeartsOrSmokeFX(true);
            lidrot = 0.15F;
            if (itemstack.stackSize <= 0)
            {
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
            }
            return true;
        }
        if (entityplayer.username.equals(getOwner()))
        {
            open = true;
            worldObj.playSoundAtEntity(this, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            ModLoader.OpenGUI(entityplayer, new ILN_GuiMobInv(entityplayer, this));
            return true;
        }
        else
        {
            ModLoader.getMinecraftInstance().ingameGUI.addChatMessage((new StringBuilder()).append("The trunk refuses to open for anyone but ").append(getOwner()).toString());
            return false;
        }
    }

    void showHeartsOrSmokeFX(boolean flag)
    {
        String s = "heart";
        byte byte0 = 1;
        if (!flag)
        {
            s = "explode";
            byte0 = 7;
        }
        for (int i = 0; i < byte0; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }
    }

    private void pullItems()
    {
        if (isDead || health == 0)
        {
            return;
        }
        List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(posX - 0.5D, posY - 0.5D, posZ - 0.5D, posX + 0.5D, posY + 0.5D, posZ + 0.5D));
        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);
            if (!(entity instanceof EntityItem) || !inventory.addItemStackToInventory(((EntityItem)entity).item))
            {
                continue;
            }
            worldObj.playSoundAtEntity(this, "random.eat", 0.5F, worldObj.rand.nextFloat() * 0.5F + 0.5F);
            lidrot = 0.15F;
            if (((EntityItem)entity).item.stackSize <= 0)
            {
                entity.setEntityDead();
            }
        }

        if (inventory.getFirstEmptyStack() == -1)
        {
            return;
        }
        list = worldObj.getEntitiesWithinAABB(net.minecraft.src.Entity.class, AxisAlignedBB.getBoundingBoxFromPool(posX - 3D, posY - 3D, posZ - 3D, posX + 3D, posY + 3D, posZ + 3D));
        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);
            if (entity1 instanceof EntityItem)
            {
                double d = entity1.posX - posX;
                double d1 = entity1.posY - posY;
                double d2 = entity1.posZ - posZ;
                double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
                d /= d3;
                d1 /= d3;
                d2 /= d3;
                double d4 = 0.20000000000000001D;
                entity1.motionX -= d * d4;
                entity1.motionY -= d1 * d4;
                entity1.motionZ -= d2 * d4;
            }
        }
    }

    public void onEntityDeath()
    {
        inventory.dropAllItems();
        super.onEntityDeath();
    }

    protected EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return null;
    }

    public boolean canBreatheUnderwater()
    {
        return true;
    }
}
