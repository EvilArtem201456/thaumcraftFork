package thaumcraft;

import java.util.Random;
import net.minecraft.src.*;

public class EntityGrub extends EntitySilverfish
{
    public EntityGrub(World world)
    {
        super(world);
        texture = (new StringBuilder()).append("/thaumcraft/grub").append(world.rand.nextInt(4)).append(".png").toString();
        experienceValue = 0;
    }

    protected boolean canDespawn()
    {
        return true;
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
        }
    }

    protected void updateEntityActionState()
    {
        if (entityAge < 10)
        {
            fallDistance = 0.0F;
        }
        super.updateEntityActionState();
        if (worldObj.multiplayerWorld)
        {
            return;
        }
        else
        {
            return;
        }
    }

    protected void dropFewItems(boolean flag, int i)
    {
        if (worldObj.rand.nextInt(4) == 0)
        {
            entityDropItem(new ItemStack(mod_ThaumCraft.eTreeItems, 1, 0), 0.2F);
        }
    }
}
