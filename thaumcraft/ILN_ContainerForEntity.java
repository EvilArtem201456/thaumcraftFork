package thaumcraft;

import java.util.List;
import net.minecraft.src.*;

public class ILN_ContainerForEntity extends Container
{
    private ILN_InventoryMob mobInv;

    public ILN_ContainerForEntity(IInventory iinventory, ILN_InventoryMob iln_inventorymob)
    {
        mobInv = iln_inventorymob;
        int i = (int)Math.floor(mobInv.slotCount / 9);
        for (int j = 0; j < i; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(mobInv, i1 + j * 9, 8 + i1 * 18, 11 + j * 18));
            }
        }

        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
                addSlot(new Slot(iinventory, j1 + k * 9 + 9, 8 + j1 * 18, 96 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++)
        {
            addSlot(new Slot(iinventory, l, 8 + l * 18, 154));
        }
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return mobInv.canInteractWith(entityplayer);
    }

    public ItemStack getStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i >= 10 && i < 46)
            {
                mergeItemStack(itemstack1, 1, 10, false);
            }
            else
            {
                mergeItemStack(itemstack1, 10, 46, false);
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(itemstack1);
            }
            else
            {
                return null;
            }
        }
        return itemstack;
    }

    public ItemStack transferStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        int j = (int)Math.floor(mobInv.slotCount / 9);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < j * 9)
            {
                if (!mergeItemStack(itemstack1, j * 9, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, j * 9, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
