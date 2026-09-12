package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.src.*;
import thaumcraft.codechicken.LightningBolt;

public class ItemLightningWand extends Item
    implements ITextureProvider
{
    public ItemLightningWand(int i)
    {
        super(i);
        setMaxStackSize(1);
        setMaxDamage(100);
        setNoRepair();
    }

    public boolean isDamageable()
    {
        return true;
    }

    public boolean isRepairable()
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return false;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        Entity entity = ThaumCraftCore.getPointedEntity(world, entityplayer, 25D);
        if (entity != null)
        {
            double d = entity.posX;
            double d1 = entity.posY;
            double d2 = entity.posZ;
            entityplayer.setItemInUse(itemstack, 2);
            itemstack.damageItem(1, entityplayer);
            world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "tcsound.shock", 1.0F, 1.0F);
            double d3 = entityplayer.posX;
            double d4 = entityplayer.posY;
            double d5 = entityplayer.posZ;
            d3 -= MathHelper.cos((entityplayer.rotationYaw / 180F) * 3.141593F) * 0.16F;
            d4 -= 0.10000000149011612D;
            d5 -= MathHelper.sin((entityplayer.rotationYaw / 180F) * 3.141593F) * 0.16F;
            Vec3D vec3d = entityplayer.getLook(1.0F);
            d3 += vec3d.xCoord * 0.25D;
            d4 += vec3d.yCoord * 0.25D;
            d5 += vec3d.zCoord * 0.25D;
            LightningBolt lightningbolt = new LightningBolt(world, d3, d4, d5, d, entity.boundingBox.minY + (double)(entity.height / 2.0F), d2, world.rand.nextLong(), 6, 0.3F, 4);
            lightningbolt.defaultFractal();
            lightningbolt.setWrapper(entityplayer);
            lightningbolt.setType(4);
            lightningbolt.finalizeBolt();
        }
        return itemstack;
    }

    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }
}
