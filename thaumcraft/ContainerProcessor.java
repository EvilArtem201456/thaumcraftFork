package thaumcraft;

import java.util.List;
import net.minecraft.src.*;

public class ContainerProcessor extends Container
{
    private TileEntityProcessor infuser;
    private int lastCookTime;

    public ContainerProcessor(InventoryPlayer inventoryplayer, TileEntityProcessor tileentityprocessor)
    {
        lastCookTime = 0;
        infuser = tileentityprocessor;
        if (tileentityprocessor.getBlockMetadata() == 0)
        {
            addSlot(new Slot(tileentityprocessor, 9, 62, 22));
            addSlot(new Slot(tileentityprocessor, 10, 62, 49));
        }
        else if (tileentityprocessor.getBlockMetadata() == 1)
        {
            addSlot(new Slot(tileentityprocessor, 9, 26, 45));
        }
        else if (tileentityprocessor.getBlockMetadata() == 2)
        {
            addSlot(new Slot(tileentityprocessor, 9, 62, 40));
        }
        if (tileentityprocessor.getBlockMetadata() < 2)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int l = 0; l < 3; l++)
                {
                    addSlot(new SlotProcessor(inventoryplayer.player, tileentityprocessor, l + i * 3, 98 + l * 18, 17 + i * 18));
                }
            }
        }
        for (int j = 0; j < 3; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(inventoryplayer, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
            addSlot(new Slot(inventoryplayer, k, 8 + k * 18, 142));
        }
    }

    public void updateCraftingResults()
    {
        super.updateCraftingResults();
        for (int i = 0; i < inventorySlots.size(); i++)
        {
            ICrafting icrafting = (ICrafting)inventorySlots.get(i);
            if ((float)lastCookTime != infuser.infuserCookTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, Math.round(infuser.infuserCookTime));
            }
        }

        lastCookTime = Math.round(infuser.infuserCookTime);
    }

    public void updateProgressBar(int i, int j)
    {
        if (i == 0)
        {
            infuser.infuserCookTime = j;
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return infuser.canInteractWith(entityplayer);
    }

    public ItemStack transferStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i <= 10)
            {
                if (!mergeItemStack(itemstack1, 11, 37, true))
                {
                    return null;
                }
            }
            else if (i >= 10 && i < 37)
            {
                if (!mergeItemStack(itemstack1, 37, 37, false))
                {
                    return null;
                }
            }
            else if (i >= 37 && i < 46)
            {
                if (!mergeItemStack(itemstack1, 11, 37, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 11, 46, false))
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
}
