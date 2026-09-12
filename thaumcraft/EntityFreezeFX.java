package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class EntityFreezeFX extends EntityFX
{
    private float particleHeight;
    private float particleSpeed;
    private double px;
    private double py;
    private double pz;
    private float transferParticleScale;
    TileEntity partDestTile;
    Entity partDestEnt;

    public EntityFreezeFX(World world, TileEntity tileentity, Entity entity)
    {
        super(world, entity.posX, entity.posY, entity.posZ, 0.0D, 0.0D, 0.0D);
        partDestEnt = entity;
        posX = (double)tileentity.xCoord + 0.5D;
        posY = (double)tileentity.yCoord + 0.5D;
        posZ = (double)tileentity.zCoord + 0.5D;
        px = (partDestEnt.posX + (double)(rand.nextFloat() * 0.6F)) - 0.30000001192092896D;
        py = (partDestEnt.posY + (double)(rand.nextFloat() * 0.6F)) - 0.30000001192092896D;
        pz = (partDestEnt.posZ + (double)(rand.nextFloat() * 0.6F)) - 0.30000001192092896D;
        double d = partDestEnt.posX - posX;
        double d1 = partDestEnt.posY - posY;
        double d2 = partDestEnt.posZ - posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        motionX = d /= d3 * 8D;
        motionY = d1 /= d3 * 8D;
        motionZ = d2 /= d3 * 8D;
        transferParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        float f = rand.nextFloat() * 0.2F + 0.8F;
        particleRed = particleGreen = particleBlue = 1.0F * f;
        particleMaxAge = (int)(d3 * 8D);
        noClip = true;
        setParticleTextureIndex(0);
    }

    public void onUpdate()
    {
        setParticleTextureIndex(Math.round(((float)particleAge / (float)particleMaxAge) * 8F));
        double d = px - posX;
        double d1 = py - posY;
        double d2 = pz - posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        motionX = d /= d3 * 8D;
        motionY = d1 /= d3 * 8D;
        motionZ = d2 /= d3 * 8D;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        posX += (motionX + (double)(rand.nextFloat() * 0.2F)) - 0.10000000149011612D;
        posY += (motionY + (double)(rand.nextFloat() * 0.2F)) - 0.10000000149011612D;
        posZ += (motionZ + (double)(rand.nextFloat() * 0.2F)) - 0.10000000149011612D;
        particleAge++;
        if (particleAge >= particleMaxAge)
        {
            setEntityDead();
        }
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
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
