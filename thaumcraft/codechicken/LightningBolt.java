package thaumcraft.codechicken;

import forge.MinecraftForgeClient;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class LightningBolt extends EntityFX
{
    private int type;
    private LightningBoltCommon main;

    public LightningBolt(World world, WRVector3 wrvector3, WRVector3 wrvector3_1, long l)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, wrvector3, wrvector3_1, l);
        setupFromMain();
    }

    public LightningBolt(World world, Entity entity, Entity entity1, long l)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, entity, entity1, l);
        setupFromMain();
    }

    public LightningBolt(World world, Entity entity, Entity entity1, long l, int i)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, entity, entity1, l, i);
        setupFromMain();
    }

    public LightningBolt(World world, TileEntity tileentity, Entity entity, long l)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, tileentity, entity, l);
        setupFromMain();
    }

    public LightningBolt(World world, double d, double d1, double d2,
            double d3, double d4, double d5, long l, int i, float f)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, d, d1, d2, d3, d4, d5, l, i, f);
        setupFromMain();
    }

    public LightningBolt(World world, double d, double d1, double d2,
            double d3, double d4, double d5, long l, int i, float f, int j)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, d, d1, d2, d3, d4, d5, l, i, f, j);
        setupFromMain();
    }

    public LightningBolt(World world, double d, double d1, double d2,
            double d3, double d4, double d5, long l, int i)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, d, d1, d2, d3, d4, d5, l, i, 1.0F);
        setupFromMain();
    }

    public LightningBolt(World world, TileEntity tileentity, double d, double d1, double d2, long l)
    {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        type = 0;
        main = new LightningBoltCommon(world, tileentity, d, d1, d2, l);
        setupFromMain();
    }

    private void setupFromMain()
    {
        main.setWrapper(this);
        particleAge = main.particleMaxAge;
        setPosition(main.start.x, main.start.y, main.start.z);
        setVelocity(0.0D, 0.0D, 0.0D);
    }

    public void defaultFractal()
    {
        main.defaultFractal();
    }

    public void fractal(int i, float f, float f1, float f2, float f3)
    {
        main.fractal(i, f, f1, f2, f3);
    }

    public void finalizeBolt()
    {
        main.finalizeBolt();
        ModLoader.getMinecraftInstance().effectRenderer.addEffect(this);
    }

    public void setWrapper(Entity entity)
    {
        main.wrapper = entity;
    }

    public void setType(int i)
    {
        type = i;
        main.type = i;
    }

    public void onUpdate()
    {
        main.onUpdate();
        if (main.particleAge >= main.particleMaxAge)
        {
            setEntityDead();
        }
    }

    private static WRVector3 getRelativeViewVector(WRVector3 wrvector3)
    {
        net.minecraft.src.EntityPlayerSP entityplayersp = ModLoader.getMinecraftInstance().thePlayer;
        return new WRVector3((float)((EntityPlayer) (entityplayersp)).posX - wrvector3.x, (float)((EntityPlayer) (entityplayersp)).posY - wrvector3.y, (float)((EntityPlayer) (entityplayersp)).posZ - wrvector3.z);
    }

    private void renderBolt(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, int i)
    {
        WRVector3 wrvector3 = new WRVector3(f3 * -f2, -f4 / f1, f1 * f2);
        float f5 = main.particleAge < 0 ? 0.0F : (float)main.particleAge / (float)main.particleMaxAge;
        float f6 = 1.0F;
        if (i == 0)
        {
            f6 = (1.0F - f5) * 0.4F;
        }
        else
        {
            f6 = 1.0F - f5 * 0.5F;
        }
        int j = (int)((((float)main.particleAge + f + (float)(int)(main.length * 3F)) / (float)(int)(main.length * 3F)) * (float)main.numsegments0);
        Iterator iterator = main.segments.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Segment segment = (Segment)iterator.next();
            if (segment.segmentno <= j)
            {
                float f7 = 0.025F * (getRelativeViewVector(segment.startpoint.point).length() / 5F + 1.0F) * (1.0F + segment.light) * 0.5F;
                WRVector3 wrvector3_1 = WRVector3.crossProduct(wrvector3, segment.prevdiff).scale(f7 / segment.sinprev);
                WRVector3 wrvector3_2 = WRVector3.crossProduct(wrvector3, segment.nextdiff).scale(f7 / segment.sinnext);
                WRVector3 wrvector3_3 = segment.startpoint.point;
                WRVector3 wrvector3_4 = segment.endpoint.point;
                float f8 = (float)((double)wrvector3_3.x - interpPosX);
                float f9 = (float)((double)wrvector3_3.y - interpPosY);
                float f10 = (float)((double)wrvector3_3.z - interpPosZ);
                float f11 = (float)((double)wrvector3_4.x - interpPosX);
                float f12 = (float)((double)wrvector3_4.y - interpPosY);
                float f13 = (float)((double)wrvector3_4.z - interpPosZ);
                tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, f6 * segment.light);
                tessellator.addVertexWithUV(f11 - wrvector3_2.x, f12 - wrvector3_2.y, f13 - wrvector3_2.z, 0.5D, 0.0D);
                tessellator.addVertexWithUV(f8 - wrvector3_1.x, f9 - wrvector3_1.y, f10 - wrvector3_1.z, 0.5D, 0.0D);
                tessellator.addVertexWithUV(f8 + wrvector3_1.x, f9 + wrvector3_1.y, f10 + wrvector3_1.z, 0.5D, 1.0D);
                tessellator.addVertexWithUV(f11 + wrvector3_2.x, f12 + wrvector3_2.y, f13 + wrvector3_2.z, 0.5D, 1.0D);
                if (segment.next == null)
                {
                    WRVector3 wrvector3_5 = segment.endpoint.point.copy().add(segment.diff.copy().normalize().scale(f7));
                    float f14 = (float)((double)wrvector3_5.x - interpPosX);
                    float f16 = (float)((double)wrvector3_5.y - interpPosY);
                    float f18 = (float)((double)wrvector3_5.z - interpPosZ);
                    tessellator.addVertexWithUV(f14 - wrvector3_2.x, f16 - wrvector3_2.y, f18 - wrvector3_2.z, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(f11 - wrvector3_2.x, f12 - wrvector3_2.y, f13 - wrvector3_2.z, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(f11 + wrvector3_2.x, f12 + wrvector3_2.y, f13 + wrvector3_2.z, 0.5D, 1.0D);
                    tessellator.addVertexWithUV(f14 + wrvector3_2.x, f16 + wrvector3_2.y, f18 + wrvector3_2.z, 0.0D, 1.0D);
                }
                if (segment.prev == null)
                {
                    WRVector3 wrvector3_6 = segment.startpoint.point.copy().sub(segment.diff.copy().normalize().scale(f7));
                    float f15 = (float)((double)wrvector3_6.x - interpPosX);
                    float f17 = (float)((double)wrvector3_6.y - interpPosY);
                    float f19 = (float)((double)wrvector3_6.z - interpPosZ);
                    tessellator.addVertexWithUV(f8 - wrvector3_1.x, f9 - wrvector3_1.y, f10 - wrvector3_1.z, 0.5D, 0.0D);
                    tessellator.addVertexWithUV(f15 - wrvector3_1.x, f17 - wrvector3_1.y, f19 - wrvector3_1.z, 0.0D, 0.0D);
                    tessellator.addVertexWithUV(f15 + wrvector3_1.x, f17 + wrvector3_1.y, f19 + wrvector3_1.z, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(f8 + wrvector3_1.x, f9 + wrvector3_1.y, f10 + wrvector3_1.z, 0.5D, 1.0D);
                }
            }
        }
        while (true);
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        particleRed = particleGreen = particleBlue = 1.0F;
        if (type == 1 || type == 6 || type == 4)
        {
            particleRed = 1.0F;
            particleGreen = particleBlue = 0.2F;
        }
        else if (type == 2)
        {
            particleRed = particleBlue = 0.2F;
            particleGreen = 1.0F;
        }
        else if (type == 5)
        {
            particleRed = particleGreen = 0.2F;
            particleBlue = 1.0F;
        }
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
        tessellator.draw();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042 /*GL_BLEND*/);
        if (type == 3)
        {
            GL11.glBlendFunc(770, 771);
        }
        else if (type == 0 || type == 1 || type == 2 || type == 5 || type == 6 || type == 4)
        {
            GL11.glBlendFunc(770, 1);
        }
        MinecraftForgeClient.bindTexture("/thaumcraft/lightning_l_purple.png");
        tessellator.startDrawingQuads();
        tessellator.setBrightness(0xf000f0);
        renderBolt(tessellator, f, f1, f2, f3, f5, 0);
        tessellator.draw();
        if (type == 3 || type == 4 || type == 6)
        {
            if (type == 6 || type == 4)
            {
                MinecraftForgeClient.bindTexture("/thaumcraft/lightning_s_green.png");
                particleGreen = 0.1F;
                particleRed = 0.1F;
                particleBlue = 1.0F;
            }
            else
            {
                MinecraftForgeClient.bindTexture("/thaumcraft/lightning_s_black.png");
            }
            tessellator.startDrawingQuads();
            tessellator.setBrightness(0xf000f0);
            renderBolt(tessellator, f, f1, f2, f3, f5, 1);
            tessellator.draw();
        }
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, ModLoader.getMinecraftInstance().renderEngine.getTexture("/particles.png"));
        tessellator.startDrawingQuads();
    }

    public int getRenderPass()
    {
        return 2;
    }
}
