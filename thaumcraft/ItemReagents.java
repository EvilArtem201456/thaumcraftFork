package thaumcraft;

import forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.src.*;

public class ItemReagents extends Item
    implements ITextureProvider
{
    public ItemReagents(int i)
    {
        super(i);
        maxStackSize = 64;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        if (itemstack.getItemDamage() == 6 || itemstack.getItemDamage() >= 11)
        {
            return EnumRarity.common;
        }
        else
        {
            return EnumRarity.uncommon;
        }
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return itemstack.getItemDamage() == 6 || itemstack.getItemDamage() >= 11;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex + i;
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < 15; i++)
        {
            arraylist.add(new ItemStack(this, 1, i));
        }
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "coal0";
                break;

            case 1:
                s = "coal1";
                break;

            case 2:
                s = "coal2";
                break;

            case 3:
                s = "dust0";
                break;

            case 4:
                s = "dust1";
                break;

            case 5:
                s = "dust2";
                break;

            case 6:
                s = "crystal";
                break;

            case 7:
                s = "distsouls";
                break;

            case 8:
                s = "disthate";
                break;

            case 9:
                s = "distempty";
                break;

            case 10:
                s = "disthunger";
                break;

            case 11:
                s = "crystalelec";
                break;

            case 12:
                s = "crystalfire";
                break;

            case 13:
                s = "crystalvenom";
                break;

            case 14:
                s = "crystalice";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (itemstack.getItemDamage() == 6 && (tileentity instanceof TileEntityCrucible) && tileentity.getBlockMetadata() == 9 && world.isAirBlock(i, j + 1, k))
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.2F, 0.8F);
            itemstack.stackSize--;
            world.setBlockAndMetadataWithNotify(i, j + 1, k, mod_ThaumCraft.thaumEffects.blockID, 2);
            TileEntityEffects tileentityeffects = (TileEntityEffects)world.getBlockTileEntity(i, j + 1, k);
            tileentityeffects.contains = getIconFromDamage(6);
            return true;
        }
        else
        {
            return super.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l);
        }
    }
}
