package thaumcraft;

import forge.ITextureProvider;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class BlockProcessor extends BlockContainer
    implements ITextureProvider
{
    private Random infRand;
    private final boolean isActive;
    private static boolean keepInfuserInventory = false;

    public BlockProcessor(int i, boolean flag)
    {
        super(i, Material.rock);
        setHardness(4F);
        setResistance(10F);
        setStepSound(Block.soundStoneFootstep);
        setBlockName("thaumprocessor");
        setRequiresSelfNotify();
        infRand = new Random();
        isActive = flag;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    protected int damageDropped(int i)
    {
        return i;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityProcessor tileentityprocessor = (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
        if (tileentityprocessor.getBlockMetadata() == 1 && tileentityprocessor.isReceiving)
        {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.25F;
            switch (world.rand.nextInt(4))
            {
                case 0:
                    f += f2;
                    f1 += f2;
                    break;

                case 1:
                    f += 1.0F - f2;
                    f1 += 1.0F - f2;
                    break;

                case 2:
                    f += f2;
                    f1 += 1.0F - f2;
                    break;

                case 3:
                    f += 1.0F - f2;
                    f1 += f2;
                    break;
            }
            ThaumCraftCore.createGreenFlameFX(world, (float)i + f, (float)j + 1.1F, (float)k + f1);
            world.spawnParticle("smoke", (float)i + f, (float)j + 1.1F, (float)k + f1, 0.0D, 0.0D, 0.0D);
        }
        if (tileentityprocessor.getBlockMetadata() == 4)
        {
            int l = 20;
            if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
            {
                l = 10;
            }
            l = (l * tileentityprocessor.storedEnergy) / tileentityprocessor.energyMax;
            if (world.rand.nextInt(30) < l)
            {
                LightningBolt lightningbolt = new LightningBolt(world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, ((double)i + 0.5D + (double)world.rand.nextFloat()) - (double)world.rand.nextFloat(), ((double)j + 0.5D + (double)world.rand.nextFloat()) - (double)world.rand.nextFloat(), ((double)k + 0.5D + (double)world.rand.nextFloat()) - (double)world.rand.nextFloat(), world.rand.nextLong(), 6, 9F);
                lightningbolt.defaultFractal();
                lightningbolt.setType(6);
                lightningbolt.finalizeBolt();
            }
        }
    }

    public boolean getIsBlockSolid(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return iblockaccess.getBlockMetadata(i, j, k) != 3;
    }

    public boolean isBlockSolidOnSide(World world, int i, int j, int k, int l)
    {
        return world.getBlockMetadata(i, j, k) != 3;
    }

    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
        int i1 = ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem() != null ? ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem().getItemDamage() : -1;
        if (i1 == 3)
        {
            if (l == 0 && world.getBlockId(i, j + 1, k) == blockID && world.getBlockMetadata(i, j + 1, k) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i, j + 1, k)).orientation == -1)
            {
                return true;
            }
            if (l == 1 && world.getBlockId(i, j - 1, k) == blockID && world.getBlockMetadata(i, j - 1, k) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i, j - 1, k)).orientation == -1)
            {
                return true;
            }
            if (l == 2 && world.getBlockId(i, j, k + 1) == blockID && world.getBlockMetadata(i, j, k + 1) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i, j, k + 1)).orientation == -1)
            {
                return true;
            }
            if (l == 3 && world.getBlockId(i, j, k - 1) == blockID && world.getBlockMetadata(i, j, k - 1) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i, j, k - 1)).orientation == -1)
            {
                return true;
            }
            if (l == 4 && world.getBlockId(i + 1, j, k) == blockID && world.getBlockMetadata(i + 1, j, k) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i + 1, j, k)).orientation == -1)
            {
                return true;
            }
            else
            {
                return l == 5 && world.getBlockId(i - 1, j, k) == blockID && world.getBlockMetadata(i - 1, j, k) == 2 && ((TileEntityProcessor)world.getBlockTileEntity(i - 1, j, k)).orientation == -1;
            }
        }
        else
        {
            return super.canPlaceBlockOnSide(world, i, j, k, l);
        }
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        int i1 = ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem() != null ? ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem().getItemDamage() : -1;
        if (i1 == 3)
        {
            int j1 = -1;
            if (l == 0 && world.getBlockId(i, j + 1, k) == blockID && world.getBlockMetadata(i, j + 1, k) == 2)
            {
                j1 = 0;
            }
            else if (l == 1 && world.getBlockId(i, j - 1, k) == blockID && world.getBlockMetadata(i, j - 1, k) == 2)
            {
                j1 = 1;
            }
            else if (l == 2 && world.getBlockId(i, j, k + 1) == blockID && world.getBlockMetadata(i, j, k + 1) == 2)
            {
                j1 = 2;
            }
            else if (l == 3 && world.getBlockId(i, j, k - 1) == blockID && world.getBlockMetadata(i, j, k - 1) == 2)
            {
                j1 = 3;
            }
            else if (l == 4 && world.getBlockId(i + 1, j, k) == blockID && world.getBlockMetadata(i + 1, j, k) == 2)
            {
                j1 = 4;
            }
            else if (l == 5 && world.getBlockId(i - 1, j, k) == blockID && world.getBlockMetadata(i - 1, j, k) == 2)
            {
                j1 = 5;
            }
            TileEntityProcessor tileentityprocessor = (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
            tileentityprocessor.orientation = j1;
            TileEntityProcessor tileentityprocessor1 = null;
            switch (l)
            {
                case 0:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i, j + 1, k);
                    break;

                case 1:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i, j - 1, k);
                    break;

                case 2:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i, j, k + 1);
                    break;

                case 3:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i, j, k - 1);
                    break;

                case 4:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i + 1, j, k);
                    break;

                case 5:
                    tileentityprocessor1 = (TileEntityProcessor)world.getBlockTileEntity(i - 1, j, k);
                    break;
            }
            if (tileentityprocessor1 != null)
            {
                tileentityprocessor1.orientation = j1;
                world.markBlocksDirty(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
            }
        }
        super.onBlockPlaced(world, i, j, k, l);
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if (j == 0)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumInfuserTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumInfuserTop;
            }
            else
            {
                return mod_ThaumCraft.thaumInfuserSide;
            }
        }
        if (j == 1)
        {
            if (i == 1)
            {
                return mod_ThaumCraft.thaumDuplicatorTop;
            }
            if (i == 0)
            {
                return mod_ThaumCraft.thaumDuplicatorTop;
            }
            else
            {
                return mod_ThaumCraft.thaumDuplicatorSide;
            }
        }
        if (j == 2)
        {
            return mod_ThaumCraft.thaumBoreSide;
        }
        if (j == 3)
        {
            return mod_ThaumCraft.thaumBoreSide + 2;
        }
        if (j == 4)
        {
            return mod_ThaumCraft.generatorFrame;
        }
        else
        {
            return 0;
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        int l = ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem() != null ? ModLoader.getMinecraftInstance().thePlayer.getCurrentEquippedItem().getItemDamage() : -1;
        if (world.multiplayerWorld)
        {
            return true;
        }
        int i1 = world.getBlockMetadata(i, j, k);
        TileEntityProcessor tileentityprocessor = (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
        if (i1 == 0)
        {
            ModLoader.OpenGUI(entityplayer, new GuiThaumInfuser(entityplayer.inventory, tileentityprocessor));
            return true;
        }
        if (i1 == 1)
        {
            ModLoader.OpenGUI(entityplayer, new GuiDuplicator(entityplayer.inventory, tileentityprocessor));
            return true;
        }
        if (i1 == 2 && l != 3)
        {
            ModLoader.OpenGUI(entityplayer, new GuiBore(entityplayer.inventory, tileentityprocessor));
            return true;
        }
        else
        {
            return false;
        }
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityProcessor();
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        if (!keepInfuserInventory)
        {
            TileEntityProcessor tileentityprocessor = (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
            if (tileentityprocessor != null)
            {
                label0:
                for (int l = 0; l < tileentityprocessor.getSizeInventory(); l++)
                {
                    ItemStack itemstack = tileentityprocessor.getStackInSlot(l);
                    if (itemstack == null)
                    {
                        continue;
                    }
                    float f = infRand.nextFloat() * 0.8F + 0.1F;
                    float f1 = infRand.nextFloat() * 0.8F + 0.1F;
                    float f2 = infRand.nextFloat() * 0.8F + 0.1F;
                    do
                    {
                        if (itemstack.stackSize <= 0)
                        {
                            continue label0;
                        }
                        int i1 = infRand.nextInt(21) + 10;
                        if (i1 > itemstack.stackSize)
                        {
                            i1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)infRand.nextGaussian() * f3;
                        entityitem.motionY = (float)infRand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)infRand.nextGaussian() * f3;
                        world.spawnEntityInWorld(entityitem);
                    }
                    while (true);
                }
            }
        }
        super.onBlockRemoval(world, i, j, k);
    }

    public boolean renderMyBlock(World world, RenderBlocks renderblocks, int i, int j, int k, Block block)
    {
        TileEntityProcessor tileentityprocessor = getData(world, i, j, k);
        doRender(world, false, renderblocks, i, j, k, tileentityprocessor.getBlockMetadata(), block);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    public boolean renderMyBlockInv(World world, Block block, int i, int j, RenderBlocks renderblocks)
    {
        return doRender(world, true, renderblocks, 0, 0, 0, i, block);
    }

    private boolean doRender(World world, boolean flag, RenderBlocks renderblocks, int i, int j, int k, int l,
            Block block)
    {
        if (l == 0)
        {
            float f = 0.875F;
            float f2 = 0.9375F;
            float f5 = 0.25F;
            int i2 = mod_ThaumCraft.thaumInfuserTop;
            int l2 = mod_ThaumCraft.thaumInfuserSide;
            int j3 = mod_ThaumCraft.thaumInfuserSide;
            block.setBlockBounds(f5, f, f5, 1.0F - f5, f2, 1.0F - f5);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l2, l2, l2, l2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            if (flag)
            {
                block.setBlockBounds(0.0F, f2 + 0.1F, 0.0F, 1.0F, f2 + 0.1F, 1.0F);
                ThaumCraftCore.DrawFaces(renderblocks, block, 48, 48, 0, 0, 0, 0);
            }
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i2, i2, l2, l2, l2, l2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else if (l == 1)
        {
            int i1 = mod_ThaumCraft.thaumDuplicatorTop;
            int k1 = mod_ThaumCraft.thaumDuplicatorSide;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, i1, i1, k1, k1, k1, k1);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
        }
        else if (l == 2)
        {
            TileEntityProcessor tileentityprocessor = (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
            float f3 = 0.125F;
            float f6 = 0.25F;
            int j2 = mod_ThaumCraft.thaumBoreSide;
            int i3 = mod_ThaumCraft.thaumBoreOut;
            int k3 = 0;
            block.setBlockBounds(f3, f3, f3, 1.0F - f3, 1.0F - f3, 1.0F - f3);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, j2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            if (tileentityprocessor != null && (tileentityprocessor.orientation == 0 || tileentityprocessor.orientation == 1))
            {
                k3 = i3;
            }
            else
            {
                k3 = j2;
            }
            renderblocks.overrideBlockTexture = k3;
            block.setBlockBounds(f6, 0.0F, f6, 1.0F - f6, f3, 1.0F - f6);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f6, 1.0F - f3, f6, 1.0F - f6, 1.0F, 1.0F - f6);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            if (tileentityprocessor != null && (tileentityprocessor.orientation == 4 || tileentityprocessor.orientation == 5))
            {
                k3 = i3;
            }
            else
            {
                k3 = j2;
            }
            renderblocks.overrideBlockTexture = k3;
            block.setBlockBounds(0.0F, f6, f6, f3, 1.0F - f6, 1.0F - f6);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f3, f6, f6, 1.0F, 1.0F - f6, 1.0F - f6);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            if (tileentityprocessor != null && (tileentityprocessor.orientation == 2 || tileentityprocessor.orientation == 3))
            {
                k3 = i3;
            }
            else
            {
                k3 = j2;
            }
            renderblocks.overrideBlockTexture = k3;
            block.setBlockBounds(f6, f6, 0.0F, 1.0F - f6, 1.0F - f6, f3);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f6, f6, 1.0F - f3, 1.0F - f6, 1.0F - f6, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k3);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            renderblocks.overrideBlockTexture = -1;
        }
        else if (l == 3 && flag)
        {
            int j1 = mod_ThaumCraft.thaumBoreSide + 1;
            int l1 = mod_ThaumCraft.thaumBoreSide + 2;
            block.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.1F, 0.8F);
            ThaumCraftCore.DrawFaces(renderblocks, block, l1);
            block.setBlockBounds(0.45F, 0.1F, 0.45F, 0.55F, 1.0F, 0.55F);
            ThaumCraftCore.DrawFaces(renderblocks, block, j1);
            block.setBlockBounds(0.3F, 0.55F, 0.3F, 0.7F, 0.6F, 0.7F);
            ThaumCraftCore.DrawFaces(renderblocks, block, l1);
            block.setBlockBounds(0.25F, 0.7F, 0.25F, 0.75F, 0.75F, 0.75F);
            ThaumCraftCore.DrawFaces(renderblocks, block, l1);
            block.setBlockBounds(0.3F, 0.85F, 0.3F, 0.7F, 0.9F, 0.7F);
            ThaumCraftCore.DrawFaces(renderblocks, block, l1);
            renderblocks.overrideBlockTexture = -1;
        }
        else if (l == 4)
        {
            float f1 = 0.0625F;
            float f4 = 0.125F;
            float f7 = 0.3125F;
            int k2 = mod_ThaumCraft.generatorFrame;
            renderblocks.overrideBlockTexture = k2;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, f4, 1.0F, f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f4, 0.0F, 0.0F, 1.0F, 1.0F, f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, 0.0F, 1.0F - f4, f4, 1.0F, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f4, 0.0F, 1.0F - f4, 1.0F, 1.0F, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, 1.0F - f4, f4, f4, 1.0F, 1.0F - f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f4, 1.0F - f4, f4, 1.0F, 1.0F, 1.0F - f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f4, 1.0F - f4, 0.0F, 1.0F - f4, 1.0F, f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f4, 1.0F - f4, 1.0F - f4, 1.0F - f4, 1.0F, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(0.0F, 0.0F, f4, f4, f4, 1.0F - f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(1.0F - f4, 0.0F, f4, 1.0F, f4, 1.0F - f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f4, 0.0F, 0.0F, 1.0F - f4, f4, f4);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f4, 0.0F, 1.0F - f4, 1.0F - f4, f4, 1.0F);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            block.setBlockBounds(f7, f7, f7, 1.0F - f7, 1.0F - f7, 1.0F - f7);
            if (flag)
            {
                ThaumCraftCore.DrawFaces(renderblocks, block, k2);
            }
            else
            {
                renderblocks.renderStandardBlock(block, i, j, k);
            }
            renderblocks.overrideBlockTexture = -1;
        }
        return true;
    }

    public TileEntityProcessor getData(World world, int i, int j, int k)
    {
        return (TileEntityProcessor)world.getBlockTileEntity(i, j, k);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return mod_ThaumCraft.processorRenderID;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        float f = 0.875F;
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if (l == 0)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
        float f = 0.875F;
        float f1 = 0.9375F;
        float f2 = 0.25F;
        TileEntityProcessor tileentityprocessor = getData(world, i, j, k);
        if (tileentityprocessor.getBlockMetadata() == 0)
        {
            setBlockBounds(f2, f, f2, 1.0F - f2, f1, 1.0F - f2);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
            return;
        }
    }
}
