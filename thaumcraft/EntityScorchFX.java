package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class EntityScorchFX extends EntityFX
{
    private float particleHeight;
    private float particleSpeed;
    private double px;
    private double py;
    private double pz;
    private float transferParticleScale;
    TileEntity partDestTile;
    Entity partDestEnt;
    private boolean follow;

    public EntityScorchFX(World world, TileEntity tileentity, Entity entity)
    {
        super(world, entity.posX, entity.posY, entity.posZ, 0.0D, 0.0D, 0.0D);
        follow = false;
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
        motionX = d /= d3 * 3D;
        motionY = d1 /= d3 * 3D;
        motionZ = d2 /= d3 * 3D;
        transferParticleScale = particleScale = rand.nextFloat() * 0.5F + 1.0F;
        particleMaxAge = (int)(d3 * 5.5D);
        noClip = false;
        setParticleTextureIndex(48 + rand.nextInt(2));
    }

    public void onUpdate()
    {
        double d = px - posX;
        double d1 = py - posY;
        double d2 = pz - posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        motionX = d /= d3 * 3D;
        motionY = d1 /= d3 * 3D;
        motionZ = d2 /= d3 * 3D;
        motionX *= (float)(particleMaxAge - particleAge) / (float)particleMaxAge;
        motionY *= (float)(particleMaxAge - particleAge) / (float)particleMaxAge;
        motionZ *= (float)(particleMaxAge - particleAge) / (float)particleMaxAge;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        posX += (motionX + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        posY += (motionY + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        posZ += (motionZ + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        particleAge++;
        if (particleAge >= particleMaxAge)
        {
            setEntityDead();
        }
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (float)particleAge / (float)particleMaxAge;
        particleScale = transferParticleScale * (f6 + 0.5F);
        float f7 = ((float)particleAge * 2.0F) / (float)particleMaxAge;
        if (f7 > 1.0F)
        {
            f7 = 1.0F;
        }
        particleRed = particleGreen = particleBlue = f7;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    public float getEntityBrightness(float f)
    {
        float f1 = super.getEntityBrightness(f);
        float f2 = (float)particleAge / (float)particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        return 200F;
    }
}
