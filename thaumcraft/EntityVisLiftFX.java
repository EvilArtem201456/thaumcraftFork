package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class EntityVisLiftFX extends EntityFX
{
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    private float liftParticleScale;
    private float liftParticleHeight;
    private float liftParticleSpeed;

    public EntityVisLiftFX(World world, double d, double d1, double d2,
            double d3, float f)
    {
        super(world, d, d1, d2, 0.0D, f, 0.0D);
        liftParticleSpeed = f;
        portalPosX = posX = d;
        portalPosY = posY = d1;
        portalPosZ = posZ = d2;
        float f1 = rand.nextFloat() * 0.6F + 0.4F;
        liftParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleRed = particleGreen = particleBlue = 1.0F * f1;
        particleGreen *= 0.3F;
        particleRed *= 0.9F;
        particleMaxAge = 400;
        liftParticleHeight = (float)d3;
        noClip = true;
        setParticleTextureIndex(32);
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        posY += liftParticleSpeed;
        particleAge++;
        if (posY >= portalPosY + (double)liftParticleHeight)
        {
            setEntityDead();
        }
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (((float)posY - (float)portalPosY) + f) / liftParticleHeight;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        particleScale = liftParticleScale;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    public float getEntityBrightness(float f)
    {
        float f1 = super.getEntityBrightness(f);
        float f2 = (float)particleAge / (float)particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        return f1 * (1.0F - f2) + f2;
    }
}
