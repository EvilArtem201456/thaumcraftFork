package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemThaumiumAxe extends ItemAxe
    implements ITextureProvider
{
    public ItemThaumiumAxe(int i, EnumToolMaterial enumtoolmaterial)
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
