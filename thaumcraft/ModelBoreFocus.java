package thaumcraft;

import net.minecraft.src.*;

public class ModelBoreFocus extends ModelBase
{
    ModelRenderer Base;
    ModelRenderer Core;
    ModelRenderer Focus1;
    ModelRenderer Focus2;
    ModelRenderer Focus3;

    public ModelBoreFocus()
    {
        textureWidth = 64;
        textureHeight = 32;
        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(-5F, -2F, -5F, 10, 2, 10);
        Base.setRotationPoint(0.0F, 24F, 0.0F);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        setRotation(Base, 0.0F, 0.0F, 0.0F);
        Core = new ModelRenderer(this, 0, 12);
        Core.addBox(-1F, -16F, -1F, 2, 14, 2);
        Core.setRotationPoint(0.0F, 24F, 0.0F);
        Core.setTextureSize(64, 32);
        Core.mirror = true;
        setRotation(Core, 0.0F, 0.0F, 0.0F);
        Focus1 = new ModelRenderer(this, 8, 12);
        Focus1.addBox(-3F, -14F, -3F, 6, 1, 6);
        Focus1.setRotationPoint(0.0F, 24F, 0.0F);
        Focus1.setTextureSize(64, 32);
        Focus1.mirror = true;
        setRotation(Focus1, 0.0F, 0.0F, 0.0F);
        Focus2 = new ModelRenderer(this, 8, 12);
        Focus2.addBox(-3F, -10F, -3F, 6, 1, 6);
        Focus2.setRotationPoint(0.0F, 24F, 0.0F);
        Focus2.setTextureSize(64, 32);
        Focus2.mirror = true;
        setRotation(Focus2, 0.0F, 0.0F, 0.0F);
        Focus3 = new ModelRenderer(this, 8, 19);
        Focus3.addBox(-4F, -12F, -4F, 8, 1, 8);
        Focus3.setRotationPoint(0.0F, 24F, 0.0F);
        Focus3.setTextureSize(64, 32);
        Focus3.mirror = true;
        setRotation(Focus3, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Base.render(f5);
        Core.render(f5);
        Focus1.render(f5);
        Focus2.render(f5);
        Focus3.render(f5);
    }

    public void render()
    {
        Base.render(0.0625F);
        Core.render(0.0625F);
        Focus1.render(0.0625F);
        Focus2.render(0.0625F);
        Focus3.render(0.0625F);
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
