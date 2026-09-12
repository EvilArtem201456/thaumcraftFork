package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.src.*;

public class ItemTinker extends Item
    implements ITextureProvider
{
    public ItemTinker(int i)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(50);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return false;
    }

    public int getIconFromDamage(int i)
    {
        return iconIndex;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if ((tileentity instanceof TileEntityCrucible) && tileentity.getBlockMetadata() == 2)
        {
            TileEntityCrucible tileentitycrucible = (TileEntityCrucible)tileentity;
            tileentitycrucible.orientation++;
            if (tileentitycrucible.orientation > 3)
            {
                tileentitycrucible.orientation = 0;
            }
            world.markBlocksDirty(i, j, k, i, j, k);
            world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "note.snare", 1.0F, 0.8F);
            itemstack.damageItem(1, entityplayer);
        }
        if ((tileentity instanceof TileEntityCrucible) && (tileentity.getBlockMetadata() < 2 || tileentity.getBlockMetadata() == 8))
        {
            TileEntityCrucible tileentitycrucible1 = (TileEntityCrucible)tileentity;
            if (tileentitycrucible1.ejectContents(entityplayer))
            {
                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "liquid.lavapop", 1.0F, 0.6F + world.rand.nextFloat() * 0.2F);
                itemstack.damageItem(1, entityplayer);
            }
        }
        return super.onItemUse(itemstack, entityplayer, world, i, j, k, l);
    }
}
