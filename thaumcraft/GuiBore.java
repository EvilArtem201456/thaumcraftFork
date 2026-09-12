package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GuiBore extends GuiContainer
{
    private TileEntityProcessor thaumInfuserInventory;
    private int alternate;
    private long Time;

    public GuiBore(InventoryPlayer inventoryplayer, TileEntityProcessor tileentityprocessor)
    {
        super(new ContainerProcessor(inventoryplayer, tileentityprocessor));
        alternate = 0;
        thaumInfuserInventory = tileentityprocessor;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Arcane Bore", 56, 5, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/thaumcraft/boregui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (thaumInfuserInventory.isCooking())
        {
            int j1 = thaumInfuserInventory.getCookProgressScaled(52);
            char c = '\260';
            if (thaumInfuserInventory.currentItemCookCost == (float)thaumInfuserInventory.singValStbl)
            {
                c = '\300';
            }
            drawTexturedModalRect(l + 101, (i1 + 22 + 52) - j1, c, 52 - j1, 5, j1);
        }
    }
}
