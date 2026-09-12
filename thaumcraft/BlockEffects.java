package thaumcraft;

import forge.*;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class BlockEffects extends BlockContainer
    implements ITextureProvider, IMultipassRender
{
    int tickdelay;
    protected int currentPass;

    public BlockEffects(int i)
    {
        super(i, Material.air);
        tickdelay = 0;
        setBlockUnbreakable();
        setResistance(6000000F);
        setStepSound(Block.soundStoneFootstep);
        setBlockName("thaumeffects");
        setRequiresSelfNotify();
        setLightValue(0.4F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        currentPass = 1;
        setTickOnLoad(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityEffects();
    }

    public int getRenderType()
    {
        return mod_ThaumCraft.effectsRenderID;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public TileEntityEffects getData(World world, int i, int j, int k)
    {
        return (TileEntityEffects)world.getBlockTileEntity(i, j, k);
    }

    public int getBlockTextureFromSide(int i)
    {
        return 255;
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        tickdelay++;
        if (tickdelay < 5)
        {
            return;
        }
        TileEntityEffects tileentityeffects = (TileEntityEffects)world.getBlockTileEntity(i, j, k);
        if (tileentityeffects == null)
        {
            return;
        }
        if (tileentityeffects.getBlockMetadata() != 0)
        {
            return;
        }
        if ((entity instanceof EntityLiving) && !(entity instanceof EntityThaumSlime))
        {
            entity.attackEntityFrom(DamageSource.magic, 1);
            world.playSoundEffect(i, j, k, "random.fizz", 0.4F, 2.0F + world.rand.nextFloat() * 0.4F);
        }
        else if (entity instanceof EntityThaumSlime)
        {
            world.playSoundEffect(i, j, k, "random.fizz", 0.4F, 2.0F + world.rand.nextFloat() * 0.4F);
            ((EntityThaumSlime)entity).currentVis += 50F;
            world.setBlockWithNotify(i, j, k, 0);
            tileentityeffects.invalidate();
        }
        tickdelay = 0;
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
        int l = world.getBlockMetadata(i, j, k);
        if (l == 0)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        }
        if (l == 1)
        {
            return;
        }
        if (l == 2)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if (l == 1 || l == 2)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        if (l == 1)
        {
            return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            return super.getSelectedBoundingBoxFromPool(world, i, j, k);
        }
    }

    private boolean doRenderHole(World world, RenderBlocks renderblocks, int i, int j, int k, int l, Block block)
    {
        float f = 0.0625F;
        MinecraftForgeClient.unbindTexture();
        renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumPHSideFX;
        if (world.isBlockOpaqueCube(i, j + 1, k))
        {
            block.setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        else if (world.isBlockOpaqueCube(i, j - 1, k))
        {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (world.isBlockOpaqueCube(i, j, k + 1))
        {
            block.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (world.isBlockOpaqueCube(i, j, k - 1))
        {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (world.isBlockOpaqueCube(i + 1, j, k))
        {
            block.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (world.isBlockOpaqueCube(i - 1, j, k))
        {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        return true;
    }

    public boolean renderMyBlock(World world, RenderBlocks renderblocks, int i, int j, int k, Block block)
    {
        TileEntityEffects tileentityeffects = getData(world, i, j, k);
        if (tileentityeffects.getBlockMetadata() == 1 && currentPass == 0)
        {
            doRenderHole(world, renderblocks, i, j, k, 0, block);
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            return true;
        }
        if (tileentityeffects.getBlockMetadata() == 0 || tileentityeffects.getBlockMetadata() == 2)
        {
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityEffects tileentityeffects = getData(world, i, j, k);
        if (tileentityeffects.getBlockMetadata() == 0)
        {
            float f = (0.5F + random.nextFloat() * 0.8F) - 0.4F;
            float f1 = (0.5F + random.nextFloat() * 0.8F) - 0.4F;
            float f2 = 0.125F;
            ThaumCraftCore.spawnParticle((float)i + f, j, (float)k + f1, 0.0D, 0.30000001192092896D, 0.0D, false, false);
        }
    }

    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (world.multiplayerWorld)
        {
            return;
        }
        TileEntityEffects tileentityeffects = getData(world, i, j, k);
        if (tileentityeffects.getBlockMetadata() == 2)
        {
            removeItem(world, tileentityeffects.contains, i, j, k);
        }
    }

    public void removeItem(World world, int i, int j, int k, int l)
    {
        EntityItem entityitem = null;
        if (i == 86)
        {
            entityitem = new EntityItem(world, (double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, new ItemStack(mod_ThaumCraft.thaumReagent, 1, 6));
        }
        if (i == 186)
        {
            entityitem = new EntityItem(world, (double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, new ItemStack(mod_ThaumCraft.talismanBlank, 1, 0));
        }
        if (entityitem != null)
        {
            world.spawnEntityInWorld(entityitem);
            world.setBlockWithNotify(j, k, l, 0);
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        if (j - 1 < 0)
        {
            return;
        }
        TileEntityEffects tileentityeffects = getData(world, i, j, k);
        if (tileentityeffects.getBlockMetadata() == 0 && world.isAirBlock(i, j - 1, k))
        {
            world.setBlockAndMetadataWithNotify(i, j - 1, k, blockID, 0);
            world.setBlockWithNotify(i, j, k, 0);
            world.playSoundEffect(i, j - 1, k, "random.splash", 0.1F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
        }
        if (tileentityeffects.getBlockMetadata() == 2 && world.isAirBlock(i, j - 1, k))
        {
            removeItem(world, tileentityeffects.contains, i, j, k);
        }
    }

    public boolean canRenderInPass(int i)
    {
        currentPass = i;
        return true;
    }

    public int getRenderBlockPass()
    {
        return currentPass;
    }
}
