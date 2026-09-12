package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class BlockSymbol extends BlockContainer
    implements ITextureProvider
{
    public BlockSymbol(int i)
    {
        super(i, Material.rock);
        setHardness(1.0F);
        setResistance(10F);
        setStepSound(Block.soundStoneFootstep);
        setBlockName("thaumsymbol");
        setRequiresSelfNotify();
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setTickOnLoad(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return ((TileEntitySymbol)iblockaccess.getBlockTileEntity(i, j, k)).isPowering;
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        return ((TileEntitySymbol)world.getBlockTileEntity(i, j, k)).isPowering;
    }

    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
        if (l == 0 && world.isBlockSolidOnSide(i, j + 1, k, 0))
        {
            return true;
        }
        if (l == 1 && world.isBlockSolidOnSide(i, j - 1, k, 1))
        {
            return true;
        }
        if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2))
        {
            return true;
        }
        if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3))
        {
            return true;
        }
        if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4))
        {
            return true;
        }
        else
        {
            return l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5);
        }
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if (world.isBlockSolidOnSide(i - 1, j, k, 5))
        {
            return true;
        }
        if (world.isBlockSolidOnSide(i + 1, j, k, 4))
        {
            return true;
        }
        if (world.isBlockSolidOnSide(i, j, k - 1, 3))
        {
            return true;
        }
        if (world.isBlockSolidOnSide(i, j, k + 1, 2))
        {
            return true;
        }
        if (world.isBlockSolidOnSide(i, j - 1, k, 1))
        {
            return true;
        }
        else
        {
            return world.isBlockSolidOnSide(i, j + 1, k, 0);
        }
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        int i1 = -1;
        if (l == 0 && world.isBlockSolidOnSide(i, j + 1, k, 0))
        {
            i1 = 0;
        }
        else if (l == 1 && world.isBlockSolidOnSide(i, j - 1, k, 1))
        {
            i1 = 1;
        }
        else if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2))
        {
            i1 = 2;
        }
        else if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3))
        {
            i1 = 3;
        }
        else if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4))
        {
            i1 = 4;
        }
        else if (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5))
        {
            i1 = 5;
        }
        TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(i, j, k);
        tileentitysymbol.orientation = i1;
        if (tileentitysymbol.getBlockMetadata() == 5)
        {
            tileentitysymbol.cooldown = 50;
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(i, j, k);
        if (tileentitysymbol.getBlockMetadata() == 5)
        {
            tileentitysymbol.removeDestination(new TileEntityThaum.TPDest(tileentitysymbol, i, j, k, tileentitysymbol.getRune(), ModLoader.getMinecraftInstance().thePlayer.dimension));
            ThaumCraftCore.DeleteChunkFromList(i, k);
        }
        tileentitysymbol.placeRune(-1);
        tileentitysymbol.invalidateNeighbourConnections();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = ((TileEntitySymbol)iblockaccess.getBlockTileEntity(i, j, k)).orientation;
        float f = 0.0625F;
        if (l == 0)
        {
            setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        if (l == 1)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
        }
        if (l == 2)
        {
            setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }
        if (l == 3)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }
        if (l == 4)
        {
            setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        if (l == 5)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntitySymbol();
    }

    public int getRenderType()
    {
        return mod_ThaumCraft.symbolRenderID;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    public int idDropped(int i, Random random, int j)
    {
        return mod_ThaumCraft.thaumSymbolItem.shiftedIndex;
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

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (world.multiplayerWorld)
        {
            return;
        }
        else
        {
            TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(i, j, k);
            tileentitysymbol.placeRune(-1);
            return;
        }
    }

    private boolean checkIfAttachedToBlock(World world, int i, int j, int k)
    {
        if (!canPlaceBlockAt(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (checkIfAttachedToBlock(world, i, j, k))
        {
            TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(i, j, k);
            int i1 = tileentitysymbol.orientation;
            boolean flag = false;
            if (!world.isBlockSolidOnSide(i - 1, j, k, 5) && i1 == 5)
            {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i + 1, j, k, 4) && i1 == 4)
            {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j, k - 1, 3) && i1 == 3)
            {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j, k + 1, 2) && i1 == 2)
            {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j - 1, k, 1) && i1 == 1)
            {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j + 1, k, 0) && i1 == 0)
            {
                flag = true;
            }
            if (flag)
            {
                dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                world.setBlockWithNotify(i, j, k, 0);
            }
        }
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        return mod_ThaumCraft.thaumSymbolSprite + j;
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(i, j, k);
        if (tileentitysymbol.getBlockMetadata() == 0 && tileentitysymbol.hasDiamondRune() && (entity instanceof EntityItem))
        {
            tileentitysymbol.attemptItemPickup((EntityItem)entity);
        }
    }
}
