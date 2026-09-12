package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemTalismanHealth extends Item
    implements ITextureProvider
{
    public ItemTalismanHealth(int i)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(101);
        setNoRepair();
    }

    public boolean isRepairable()
    {
        return false;
    }

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return super.getMaxItemUseDuration(itemstack);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
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
