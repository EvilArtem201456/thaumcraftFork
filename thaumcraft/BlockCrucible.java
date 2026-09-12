package thaumcraft;

import forge.*;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class BlockCrucible extends BlockContainer
    implements ITextureProvider, IMultipassRender
{
    int delay;
    protected int currentPass;

    public BlockCrucible(int i)
    {
        super(i, Material.rock);
        delay = 0;
        setHardness(4F);
        setResistance(10F);
        setStepSound(Block.soundStoneFootstep);
        setBlockName("thaumcrucible");
        setRequiresSelfNotify();
        setLightValue(0.4F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
        currentPass = 1;
        setTickOnLoad(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (tileentitycrucible == null)
        {
            return;
        }
        if (tileentitycrucible.getBlockMetadata() > 1 && tileentitycrucible.getBlockMetadata() != 8)
        {
            return;
        }
        if ((entity instanceof EntityItem) && entity.posY <= (double)j + 0.40000000000000002D)
        {
            entity.motionX = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F;
            entity.motionY = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F;
            entity.motionZ = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F;
            ((EntityItem)entity).delayBeforeCanPickup = 10;
            ((EntityItem)entity).age = 0;
        }
        delay++;
        if (delay < 5)
        {
            return;
        }
        delay = 0;
        if ((entity instanceof EntityLiving) && !(entity instanceof EntityThaumSlime))
        {
            entity.attackEntityFrom(DamageSource.magic, 1);
            world.playSoundEffect(i, j, k, "random.fizz", 0.4F, 2.0F + world.rand.nextFloat() * 0.4F);
        }
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityCrucible();
    }

    public int getRenderType()
    {
        return mod_ThaumCraft.crucibleRenderID;
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if (j == 0)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumCrucibleTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumCrucibleTop;
            }
            else
            {
                return mod_ThaumCraft.thaumCrucibleSide;
            }
        }
        if (j == 1)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumSeeingCrucibleTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumSeeingCrucibleTop;
            }
            else
            {
                return mod_ThaumCraft.thaumSeeingCrucibleSide;
            }
        }
        if (j == 2)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumCondenserTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumCondenserTop;
            }
            else
            {
                return mod_ThaumCraft.thaumCondenserSide;
            }
        }
        if (j == 3)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            else
            {
                return mod_ThaumCraft.thaumObeliskBaseSide;
            }
        }
        if (j == 4)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            else
            {
                return mod_ThaumCraft.thaumObeliskMiddle1Side;
            }
        }
        if (j == 5)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            else
            {
                return mod_ThaumCraft.thaumObeliskMiddle2Side;
            }
        }
        if (j == 6)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumObeliskTop;
            }
            else
            {
                return mod_ThaumCraft.thaumObeliskCapSide;
            }
        }
        if (j == 7)
        {
            return mod_ThaumCraft.thaumCubeSideFX;
        }
        if (j == 8)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumThaumiumCrucibleTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumThaumiumCrucibleTop;
            }
            else
            {
                return mod_ThaumCraft.thaumThaumiumCrucibleSide;
            }
        }
        if (j == 9)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumObeliskAltarTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumObeliskAltarTop;
            }
            else
            {
                return mod_ThaumCraft.thaumObeliskAltarSide;
            }
        }
        else
        {
            return 0;
        }
    }

    protected int damageDropped(int i)
    {
        return i;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public TileEntityCrucible getData(World world, int i, int j, int k)
    {
        if (world.getBlockTileEntity(i, j, k) instanceof TileEntityEffects)
        {
            world.setBlock(i, j, k, 0);
        }
        return (TileEntityCrucible)world.getBlockTileEntity(i, j, k);
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
        float f = 0.0625F;
        float f1 = 0.125F;
        float f2 = 0.1875F;
        float f3 = 0.25F;
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (tileentitycrucible == null)
        {
            return;
        }
        if (tileentitycrucible.getBlockMetadata() == 2 || tileentitycrucible.getBlockMetadata() == 9)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
        if (tileentitycrucible.getBlockMetadata() >= 3 && tileentitycrucible.getBlockMetadata() <= 6)
        {
            setBlockBounds(f1, 0.0F, f1, 1.0F - f1, 1.0F, 1.0F - f1);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
        if (tileentitycrucible.getBlockMetadata() == 7)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
        else
        {
            setBlockBounds(f, 0.0F, f, 1.0F - f, f, 1.0F - f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(f, f2, f, 1.0F - f, f3, 1.0F - f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(f2, f, f2, 1.0F - f2, f2, 1.0F - f2);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(f, f3, 0.0F, 1.0F - f, 1.0F - f, f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(0.0F, f3, 0.0F, 1.0F, 1.0F - f, f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(0.0F, f3, 1.0F - f, 1.0F, 1.0F - f, 1.0F);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(0.0F, f3, f, f, 1.0F - f, 1.0F - f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(1.0F - f, f3, f, 1.0F, 1.0F - f, 1.0F - f);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        float f = 0.125F;
        if (l < 2 || l == 8)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        }
        else if (l == 2 || l == 9)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (l >= 3 && l <= 6)
        {
            setBlockBounds(f, 0.0F, f, 1.0F - f, 1.0F, 1.0F - f);
        }
        else if (l == 7)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    private boolean doRenderCrucible(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        float f = 0.0625F;
        float f1 = 0.125F;
        float f2 = 0.1875F;
        float f3 = 0.25F;
        int i1 = mod_ThaumCraft.thaumCrucibleTop;
        int j1 = mod_ThaumCraft.thaumCrucibleSide;
        if (flag && l == 1)
        {
            i1 = mod_ThaumCraft.thaumSeeingCrucibleTop;
            j1 = mod_ThaumCraft.thaumSeeingCrucibleSide;
        }
        if (flag && l == 8)
        {
            i1 = mod_ThaumCraft.thaumThaumiumCrucibleTop;
            j1 = mod_ThaumCraft.thaumThaumiumCrucibleSide;
        }
        block.setBlockBounds(0.001F, 1.0F - f1, 0.0F, 0.999F, 1.0F - f, f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.001F, 1.0F - f1, 1.0F - f, 0.999F, 1.0F - f, 1.0F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.0F, 1.0F - f1, 0.001F, f, 1.0F - f, 0.999F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f, 1.0F - f1, 0.001F, 1.0F, 1.0F - f, 0.999F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f, 0.0F, f, 1.0F - f, f, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f, f2, f, 1.0F - f, f3, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (!flag)
        {
            TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
            if (tileentitycrucible.getBlockMetadata() == 1)
            {
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumSeeingCrucibleSide;
                if ((double)tileentitycrucible.currentVis >= (double)tileentitycrucible.maxVis * 0.90000000000000002D)
                {
                    renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumSeeingCrucibleSide + 1;
                }
            }
            if (tileentitycrucible.getBlockMetadata() == 8)
            {
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumThaumiumCrucibleSide;
                if ((double)tileentitycrucible.currentVis >= (double)tileentitycrucible.maxVis * 0.90000000000000002D)
                {
                    renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumThaumiumCrucibleSide + 1;
                }
            }
        }
        else
        {
            if (l == 1)
            {
                j1 = mod_ThaumCraft.thaumSeeingCrucibleSide;
            }
            if (l == 8)
            {
                j1 = mod_ThaumCraft.thaumThaumiumCrucibleSide;
            }
        }
        block.setBlockBounds(0.001F, f3, 0.0F, 0.999F, 1.0F - f1, f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.001F, f3, 1.0F - f, 0.999F, 1.0F - f1, 1.0F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.0F, f3, 0.001F, f, 1.0F - f1, 0.999F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f, f3, 0.001F, 1.0F, 1.0F - f1, 0.999F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f2, f, f2, 1.0F - f2, f2, 1.0F - f2);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        return true;
    }

    private boolean doRenderObelisk(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        float f = 0.0625F;
        float f1 = 0.125F;
        float f2 = 0.1875F;
        float f3 = 0.25F;
        int i1 = mod_ThaumCraft.thaumObeliskBaseSide;
        int j1 = mod_ThaumCraft.thaumObeliskMiddle1Side;
        int k1 = mod_ThaumCraft.thaumObeliskMiddle2Side;
        int l1 = mod_ThaumCraft.thaumObeliskCapSide;
        int i2 = mod_ThaumCraft.thaumObeliskTop;
        if (l == 3)
        {
            block.setBlockBounds(f1, 0.0F, f1, 1.0F - f1, f, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, i1, i1, i1, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f2, f, f2, 1.0F - f2, f1, 1.0F - f2);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, i1, i1, i1, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f3, f1, f3, 1.0F - f3, 1.0F - f1, 1.0F - f3);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, i1, i1, i1, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f2, 1.0F - f1, f2, 1.0F - f2, 1.0F, 1.0F - f2);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, i1, i1, i1, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else if (l == 4)
        {
            block.setBlockBounds(f1, 0.0F, f1, 1.0F - f1, 1.0F, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, j1, j1, j1, j1, j1, j1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else if (l == 5)
        {
            block.setBlockBounds(f1, 0.0F, f1, 1.0F - f1, 1.0F, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k1, k1, k1, k1, k1, k1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else if (l == 6)
        {
            block.setBlockBounds(f1, 0.0F, f1, 1.0F - f1, 0.5F, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l1, l1, l1, l1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f2, 0.5F, f2, 1.0F - f2, 0.5F + f2, 1.0F - f2);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l1, l1, l1, l1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f3, 0.5F + f2, f3, 1.0F - f3, 1.0F - f2, 1.0F - f3);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l1, l1, l1, l1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.5F - f2, 1.0F - f2, 0.5F - f2, 0.5F + f2, 1.0F - f1, 0.5F + f2);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l1, l1, l1, l1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        return true;
    }

    private boolean doRenderAltar(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        float f = 0.25F;
        float f1 = 0.125F;
        int i1 = mod_ThaumCraft.thaumObeliskAltarTop;
        int j1 = mod_ThaumCraft.thaumObeliskAltarSide;
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f1, f, f1, 1.0F - f1, 1.0F - f, 1.0F - f1);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F, 1.0F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        return true;
    }

    private boolean doRenderCondenser(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        float f = 0.0625F;
        float f1 = 0.125F;
        float f2 = 0.1875F;
        float f3 = 0.25F;
        int i1 = mod_ThaumCraft.thaumCondenserTop;
        int j1 = mod_ThaumCraft.thaumCondenserSide;
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (flag)
        {
            block.setBlockBounds(0.0F, 0.55F + f1, 0.0F, 1.0F, 0.55F + f1, 1.0F);
            ThaumCraftCore.DrawFaces(renderblocks, block, 49, 49, 0, 0, 0, 0);
            block.setBlockBounds(0.0F, 0.6F + f1, 0.0F, 1.0F, 0.6F + f1, 1.0F);
            ThaumCraftCore.DrawFaces(renderblocks, block, mod_ThaumCraft.thaumReagentSprite + 6, mod_ThaumCraft.thaumReagentSprite + 6, 0, 0, 0, 0);
        }
        block.setBlockBounds(f3, 0.0F, f3, 1.0F - f3, f1, 1.0F - f3);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.5F - f1, f1, 0.5F - f1, 0.5F + f1, f1 + f3, 0.5F + f1);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.5F - f3, 0.5F - f1, 0.5F - f3, 0.5F + f3, 0.5F + f1, 0.5F + f3);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (flag)
        {
            block.setBlockBounds(1.0F - f3, 0.5F - f1, 0.5F - f1, 1.0F, 0.5F + f1, 0.5F + f1);
            ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, j1);
        }
        else
        {
            if (tileentitycrucible.orientation == 0)
            {
                block.setBlockBounds(0.5F - f1, 0.5F - f1, 0.0F, 0.5F + f1, 0.5F + f1, f3);
            }
            if (tileentitycrucible.orientation == 1)
            {
                block.setBlockBounds(1.0F - f3, 0.5F - f1, 0.5F - f1, 1.0F, 0.5F + f1, 0.5F + f1);
            }
            if (tileentitycrucible.orientation == 2)
            {
                block.setBlockBounds(0.5F - f1, 0.5F - f1, 1.0F - f3, 0.5F + f1, 0.5F + f1, 1.0F);
            }
            if (tileentitycrucible.orientation == 3)
            {
                block.setBlockBounds(0.0F, 0.5F - f1, 0.5F - f1, f3, 0.5F + f1, 0.5F + f1);
            }
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        return true;
    }

    public boolean renderMyBlock(World world, RenderBlocks renderblocks, int i, int j, int k, Block block)
    {
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (tileentitycrucible == null)
        {
            return false;
        }
        if (tileentitycrucible.getBlockMetadata() == 2 && currentPass == 0)
        {
            doRenderCondenser(world, false, renderblocks, i, j, k, 0, block);
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return true;
        }
        if (tileentitycrucible.getBlockMetadata() >= 3 && tileentitycrucible.getBlockMetadata() <= 6 && currentPass == 0)
        {
            doRenderObelisk(world, false, renderblocks, i, j, k, tileentitycrucible.getBlockMetadata(), block);
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return true;
        }
        if (tileentitycrucible.getBlockMetadata() == 9 && currentPass == 0)
        {
            doRenderAltar(world, false, renderblocks, i, j, k, tileentitycrucible.getBlockMetadata(), block);
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return true;
        }
        if (tileentitycrucible.getBlockMetadata() < 2 || tileentitycrucible.getBlockMetadata() == 8)
        {
            if (currentPass == 0)
            {
                doRenderCrucible(world, false, renderblocks, i, j, k, 0, block);
            }
            else if ((double)tileentitycrucible.currentVis > 0.5D)
            {
                float f = 0.0625F;
                float f2 = 0.25F;
                float f3 = 0.6875F;
                float f4 = (tileentitycrucible.currentVis / tileentitycrucible.maxVis) * f3;
                MinecraftForgeClient.unbindTexture();
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumCubeSideFX;
                if (tileentitycrucible.currentVis / tileentitycrucible.maxVis > 1.0F)
                {
                    f4 = f3;
                    block.setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F + f, 1.0F);
                    renderblocks.renderStandardBlock(block, i, j, k);
                }
                block.setBlockBounds(f, f2, f, 1.0F - f, f2 + f4, 1.0F - f);
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
            return true;
        }
        if (tileentitycrucible.getBlockMetadata() == 7)
        {
            if (currentPass == 1)
            {
                MinecraftForgeClient.unbindTexture();
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumCubeSideFX;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderblocks.renderStandardBlock(block, i, j, k);
                renderblocks.overrideBlockTexture = -1;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                return true;
            }
            else
            {
                float f1 = 0.0625F;
                MinecraftForgeClient.unbindTexture();
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumCubeSideFX;
                block.setBlockBounds(f1, f1, f1, 1.0F - f1, 1.0F - f1, 1.0F - f1);
                renderblocks.renderStandardBlock(block, i, j, k);
                renderblocks.overrideBlockTexture = -1;
                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean renderMyBlockInv(World world, Block block, int i, int j, RenderBlocks renderblocks)
    {
        if (i < 2 || i == 8)
        {
            return doRenderCrucible(world, true, renderblocks, 0, 0, 0, i, block);
        }
        if (i == 2)
        {
            return doRenderCondenser(world, true, renderblocks, 0, 0, 0, i, block);
        }
        if (i >= 3 && i <= 6)
        {
            return doRenderObelisk(world, true, renderblocks, 0, 0, 0, i, block);
        }
        if (i == 7)
        {
            MinecraftForgeClient.unbindTexture();
            renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumCubeSideFX;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            ThaumCraftCore.DrawFaces(renderblocks, block, mod_ThaumCraft.thaumCubeSideFX);
            renderblocks.overrideBlockTexture = -1;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return true;
        }
        if (i == 9)
        {
            return doRenderAltar(world, true, renderblocks, 0, 0, 0, i, block);
        }
        else
        {
            return false;
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (tileentitycrucible == null)
        {
            return;
        }
        float f = (0.5F + random.nextFloat() * 0.8F) - 0.4F;
        float f1 = (0.5F + random.nextFloat() * 0.8F) - 0.4F;
        float f2 = 0.125F;
        if (tileentitycrucible.getBlockMetadata() == 2 && tileentitycrucible.inAura)
        {
            if (world.rand.nextInt(6 - tileentitycrucible.moonphase) == 0)
            {
                ThaumCraftCore.spawnParticle((float)i + 0.5F, (float)j + 0.8F, (float)k + 0.5F, world.rand.nextFloat() - 0.5F, 0.30000001192092896D, world.rand.nextFloat() - 0.5F, true, true);
            }
        }
        else if (tileentitycrucible.getBlockMetadata() == 6 && tileentitycrucible.isObeliskComplete(tileentitycrucible))
        {
            if (world.rand.nextInt(6 - tileentitycrucible.moonphase) == 0)
            {
                ThaumCraftCore.spawnParticle((float)i + 0.5F, (float)j + 0.8F, (float)k + 0.5F, world.rand.nextFloat() - 0.5F, 0.30000001192092896D, world.rand.nextFloat() - 0.5F, true, false);
            }
        }
        else if (tileentitycrucible.getBlockMetadata() == 9)
        {
            if (random.nextInt(100) == 0)
            {
                world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "tcsound.whispers", 0.5F, 1.0F);
            }
            if (tileentitycrucible.currentVis == 500F)
            {
                ThaumCraftCore.spawnParticle((double)((float)i - 0.4F) + (double)world.rand.nextFloat() * 1.6000000000000001D, (float)j + 0.5F, (double)((float)k - 0.4F) + (double)world.rand.nextFloat() * 1.6000000000000001D, 0.0D, 2.0F + world.rand.nextFloat() * 2.0F, 0.0D, false, false);
            }
        }
        else if (tileentitycrucible.getBlockMetadata() < 2 || tileentitycrucible.getBlockMetadata() == 8)
        {
            float f3 = 0.0625F;
            float f4 = 0.125F;
            float f5 = 0.25F;
            float f6 = 0.3125F;
            float f7 = 0.6875F;
            float f8 = (tileentitycrucible.currentVis / tileentitycrucible.maxVis) * f7;
            if (f8 > f7 + f5)
            {
                f8 = f7 + f4;
            }
            float f10 = 0.15F + random.nextFloat() * 0.7F;
            float f11 = 0.15F + random.nextFloat() * 0.7F;
            ThaumCraftCore.createGreenFlameFX(world, (float)i + f10, (float)j + f6 + f8, (float)k + f11);
            world.spawnParticle("smoke", (float)i + f10, (float)j + f6 + f8, (float)k + f11, 0.0D, 0.0D, 0.0D);
            if ((double)tileentitycrucible.currentVis > 0.5D)
            {
                float f9 = 0.25F + (tileentitycrucible.currentVis / tileentitycrucible.maxVis) * f7;
                if (f9 > 1.25F)
                {
                    f9 = 1.25F;
                }
                ThaumCraftCore.spawnParticle((float)i + f, (float)j + f5, (float)k + f1, 0.0D, f9, 0.0D, false, false);
            }
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        tileentitycrucible.invalidateNeighbourConnections();
        super.onBlockRemoval(world, i, j, k);
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if (l >= 3 && l <= 6)
        {
            entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
            entityplayer.addExhaustion(0.025F);
            if (EnchantmentHelper.getSilkTouchModifier(entityplayer.inventory))
            {
                if (world.getBlockId(i, j + 1, k) == blockID)
                {
                    world.createExplosion(null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 2.0F);
                    return;
                }
                net.minecraft.src.ItemStack itemstack = createStackedBlock(l);
                if (itemstack != null)
                {
                    dropBlockAsItem_do(world, i, j, k, itemstack);
                }
            }
            else
            {
                world.createExplosion(null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 2.0F);
            }
        }
        else
        {
            super.harvestBlock(world, entityplayer, i, j, k, l);
        }
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        if (i1 < 4 || i1 > 6)
        {
            super.onBlockPlaced(world, i, j, k, l);
        }
        else if (world.getBlockId(i, j - 1, k) != blockID || world.getBlockMetadata(i, j - 1, k) != i1 - 1)
        {
            world.setBlockWithNotify(i, j, k, 0);
            world.createExplosion(null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 2.0F);
        }
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        if (tileentitycrucible != null)
        {
            tileentitycrucible.invalidateNeighbourConnections();
        }
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        TileEntityCrucible tileentitycrucible = getData(world, i, j, k);
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (tileentitycrucible != null && tileentitycrucible.getBlockMetadata() == 2)
        {
            tileentitycrucible.orientation = l;
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        if (i1 < 4 || i1 > 6)
        {
            super.onNeighborBlockChange(world, i, j, k, l);
        }
        else if (world.getBlockId(i, j - 1, k) != blockID || world.getBlockMetadata(i, j - 1, k) != i1 - 1)
        {
            world.setBlockWithNotify(i, j, k, 0);
            world.createExplosion(null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 2.0F);
            return;
        }
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        TileEntityCrucible tileentitycrucible = (TileEntityCrucible)iblockaccess.getBlockTileEntity(i, j, k);
        return (tileentitycrucible.getBlockMetadata() == 1 || tileentitycrucible.getBlockMetadata() == 8) && (double)tileentitycrucible.currentVis >= (double)tileentitycrucible.maxVis * 0.90000000000000002D;
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        TileEntityCrucible tileentitycrucible = (TileEntityCrucible)world.getBlockTileEntity(i, j, k);
        return (tileentitycrucible.getBlockMetadata() == 1 || tileentitycrucible.getBlockMetadata() == 8) && (double)tileentitycrucible.currentVis >= (double)tileentitycrucible.maxVis * 0.90000000000000002D;
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
