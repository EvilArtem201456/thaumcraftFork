package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemOrichalcumIngot extends Item
    implements ITextureProvider
{
    public ItemOrichalcumIngot(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
        setHasSubtypes(false);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }
}
