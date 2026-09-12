package thaumcraft;

import java.util.*;
import net.minecraft.src.*;

public class RecipesCrucible
{
    private static final RecipesCrucible smeltingBase = new RecipesCrucible();
    private Map smeltingList;
    private Map metaSmeltingList;
    private Map cacheList;
    private int depth;

    public static final RecipesCrucible smelting()
    {
        return smeltingBase;
    }

    private RecipesCrucible()
    {
        depth = 0;
        smeltingList = new HashMap();
        metaSmeltingList = new HashMap();
        cacheList = new HashMap();
    }

    public void addSmelting(int i, float f)
    {
        smeltingList.put(Integer.valueOf(i), Float.valueOf(f));
    }

    public void addSmelting(int i, int j, float f)
    {
        metaSmeltingList.put(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(i), Integer.valueOf(j)
                }), Float.valueOf(f));
    }

    public float getSmeltingResult(ItemStack itemstack, boolean flag, boolean flag1)
    {
        if (itemstack == null)
        {
            return 0.0F;
        }
        if (metaSmeltingList.get(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                })) != null)
        {
            return ((Float)metaSmeltingList.get(Arrays.asList(new Integer[]
                    {
                        Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                    }))).floatValue();
        }
        if (smeltingList.get(Integer.valueOf(itemstack.itemID)) != null)
        {
            return ((Float)smeltingList.get(Integer.valueOf(itemstack.itemID))).floatValue();
        }
        float f = ThaumCraftCore.getCustomSmeltItem(itemstack);
        if (f > 0.0F)
        {
            return f;
        }
        if (flag1)
        {
            return 0.0F;
        }
        if (cacheList.get(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                })) != null)
        {
            return ((Float)cacheList.get(Arrays.asList(new Integer[]
                    {
                        Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                    }))).floatValue();
        }
        if (flag)
        {
            f = recipeCost(itemstack);
        }
        if (flag && f > 0.0F && cacheList.get(Arrays.asList(new Integer[]
                {
                    Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                })) == null)
        {
            cacheList.put(Arrays.asList(new Integer[]
                    {
                        Integer.valueOf(itemstack.itemID), Integer.valueOf(itemstack.getItemDamage())
                    }), Float.valueOf(f));
        }
        return f;
    }

    private float recipeCost(ItemStack itemstack)
    {
        float f = 0.0F;
        for (int i = 0; i < mod_ThaumCraft.recipeList.size(); i++)
        {
            if (mod_ThaumCraft.recipeList.get(i) instanceof ShapedRecipes)
            {
                ShapedRecipes shapedrecipes = (ShapedRecipes)mod_ThaumCraft.recipeList.get(i);
                int j = shapedrecipes.getRecipeOutput().getItemDamage() >= 0 ? shapedrecipes.getRecipeOutput().getItemDamage() : 0;
                int l = itemstack.getItemDamage() >= 0 ? itemstack.getItemDamage() : 0;
                if (shapedrecipes.getRecipeOutput().itemID != itemstack.itemID || j != l)
                {
                    continue;
                }
                try
                {
                    int j1 = ((Integer)ModLoader.getPrivateValue(net.minecraft.src.ShapedRecipes.class, shapedrecipes, 0)).intValue();
                    int k1 = ((Integer)ModLoader.getPrivateValue(net.minecraft.src.ShapedRecipes.class, shapedrecipes, 1)).intValue();
                    ItemStack aitemstack[] = (ItemStack[])ModLoader.getPrivateValue(net.minecraft.src.ShapedRecipes.class, shapedrecipes, 2);
                    for (int i2 = 0; i2 < j1 && i2 < 3; i2++)
                    {
                        for (int j2 = 0; j2 < k1 && j2 < 3; j2++)
                        {
                            if (aitemstack[i2 + j2 * j1] == null)
                            {
                                continue;
                            }
                            float f2 = getSmeltingResult(aitemstack[i2 + j2 * j1], false, false) / (float)shapedrecipes.getRecipeOutput().stackSize;
                            if (f2 == 0.0F)
                            {
                                return 0.0F;
                            }
                            f += f2;
                        }
                    }
                }
                catch (IllegalArgumentException illegalargumentexception)
                {
                    illegalargumentexception.printStackTrace();
                }
                catch (SecurityException securityexception)
                {
                    securityexception.printStackTrace();
                }
                catch (NoSuchFieldException nosuchfieldexception)
                {
                    nosuchfieldexception.printStackTrace();
                }
                i = mod_ThaumCraft.recipeList.size();
                continue;
            }
            if (!(mod_ThaumCraft.recipeList.get(i) instanceof ShapelessRecipes))
            {
                continue;
            }
            ShapelessRecipes shapelessrecipes = (ShapelessRecipes)mod_ThaumCraft.recipeList.get(i);
            int k = shapelessrecipes.getRecipeOutput().getItemDamage() >= 0 ? shapelessrecipes.getRecipeOutput().getItemDamage() : 0;
            int i1 = itemstack.getItemDamage() >= 0 ? itemstack.getItemDamage() : 0;
            if (shapelessrecipes.getRecipeOutput().itemID != itemstack.itemID || k != i1)
            {
                continue;
            }
            try
            {
                List list = (List)ModLoader.getPrivateValue(net.minecraft.src.ShapelessRecipes.class, shapelessrecipes, 1);
                for (int l1 = 0; l1 < list.size() && l1 < 9; l1++)
                {
                    if (list.get(l1) != null)
                    {
                        float f1 = getSmeltingResult((ItemStack)list.get(l1), false, false) / (float)shapelessrecipes.getRecipeOutput().stackSize;
                        if (f1 == 0.0F)
                        {
                            return 0.0F;
                        }
                        f += f1;
                    }
                }
            }
            catch (IllegalArgumentException illegalargumentexception1)
            {
                illegalargumentexception1.printStackTrace();
            }
            catch (SecurityException securityexception1)
            {
                securityexception1.printStackTrace();
            }
            catch (NoSuchFieldException nosuchfieldexception1)
            {
                nosuchfieldexception1.printStackTrace();
            }
            i = mod_ThaumCraft.recipeList.size();
        }

        return f;
    }

    public Map getSmeltingList()
    {
        return smeltingList;
    }
}
