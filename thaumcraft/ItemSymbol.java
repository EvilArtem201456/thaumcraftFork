package thaumcraft;

import forge.ITextureProvider;
import java.util.ArrayList;
import net.minecraft.src.*;

public class ItemSymbol extends Item
    implements ITextureProvider
{
    public ItemSymbol(int i)
    {
        super(i);
        setHasSubtypes(true);
        maxStackSize = 16;
    }

    public int getIconFromDamage(int i)
    {
        if (i > 9)
        {
            i++;
        }
        return mod_ThaumCraft.thaumSymbolSprite + i;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < 11; i++)
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
                s = "stone";
                break;

            case 2:
                s = "gold";
                break;

            case 3:
                s = "lapis";
                break;

            case 4:
                s = "mossy";
                break;

            case 5:
                s = "void";
                break;

            case 6:
                s = "earth";
                break;

            case 7:
                s = "ice";
                break;

            case 8:
                s = "blazing";
                break;

            case 9:
                s = "eye";
                break;

            case 10:
                s = "shock";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockId(i, j, k);
        if (i1 == Block.snow.blockID)
        {
            l = 0;
        }
        else if (i1 != Block.vine.blockID)
        {
            if (l == 0)
            {
                j--;
            }
            if (l == 1)
            {
                j++;
            }
            if (l == 2)
            {
                k--;
            }
            if (l == 3)
            {
                k++;
            }
            if (l == 4)
            {
                i--;
            }
            if (l == 5)
            {
                i++;
            }
        }
        if (itemstack.stackSize == 0)
        {
            return false;
        }
        if (entityplayer != null && !entityplayer.canPlayerEdit(i, j, k))
        {
            return false;
        }
        world.getClass();
        if (j == 127 && Block.blocksList[mod_ThaumCraft.thaumSymbol.blockID].blockMaterial.isSolid())
        {
            return false;
        }
        if (world.canBlockBePlacedAt(mod_ThaumCraft.thaumSymbol.blockID, i, j, k, false, l))
        {
            Block block = Block.blocksList[mod_ThaumCraft.thaumSymbol.blockID];
            if (world.setBlockAndMetadataWithNotify(i, j, k, mod_ThaumCraft.thaumSymbol.blockID, itemstack.getItemDamage()))
            {
                if (world.getBlockId(i, j, k) == mod_ThaumCraft.thaumSymbol.blockID)
                {
                    Block.blocksList[mod_ThaumCraft.thaumSymbol.blockID].onBlockPlaced(world, i, j, k, l);
                    Block.blocksList[mod_ThaumCraft.thaumSymbol.blockID].onBlockPlacedBy(world, i, j, k, entityplayer);
                }
                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.stepSoundDir2(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}
