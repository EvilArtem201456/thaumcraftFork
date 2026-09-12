package thaumcraft;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class ModelETree extends ModelBase
{
    ModelRenderer Trunk;
    ArrayList Leaves;

    public ModelETree(int i)
    {
        Leaves = new ArrayList();
        textureWidth = 256;
        textureHeight = 128;
        Trunk = new ModelRenderer(this, 0, 0);
        Trunk.addBox(-8F, -32F, -8F, 16, 64, 16);
        Trunk.setRotationPoint(0.0F, -8F, 0.0F);
        Trunk.setTextureSize(64, 32);
        Trunk.mirror = true;
        setRotation(Trunk, 0.0F, 0.0F, 0.0F);
        Random random = new Random();
        random.setSeed(i);
        byte byte0 = 5;
        boolean flag = true;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = byte0 - random.nextInt(2) - 3;
        int k1 = byte0 - j1;
        int l1 = 1 + random.nextInt(k1 + 1);
        for (int i2 = k + byte0; i2 >= k + j1; i2--)
        {
            for (int j2 = j - i1; j2 <= j + i1; j2++)
            {
                int k2 = j2 - j;
                for (int l2 = l - i1; l2 <= l + i1; l2++)
                {
                    int i3 = l2 - l;
                    if (Math.abs(k2) != i1 || Math.abs(i3) != i1 || i1 <= 0)
                    {
                        ModelRenderer modelrenderer = new ModelRenderer(this, 64, 0);
                        modelrenderer.addBox(-8F, -8F, -8F, 16, 16, 16);
                        modelrenderer.setTextureSize(64, 32);
                        modelrenderer.mirror = true;
                        modelrenderer.setRotationPoint(-j2 * 16, -16 - i2 * 16, -l2 * 16);
                        setRotation(modelrenderer, (float)random.nextInt(2) * 3.141593F, (float)random.nextInt(2) * 3.141593F, (float)random.nextInt(2) * 3.141593F);
                        Leaves.add(modelrenderer);
                    }
                }
            }

            if (i1 >= 1 && i2 == k + j1 + 1)
            {
                i1--;
                continue;
            }
            if (i1 < l1)
            {
                i1++;
            }
        }
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Trunk.render(f5);
        for (int i = 0; i < Leaves.size(); i++)
        {
            ((ModelRenderer)Leaves.get(i)).render(f5);
        }
    }

    private void setRotation(ModelRenderer modelrenderer, float f, float f1, float f2)
    {
        modelrenderer.rotateAngleX = f;
        modelrenderer.rotateAngleY = f1;
        modelrenderer.rotateAngleZ = f2;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
