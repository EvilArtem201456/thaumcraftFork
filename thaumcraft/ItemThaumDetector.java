package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class ItemThaumDetector extends Item
    implements ITextureProvider
{
    public ItemThaumDetector(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public int getIconFromDamage(int i)
    {
        if (i == 0)
        {
            return mod_ThaumCraft.thaumDetectorOffSprite;
        }
        else
        {
            return mod_ThaumCraft.thaumDetectorOnSprite;
        }
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if (tileentity instanceof TileEntityThaum)
        {
            TileEntityThaum tileentitythaum = (TileEntityThaum)tileentity;
            if (tileentitythaum.currentVis > 0.0F)
            {
                int i1 = Math.round(((float)Math.round(tileentitythaum.currentVis) / tileentitythaum.maxVis) * 100F);
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage((new StringBuilder()).append("Detected approximately ").append(Math.round(tileentitythaum.currentVis)).append(" Vis. (").append(i1).append("%)").toString());
                world.playSoundEffect(i, j, k, "note.harp", 0.8F, 1.0F + (1.0F * (float)i1) / 100F);
            }
            else
            {
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("No Vis detected.");
            }
        }
        return super.onItemUse(itemstack, entityplayer, world, i, j, k, l);
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.uncommon;
    }
}
