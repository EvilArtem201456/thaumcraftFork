package thaumcraft;

import java.lang.reflect.Field;
import net.minecraft.src.*;

public class ThaumCubeFx extends TextureFX
{
    private int imgSize;
    private int imgPixels;
    protected float field_1147_g[];
    protected float field_1146_h[];
    protected float field_1145_i[];
    protected float field_1144_j[];
    protected float field_1158_g[];
    protected float field_1157_h[];
    protected float field_1156_i[];
    protected float field_1155_j[];
    private int tickCounter;

    public ThaumCubeFx()
    {
        super(mod_ThaumCraft.thaumCubeSideFX);
        byte byte0 = 16;
        int j = 256;
        try
        {
            Class class1 = Class.forName("com.pclewis.mcpatcher.mod.TileSize");
            j = class1.getDeclaredField("int_numPixels").getInt(class1);
            int i = class1.getDeclaredField("int_size").getInt(class1);
        }
        catch (Throwable throwable) { }
        field_1158_g = new float[j];
        field_1157_h = new float[j];
        field_1156_i = new float[j];
        field_1155_j = new float[j];
        tickCounter = 0;
        field_1147_g = new float[j];
        field_1146_h = new float[j];
        field_1145_i = new float[j];
        field_1144_j = new float[j];
    }

    public void onTick()
    {
        for (int i = 0; i < imgSize; i++)
        {
            for (int j = 0; j < imgSize; j++)
            {
                float f = 0.0F;
                int l = (int)(MathHelper.sin(((float)j * 3.141593F * 2.0F) / (float)imgSize) * 1.2F);
                int i1 = (int)(MathHelper.sin(((float)i * 3.141593F * 2.0F) / (float)imgSize) * 1.2F);
                for (int k1 = i - 1; k1 <= i + 1; k1++)
                {
                    for (int i2 = j - 1; i2 <= j + 1; i2++)
                    {
                        int k2 = k1 + l & 0xf;
                        int i3 = i2 + i1 & 0xf;
                        f += field_1147_g[k2 + i3 * imgSize];
                    }
                }

                field_1146_h[i + j * imgSize] = f / 10F + ((field_1145_i[(i + 0 & 0xf) + (j + 0 & 0xf) * imgSize] + field_1145_i[(i + 1 & 0xf) + (j + 0 & 0xf) * imgSize] + field_1145_i[(i + 1 & 0xf) + (j + 1 & 0xf) * imgSize] + field_1145_i[(i + 0 & 0xf) + (j + 1 & 0xf) * imgSize]) / 4F) * 0.8F;
                field_1145_i[i + j * imgSize] += field_1144_j[i + j * imgSize] * 0.01F;
                if (field_1145_i[i + j * imgSize] < 0.0F)
                {
                    field_1145_i[i + j * imgSize] = 0.0F;
                }
                field_1144_j[i + j * imgSize] -= 0.06F;
                if (Math.random() < 0.0050000000000000001D)
                {
                    field_1144_j[i + j * imgSize] = 1.5F;
                }
            }
        }

        float af[] = field_1146_h;
        field_1146_h = field_1147_g;
        field_1147_g = af;
        for (int k = 0; k < imgPixels; k++)
        {
            float f1 = field_1147_g[k] * 2.0F;
            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            if (f1 < 0.0F)
            {
                f1 = 0.0F;
            }
            float f2 = f1;
            int j1 = (int)(f2 * 155F);
            int l1 = (int)(f2 * 128F);
            int j2 = (int)(f2 * 255F);
            int l2 = (int)(116F + f2 * 30F);
            if (anaglyphEnabled)
            {
                int j3 = (j1 * 30 + l1 * 59 + j2 * 11) / 100;
                int k3 = (j1 * 30 + l1 * 70) / 100;
                int l3 = (j1 * 30 + j2 * 70) / 100;
                j1 = j3;
                l1 = k3;
                j2 = l3;
            }
            imageData[k * 4 + 0] = (byte)j1;
            imageData[k * 4 + 1] = (byte)l1;
            imageData[k * 4 + 2] = (byte)j2;
            imageData[k * 4 + 3] = (byte)l2;
        }
    }
}
