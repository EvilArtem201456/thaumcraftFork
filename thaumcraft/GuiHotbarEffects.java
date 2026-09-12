package thaumcraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class GuiHotbarEffects extends Gui
{
    private static RenderItem itemRenderer = new RenderItem();

    public GuiHotbarEffects()
    {
    }

    public void renderParticles1(int i, int j, int k)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int l = scaledresolution.getScaledWidth();
        int i1 = scaledresolution.getScaledHeight();
        minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        RenderHelper.func_41089_c();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)j / 18F);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/particles.png"));
        net.minecraft.src.InventoryPlayer inventoryplayer = minecraft.thePlayer.inventory;
        zLevel = -90F;
        int j1 = Math.abs(j % 6 - 3);
        drawTexturedModalRect((l / 2 - 89) + i * 20 + j1, (i1 - 28) + j / 3, 0 + k * 16, 0, 16, 16);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderParticles2(int i, int j, int k)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int l = scaledresolution.getScaledWidth();
        int i1 = scaledresolution.getScaledHeight();
        minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.func_41089_c();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (float)j / 18F);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/particles.png"));
        net.minecraft.src.InventoryPlayer inventoryplayer = minecraft.thePlayer.inventory;
        zLevel = -90F;
        drawTexturedModalRect((l / 2 - 88) + i * 20, i1 - 21 - j / 3, 0 + k * 16, 0, 16, 16);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
