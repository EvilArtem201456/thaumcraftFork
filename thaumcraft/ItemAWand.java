package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemAWand extends Item
    implements ITextureProvider
{
    public ItemAWand(int i)
    {
        super(i);
        setMaxStackSize(1);
        setMaxDamage(5);
        setNoRepair();
    }

    public boolean isDamageable()
    {
        return true;
    }

    public boolean isRepairable()
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public void useItemOnEntity(ItemStack itemstack, EntityLiving entityliving)
    {
        entityliving.spawnExplosionParticle();
        entityliving.worldObj.playSoundEffect(entityliving.posX, entityliving.posY, entityliving.posZ, "mob.endermen.portal", 1.0F, 1.0F);
        itemstack.damageItem(1, entityliving);
        entityliving.setLocationAndAngles(0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        entityliving.setEntityDead();
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        ThaumCraftCore.LoadAWandData();
        if (ThaumCraftCore.aWand.apportBlock != -1)
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
            world.setBlockAndMetadataWithNotify(i, j, k, ThaumCraftCore.aWand.apportBlock, ThaumCraftCore.aWand.apportMeta);
            if (ThaumCraftCore.aWand.apportBlock == Block.mobSpawner.blockID)
            {
                ((TileEntityMobSpawner)world.getBlockTileEntity(i, j, k)).setMobID(ThaumCraftCore.aWand.spawnerMob);
            }
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
            ThaumCraftCore.poof(world, i, j, k);
            ThaumCraftCore.aWand.apportBlock = -1;
            ThaumCraftCore.aWand.apportMeta = -1;
            ThaumCraftCore.SaveAWandData();
            return true;
        }
        if (world.getBlockId(i, j, k) != mod_ThaumCraft.thaumEffects.blockID && world.getBlockId(i, j, k) != Block.bedrock.blockID && world.getBlockId(i, j, k) != Block.pistonMoving.blockID && world.getBlockId(i, j, k) != Block.pistonExtension.blockID)
        {
            ThaumCraftCore.aWand.apportBlock = world.getBlockId(i, j, k);
            ThaumCraftCore.aWand.apportMeta = world.getBlockMetadata(i, j, k);
            if (ThaumCraftCore.aWand.apportBlock == Block.pistonBase.blockID && (ThaumCraftCore.aWand.apportMeta & 8) != 0)
            {
                ThaumCraftCore.aWand.apportMeta = ThaumCraftCore.aWand.apportMeta & 7;
            }
            if (world.getBlockId(i, j, k) == Block.mobSpawner.blockID)
            {
                ThaumCraftCore.aWand.spawnerMob = ((TileEntityMobSpawner)world.getBlockTileEntity(i, j, k)).getMobID();
            }
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
            ThaumCraftCore.poof(world, i, j, k);
            world.setBlockWithNotify(i, j, k, 0);
            itemstack.damageItem(1, entityplayer);
        }
        ThaumCraftCore.SaveAWandData();
        return true;
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }
}
