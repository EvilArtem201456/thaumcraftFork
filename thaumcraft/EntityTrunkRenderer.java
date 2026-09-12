package thaumcraft;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class EntityTrunkRenderer extends RenderLiving
{
    private ModelTrunk trunkModel;

    public EntityTrunkRenderer(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        trunkModel = (ModelTrunk)modelbase;
    }

    protected void adjustTrunk(EntityTravelingTrunk entitytravelingtrunk, float f)
    {
        int i = 2;
        float f1 = (entitytravelingtrunk.field_767_b + (entitytravelingtrunk.field_768_a - entitytravelingtrunk.field_767_b) * f) / ((float)i * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        float f3 = i;
        f1 = (float)((double)f1 / 1.5D);
        f2 = (float)((double)f2 / 1.3999999999999999D);
        if (entitytravelingtrunk.trunkType == 0 || entitytravelingtrunk.trunkType == 1)
        {
            f3 = (float)((double)f3 / 1.3999999999999999D);
        }
        if (entitytravelingtrunk.trunkType == 2)
        {
            f3 = (float)((double)f3 / 1.2D);
        }
        if (entitytravelingtrunk.trunkType == 3)
        {
            f3 = (float)((double)f3 / 1.5D);
        }
        GL11.glScalef(f2 * f3, (0.5F / f2) * f3, f2 * f3);
        GL11.glTranslatef(-0.45F, 0.45F, -0.45F);
        f1 = 1.0F - entitytravelingtrunk.lidrot;
        f1 = 1.0F - f1 * f1 * f1;
        trunkModel.chestLid.rotateAngleX = -((f1 * 3.141593F) / 2.0F);
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        adjustTrunk((EntityTravelingTrunk)entityliving, f);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        super.doRenderLiving(entityliving, d, d1, d2, f, f1);
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
    }
}
