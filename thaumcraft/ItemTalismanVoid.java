package thaumcraft;

import forge.ITextureProvider;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class ItemTalismanVoid extends Item
    implements ITextureProvider
{
    public ItemTalismanVoid(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    public int getIconFromDamage(int i)
    {
        return mod_ThaumCraft.eVTSprite + i;
    }

    public boolean getTalismanDestination(int i)
    {
        if (i == 6)
        {
            i = -1;
        }
        World world = ModLoader.getMinecraftInstance().theWorld;
        ArrayList arraylist = new ArrayList();
        for (int j = 0; j < TileEntityThaum.TPDestinations.size(); j++)
        {
            if (i == ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).rune && world.getBlockTileEntity(((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).x, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).y, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).z) != null && !((TileEntityThaum)world.getBlockTileEntity(((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).x, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).y, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).z)).gettingPower() && ((TileEntitySymbol)world.getBlockTileEntity(((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).x, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).y, ((TileEntityThaum.TPDest)TileEntityThaum.TPDestinations.get(j)).z)).cooldown == 0)
            {
                arraylist.add(TileEntityThaum.TPDestinations.get(j));
            }
        }

        if (arraylist.size() > 0)
        {
            TileEntityThaum.TPDest tpdest = (TileEntityThaum.TPDest)arraylist.get(world.rand.nextInt(arraylist.size()));
            TileEntitySymbol tileentitysymbol = (TileEntitySymbol)world.getBlockTileEntity(tpdest.x, tpdest.y, tpdest.z);
            tileentitysymbol.cooldown = 100;
            tileentitysymbol.delay = 5;
            float f = 0.0F;
            switch (tileentitysymbol.orientation)
            {
                case 2:
                    f = 180F;
                    break;

                case 3:
                    f = 0.0F;
                    break;

                case 4:
                    f = 90F;
                    break;

                case 5:
                    f = 270F;
                    break;
            }
            ModLoader.getMinecraftInstance().thePlayer.motionX = 0.0D;
            ModLoader.getMinecraftInstance().thePlayer.motionZ = 0.0D;
            world.playSoundEffect(ModLoader.getMinecraftInstance().thePlayer.posX, ModLoader.getMinecraftInstance().thePlayer.posY, ModLoader.getMinecraftInstance().thePlayer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
            ThaumCraftCore.poof(world, (float)ModLoader.getMinecraftInstance().thePlayer.posX - 0.5F, (float)ModLoader.getMinecraftInstance().thePlayer.posY - 0.5F, (float)ModLoader.getMinecraftInstance().thePlayer.posZ - 0.5F);
            if (tileentitysymbol.orientation < 2 || !tileentitysymbol.onArch())
            {
                ModLoader.getMinecraftInstance().thePlayer.setLocationAndAngles((double)tileentitysymbol.xCoord + 0.5D, (double)tileentitysymbol.yCoord + 0.5D, (double)tileentitysymbol.zCoord + 0.5D, f, ModLoader.getMinecraftInstance().thePlayer.rotationPitch);
            }
            else
            {
                ModLoader.getMinecraftInstance().thePlayer.setLocationAndAngles((double)tileentitysymbol.xCoord + 0.5D, (double)tileentitysymbol.yCoord - 1.8999999999999999D, (double)tileentitysymbol.zCoord + 0.5D, f, ModLoader.getMinecraftInstance().thePlayer.rotationPitch);
            }
            world.playSoundEffect(ModLoader.getMinecraftInstance().thePlayer.posX, ModLoader.getMinecraftInstance().thePlayer.posY, ModLoader.getMinecraftInstance().thePlayer.posZ, "mob.endermen.portal", 1.0F, 1.0F);
            ThaumCraftCore.poof(world, (float)ModLoader.getMinecraftInstance().thePlayer.posX - 0.5F, (float)ModLoader.getMinecraftInstance().thePlayer.posY - 0.5F, (float)ModLoader.getMinecraftInstance().thePlayer.posZ - 0.5F);
            return true;
        }
        else
        {
            return false;
        }
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        Container container = entityplayer.inventorySlots;
        for (int i = 0; i < container.inventorySlots.size(); i++)
        {
            if (!container.getSlot(i).getHasStack())
            {
                continue;
            }
            ItemStack itemstack1 = entityplayer.inventorySlots.getSlot(i).getStack();
            if (itemstack1.itemID != mod_ThaumCraft.thaumReagent.shiftedIndex || itemstack1.getItemDamage() != 6)
            {
                continue;
            }
            if (getTalismanDestination(itemstack.getItemDamage()))
            {
                itemstack1.stackSize--;
                if (itemstack1.stackSize == 0)
                {
                    itemstack1 = null;
                }
            }
            else
            {
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("No valid destinations found.");
            }
            return itemstack;
        }

        ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("This talisman needs vis crystals to function.");
        return itemstack;
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if ((tileentity instanceof TileEntitySymbol) && tileentity.getBlockMetadata() == 5)
        {
            int i1 = ((TileEntitySymbol)tileentity).getRune();
            if (i1 == -1)
            {
                i1 = 6;
            }
            if (i1 != itemstack.getItemDamage())
            {
                itemstack.setItemDamage(i1);
                world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ, "random.orb", 0.3F, 0.75F + world.rand.nextFloat() * 0.5F);
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("This talisman is now linked to a new network.");
                return true;
            }
        }
        return super.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l);
    }

    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "iron";
                break;

            case 1:
                s = "gold";
                break;

            case 2:
                s = "diamond";
                break;

            case 3:
                s = "black";
                break;

            case 4:
                s = "red";
                break;

            case 5:
                s = "lapis";
                break;

            case 6:
                s = "blank";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }
}
