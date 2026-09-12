package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class ItemWoodsmansAxe extends ItemAxe
    implements ITextureProvider
{
    private int bi;
    private int md;

    public ItemWoodsmansAxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    public boolean canHarvestBlock(Block block)
    {
        return super.canHarvestBlock(block);
    }

    public boolean onBlockStartBreak(ItemStack itemstack, int i, int j, int k, EntityPlayer entityplayer)
    {
        World world = ModLoader.getMinecraftInstance().theWorld;
        bi = world.getBlockId(i, j, k);
        md = world.getBlockMetadata(i, j, k);
        return false;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        World world = ModLoader.getMinecraftInstance().theWorld;
        if (Block.blocksList[bi].blockMaterial == Material.wood)
        {
            EntityHiddenChopper entityhiddenchopper = new EntityHiddenChopper(world, j, k, l, bi, md);
            world.spawnEntityInWorld(entityhiddenchopper);
        }
        return super.onBlockDestroyed(itemstack, i, j, k, l, entityliving);
    }
}
