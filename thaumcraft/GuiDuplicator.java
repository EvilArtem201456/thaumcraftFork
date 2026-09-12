package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GuiDuplicator extends GuiContainer
{
    private TileEntityProcessor thaumInfuserInventory;
    private int alternate;
    private long Time;

    public GuiDuplicator(InventoryPlayer inventoryplayer, TileEntityProcessor tileentityprocessor)
    {
        super(new ContainerProcessor(inventoryplayer, tileentityprocessor));
        alternate = 0;
        thaumInfuserInventory = tileentityprocessor;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Duplicator", 42, 5, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/thaumcraft/tdgui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (thaumInfuserInventory.isCooking())
        {
            int j1 = thaumInfuserInventory.getCookProgressScaled(25);
            drawTexturedModalRect(l + 57, i1 + 45, 176, 15, j1 + 1, 16);
        }
        if (thaumInfuserInventory.isReceiving)
        {
            if (Time == 0L)
            {
                Time = System.currentTimeMillis();
            }
            double d = System.currentTimeMillis() - Time;
            if (d > 15D)
            {
                alternate++;
                if (alternate > 35)
                {
                    alternate = 0;
                }
                Time = System.currentTimeMillis();
            }
            drawTexturedModalRect(l + 64, i1 + 24 + alternate, 188, 69 + alternate, 15, 10);
        }
        if (!thaumInfuserInventory.repeat)
        {
            drawTexturedModalRect(l + 29, i1 + 32, 176, 0, 10, 10);
        }
        else
        {
            drawTexturedModalRect(l + 29, i1 + 32, 186, 0, 10, 10);
        }
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        int j1 = i - (l + 29);
        int k1 = j - (i1 + 32);
        if (j1 >= 0 && k1 >= 0 && j1 < 10 && k1 <= 10)
        {
            thaumInfuserInventory.worldObj.playSoundEffect((double)thaumInfuserInventory.xCoord + 0.5D, (double)thaumInfuserInventory.yCoord + 0.5D, (double)thaumInfuserInventory.zCoord + 0.5D, "random.orb", 0.1F, 0.6F + (thaumInfuserInventory.repeat ? 0.0F : 0.2F));
            thaumInfuserInventory.repeat = !thaumInfuserInventory.repeat;
        }
    }
}
