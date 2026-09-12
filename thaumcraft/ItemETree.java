package thaumcraft;

import forge.ITextureProvider;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class ItemETree extends Item
    implements ITextureProvider
{
    public ItemETree(int i)
    {
        super(i);
        maxStackSize = 64;
        setHasSubtypes(true);
        setMaxDamage(0);
        setIconCoord(6, 4);
        setIconIndex(70);
    }

    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize--;
        entityplayer.getFoodStats().addStatsFrom((ItemFood)ItemFood.appleRed);
        world.playSoundAtEntity(entityplayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        for (int i = 0; i < 12; i++)
        {
            Vec3D vec3d = Vec3D.createVector(((double)world.rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
            vec3d.rotateAroundX((-entityplayer.rotationPitch * 3.141593F) / 180F);
            vec3d.rotateAroundY((-entityplayer.rotationYaw * 3.141593F) / 180F);
            Vec3D vec3d1 = Vec3D.createVector(((double)world.rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-world.rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
            vec3d1.rotateAroundX((-entityplayer.rotationPitch * 3.141593F) / 180F);
            vec3d1.rotateAroundY((-entityplayer.rotationYaw * 3.141593F) / 180F);
            vec3d1 = vec3d1.addVector(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ);
            world.spawnParticle((new StringBuilder()).append("iconcrack_").append(Item.rottenFlesh.shiftedIndex).toString(), vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.050000000000000003D, vec3d.zCoord);
        }

        if (!world.multiplayerWorld)
        {
            switch (world.rand.nextInt(10))
            {
                case 0:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 0));
                    break;

                case 1:
                case 2:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 0));
                    break;

                case 3:
                case 4:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 600, 0));
                    break;

                case 5:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 0));
                    break;

                case 6:
                case 7:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 600, 0));
                    break;

                case 8:
                    entityplayer.addPotionEffect(new PotionEffect(Potion.poison.id, 150, 0));
                    break;
            }
        }
        return itemstack;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.canEat(true) && itemstack.getItemDamage() == 0)
        {
            entityplayer.setItemInUse(itemstack, 16);
            world.playSoundAtEntity(entityplayer, "random.eat", 0.5F + 0.5F * (float)world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
        }
        return itemstack;
    }

    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.block;
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return false;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex + i;
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < 4; i++)
        {
            arraylist.add(new ItemStack(this, 1, i));
        }
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "husk";
                break;

            case 1:
                s = "branch";
                break;

            case 2:
                s = "heartwood";
                break;

            case 3:
                s = "planks";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
