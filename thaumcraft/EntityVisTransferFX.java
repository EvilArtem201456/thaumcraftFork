package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class EntityVisTransferFX extends EntityFX
{
    int startpart;
    int direction;
    boolean tinkle;
    private float particleHeight;
    private float particleSpeed;
    private double px;
    private double py;
    private double pz;
    private float transferParticleScale;
    TileEntity partDestTile;
    Entity partDestEnt;
    private boolean follow;

    public EntityVisTransferFX(World world, TileEntity tileentity, TileEntity tileentity1, boolean flag)
    {
        super(world, tileentity1.xCoord, tileentity1.yCoord, tileentity1.zCoord, 0.0D, 0.0D, 0.0D);
        follow = false;
        partDestTile = tileentity1;
        posX = (double)tileentity.xCoord + 0.5D;
        posY = (double)tileentity.yCoord + 0.5D;
        posZ = (double)tileentity.zCoord + 0.5D;
        px = (double)partDestTile.xCoord + 0.5D;
        py = (double)partDestTile.yCoord + 0.5D;
        pz = (double)partDestTile.zCoord + 0.5D;
        double d = ((double)partDestTile.xCoord - posX) + 0.5D;
        double d1 = ((double)partDestTile.yCoord - posY) + 0.5D;
        double d2 = ((double)partDestTile.zCoord - posZ) + 0.5D;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        motionX = d /= d3 * 8D;
        motionY = d1 /= d3 * 8D;
        motionZ = d2 /= d3 * 8D;
        transferParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        float f = rand.nextFloat() * 0.6F + 0.4F;
        particleRed = particleGreen = particleBlue = 1.0F * f;
        particleGreen *= 0.3F;
        particleRed *= 0.9F;
        particleMaxAge = (int)(d3 * 8D);
        noClip = true;
        startpart = 143;
        direction = 1;
        setParticleTextureIndex(startpart);
        tinkle = flag;
    }

    public EntityVisTransferFX(World world, TileEntity tileentity, Entity entity, boolean flag)
    {
        super(world, entity.posX, entity.posY, entity.posZ, 0.0D, 0.0D, 0.0D);
        follow = false;
        follow = true;
        partDestEnt = entity;
        posX = (double)tileentity.xCoord + 0.5D;
        posY = (double)tileentity.yCoord + 0.5D;
        posZ = (double)tileentity.zCoord + 0.5D;
        px = partDestEnt.posX;
        py = partDestEnt.posY;
        pz = partDestEnt.posZ;
        double d = partDestEnt.posX - posX;
        double d1 = partDestEnt.posY - posY;
        double d2 = partDestEnt.posZ - posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        motionX = d /= d3 * 8D;
        motionY = d1 /= d3 * 8D;
        motionZ = d2 /= d3 * 8D;
        transferParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        float f = rand.nextFloat() * 0.6F + 0.4F;
        particleRed = particleGreen = particleBlue = 1.0F * f;
        particleGreen *= 0.3F;
        particleRed *= 0.9F;
        particleMaxAge = (int)(d3 * 8D);
        noClip = true;
        startpart = 143;
        direction = 1;
        setParticleTextureIndex(startpart);
        tinkle = flag;
    }

    public void onUpdate()
    {
        startpart += direction;
        if (startpart < 144)
        {
            startpart = 145;
            direction = 1;
        }
        if (startpart > 151)
        {
            startpart = 150;
            direction = -1;
        }
        setParticleTextureIndex(startpart);
        if (follow)
        {
            px = partDestEnt.posX;
            py = partDestEnt.posY;
            pz = partDestEnt.posZ;
        }
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
        posX += (motionX + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        posY += (motionY + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        posZ += (motionZ + (double)(rand.nextFloat() * 0.07F)) - 0.035000000149011612D;
        particleAge++;
        if (particleAge >= particleMaxAge)
        {
            if (tinkle)
            {
                worldObj.playSoundAtEntity(this, "random.orb", 0.05F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.6F + 2.0F));
            }
            setEntityDead();
        }
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (1.0F + f) / 3F;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        particleScale = transferParticleScale * f6;
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
