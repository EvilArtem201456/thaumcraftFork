package thaumcraft;

import forge.MinecraftForgeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class EntityWispFX extends EntityFX
{
    float moteParticleScale;
    int moteHalfLife;

    public EntityWispFX(World world, double d, double d1, double d2,
            float f, float f1, float f2)
    {
        this(world, d, d1, d2, 1.0F, f, f1, f2);
    }

    public EntityWispFX(World world, double d, double d1, double d2,
            float f, float f1, float f2, float f3)
    {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        if (f1 == 0.0F)
        {
            f1 = 1.0F;
        }
        particleRed = f1;
        particleGreen = f2;
        particleBlue = f3;
        particleScale *= 1.0F;
        particleScale *= f;
        moteParticleScale = particleScale;
        particleMaxAge = (int)(24D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
        particleMaxAge *= f;
        moteHalfLife = particleMaxAge / 2;
        noClip = false;
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        net.minecraft.src.EntityPlayerSP entityplayersp = ModLoader.getMinecraftInstance().thePlayer;
        int i = 100;
        if (!ModLoader.getMinecraftInstance().gameSettings.fancyGraphics || mod_ThaumCraft.lowGfx)
        {
            i = 50;
        }
        if (entityplayersp.getDistance(posX, posY, posZ) > (double)i)
        {
            return;
        }
        float f6 = (float)particleAge / (float)moteHalfLife;
        if (f6 > 1.0F)
        {
            f6 = 2.0F - f6;
        }
        particleScale = moteParticleScale * f6;
        tessellator.draw();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);
        MinecraftForgeClient.bindTexture("/thaumcraft/lightning_l_purple.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        float f7 = 0.5F * particleScale;
        float f8 = (float)((prevPosX + (posX - prevPosX) * (double)f) - interpPosX);
        float f9 = (float)((prevPosY + (posY - prevPosY) * (double)f) - interpPosY);
        float f10 = (float)((prevPosZ + (posZ - prevPosZ) * (double)f) - interpPosZ);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 0.5F);
        tessellator.addVertexWithUV(f8 - f1 * f7 - f4 * f7, f9 - f2 * f7, f10 - f3 * f7 - f5 * f7, 0.0D, 1.0D);
        tessellator.addVertexWithUV((f8 - f1 * f7) + f4 * f7, f9 + f2 * f7, (f10 - f3 * f7) + f5 * f7, 1.0D, 1.0D);
        tessellator.addVertexWithUV(f8 + f1 * f7 + f4 * f7, f9 + f2 * f7, f10 + f3 * f7 + f5 * f7, 1.0D, 0.0D);
        tessellator.addVertexWithUV((f8 + f1 * f7) - f4 * f7, f9 - f2 * f7, (f10 + f3 * f7) - f5 * f7, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, ModLoader.getMinecraftInstance().renderEngine.getTexture("/particles.png"));
        tessellator.startDrawingQuads();
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge)
        {
            setEntityDead();
        }
    }
}
