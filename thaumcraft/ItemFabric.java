package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemFabric extends Item
    implements ITextureProvider
{
    public ItemFabric(int i)
    {
        super(i);
        maxStackSize = 64;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        if (itemstack.getItemDamage() == 1)
        {
            return EnumRarity.uncommon;
        }
        else
        {
            return EnumRarity.common;
        }
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return itemstack.getItemDamage() == 1;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "fabric";
                break;

            case 1:
                s = "enchantedfabric";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
