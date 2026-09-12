package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GuiApportWand extends Gui
{
    private static RenderItem itemRenderer = new RenderItem();

    public GuiApportWand()
    {
    }

    public void render(ItemStack itemstack)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.func_41089_c();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/gui/gui.png"));
        InventoryPlayer inventoryplayer = minecraft.thePlayer.inventory;
        zLevel = -90F;
        drawTexturedModalRect(8, 8, 1, 1, 20, 20);
        try
        {
            itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemstack, 10, 10);
        }
        catch (Exception exception)
        {
            minecraft.fontRenderer.drawStringWithShadow("?", 16, 15, 0xf4f4f4);
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
