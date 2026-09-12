package thaumcraft;

import forge.IArmorTextureProvider;
import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemSevenBoots extends ItemArmor
    implements ITextureProvider, IArmorTextureProvider
{
    public ItemSevenBoots(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
    {
        super(i, enumarmormaterial, j, k);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public String getArmorTextureFile(ItemStack itemstack)
    {
        return "/thaumcraft/bootsseven.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }
}
