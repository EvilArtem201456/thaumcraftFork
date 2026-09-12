package thaumcraft;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class EntityTGRenderer extends Render
{
    public EntityTGRenderer()
    {
        shadowSize = 0.1F;
    }

    public void renderEntityAt(EntityHiddenTG entityhiddentg, double d, double d1, double d2,
            float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        int i = ModLoader.getMinecraftInstance().gameSettings.fancyGraphics && !mod_ThaumCraft.lowGfx ? 60 : 30;
        Tessellator tessellator = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        float f1 = (float)entityhiddentg.duration / (float)entityhiddentg.startDuration;
        float f2 = 0.9F;
        float f3 = 0.0F;
        if (f1 > 0.8F)
        {
            f3 = (f1 - 0.8F) / 0.2F;
        }
        Random random = new Random(245L);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glShadeModel(7425 /*GL_SMOOTH*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        for (int j = 0; (float)j < ((f1 + f1 * f1) / 2.0F) * (float)i; j++)
        {
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
            tessellator.startDrawing(6);
            float f4 = random.nextFloat() * 20F + 5F + f3 * 10F;
            float f6 = random.nextFloat() * 2.0F + 1.0F + f3 * 2.0F;
            f4 /= 600 / entityhiddentg.startDuration;
            f6 /= 600 / entityhiddentg.startDuration;
            tessellator.setColorRGBA_I(0xffffff, (int)(255F * (1.0F - f3)));
            tessellator.addVertex(0.0D, 0.0D, 0.0D);
            tessellator.setColorRGBA_I(0xff00ff, 0);
            tessellator.addVertex(-0.86599999999999999D * (double)f6, f4, -0.5F * f6);
            tessellator.addVertex(0.86599999999999999D * (double)f6, f4, -0.5F * f6);
            tessellator.addVertex(0.0D, f4, 1.0F * f6);
            tessellator.addVertex(-0.86599999999999999D * (double)f6, f4, -0.5F * f6);
            tessellator.draw();
        }

        for (int k = 0; (float)k < ((f2 + f2 * f2) / 2.0F) * (float)i; k++)
        {
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
            tessellator.startDrawing(6);
            float f5 = random.nextFloat() * 20F + 5F + f3 * 10F;
            float f7 = random.nextFloat() * 2.0F + 1.0F + f3 * 2.0F;
            f5 /= 1500 / entityhiddentg.startDuration;
            f7 /= 1500 / entityhiddentg.startDuration;
            tessellator.setColorRGBA_I(0xffffff, (int)(255F * (1.0F - f3)));
            tessellator.addVertex(0.0D, 0.0D, 0.0D);
            tessellator.setColorRGBA_I(255, 0);
            tessellator.addVertex(-0.86599999999999999D * (double)f7, f5, -0.5F * f7);
            tessellator.addVertex(0.86599999999999999D * (double)f7, f5, -0.5F * f7);
            tessellator.addVertex(0.0D, f5, 1.0F * f7);
            tessellator.addVertex(-0.86599999999999999D * (double)f7, f5, -0.5F * f7);
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

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderEntityAt((EntityHiddenTG)entity, d, d1, d2, f);
    }
}
