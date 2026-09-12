package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemTalismanBlank extends Item
    implements ITextureProvider
{
    public ItemTalismanBlank(int i)
    {
        super(i);
        maxStackSize = 64;
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex + i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "inert";
                break;

            case 1:
                s = "charged";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (itemstack.getItemDamage() == 0 && (tileentity instanceof TileEntityCrucible) && tileentity.getBlockMetadata() == 9 && world.isAirBlock(i, j + 1, k))
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.2F, 0.8F);
            itemstack.stackSize--;
            world.setBlockAndMetadataWithNotify(i, j + 1, k, mod_ThaumCraft.thaumEffects.blockID, 2);
            TileEntityEffects tileentityeffects = (TileEntityEffects)world.getBlockTileEntity(i, j + 1, k);
            tileentityeffects.contains = getIconFromDamage(0);
            return true;
        }
        else
        {
            return super.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l);
        }
    }
}
