package thaumcraft;

import java.util.*;
import net.minecraft.src.*;

public class RecipesProcessor
{
    private static final RecipesProcessor infusingBase = new RecipesProcessor();
    private Map infusingList;
    private Map costList;
    private Map damageList;

    public static final RecipesProcessor infusing()
    {
        return infusingBase;
    }

    private RecipesProcessor()
    {
        infusingList = new HashMap();
        costList = new HashMap();
        damageList = new HashMap();
    }

    public void addInfusing(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j)
    {
        infusingList.put(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                }), itemstack2);
        costList.put(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                }), Integer.valueOf(i));
        damageList.put(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                }), Integer.valueOf(j));
    }

    public ItemStack getInfusingResult(ItemStack itemstack, ItemStack itemstack1)
    {
        if (itemstack == null || itemstack1 == null)
        {
            return null;
        }
        else
        {
            ItemStack itemstack2 = (ItemStack)infusingList.get(Arrays.asList(new Integer[]
                    {
                        Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                    }));
            return itemstack2;
        }
    }

    public ItemStack getCopyResult(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return null;
        }
        if (RecipesCrucible.smelting().getSmeltingResult(itemstack, true, false) > 0.0F)
        {
            ItemStack itemstack1 = new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage());
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    public int getInfusingCost(ItemStack itemstack, ItemStack itemstack1)
    {
        int i = 0;
        if (itemstack == null || itemstack1 == null)
        {
            return 0;
        }
        Object obj = costList.get(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                }));
        if (obj != null)
        {
            i = ((Integer)obj).intValue();
        }
        if (i != 0)
        {
            return i;
        }
        else
        {
            return 0;
        }
    }

    public int getCopyCost(ItemStack itemstack)
    {
        boolean flag = false;
        if (itemstack == null || itemstack.func_40707_s() != EnumRarity.common)
        {
            return 0;
        }
        if (itemstack.itemID == Block.cobblestone.blockID)
        {
            return 3;
        }
        float f = RecipesCrucible.smelting().getSmeltingResult(itemstack, true, false);
        if (f > 0.0F)
        {
            int i = Math.round(f * 5F);
            return i;
        }
        else
        {
            return 0;
        }
    }

    public int getTemplateDamage(ItemStack itemstack, ItemStack itemstack1)
    {
        if (itemstack == null || itemstack1 == null)
        {
            return 0;
        }
        Object obj = damageList.get(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage()), Integer.valueOf(itemstack1.itemID), Integer.valueOf(itemstack1.getItemDamage())
                }));
        if (obj != null)
        {
            return ((Integer)obj).intValue();
        }
        else
        {
            return 0;
        }
    }

    public Map getInfusingList()
    {
        return infusingList;
    }
}
