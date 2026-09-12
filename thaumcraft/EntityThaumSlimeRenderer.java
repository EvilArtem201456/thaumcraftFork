package thaumcraft;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class EntityThaumSlimeRenderer extends RenderLiving
{
    private ModelBase scaleAmount;

    public EntityThaumSlimeRenderer(ModelBase modelbase, ModelBase modelbase1, float f)
    {
        super(modelbase, f);
        scaleAmount = modelbase1;
    }

    protected boolean renderSlimePassModel(EntityThaumSlime entitythaumslime, int i, float f)
    {
        if (i == 0)
        {
            setRenderPassModel(scaleAmount);
            GL11.glEnable(2977 /*GL_NORMALIZE*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 771);
            return true;
        }
        if (i == 1)
        {
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        return false;
    }

    protected void scaleSlime(EntityThaumSlime entitythaumslime, float f)
    {
        int i = entitythaumslime.getSlimeSize();
        float f1 = (entitythaumslime.field_767_b + (entitythaumslime.field_768_a - entitythaumslime.field_767_b) * f) / ((float)i * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        float f3 = i;
        f1 = (float)((double)f1 / 1.5D);
        f2 = (float)((double)f2 / 1.5D);
        f3 = (float)((double)f3 / 1.5D);
        GL11.glScalef(f2 * f3, (0.5F / f2) * f3, f2 * f3);
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        scaleSlime((EntityThaumSlime)entityliving, f);
    }

    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return renderSlimePassModel((EntityThaumSlime)entityliving, i, f) ? 1 : -1;
    }
}
