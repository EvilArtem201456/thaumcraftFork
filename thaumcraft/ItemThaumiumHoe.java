package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemThaumiumHoe extends ItemHoe
    implements ITextureProvider
{
    public ItemThaumiumHoe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
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
