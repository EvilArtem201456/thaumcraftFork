package thaumcraft;

import forge.MinecraftForgeClient;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntityEffectsRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks rb;
    private World w;

    public TileEntityEffectsRenderer()
    {
    }

    private void translateFromOrientation(double d, double d1, double d2, int i)
    {
        float f = 0.0625F;
        GL11.glPushMatrix();
        if (i == 0)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2);
            GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 1)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 2)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2 + 1.0F + f);
        }
        else if (i == 3)
        {
            GL11.glTranslatef((float)d + 1.0F, (float)d1, (float)d2 - f);
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        }
        else if (i == 4)
        {
            GL11.glTranslatef((float)d + 1.0F + f, (float)d1, (float)d2 + 1.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        }
        else if (i == 5)
        {
            GL11.glTranslatef((float)d - f, (float)d1, (float)d2);
            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glPushMatrix();
    }

    public void renderEntityAt(TileEntityEffects tileentityeffects, double d, double d1, double d2,
            float f)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        if (tileentityeffects.getBlockMetadata() == 0)
        {
            GL11.glPushMatrix();
            MinecraftForgeClient.unbindTexture();
            translateFromOrientation((float)d, (double)(float)d1 + 0.01D, (float)d2, 1);
            ThaumCraftCore.renderItemFromTexture(minecraft, mod_ThaumCraft.thaumSludgeFX);
            GL11.glPopMatrix();
        }
        else if (tileentityeffects.getBlockMetadata() == 2)
        {
            float f1 = (float)Math.abs(tileentityeffects.bob - 15) * 0.005F;
            float f2 = 0.0F;
            TileEntity tileentity = ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(tileentityeffects.xCoord, tileentityeffects.yCoord - 1, tileentityeffects.zCoord);
            if (tileentity != null && (tileentity instanceof TileEntityCrucible) && ((TileEntityCrucible)tileentity).chargeup > 0)
            {
                f2 = ModLoader.getMinecraftInstance().theWorld.rand.nextFloat() * 0.001F * (float)((TileEntityCrucible)tileentity).chargeup;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.5F + f2, (float)d1 + f1 + f2, (float)d2 + 0.5F + f2);
            GL11.glPushMatrix();
            GL11.glRotatef(tileentityeffects.rota, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/main.png"));
            ThaumCraftCore.renderItemFromTexture(minecraft, tileentityeffects.contains);
            GL11.glPopMatrix();
        }
    }

    public void func_31069_a(World world)
    {
        rb = new RenderBlocks(world);
        w = world;
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
            float f)
    {
        renderEntityAt((TileEntityEffects)tileentity, d, d1, d2, f);
    }
}
