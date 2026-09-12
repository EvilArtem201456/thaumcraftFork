package thaumcraft;

import forge.ITextureProvider;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class ItemPortableHole extends Item
    implements ITextureProvider
{
    public ItemPortableHole(int i)
    {
        super(i);
        setMaxStackSize(1);
        setMaxDamage(500);
        setNoRepair();
    }

    public boolean isRepairable()
    {
        return false;
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        int i1 = l;
        if (l == 0)
        {
            j--;
        }
        if (l == 1)
        {
            j++;
        }
        if (l == 2)
        {
            k--;
        }
        if (l == 3)
        {
            k++;
        }
        if (l == 4)
        {
            i--;
        }
        if (l == 5)
        {
            i++;
        }
        int j1 = i;
        int l1 = j;
        int j2 = k;
        boolean flag = false;
        int l2 = 0;
        for (int i3 = 0; i3 < 32; i3++)
        {
            switch (l)
            {
                case 0:
                    l1++;
                    break;

                case 1:
                    l1--;
                    break;

                case 2:
                    j2++;
                    break;

                case 3:
                    j2--;
                    break;

                case 4:
                    j1++;
                    break;

                case 5:
                    j1--;
                    break;
            }
            if (l1 < 0 || l1 > 127)
            {
                continue;
            }
            if (l > 1)
            {
                if (world.getBlockId(j1, l1, j2) == Block.bedrock.blockID || world.getBlockId(j1, l1 - 1, j2) == Block.bedrock.blockID || world.getBlockId(j1, l1, j2) == mod_ThaumCraft.thaumEffects.blockID || world.getBlockId(j1, l1 - 1, j2) == mod_ThaumCraft.thaumEffects.blockID)
                {
                    ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("You cannot create a hole here.");
                    return true;
                }
                if (!world.isAirBlock(j1, l1, j2) || !world.isAirBlock(j1, l1 - 1, j2))
                {
                    continue;
                }
                flag = true;
                l2 = i3;
                break;
            }
            if (world.getBlockId(j1, l1, j2) == Block.bedrock.blockID || world.getBlockId(j1, l1, j2) == mod_ThaumCraft.thaumEffects.blockID)
            {
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("You cannot create a hole here.");
                return true;
            }
            if (!world.isAirBlock(j1, l1, j2))
            {
                continue;
            }
            flag = true;
            l2 = i3;
            break;
        }

        if (!flag)
        {
            ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("Can't make a hole that deep.");
            return true;
        }
        if (l2 + itemstack.getItemDamage() > itemstack.getMaxDamage())
        {
            ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("Not enough charge.");
            return true;
        }
        EntityHiddenPH entityhiddenph = new EntityHiddenPH(world, i, j, k, l2, l);
        itemstack.damageItem(l2, entityplayer);
        entityhiddenph.blocks = new byte[l2 * 2];
        entityhiddenph.blocksMeta = new byte[l2 * 2];
        int j3 = 0;
        do
        {
            if (j3 >= 2)
            {
                break;
            }
            int k1 = i;
            int i2 = j;
            int k2 = k;
            double d = j3 != 0 ? -1D : 0.0D;
            for (int k3 = 0; k3 < l2; k3++)
            {
                switch (l)
                {
                    case 0:
                        i2++;
                        break;

                    case 1:
                        i2--;
                        break;

                    case 2:
                        k2++;
                        break;

                    case 3:
                        k2--;
                        break;

                    case 4:
                        k1++;
                        break;

                    case 5:
                        k1--;
                        break;
                }
                if (i2 - j3 < 0 || i2 - j3 > 127)
                {
                    continue;
                }
                entityhiddenph.blocks[k3 + l2 * j3] = (byte)(world.getBlockId(k1, i2 - j3, k2) - 128);
                entityhiddenph.blocksMeta[k3 + l2 * j3] = (byte)world.getBlockMetadata(k1, i2 - j3, k2);
                world.setBlockAndMetadataWithNotify(k1, i2 - j3, k2, mod_ThaumCraft.thaumEffects.blockID, 1);
                for (int l3 = 0; l3 < 3; l3++)
                {
                    world.spawnParticle("portal", (float)k1 + world.rand.nextFloat(), (float)(i2 - j3) + world.rand.nextFloat(), (float)k2 + world.rand.nextFloat(), 0.0D, d, 0.0D);
                }
            }

            if (l < 2)
            {
                break;
            }
            j3++;
        }
        while (true);
        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.endermen.portal", 1.0F, 1.0F);
        world.spawnEntityInWorld(entityhiddenph);
        return true;
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.epic;
    }

    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }
}
