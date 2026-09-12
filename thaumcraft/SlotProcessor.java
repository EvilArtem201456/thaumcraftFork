package thaumcraft;

import net.minecraft.src.*;

public class SlotProcessor extends Slot
{
    private EntityPlayer thePlayer;

    public SlotProcessor(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        thePlayer = entityplayer;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        super.onPickupFromSlot(itemstack);
    }
}
