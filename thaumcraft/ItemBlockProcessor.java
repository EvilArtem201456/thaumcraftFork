package thaumcraft;

import net.minecraft.src.*;

public class ItemBlockProcessor extends ItemBlock
{
    public ItemBlockProcessor(int i, Block block)
    {
        super(i);
        setHasSubtypes(true);
    }

    public int getMetadata(int i)
    {
        return i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        String s = "";
        switch (itemstack.getItemDamage())
        {
            case 0:
                s = "infuser";
                break;

            case 1:
                s = "duplicator";
                break;

            case 2:
                s = "borecore";
                break;

            case 3:
                s = "borefocus";
                break;

            case 4:
                s = "generator";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
