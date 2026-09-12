package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemThaumGrenade extends Item
    implements ITextureProvider
{
    public static int grenadeID;

    public ItemThaumGrenade(int i)
    {
        super(i);
        grenadeID = shiftedIndex;
        maxStackSize = 64;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex + i;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        world.spawnEntityInWorld(new EntityThaumGrenade(world, entityplayer, itemstack.getItemDamage() == 1));
        itemstack.stackSize--;
        return itemstack;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "grenade0";
                break;

            case 1:
                s = "grenade1";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
