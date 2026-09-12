package thaumcraft;

import java.util.Random;
import net.minecraft.src.EntityPortalFX;
import net.minecraft.src.World;

public class EntityVisFX extends EntityPortalFX
{
    boolean renderNormal;
    boolean tinkle;
    int startpart;
    int direction;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;

    public EntityVisFX(World world, double d, double d1, double d2,
            double d3, double d4, double d5, boolean flag,
            boolean flag1)
    {
        super(world, d, d1, d2, d3, d4, d5);
        portalPosX = posX = d;
        portalPosY = posY = d1;
        portalPosZ = posZ = d2;
        startpart = 143;
        direction = 1;
        setParticleTextureIndex(startpart);
        renderNormal = flag;
        tinkle = false;
        tinkle = flag1;
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
        if (renderNormal)
        {
            super.onUpdate();
            if (isDead && tinkle)
            {
                worldObj.playSoundAtEntity(this, "random.orb", 0.05F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.6F + 2.0F));
            }
            return;
        }
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float f = (float)particleAge / (float)particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        float f2 = f;
        f = 1.0F - f;
        posX = portalPosX + motionX * (double)f2;
        posY = portalPosY + motionY * (double)f2;
        posZ = portalPosZ + motionZ * (double)f2;
        if (particleAge++ >= particleMaxAge)
        {
            if (tinkle)
            {
                worldObj.playSoundAtEntity(this, "random.orb", 0.05F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.6F + 2.0F));
            }
            setEntityDead();
        }
    }
}
