package thaumcraft;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityEldritchTreeRenderer extends RenderLiving
{
    private ModelETree treeModel[];

    public EntityEldritchTreeRenderer(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        treeModel = new ModelETree[10];
        for (int i = 0; i < 10; i++)
        {
            treeModel[i] = new ModelETree(i);
        }
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        EntityEldritchTree entityeldritchtree = (EntityEldritchTree)entityliving;
        mainModel = treeModel[entityeldritchtree.modelSeed];
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        EntityEldritchTree entityeldritchtree = (EntityEldritchTree)entityliving;
        World world = ModLoader.getMinecraftInstance().theWorld;
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        if (entityeldritchtree.shudder > 0)
        {
            f2 = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.15F;
            f3 = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.15F;
            f4 = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.15F;
        }
        super.doRenderLiving(entityliving, d + (double)f2, d1 + (double)f3, d2 + (double)f4, f, f1);
    }
}
