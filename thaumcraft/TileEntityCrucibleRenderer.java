package thaumcraft;

import forge.MinecraftForgeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntityCrucibleRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks rb;
    private World w;

    public TileEntityCrucibleRenderer()
    {
    }

    private void translateFromOrientation(double d, double d1, double d2, int i)
    {
        float f = 0.0625F;
        GL11.glPushMatrix();
        if (i == 1)
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

    public void renderEntityAt(TileEntityCrucible tileentitycrucible, double d, double d1, double d2,
            float f)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        float f1 = tileentitycrucible.currentVis / tileentitycrucible.maxVis;
        if (tileentitycrucible.getBlockMetadata() < 2)
        {
            MinecraftForgeClient.unbindTexture();
            if (f1 > 1.0F)
            {
                for (int i = 2; i < 6; i++)
                {
                    translateFromOrientation((float)d, (float)d1, (float)d2, i);
                    ThaumCraftCore.renderItemFromTexture(minecraft, mod_ThaumCraft.thaumCrucibleSludgeFX);
                }
            }
        }
        else if (tileentitycrucible.getBlockMetadata() == 2)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.6F, (float)d2 + 0.5F);
            GL11.glPushMatrix();
            GL11.glRotatef(tileentitycrucible.rotb, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.25F, 0.0F, 0.0F);
            GL11.glPushMatrix();
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/main.png"));
            ThaumCraftCore.renderItemFromTexture(minecraft, mod_ThaumCraft.thaumReagentSprite + 6, 0.5F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.9F, (float)d2 + 0.5F);
            GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(tileentitycrucible.rota, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glPushMatrix();
            ThaumCraftCore.renderItemFromTexture(minecraft, 49);
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
        renderEntityAt((TileEntityCrucible)tileentity, d, d1, d2, f);
    }
}
