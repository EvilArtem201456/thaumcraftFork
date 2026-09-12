package thaumcraft.codechicken;

public class WRMat4
{
    float mat[];

    public WRMat4()
    {
        loadIdentity();
    }

    public WRMat4 loadIdentity()
    {
        mat = new float[16];
        mat[0] = mat[5] = mat[10] = mat[15] = 1.0F;
        return this;
    }

    public WRVector3 translate(WRVector3 wrvector3)
    {
        float f = wrvector3.x * mat[0] + wrvector3.y * mat[1] + wrvector3.z * mat[2] + mat[3];
        float f1 = wrvector3.x * mat[4] + wrvector3.y * mat[5] + wrvector3.z * mat[6] + mat[7];
        float f2 = wrvector3.x * mat[8] + wrvector3.y * mat[9] + wrvector3.z * mat[10] + mat[11];
        wrvector3.x = f;
        wrvector3.y = f1;
        wrvector3.z = f2;
        return wrvector3;
    }

    public static WRMat4 rotationMat(double d, WRVector3 wrvector3)
    {
        wrvector3 = wrvector3.copy().normalize();
        float f = wrvector3.x;
        float f1 = wrvector3.y;
        float f2 = wrvector3.z;
        d *= 0.017453292499999998D;
        float f3 = (float)Math.cos(d);
        float f4 = 1.0F - f3;
        float f5 = (float)Math.sin(d);
        WRMat4 wrmat4 = new WRMat4();
        wrmat4.mat[0] = f * f * f4 + f3;
        wrmat4.mat[1] = f1 * f * f4 + f2 * f5;
        wrmat4.mat[2] = f * f2 * f4 - f1 * f5;
        wrmat4.mat[4] = f * f1 * f4 - f2 * f5;
        wrmat4.mat[5] = f1 * f1 * f4 + f3;
        wrmat4.mat[6] = f1 * f2 * f4 + f * f5;
        wrmat4.mat[8] = f * f2 * f4 + f1 * f5;
        wrmat4.mat[9] = f1 * f2 * f4 - f * f5;
        wrmat4.mat[10] = f2 * f2 * f4 + f3;
        wrmat4.mat[15] = 1.0F;
        return wrmat4;
    }
}
