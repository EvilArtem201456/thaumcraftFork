package thaumcraft;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntitySymbolRenderer extends TileEntitySpecialRenderer
{
    private RenderBlocks rb;
    private World w;
    private static int colors[] =
    {
        0xc0c0c0, 0x404040, 0xdeb220, 192, 32768 /*GL_ABGR_EXT*/, 0xff00ff, 0x400000, 0x80ffff, 0xfc3a00, 0x800000,
        0xc0ffff, 0, 0
    };

    public TileEntitySymbolRenderer()
    {
    }

    private void translateFromOrientation(double d, double d1, double d2, int i)
    {
        GL11.glPushMatrix();
        if (i == 0)
        {
            GL11.glTranslatef((float)d, (float)d1 + 1.0F, (float)d2 + 1.0F);
            GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 1)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2);
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
        }
        else if (i == 2)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2 + 1.0F);
        }
        else if (i == 3)
        {
            GL11.glTranslatef((float)d + 1.0F, (float)d1, (float)d2);
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        }
        else if (i == 4)
        {
            GL11.glTranslatef((float)d + 1.0F, (float)d1, (float)d2 + 1.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        }
        else if (i == 5)
        {
            GL11.glTranslatef((float)d, (float)d1, (float)d2);
            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glPushMatrix();
    }

    public void renderEntityAt(TileEntitySymbol tileentitysymbol, double d, double d1, double d2,
            float f)
    {
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        int i = ModLoader.getMinecraftInstance().gameSettings.fancyGraphics && !mod_ThaumCraft.lowGfx ? 30 : 15;
        translateFromOrientation((float)d, (float)d1, (float)d2, tileentitysymbol.orientation);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/main.png"));
        int j = mod_ThaumCraft.thaumSymbolSprite + tileentitysymbol.getBlockMetadata();
        if (tileentitysymbol.getBlockMetadata() == 9 && tileentitysymbol.isPowering)
        {
            j++;
        }
        if (tileentitysymbol.getBlockMetadata() > 9)
        {
            j++;
        }
        ThaumCraftCore.renderItemFromTexture(minecraft, j);
        if (tileentitysymbol.rune != -1)
        {
            translateFromOrientation((float)d, (float)d1, (float)d2, tileentitysymbol.orientation);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/thaumcraft/main.png"));
            ThaumCraftCore.renderItemFromTexture(minecraft, mod_ThaumCraft.thaumRune.getIconFromDamage(tileentitysymbol.rune));
        }
        if (tileentitysymbol.getBlockMetadata() == 5 && !tileentitysymbol.onArch())
        {
            return;
        }
        if (!tileentitysymbol.gettingPower() && tileentitysymbol.getBlockMetadata() != 9)
        {
            GL11.glPushMatrix();
            if (tileentitysymbol.getBlockMetadata() == 5)
            {
                switch (tileentitysymbol.orientation)
                {
                    case 2:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 - 1.0F, (float)d2 + 1.5F);
                        break;

                    case 3:
                        GL11.glTranslatef((float)d + 0.5F, (float)d1 - 1.0F, (float)d2 - 0.5F);
                        break;

                    case 4:
                        GL11.glTranslatef((float)d + 1.5F, (float)d1 - 1.0F, (float)d2 + 0.5F);
                        break;

                    case 5:
                        GL11.glTranslatef((float)d - 0.5F, (float)d1 - 1.0F, (float)d2 + 0.5F);
                        break;
                }
            }
            else
            {
                GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.5F, (float)d2 + 0.5F);
            }
            Tessellator tessellator = Tessellator.instance;
            RenderHelper.disableStandardItemLighting();
            float f1 = tileentitysymbol.rotation / 90F;
            Random random = new Random(245L);
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 1);
            GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            GL11.glDepthMask(false);
            GL11.glPushMatrix();
            float f2 = 45F;
            if (tileentitysymbol.getBlockMetadata() == 5)
            {
                if (tileentitysymbol.orientation < 4)
                {
                    GL11.glScalef(1.0F, 2.0F, 0.5F);
                }
                else
                {
                    GL11.glScalef(0.5F, 2.0F, 1.0F);
                }
                f2 = 18F;
            }
            for (int k = 0; (float)k < (float)i; k++)
            {
                GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
                tessellator.startDrawing(6);
                float f3 = (random.nextFloat() * 20F + 5F) / f2 / (float)(tileentitysymbol.cooldown + 1);
                float f4 = (random.nextFloat() * 2.0F + 1.0F) / f2 / (float)(tileentitysymbol.cooldown + 1);
                tessellator.setColorRGBA_I(0x333333, 155);
                tessellator.addVertex(0.0D, 0.0D, 0.0D);
                tessellator.setColorRGBA_I(colors[tileentitysymbol.getBlockMetadata()], 0);
                tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
                tessellator.addVertex(0.86599999999999999D * (double)f4, f3, -0.5F * f4);
                tessellator.addVertex(0.0D, f3, 1.0F * f4);
                tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
                tessellator.draw();
            }

            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            GL11.glDisable(2884 /*GL_CULL_FACE*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
            GL11.glShadeModel(7424 /*GL_FLAT*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
        }
    }

    public void func_31069_a(World world)
    {
        rb = new RenderBlocks(world);
        w = world;
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2,
            float f)
    {
        renderEntityAt((TileEntitySymbol)tileentity, d, d1, d2, f);
    }
}
