package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.src.*;

public class ItemBlockConduit extends ItemBlock
    implements ITextureProvider
{
    public ItemBlockConduit(int i, Block block)
    {
        super(i);
        setHasSubtypes(true);
    }

    public int getIconFromDamage(int i)
    {
        return mod_ThaumCraft.thaumConduitSide + i;
    }

    public int getMetadata(int i)
    {
        return i;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "node";
                break;

            case 1:
                s = "pipe";
                break;

            case 2:
                s = "valve";
                break;

            case 3:
                s = "pnode";
                break;

            case 10:
                s = "trunknormal";
                break;

            case 11:
                s = "trunkgreedy";
                break;

            case 12:
                s = "trunkroomy";
                break;

            case 13:
                s = "trunkangry";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (itemstack.getItemDamage() < 10)
        {
            return super.onItemUse(itemstack, entityplayer, world, i, j, k, l);
        }
        EntityTravelingTrunk entitytravelingtrunk = new EntityTravelingTrunk(world, (byte)(itemstack.getItemDamage() - 10));
        entitytravelingtrunk.setLocationAndAngles((float)i + 0.5F, j + 1, (float)k + 0.5F, world.rand.nextFloat() * 360F, 0.0F);
        world.spawnEntityInWorld(entitytravelingtrunk);
        itemstack.stackSize--;
        if (itemstack.stackSize == 0)
        {
            itemstack = null;
        }
        return true;
    }

    public boolean isFull3D()
    {
        return true;
    }
}
