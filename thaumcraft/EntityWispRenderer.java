package thaumcraft;

import forge.MinecraftForgeClient;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class EntityWispRenderer extends Render
{
    int particleAge;
    int moteHalfLife;

    public EntityWispRenderer()
    {
        particleAge = 0;
        moteHalfLife = 10;
        shadowSize = 0.0F;
    }

    public void renderEntityAt(Entity entity, double d, double d1, double d2,
            float f)
    {
        if (((EntityLiving)entity).getEntityHealth() <= 0)
        {
            return;
        }
        particleAge = (int)(System.currentTimeMillis() % 800L);
        float f1 = (float)particleAge / 400F;
        if (f1 > 1.0F)
        {
            f1 = 2.0F - f1;
        }
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);
        MinecraftForgeClient.bindTexture("/thaumcraft/lightning_l_purple.png");
        float f2 = ActiveRenderInfo.rotationX;
        float f3 = ActiveRenderInfo.rotationXZ;
        float f4 = ActiveRenderInfo.rotationZ;
        float f5 = ActiveRenderInfo.rotationYZ;
        float f6 = ActiveRenderInfo.rotationXY;
        float f7 = f1 * 0.6F;
        float f8 = (float)d;
        float f9 = (float)d1;
        float f10 = (float)d2;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        if (((EntityLiving)entity).getCurrentTarget() != null)
        {
            tessellator.setColorRGBA_F(1.0F, 0.0F, 0.0F, 1.0F);
        }
        else
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 0.8F);
        }
        tessellator.addVertexWithUV(f8 - f2 * f7 - f5 * f7, f9 - f3 * f7, f10 - f4 * f7 - f6 * f7, 0.0D, 1.0D);
        tessellator.addVertexWithUV((f8 - f2 * f7) + f5 * f7, f9 + f3 * f7, (f10 - f4 * f7) + f6 * f7, 1.0D, 1.0D);
        tessellator.addVertexWithUV(f8 + f2 * f7 + f5 * f7, f9 + f3 * f7, f10 + f4 * f7 + f6 * f7, 1.0D, 0.0D);
        tessellator.addVertexWithUV((f8 + f2 * f7) - f5 * f7, f9 - f3 * f7, (f10 + f4 * f7) - f6 * f7, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, ModLoader.getMinecraftInstance().renderEngine.getTexture("/thaumcraft/main.png"));
        int i = mod_ThaumCraft.thaumReagentSprite + 6;
        int j = 16;
        float f11 = 256F;
        float f12 = 15.99F;
        float f13 = 0.001953125F;
        float f15 = 0.0625F;
        try
        {
            Class class1 = Class.forName("com.pclewis.mcpatcher.mod.TileSize");
            j = class1.getDeclaredField("int_size").getInt(class1);
            f11 = class1.getDeclaredField("float_size16").getFloat(class1);
            f12 = class1.getDeclaredField("float_sizeMinus0_01").getFloat(class1);
            float f14 = class1.getDeclaredField("float_texNudge").getFloat(class1);
            float f16 = class1.getDeclaredField("float_reciprocal").getFloat(class1);
        }
        catch (Throwable throwable) { }
        f2 = ActiveRenderInfo.rotationX;
        f3 = ActiveRenderInfo.rotationXZ;
        f4 = ActiveRenderInfo.rotationZ;
        f5 = ActiveRenderInfo.rotationYZ;
        f6 = ActiveRenderInfo.rotationXY;
        float f17 = ((float)((i % 16) * j) + 0.0F) / f11;
        float f18 = ((float)((i % 16) * j) + f12) / f11;
        float f19 = ((float)((i / 16) * j) + 0.0F) / f11;
        float f20 = ((float)((i / 16) * j) + f12) / f11;
        f7 = 0.25F;
        f8 = (float)d;
        f9 = (float)d1;
        f10 = (float)d2;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        if (((EntityLiving)entity).getCurrentTarget() != null)
        {
            tessellator.setColorRGBA_F(1.0F, 0.0F, 0.0F, 1.0F);
        }
        else
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        }
        tessellator.addVertexWithUV(f8 - f2 * f7 - f5 * f7, f9 - f3 * f7, f10 - f4 * f7 - f6 * f7, f18, f20);
        tessellator.addVertexWithUV((f8 - f2 * f7) + f5 * f7, f9 + f3 * f7, (f10 - f4 * f7) + f6 * f7, f18, f19);
        tessellator.addVertexWithUV(f8 + f2 * f7 + f5 * f7, f9 + f3 * f7, f10 + f4 * f7 + f6 * f7, f17, f19);
        tessellator.addVertexWithUV((f8 + f2 * f7) - f5 * f7, f9 - f3 * f7, (f10 + f4 * f7) - f6 * f7, f17, f20);
        tessellator.draw();
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderEntityAt(entity, d, d1, d2, f);
    }
}
