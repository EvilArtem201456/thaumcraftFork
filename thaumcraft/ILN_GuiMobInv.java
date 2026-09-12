package thaumcraft;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

class ILN_GuiMobInv extends GuiContainer
{
    private EntityPlayer theplayer;
    private EntityTravelingTrunk themob;
    private int inventoryRows;

    public ILN_GuiMobInv(EntityPlayer entityplayer, EntityTravelingTrunk entitytravelingtrunk)
    {
        super(new ILN_ContainerForEntity(entityplayer.inventory, entitytravelingtrunk.inventory));
        theplayer = entityplayer;
        themob = entitytravelingtrunk;
        inventoryRows = entitytravelingtrunk.inventory.slotCount / 9;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString((new StringBuilder()).append("Trunk (Owned by ").append(themob.getOwner()).append(")").toString(), 8, 0, 0x404040);
        fontRenderer.drawString((new StringBuilder()).append(theplayer.username).append("'s Inventory").toString(), 8, (ySize - 96) + 16, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/thaumcraft/InvPlayerAndBlank.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1 - 6, 0, 0, xSize, ySize + 24);
        int j1 = mc.renderEngine.getTexture("/thaumcraft/4x9InvBoxes.png");
        mc.renderEngine.bindTexture(j1);
        l = (width - xSize) / 2;
        i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1 - 6, 0, 0, xSize, inventoryRows * 18 + 16);
        mc.renderEngine.bindTexture(k);
        if (!themob.stay)
        {
            drawTexturedModalRect(l + 159, i1 + 84, 176, 0, 10, 10);
        }
        else
        {
            drawTexturedModalRect(l + 159, i1 + 84, 186, 0, 10, 10);
        }
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        int j1 = i - (l + 159);
        int k1 = j - (i1 + 84);
        if (j1 >= 0 && k1 >= 0 && j1 < 10 && k1 <= 10)
        {
            themob.worldObj.playSoundEffect(themob.posX, themob.posY, themob.posZ, "step.wood", 0.3F, 0.6F + (themob.stay ? 0.0F : 0.2F));
            themob.stay = !themob.stay;
            if (themob.stay)
            {
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("The trunk will remain here.");
            }
            else
            {
                ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("The trunk will now follow you.");
            }
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void onGuiClosed()
    {
        mc.theWorld.playSoundAtEntity(themob, "random.chestclosed", 0.5F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
        themob.open = false;
        super.onGuiClosed();
    }
}
