package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemMold extends Item
    implements ITextureProvider
{
    public ItemMold(int i)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(50);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return false;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex;
    }
}
