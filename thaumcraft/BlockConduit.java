package thaumcraft;

import forge.*;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class BlockConduit extends BlockContainer
    implements ITextureProvider, IMultipassRender
{
    protected int currentPass;

    public BlockConduit(int i)
    {
        super(i, Material.glass);
        setHardness(0.3F);
        setStepSound(Block.soundGlassFootstep);
        setBlockName("thaumconduit");
        setRequiresSelfNotify();
        currentPass = 1;
        setTickOnLoad(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityConduit();
    }

    public int getRenderType()
    {
        return mod_ThaumCraft.thaumConduitRenderID;
    }

    public int getBlockTextureFromSide(int i)
    {
        return mod_ThaumCraft.thaumConduitSide;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    protected int damageDropped(int i)
    {
        if (i == 3)
        {
            return 0;
        }
        else
        {
            return i;
        }
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem() != null ? ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem().getItemDamage() : -1;
        if (l == 0)
        {
            for (int i1 = -1; i1 <= 1; i1++)
            {
                for (int j1 = -1; j1 <= 1; j1++)
                {
                    for (int k1 = -1; k1 <= 1; k1++)
                    {
                        if (i1 == 0 && j1 == 0 && k1 == 0 || i1 != 0 && k1 != 0 || i1 != 0 && j1 != 0 || j1 != 0 && k1 != 0)
                        {
                            continue;
                        }
                        TileEntity tileentity = world.getBlockTileEntity(i + i1, j + j1, k + k1);
                        if ((tileentity instanceof TileEntityConduit) && tileentity.getBlockMetadata() == 0)
                        {
                            return false;
                        }
                    }
                }
            }
        }
        if (l > 9)
        {
            return false;
        }
        else
        {
            return super.canPlaceBlockAt(world, i, j, k);
        }
    }

    public TileEntityConduit getData(World world, int i, int j, int k)
    {
        return (TileEntityConduit)world.getBlockTileEntity(i, j, k);
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
        float f = 0.25F;
        setBlockBounds(f, f, f, 1.0F - f, 1.0F - f, 1.0F - f);
        super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        float f = 0.25F;
        setBlockBounds(f, f, f, 1.0F - f, 1.0F - f, 1.0F - f);
    }

    private boolean doRenderTrunk(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        int i1 = mod_ThaumCraft.trunkTop;
        int j1 = mod_ThaumCraft.trunkSide;
        int k1 = mod_ThaumCraft.trunkFace + l;
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, j1, j1, j1, k1);
        return true;
    }

    private boolean doRenderNode(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        float f = 0.375F;
        float f1 = 0.25F;
        float f2 = 0.1875F;
        float f3 = 0.0625F;
        float f4 = 0.3125F;
        int i1 = mod_ThaumCraft.thaumConduitSide;
        int j1 = 20;
        if (l == 0)
        {
            block.setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i, j - 1, k))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, 1.0F - f1, f, 1.0F - f, 1.0F, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i, j + 1, k))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, f, f, f1, 1.0F - f, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i - 1, j, k))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f1, f, f, 1.0F, 1.0F - f, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i + 1, j, k))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, f, 0.0F, 1.0F - f, 1.0F - f, f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i, j, k - 1))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, f, 1.0F - f1, 1.0F - f, 1.0F - f, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else if (ThaumCraftCore.isConnectable(i, j, k + 1))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else
        {
            renderblocks.overrideBlockTexture = j1;
            block.setBlockBounds(f1, 0.0F, f1, 1.0F - f1, f3, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, j1);
            }
            else if (world.isBlockSolidOnSide(i, j - 1, k, 1))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, j1);
            }
            else if (world.isBlockSolidOnSide(i, j - 1, k, 1))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f1, 1.0F - f3, f1, 1.0F - f1, 1.0F, 1.0F - f1);
            if (!flag && world.isBlockSolidOnSide(i, j + 1, k, 0))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, 1.0F - f1, f, 1.0F - f, 1.0F, 1.0F - f);
            if (!flag && world.isBlockSolidOnSide(i, j + 1, k, 0))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, f1, f1, f3, 1.0F - f1, 1.0F - f1);
            if (!flag && world.isBlockSolidOnSide(i - 1, j, k, 3))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, f, f, f1, 1.0F - f, 1.0F - f);
            if (!flag && world.isBlockSolidOnSide(i - 1, j, k, 3))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f3, f1, f1, 1.0F, 1.0F - f1, 1.0F - f1);
            if (!flag && world.isBlockSolidOnSide(i + 1, j, k, 2))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f1, f, f, 1.0F, 1.0F - f, 1.0F - f);
            if (!flag && world.isBlockSolidOnSide(i + 1, j, k, 2))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f1, f1, 0.0F, 1.0F - f1, 1.0F - f1, f3);
            if (!flag && world.isBlockSolidOnSide(i, j, k - 1, 5))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, f, 0.0F, 1.0F - f, 1.0F - f, f1);
            if (!flag && world.isBlockSolidOnSide(i, j, k - 1, 5))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f1, f1, 1.0F - f3, 1.0F - f1, 1.0F - f1, 1.0F);
            if (!flag && world.isBlockSolidOnSide(i, j, k + 1, 4))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f, f, 1.0F - f1, 1.0F - f, 1.0F - f, 1.0F);
            if (!flag && world.isBlockSolidOnSide(i, j, k + 1, 4))
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            renderblocks.overrideBlockTexture = i1;
        }
        block.setBlockBounds(f1, f1, f1, 1.0F - f1, 1.0F - f1, 1.0F - f1);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        renderblocks.overrideBlockTexture = i1 + 2;
        block.setBlockBounds(f2, f2, f2, f4, f4, f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f2, f2, f2, 1.0F - f4, f4, f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f2, f2, 1.0F - f2, f4, f4, 1.0F - f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f2, f2, 1.0F - f2, 1.0F - f4, f4, 1.0F - f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f2, 1.0F - f4, f2, f4, 1.0F - f2, f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f2, 1.0F - f4, f2, 1.0F - f4, 1.0F - f2, f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f2, 1.0F - f4, 1.0F - f2, f4, 1.0F - f2, 1.0F - f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f2, 1.0F - f4, 1.0F - f2, 1.0F - f4, 1.0F - f2, 1.0F - f4);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1 + 2);
        }
        else
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        renderblocks.overrideBlockTexture = -1;
        return true;
    }

    private boolean doRenderConduit(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block, boolean flag1)
    {
        float f = 0.375F;
        float f1 = 0.3125F;
        float f2 = 0.25F;
        TileEntityConduit tileentityconduit = getData(world, i, j, k);
        int i1 = mod_ThaumCraft.thaumConduitSide + 1;
        block.setBlockBounds(f, 0.0F, f, 1.0F - f, f, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i, j - 1, k))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f, 1.0F - f, f, 1.0F - f, 1.0F, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i, j + 1, k))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(0.0F, f, f, f, 1.0F - f, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i - 1, j, k))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(1.0F - f, f, f, 1.0F, 1.0F - f, 1.0F - f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i + 1, j, k))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f, f, 0.0F, 1.0F - f, 1.0F - f, f);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i, j, k - 1))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        block.setBlockBounds(f, f, 1.0F - f, 1.0F - f, 1.0F - f, 1.0F);
        if (flag)
        {
            ThaumCraftCore.DrawFaces(renderblocks, block, i1);
        }
        else if (ThaumCraftCore.isConnectable(i, j, k + 1))
        {
            renderblocks.renderStandardBlock(block, i, j, k);
        }
        if (flag1)
        {
            i1 = mod_ThaumCraft.thaumConduitSide + 3;
            if (tileentityconduit != null && !tileentityconduit.valveOpen)
            {
                i1 = mod_ThaumCraft.thaumConduitSide + 4;
            }
            renderblocks.overrideBlockTexture = i1;
            block.setBlockBounds(f1, f1, f1, 1.0F - f1, 1.0F - f1, 1.0F - f1);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else
        {
            block.setBlockBounds(f, f, f, 1.0F - f, 1.0F - f, 1.0F - f);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        renderblocks.overrideBlockTexture = -1;
        return true;
    }

    public boolean renderMyBlock(World world, RenderBlocks renderblocks, int i, int j, int k, Block block)
    {
        if (world.getBlockMetadata(i, j, k) > 9)
        {
            return false;
        }
        if (currentPass == 1)
        {
            if (world.getBlockMetadata(i, j, k) == 0 || world.getBlockMetadata(i, j, k) == 3)
            {
                return doRenderNode(world, false, renderblocks, i, j, k, world.getBlockMetadata(i, j, k), block);
            }
            if (world.getBlockMetadata(i, j, k) == 1)
            {
                return doRenderConduit(world, false, renderblocks, i, j, k, 0, block, false);
            }
            if (world.getBlockMetadata(i, j, k) == 2)
            {
                return doRenderConduit(world, false, renderblocks, i, j, k, 0, block, true);
            }
        }
        else if (world.getBlockMetadata(i, j, k) == 0 || world.getBlockMetadata(i, j, k) == 3)
        {
            TileEntityConduit tileentityconduit = getData(world, i, j, k);
            if (tileentityconduit.currentVis > 0.0F)
            {
                float f = 0.3125F;
                float f1 = (tileentityconduit.currentVis / tileentityconduit.maxVis) * (1.0F - f - f);
                MinecraftForgeClient.unbindTexture();
                renderblocks.overrideBlockTexture = mod_ThaumCraft.thaumCubeSideFX;
                block.setBlockBounds(f, f, f, 1.0F - f, f + f1, 1.0F - f);
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        renderblocks.overrideBlockTexture = -1;
        return true;
    }

    public boolean renderMyBlockInv(World world, Block block, int i, int j, RenderBlocks renderblocks)
    {
        if (i == 0 || i == 3)
        {
            return doRenderNode(world, true, renderblocks, 0, 0, 0, i, block);
        }
        if (i == 1)
        {
            return doRenderConduit(world, true, renderblocks, 0, 0, 0, i, block, false);
        }
        if (i == 2)
        {
            return doRenderConduit(world, true, renderblocks, 0, 0, 0, i, block, true);
        }
        if (i == 10)
        {
            return doRenderTrunk(world, true, renderblocks, 0, 0, 0, 0, block);
        }
        if (i == 11)
        {
            return doRenderTrunk(world, true, renderblocks, 0, 0, 0, 1, block);
        }
        if (i == 12)
        {
            return doRenderTrunk(world, true, renderblocks, 0, 0, 0, 2, block);
        }
        if (i == 13)
        {
            return doRenderTrunk(world, true, renderblocks, 0, 0, 0, 3, block);
        }
        else
        {
            return false;
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
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

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        super.onBlockPlaced(world, i, j, k, l);
        TileEntityConduit tileentityconduit = (TileEntityConduit)world.getBlockTileEntity(i, j, k);
        tileentityconduit.invalidateNeighbourConnections();
        if (world.getBlockMetadata(i, j, k) == 3)
        {
            tileentityconduit.maxVis = 250F;
            tileentityconduit.currentVis = 250F;
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityConduit tileentityconduit = getData(world, i, j, k);
        if (tileentityconduit != null)
        {
            tileentityconduit.invalidateNeighbourConnections();
            if ((tileentityconduit.getBlockMetadata() == 0 || tileentityconduit.getBlockMetadata() == 3) && tileentityconduit.currentVis >= tileentityconduit.maxVis / 3F)
            {
                int l = world.rand.nextInt(6);
                if (l < 1)
                {
                    world.createExplosion(null, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 1.0F);
                }
                else if (l < 4)
                {
                    ThaumCraftCore.createThaumPuddle(world, i, j, k, 1);
                }
            }
        }
        super.onBlockRemoval(world, i, j, k);
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        TileEntityConduit tileentityconduit = getData(world, i, j, k);
        if (tileentityconduit.getBlockMetadata() == 2 && !entityplayer.isSneaking())
        {
            tileentityconduit.valveOpen = !tileentityconduit.valveOpen;
            world.markBlocksDirty(i, j, k, i, j, k);
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, tileentityconduit.valveOpen ? 0.5F : 0.6F);
            world.notifyBlocksOfNeighborChange(i, j, k, blockID);
            return true;
        }
        else
        {
            return false;
        }
    }
}
