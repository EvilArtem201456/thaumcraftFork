package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemTemplate extends Item
    implements ITextureProvider
{
    public ItemTemplate(int i)
    {
        super(i);
        maxStackSize = 16;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex + i;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return itemstack.getItemDamage() == 2 || itemstack.getItemDamage() == 3;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "symbol";
                break;

            case 1:
                s = "rune";
                break;

            case 2:
                s = "device";
                break;

            case 3:
                s = "reagent";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
