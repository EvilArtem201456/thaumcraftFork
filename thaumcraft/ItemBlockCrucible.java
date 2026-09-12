package thaumcraft;

import forge.ITextureProvider;
import net.minecraft.src.*;

public class ItemBlockCrucible extends ItemBlock
    implements ITextureProvider
{
    public ItemBlockCrucible(int i, Block block)
    {
        super(i);
        setHasSubtypes(true);
    }

    public String getTextureFile()
    {
        return "/thaumcraft/main.png";
    }

    public boolean isFull3D()
    {
        return true;
    }

    public int getIconFromDamage(int i)
    {
        return 64;
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
                s = "crucible";
                break;

            case 1:
                s = "eyecrucible";
                break;

            case 2:
                s = "condenser";
                break;

            case 3:
                s = "obbase";
                break;

            case 4:
                s = "obmiddle1";
                break;

            case 5:
                s = "obmiddle2";
                break;

            case 6:
                s = "obcap";
                break;

            case 7:
                s = "viscube";
                break;

            case 8:
                s = "oricrucible";
                break;

            case 9:
                s = "altar";
                break;
        }
        return (new StringBuilder()).append(getItemName()).append(".").append(s).toString();
    }
}
