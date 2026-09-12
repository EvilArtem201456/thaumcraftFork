package thaumcraft;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntityProcessorRenderer extends TileEntitySpecialRenderer
{
    private ModelBoreFocus model;
    private float jitter;
    private RenderBlocks rb;
    private World w;

    public TileEntityProcessorRenderer()
    {
        jitter = 0.0F;
        model = new ModelBoreFocus();
    }

    private void translateFromRotation(double d, double d1, double d2, int i)
    {
        float f = 0.0625F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
        GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(i, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
        GL11.glPushMatrix();
    }

    private void translateFromOrientation(double d, double d1, double d2, int i)
    {
        if (i == 0)
        {
            GL11.glTranslatef((float)d + 0.5F + jitter, ((float)d1 - 0.501F) + jitter, (float)d2 + 0.5F + jitter);
        }
        else if (i == 1)
        {
            GL11.glTranslatef((float)d + 0.5F + jitter, (float)d1 + 1.501F + jitter, (float)d2 + 0.5F + jitter);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        }
        else if (i == 2)
        {
            GL11.glTranslatef((float)d + 0.5F + jitter, (float)d1 + 0.5F + jitter, ((float)d2 - 0.501F) + jitter);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 3)
        {
            GL11.glTranslatef((float)d + 0.5F + jitter, (float)d1 + 0.5F + jitter, (float)d2 + 1.501F + jitter);
            GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 4)
        {
            GL11.glTranslatef(((float)d - 0.501F) + jitter, (float)d1 + 0.5F + jitter, (float)d2 + 0.5F + jitter);
            GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
        }
        else if (i == 5)
        {
            GL11.glTranslatef((float)d + 1.501F + jitter, (float)d1 + 0.5F + jitter, (float)d2 + 0.5F + jitter);
            GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
        }
    }

    public void renderEntityAt(TileEntityProcessor tileentityprocessor, double d, double d1, double d2,
            float f)
    {
        int i = -180;
        if (tileentityprocessor.infuserCookTime > 0.0F)
        {
            i = Math.round((tileentityprocessor.infuserCookTime / tileentityprocessor.currentItemCookCost - 0.5F) * 360F);
            if (++i >= 180)
            {
                i = -180;
            }
        }
        int j = ModLoader.getMinecraftInstance().gameSettings.fancyGraphics && !mod_ThaumCraft.lowGfx ? 30 : 15;
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        if (tileentityprocessor.getBlockMetadata() == 0)
        {
            translateFromRotation((float)d, (float)d1 + 0.9375F, (float)d2, i);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/main.png"));
            ThaumCraftCore.renderItemFromTexture(minecraft, 48);
        }
        if (tileentityprocessor.getBlockMetadata() == 3)
        {
            if (tileentityprocessor.isReceiving)
            {
                jitter = (tileentityprocessor.worldObj.rand.nextFloat() - tileentityprocessor.worldObj.rand.nextFloat()) * 0.02F;
            }
            else
            {
                jitter = 0.0F;
            }
            bindTextureByName("/thaumcraft/borefocus.png");
            GL11.glEnable(2977 /*GL_NORMALIZE*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glPushMatrix();
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            translateFromOrientation((float)d, (float)d1, (float)d2, tileentityprocessor.orientation);
            model.render();
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glPopMatrix();
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (tileentityprocessor.isReceiving)
            {
                GL11.glPushMatrix();
                switch (tileentityprocessor.orientation)
                {
                    case 0:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.25F, (float)d2 + 0.5F);
                        break;

                    case 1:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.75F, (float)d2 + 0.5F);
                        break;

                    case 2:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.25F);
                        break;

                    case 3:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.75F);
                        break;

                    case 4:
                        GL11.glTranslatef((float)d + 0.25F, (float)d1 + 0.5F, (float)d2 + 0.5F);
                        break;

                    case 5:
                        GL11.glTranslatef((float)d + 0.75F, (float)d1 + 0.5F, (float)d2 + 0.5F);
                        break;

                    default:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.75F, (float)d2 + 0.5F);
                        break;
                }
                Tessellator tessellator = Tessellator.instance;
                RenderHelper.disableStandardItemLighting();
                float f1 = tileentityprocessor.rotation / 90F;
                Random random = new Random(245L);
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glShadeModel(7425 /*GL_SMOOTH*/);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 1);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(2884 /*GL_CULL_FACE*/);
                GL11.glDepthMask(false);
                GL11.glPushMatrix();
                float f3 = 25F;
                for (int k = 0; (float)k < (float)j; k++)
                {
                    GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
                    tessellator.startDrawing(6);
                    float f6 = (random.nextFloat() * 20F + 5F) / f3;
                    float f7 = (random.nextFloat() * 2.0F + 1.0F) / f3;
                    tessellator.setColorRGBA_I(0x333333, 155);
                    tessellator.addVertex(0.0D, 0.0D, 0.0D);
                    tessellator.setColorRGBA_I(0xff00ff, 0);
                    tessellator.addVertex(-0.86599999999999999D * (double)f7, f6, -0.5F * f7);
                    tessellator.addVertex(0.86599999999999999D * (double)f7, f6, -0.5F * f7);
                    tessellator.addVertex(0.0D, f6, 1.0F * f7);
                    tessellator.addVertex(-0.86599999999999999D * (double)f7, f6, -0.5F * f7);
                    tessellator.draw();
                }

                GL11.glPopMatrix();
                GL11.glDepthMask(true);
                GL11.glDisable(2884 /*GL_CULL_FACE*/);
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glShadeModel(7424 /*GL_FLAT*/);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
        if (tileentityprocessor.getBlockMetadata() == 4)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.5F);
            Tessellator tessellator1 = Tessellator.instance;
            RenderHelper.disableStandardItemLighting();
            float f2 = tileentityprocessor.rotation / 90F;
            Random random1 = new Random(245L);
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            float f4 = 30F;
            float f5 = tileentityprocessor.currentVis / tileentityprocessor.maxVis + 0.001F;
            for (int l = 0; (float)l < (float)j; l++)
            {
                GL11.glRotatef(random1.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random1.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random1.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(random1.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random1.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random1.nextFloat() * 360F + f2 * 90F, 0.0F, 0.0F, 1.0F);
                tessellator1.startDrawing(6);
                float f8 = ((random1.nextFloat() * 20F + 5F) / f4) * f5;
                float f9 = ((random1.nextFloat() * 2.0F + 1.0F) / f4) * f5;
                tessellator1.setColorRGBA_I(0x333333, 155);
                tessellator1.addVertex(0.0D, 0.0D, 0.0D);
                tessellator1.setColorRGBA_I(0xff00ff, 0);
                tessellator1.addVertex(-0.86599999999999999D * (double)f9, f8, -0.5F * f9);
                tessellator1.addVertex(0.86599999999999999D * (double)f9, f8, -0.5F * f9);
                tessellator1.addVertex(0.0D, f8, 1.0F * f9);
                tessellator1.addVertex(-0.86599999999999999D * (double)f9, f8, -0.5F * f9);
                tessellator1.draw();
            }

            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            GL11.glDisable(2884 /*GL_CULL_FACE*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glShadeModel(7424 /*GL_FLAT*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            RenderHelper.enableStandardItemLighting();
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
        renderEntityAt((TileEntityProcessor)tileentity, d, d1, d2, f);
    }
}
