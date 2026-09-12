package thaumcraft.codechicken;

import net.minecraft.src.*;

public class WRVector3
{
    public float x;
    public float y;
    public float z;

    public WRVector3(double d, double d1, double d2)
    {
        x = (float)d;
        y = (float)d1;
        z = (float)d2;
    }

    public WRVector3(TileEntity tileentity)
    {
        x = (float)tileentity.xCoord + 0.5F;
        y = (float)tileentity.yCoord + 0.5F;
        z = (float)tileentity.zCoord + 0.5F;
    }

    public WRVector3(Entity entity)
    {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public WRVector3 add(WRVector3 wrvector3)
    {
        x += wrvector3.x;
        y += wrvector3.y;
        z += wrvector3.z;
        return this;
    }

    public WRVector3 sub(WRVector3 wrvector3)
    {
        x -= wrvector3.x;
        y -= wrvector3.y;
        z -= wrvector3.z;
        return this;
    }

    public WRVector3 scale(float f)
    {
        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    public WRVector3 scale(float f, float f1, float f2)
    {
        x *= f;
        y *= f1;
        z *= f2;
        return this;
    }

    public WRVector3 normalize()
    {
        float f = length();
        x /= f;
        y /= f;
        z /= f;
        return this;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthPow2()
    {
        return x * x + y * y + z * z;
    }

    public WRVector3 copy()
    {
        return new WRVector3(x, y, z);
    }

    public static WRVector3 crossProduct(WRVector3 wrvector3, WRVector3 wrvector3_1)
    {
        return new WRVector3(wrvector3.y * wrvector3_1.z - wrvector3.z * wrvector3_1.y, wrvector3.z * wrvector3_1.x - wrvector3.x * wrvector3_1.z, wrvector3.x * wrvector3_1.y - wrvector3.y * wrvector3_1.x);
    }

    public static WRVector3 xCrossProduct(WRVector3 wrvector3)
    {
        return new WRVector3(0.0D, wrvector3.z, -wrvector3.y);
    }

    public static WRVector3 zCrossProduct(WRVector3 wrvector3)
    {
        return new WRVector3(-wrvector3.y, wrvector3.x, 0.0D);
    }

    public static float dotProduct(WRVector3 wrvector3, WRVector3 wrvector3_1)
    {
        return wrvector3.x * wrvector3_1.x + wrvector3.y * wrvector3_1.y + wrvector3.z * wrvector3_1.z;
    }

    public static float angle(WRVector3 wrvector3, WRVector3 wrvector3_1)
    {
        return anglePreNorm(wrvector3.copy().normalize(), wrvector3_1.copy().normalize());
    }

    public static float anglePreNorm(WRVector3 wrvector3, WRVector3 wrvector3_1)
    {
        return (float)Math.acos(dotProduct(wrvector3, wrvector3_1));
    }

    public WRVector3 rotate(float f, WRVector3 wrvector3)
    {
        return WRMat4.rotationMat(f, wrvector3).translate(this);
    }

    public String toString()
    {
        return (new StringBuilder("[")).append(x).append(",").append(y).append(",").append(z).append("]").toString();
    }

    public Vec3D toVec3D()
    {
        return Vec3D.createVector(x, y, z);
    }

    public static WRVector3 getPerpendicular(WRVector3 wrvector3)
    {
        if (wrvector3.z == 0.0F)
        {
            return zCrossProduct(wrvector3);
        }
        else
        {
            return xCrossProduct(wrvector3);
        }
    }

    public boolean isZero()
    {
        return x == 0.0F && y == 0.0F && z == 0.0F;
    }
}
