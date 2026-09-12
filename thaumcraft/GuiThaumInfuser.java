package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GuiThaumInfuser extends GuiContainer
{
    private TileEntityProcessor infuserInventory;
    private int alternate;
    private long Time;

    public GuiThaumInfuser(InventoryPlayer inventoryplayer, TileEntityProcessor tileentityprocessor)
    {
        super(new ContainerProcessor(inventoryplayer, tileentityprocessor));
        alternate = 0;
        infuserInventory = tileentityprocessor;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Vis Infuser", 48, 5, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/thaumcraft/tcgui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (infuserInventory.isCooking())
        {
            int j1 = infuserInventory.getCookProgressScaled(25);
            drawTexturedModalRect(l + 57, i1 + 35, 176, 35, j1 + 1, 16);
        }
        if (infuserInventory.isReceiving)
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
            drawTexturedModalRect(l + 34, i1 + 30 + alternate, 188, 69 + alternate, 15, 10);
        }
    }
}
