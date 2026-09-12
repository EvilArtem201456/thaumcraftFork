package thaumcraft;

import forge.IArmorTextureProvider;
import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemThaumiumArmor extends ItemArmor
    implements ITextureProvider, IArmorTextureProvider
{
    public ItemThaumiumArmor(int i, EnumArmorMaterial enumarmormaterial, int j, int k)
    {
        super(i, enumarmormaterial, j, k);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public String getArmorTextureFile(ItemStack itemstack)
    {
        if (itemstack.itemID == mod_ThaumCraft.helmetThaumium.shiftedIndex || itemstack.itemID == mod_ThaumCraft.plateThaumium.shiftedIndex || itemstack.itemID == mod_ThaumCraft.bootsThaumium.shiftedIndex)
        {
            return "/thaumcraft/thaumium_1.png";
        }
        if (itemstack.itemID == mod_ThaumCraft.legsThaumium.shiftedIndex)
        {
            return "/thaumcraft/thaumium_2.png";
        }
        else
        {
            return "/thaumcraft/thaumium_1.png";
        }
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }
}
