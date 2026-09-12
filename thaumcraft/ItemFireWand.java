package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.src.*;

public class ItemFireWand extends Item
    implements ITextureProvider
{
    public ItemFireWand(int i)
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

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 0x11940;
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        Vec3D vec3d = entityplayer.getLook(1.0F);
        int j = 0x11940 - i;
        if (j > 30)
        {
            j = 30;
        }
        for (int k = 0; k < j / 5 + 1 && itemstack.getItemDamage() <= itemstack.getMaxDamage(); k++)
        {
            if (k % 2 == 0)
            {
                itemstack.damageItem(1, entityplayer);
            }
            world.playAuxSFXAtEntity(null, 1009, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ, 0);
            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(world, entityplayer, vec3d.xCoord * 15D, vec3d.yCoord * 15D, vec3d.zCoord * 15D);
            entitysmallfireball.posX -= MathHelper.cos((entitysmallfireball.rotationYaw / 180F) * 3.141593F) * 0.16F;
            entitysmallfireball.posY -= 0.10000000149011612D;
            entitysmallfireball.posZ -= MathHelper.sin((entitysmallfireball.rotationYaw / 180F) * 3.141593F) * 0.16F;
            entitysmallfireball.posX += vec3d.xCoord * 0.5D;
            entitysmallfireball.posY += vec3d.yCoord * 0.5D;
            entitysmallfireball.posZ += vec3d.zCoord * 0.5D;
            entitysmallfireball.setPosition(entitysmallfireball.posX, entitysmallfireball.posY, entitysmallfireball.posZ);
            entitysmallfireball.yOffset = 0.0F;
            entitysmallfireball.width = 0.25F;
            entitysmallfireball.height = 0.25F;
            entitysmallfireball.rotationYaw *= world.rand.nextFloat() - world.rand.nextFloat();
            entitysmallfireball.rotationPitch *= world.rand.nextFloat() - world.rand.nextFloat();
            world.spawnEntityInWorld(entitysmallfireball);
        }
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }
}
