package thaumcraft;

import forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.src.*;

public class ItemRune extends Item
    implements ITextureProvider
{
    public ItemRune(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(64);
    }

    public int getIconFromDamage(int i)
    {
        return mod_ThaumCraft.thaumRuneSprite + i;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (tileentity instanceof TileEntitySymbol)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.2F, 0.8F);
            int i1 = MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
            ((TileEntitySymbol)tileentity).placeRune(itemstack.getItemDamage());
            itemstack.stackSize--;
            return true;
        }
        else
        {
            return false;
        }
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < 6; i++)
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
                s = "iron";
                break;

            case 1:
                s = "gold";
                break;

            case 2:
                s = "diamond";
                break;

            case 3:
                s = "black";
                break;

            case 4:
                s = "red";
                break;

            case 5:
                s = "lapis";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }
}
