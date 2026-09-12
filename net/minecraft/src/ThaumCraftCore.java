package net.minecraft.src;

import forge.MinecraftForge;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import thaumcraft.*;

public class ThaumCraftCore
{
    public static SaveWandData aWand;
    public static List loadedChunks = new ArrayList();
    public static boolean loadChunks = true;
    public static File WorldDir;

    public ThaumCraftCore()
    {
    }

    public static boolean isConnectable(int i, int j, int k)
    {
        TileEntity tileentity = ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(i, j, k);
        if (tileentity instanceof TileEntityThaum)
        {
            return ((TileEntityThaum)tileentity).isConnectable;
        }
        else
        {
            return false;
        }
    }

    public static Entity getPointedEntity(World world, EntityPlayer entityplayer, double d)
    {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        Entity entity = null;
        double d1 = d;
        Vec3D vec3d = entityplayer.getPosition(1.0F);
        Vec3D vec3d1 = entityplayer.getLook(1.0F);
        Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d1, vec3d1.yCoord * d1, vec3d1.zCoord * d1);
        float f3 = 1.0F;
        List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(vec3d1.xCoord * d1, vec3d1.yCoord * d1, vec3d1.zCoord * d1).expand(f3, f3, f3));
        double d2 = 0.0D;
        for (int i = 0; i < list.size(); i++)
        {
            Entity entity1 = (Entity)list.get(i);
            if (!entity1.canBeCollidedWith())
            {
                continue;
            }
            float f4 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.isVecInside(vec3d))
            {
                if (0.0D < d2 || d2 == 0.0D)
                {
                    entity = entity1;
                    d2 = 0.0D;
                }
                continue;
            }
            if (movingobjectposition == null)
            {
                continue;
            }
            double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
            if (d3 < d2 || d2 == 0.0D)
            {
                entity = entity1;
                d2 = d3;
            }
        }

        return entity;
    }

    public static boolean isAdjacentTo(int i, int j, int k, int l)
    {
        World world = ModLoader.getMinecraftInstance().theWorld;
        return world.getBlockId(i, j, k + 1) == l || world.getBlockId(i, j, k - 1) == l || world.getBlockId(i + 1, j, k) == l || world.getBlockId(i - 1, j, k) == l || world.getBlockId(i, j + 1, k) == l || world.getBlockId(i, j - 1, k) == l;
    }

    public static ItemStack rechargeItem(ItemStack itemstack, EntityPlayer entityplayer)
    {
        Container container = entityplayer.inventorySlots;
        for (int i = 0; i < container.inventorySlots.size(); i++)
        {
            if (!container.getSlot(i).getHasStack())
            {
                continue;
            }
            ItemStack itemstack1 = entityplayer.inventorySlots.getSlot(i).getStack();
            if (itemstack1.itemID != mod_ThaumCraft.thaumReagent.shiftedIndex || itemstack1.getItemDamage() != 6)
            {
                continue;
            }
            itemstack1.stackSize--;
            if (itemstack1.stackSize == 0)
            {
                itemstack1 = null;
            }
            itemstack.setItemDamage(0);
            return itemstack;
        }

        return itemstack;
    }

    public static boolean putIntoChest(int i, int j, int k, IInventory iinventory, EntityItem entityitem)
    {
        for (int l = 0; l < iinventory.getSizeInventory(); l++)
        {
            if (iinventory.getStackInSlot(l) == null)
            {
                iinventory.setInventorySlotContents(l, entityitem.item);
                entityitem.setEntityDead();
                return true;
            }
            if (iinventory.getStackInSlot(l).isItemEqual(entityitem.item) && iinventory.getStackInSlot(l).getMaxStackSize() >= entityitem.item.stackSize + iinventory.getStackInSlot(l).stackSize)
            {
                entityitem.item.stackSize += iinventory.getStackInSlot(l).stackSize;
                iinventory.setInventorySlotContents(l, entityitem.item);
                entityitem.setEntityDead();
                return true;
            }
            if (iinventory.getStackInSlot(l).isItemEqual(entityitem.item) && iinventory.getStackInSlot(l).getMaxStackSize() > iinventory.getStackInSlot(l).stackSize && iinventory.getStackInSlot(l).getMaxStackSize() < entityitem.item.stackSize + iinventory.getStackInSlot(l).stackSize)
            {
                int i1 = (entityitem.item.stackSize + iinventory.getStackInSlot(l).stackSize) - iinventory.getStackInSlot(l).getMaxStackSize();
                iinventory.getStackInSlot(l).stackSize = iinventory.getStackInSlot(l).getMaxStackSize();
                entityitem.item.stackSize = i1;
            }
        }

        return false;
    }


    public static void preventFallDamage(Entity entity, boolean flag)
    {
        entity.fallDistance = 0.0F;
        if (flag)
        {
            entity.motionY *= 0.69999999999999996D;
        }
    }

    public static int getCustomSmeltItem(ItemStack itemstack)
    {
        itemstack.getItem().getItemDisplayName(itemstack);
        if (mod_ThaumCraft.smeltList.get(itemstack.getItem().getItemDisplayName(itemstack)) != null)
        {
            return ((Integer)mod_ThaumCraft.smeltList.get(itemstack.getItem().getItemDisplayName(itemstack))).intValue();
        }
        String s = StatCollector.translateToLocal((new StringBuilder()).append(itemstack.getItem().getItemName()).append(".name").toString());
        if (mod_ThaumCraft.smeltList.get(s) != null)
        {
            return ((Integer)mod_ThaumCraft.smeltList.get(s)).intValue();
        }
        else
        {
            return 0;
        }
    }

    public static boolean inAura(int i, int j, int k)
    {
        Chunk chunk = ModLoader.getMinecraftInstance().theWorld.getChunkFromBlockCoords(MathHelper.floor_double(i), MathHelper.floor_double(k));
        return chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0;
    }

    public static void DrawFaces(RenderBlocks renderblocks, Block block, int i)
    {
        DrawFaces(renderblocks, block, i, i, i, i, i, i);
    }

    public static void DrawFaces(RenderBlocks renderblocks, Block block, int i, int j, int k, int l, int i1, int j1)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, j);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, k);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, l);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, j1);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void spawnParticle(double d, double d1, double d2, double d3,
            double d4, double d5, boolean flag, boolean flag1)
    {
        if (ModLoader.getMinecraftInstance() == null || ModLoader.getMinecraftInstance().renderViewEntity == null || ModLoader.getMinecraftInstance().effectRenderer == null)
        {
            return;
        }
        else
        {
            ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityVisFX(ModLoader.getMinecraftInstance().theWorld, d, d1, d2, d3, d4, d5, flag, flag1));
            return;
        }
    }

    public static void spawnParticleTransfer(TileEntity tileentity, TileEntity tileentity1, boolean flag)
    {
        if (ModLoader.getMinecraftInstance() == null || ModLoader.getMinecraftInstance().renderViewEntity == null || ModLoader.getMinecraftInstance().effectRenderer == null)
        {
            return;
        }
        else
        {
            ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityVisTransferFX(ModLoader.getMinecraftInstance().theWorld, tileentity, tileentity1, flag));
            return;
        }
    }

    public static void spawnParticleTransfer(TileEntity tileentity, Entity entity, boolean flag)
    {
        if (ModLoader.getMinecraftInstance() == null || ModLoader.getMinecraftInstance().renderViewEntity == null || ModLoader.getMinecraftInstance().effectRenderer == null)
        {
            return;
        }
        else
        {
            ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityVisTransferFX(ModLoader.getMinecraftInstance().theWorld, tileentity, entity, flag));
            return;
        }
    }

    public static void spawnParticleFreeze(TileEntity tileentity, Entity entity)
    {
        if (ModLoader.getMinecraftInstance() == null || ModLoader.getMinecraftInstance().renderViewEntity == null || ModLoader.getMinecraftInstance().effectRenderer == null)
        {
            return;
        }
        else
        {
            ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityFreezeFX(ModLoader.getMinecraftInstance().theWorld, tileentity, entity));
            return;
        }
    }

    public static void spawnParticleScorch(TileEntity tileentity, Entity entity)
    {
        if (ModLoader.getMinecraftInstance() == null || ModLoader.getMinecraftInstance().renderViewEntity == null || ModLoader.getMinecraftInstance().effectRenderer == null)
        {
            return;
        }
        else
        {
            ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityScorchFX(ModLoader.getMinecraftInstance().theWorld, tileentity, entity));
            return;
        }
    }

    public static void createGreenFlameFX(World world, float f, float f1, float f2)
    {
        EntityFlameFX entityflamefx = new EntityFlameFX(world, f, f1, f2, 0.0D, 0.0D, 0.0D);
        entityflamefx.particleRed = 0.0F;
        ModLoader.getMinecraftInstance().effectRenderer.addEffect(entityflamefx);
    }

    public static void createPurpleSpellFX(World world, float f, float f1, float f2)
    {
        EntitySpellParticleFX entityspellparticlefx = new EntitySpellParticleFX(world, f, f1, f2, 0.0D, 0.0D, 0.0D);
        float f3 = world.rand.nextFloat() * 0.3F;
        entityspellparticlefx.particleRed = f3;
        entityspellparticlefx.particleBlue = f3;
        entityspellparticlefx.particleGreen = 0.0F;
        entityspellparticlefx.particleScale = 0.8F;
        ModLoader.getMinecraftInstance().effectRenderer.addEffect(entityspellparticlefx);
    }

    public static void createPurpleDustFX(World world, float f, float f1, float f2)
    {
        EntityReddustFX entityreddustfx = new EntityReddustFX(world, f, f1, f2, 0.0F, 0.0F, 0.0F);
        float f3 = world.rand.nextFloat() * 0.3F;
        entityreddustfx.particleRed = f3 + 0.5F;
        entityreddustfx.particleBlue = f3 + 0.5F;
        entityreddustfx.particleGreen = 0.0F;
        entityreddustfx.particleScale = 3F;
        ModLoader.getMinecraftInstance().effectRenderer.addEffect(entityreddustfx);
    }

    public static void renderItemFromTexture(Minecraft minecraft, int i)
    {
        renderItemFromTexture(minecraft, i, 1.0F, 0.0625F, true, 1.0F, 1.0F, 1.0F);
    }

    public static void renderItemFromTexture(Minecraft minecraft, int i, float f, float f1)
    {
        renderItemFromTexture(minecraft, i, f, f1, true, 1.0F, 1.0F, 1.0F);
    }

    public static void renderItemFromTexture(Minecraft minecraft, int i, float f, float f1, boolean flag, float f2, float f3, float f4)
    {
        int j = 16;
        float f5 = 256F;
        float f6 = 15.99F;
        float f7 = 0.001953125F;
        float f8 = 0.0625F;
        try
        {
            Class class1 = Class.forName("com.pclewis.mcpatcher.mod.TileSize");
            j = class1.getDeclaredField("int_size").getInt(class1);
            f5 = class1.getDeclaredField("float_size16").getFloat(class1);
            f6 = class1.getDeclaredField("float_sizeMinus0_01").getFloat(class1);
            f7 = class1.getDeclaredField("float_texNudge").getFloat(class1);
            f8 = class1.getDeclaredField("float_reciprocal").getFloat(class1);
        }
        catch (Throwable throwable) { }
        Tessellator tessellator = Tessellator.instance;
        char c = '\334';
        int k = i;
        float f9 = ((float)((k % 16) * j) + 0.0F) / f5;
        float f10 = ((float)((k % 16) * j) + f6) / f5;
        float f11 = ((float)((k / 16) * j) + 0.0F) / f5;
        float f12 = ((float)((k / 16) * j) + f6) / f5;
        float f13 = 1.0F;
        float f14 = 0.0F;
        float f15 = 0.3F;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glScalef(f, f, f);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glColor3f(f2, f3, f4);
        if (flag)
        {
            tessellator.startDrawingQuads();
            tessellator.setBrightness(c);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f10, f12);
            tessellator.addVertexWithUV(f13, 0.0D, 0.0D, f9, f12);
            tessellator.addVertexWithUV(f13, 1.0D, 0.0D, f9, f11);
            tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f10, f11);
            tessellator.draw();
        }
        tessellator.startDrawingQuads();
        tessellator.setBrightness(c);
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f1, f10, f11);
        tessellator.addVertexWithUV(f13, 1.0D, 0.0F - f1, f9, f11);
        tessellator.addVertexWithUV(f13, 0.0D, 0.0F - f1, f9, f12);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f1, f10, f12);
        tessellator.draw();
        if (flag)
        {
            tessellator.startDrawingQuads();
            tessellator.setBrightness(c);
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            for (int l = 0; l < j; l++)
            {
                float f16 = (float)l / (float)j;
                float f20 = (f10 + (f9 - f10) * f16) - f7;
                float f24 = f13 * f16;
                tessellator.addVertexWithUV(f24, 0.0D, 0.0F - f1, f20, f12);
                tessellator.addVertexWithUV(f24, 0.0D, 0.0D, f20, f12);
                tessellator.addVertexWithUV(f24, 1.0D, 0.0D, f20, f11);
                tessellator.addVertexWithUV(f24, 1.0D, 0.0F - f1, f20, f11);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setBrightness(c);
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            for (int i1 = 0; i1 < j; i1++)
            {
                float f17 = (float)i1 / (float)j;
                float f21 = (f10 + (f9 - f10) * f17) - f7;
                float f25 = f13 * f17 + f8;
                tessellator.addVertexWithUV(f25, 1.0D, 0.0F - f1, f21, f11);
                tessellator.addVertexWithUV(f25, 1.0D, 0.0D, f21, f11);
                tessellator.addVertexWithUV(f25, 0.0D, 0.0D, f21, f12);
                tessellator.addVertexWithUV(f25, 0.0D, 0.0F - f1, f21, f12);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setBrightness(c);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            for (int j1 = 0; j1 < j; j1++)
            {
                float f18 = (float)j1 / (float)j;
                float f22 = (f12 + (f11 - f12) * f18) - f7;
                float f26 = f13 * f18 + f8;
                tessellator.addVertexWithUV(0.0D, f26, 0.0D, f10, f22);
                tessellator.addVertexWithUV(f13, f26, 0.0D, f9, f22);
                tessellator.addVertexWithUV(f13, f26, 0.0F - f1, f9, f22);
                tessellator.addVertexWithUV(0.0D, f26, 0.0F - f1, f10, f22);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setBrightness(c);
            tessellator.setNormal(0.0F, -1F, 0.0F);
            for (int k1 = 0; k1 < j; k1++)
            {
                float f19 = (float)k1 / (float)j;
                float f23 = (f12 + (f11 - f12) * f19) - f7;
                float f27 = f13 * f19;
                tessellator.addVertexWithUV(f13, f27, 0.0D, f9, f23);
                tessellator.addVertexWithUV(0.0D, f27, 0.0D, f10, f23);
                tessellator.addVertexWithUV(0.0D, f27, 0.0F - f1, f10, f23);
                tessellator.addVertexWithUV(f13, f27, 0.0F - f1, f9, f23);
            }

            tessellator.draw();
        }
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public static boolean createThaumPuddle(World world, int i, int j, int k, int l)
    {
        for (int i1 = 0; i1 <= l; i1++)
        {
            int j1 = i1;
            if (j1 < 1)
            {
                j1 = 1;
            }
            int k1 = (i + world.rand.nextInt(1 + j1 * 2)) - j1;
            int l1 = j + 1;
            int i2 = (k + world.rand.nextInt(1 + j1 * 2)) - j1;
            for (int j2 = 0; j2 < 5; j2++)
            {
                int k2 = world.getBlockId(k1, l1 - j2, i2);
                int l2 = world.getBlockId(k1, l1 - j2 - 1, i2);
                if (l2 != mod_ThaumCraft.thaumCrucible.blockID && k2 != mod_ThaumCraft.thaumEffects.blockID && k2 == 0 && world.isBlockSolidOnSide(k1, l1 - j2 - 1, i2, 1))
                {
                    world.setBlockAndMetadataWithNotify(k1, l1 - j2, i2, mod_ThaumCraft.thaumEffects.blockID, 0);
                    ((TileEntityEffects)world.getBlockTileEntity(k1, l1 - j2, i2)).delay += 100 + world.rand.nextInt(100);
                    world.playSoundEffect(k1, l1 - j2, i2, "random.splash", 0.1F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
                    return true;
                }
            }
        }

        return false;
    }

    public static void poof(World world, float f, float f1, float f2)
    {
        byte byte0 = 6;
        if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
        {
            byte0 = 3;
        }
        for (int i = 0; i < byte0; i++)
        {
            world.spawnParticle("explode", f + world.rand.nextFloat(), f1 + world.rand.nextFloat(), f2 + world.rand.nextFloat(), -0D, -0D, -0D);
        }
    }

    public static void AddChunkToList(int i, int j)
    {
        LoadChunkData();
        i >>= 4;
        j >>= 4;
        for (Iterator iterator = loadedChunks.iterator(); iterator.hasNext();)
        {
            SaveChunkPos savechunkpos = (SaveChunkPos)iterator.next();
            if (savechunkpos.x == i && savechunkpos.z == j)
            {
                return;
            }
        }

        loadedChunks.add(new SaveChunkPos(i, j));
        System.out.println((new StringBuilder()).append("[Thaumcraft] Forcing chunk load at ").append(i).append(",").append(j).toString());
        SaveChunkData();
    }

    public static void SaveChunkData()
    {
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(getChunkSaveFile().getAbsolutePath());
            GZIPOutputStream gzipoutputstream = new GZIPOutputStream(fileoutputstream);
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(gzipoutputstream);
            objectoutputstream.writeObject(loadedChunks);
            objectoutputstream.flush();
            objectoutputstream.close();
        }
        catch (IOException ioexception) { }
    }

    public static void LoadChunkData()
    {
        if (!loadChunks)
        {
            return;
        }
        loadChunks = false;
        try
        {
            FileInputStream fileinputstream = new FileInputStream(getChunkSaveFile().getAbsolutePath());
            GZIPInputStream gzipinputstream = new GZIPInputStream(fileinputstream);
            ObjectInputStream objectinputstream = new ObjectInputStream(gzipinputstream);
            List list = (List)objectinputstream.readObject();
            objectinputstream.close();
            loadedChunks = list;
            System.out.println((new StringBuilder()).append("[Thaumcraft] Loaded ").append(list.size()).append(" forced chunks").toString());
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            System.out.println((new StringBuilder()).append("[Thaumcraft] Chunk file not found.").toString());
        }
        catch (Exception exception)
        {
            System.out.println((new StringBuilder()).append("[Thaumcraft] forced chunks").toString());
        }
    }

    public static void DeleteChunkFromList(int i, int j)
    {
        LoadChunkData();
        i >>= 4;
        j >>= 4;
        for (Iterator iterator = loadedChunks.iterator(); iterator.hasNext();)
        {
            SaveChunkPos savechunkpos = (SaveChunkPos)iterator.next();
            if (savechunkpos.x == i && savechunkpos.z == j)
            {
                loadedChunks.remove(savechunkpos);
                SaveChunkData();
                return;
            }
        }
    }

    public static File getChunkSaveFile()
    {
        if (WorldDir == null)
        {
            WorldDir = ((SaveHandler)ModLoader.getMinecraftInstance().theWorld.getSaveHandler()).getSaveDirectory();
        }
        return new File(WorldDir, "Thaumcraft.cl");
    }

    public static File getAWandSaveFile()
    {
        if (WorldDir == null)
        {
            WorldDir = ((SaveHandler)ModLoader.getMinecraftInstance().theWorld.getSaveHandler()).getSaveDirectory();
        }
        return new File(WorldDir, "Thaumcraft.aw");
    }

    public static void SaveAWandData()
    {
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(getAWandSaveFile().getAbsolutePath());
            GZIPOutputStream gzipoutputstream = new GZIPOutputStream(fileoutputstream);
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(gzipoutputstream);
            objectoutputstream.writeObject(aWand);
            objectoutputstream.flush();
            objectoutputstream.close();
        }
        catch (IOException ioexception) { }
    }

    public static void LoadAWandData()
    {
        try
        {
            FileInputStream fileinputstream = new FileInputStream(getAWandSaveFile().getAbsolutePath());
            GZIPInputStream gzipinputstream = new GZIPInputStream(fileinputstream);
            ObjectInputStream objectinputstream = new ObjectInputStream(gzipinputstream);
            SaveWandData savewanddata = (SaveWandData)objectinputstream.readObject();
            objectinputstream.close();
            aWand = savewanddata;
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            aWand = new SaveWandData(-1, -1, "");
        }
        catch (Exception exception) { }
    }

    static void hideTMIItems()
    throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Class class1 = Class.forName("TMIItemInfo");
        Method method = class1.getDeclaredMethod("hideItem", new Class[]
                {
                    Integer.TYPE
                });
        Method method1 = class1.getDeclaredMethod("setMaxDamageException", new Class[]
                {
                    Integer.TYPE, Integer.TYPE
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumSymbol.blockID)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumEffects.blockID)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumConduit.blockID)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumSymbolItem.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumRune.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumTemplate.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumReagent.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumGrenade.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumDetector.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.eTreeItems.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.fabric.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.talismanVoid.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.talismanBlank.shiftedIndex)
                });
        method.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.fabric.shiftedIndex)
                });
        method1.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumCrucible.blockID), Integer.valueOf(9)
                });
        method1.invoke(null, new Object[]
                {
                    Integer.valueOf(mod_ThaumCraft.thaumProcessor.blockID), Integer.valueOf(4)
                });
    }
}
