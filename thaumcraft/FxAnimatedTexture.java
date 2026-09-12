package thaumcraft;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class FxAnimatedTexture extends TextureFX
{
    private byte bg[][];
    private int frame;
    private int totalFrames;
    private int size;
    private int pack_size;

    public FxAnimatedTexture(String s, int i)
    {
        super(i);
        frame = 0;
        totalFrames = 0;
        size = 0;
        String s1 = s;
        try
        {
            Minecraft minecraft = ModLoader.getMinecraftInstance();
            TexturePackList texturepacklist = (TexturePackList)ModLoader.getPrivateValue(net.minecraft.src.RenderEngine.class, minecraft.renderEngine, 11);
            InputStream inputstream = texturepacklist.selectedTexturePack.getResourceAsStream(s1);
            if (inputstream == null)
            {
                throw new Exception((new StringBuilder("Image not found: ")).append(s1).toString());
            }
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            totalFrames = bufferedimage.getHeight() / bufferedimage.getWidth();
            size = bufferedimage.getWidth();
            pack_size = 16;
            try
            {
                Class class1 = Class.forName("com.pclewis.mcpatcher.mod.TileSize");
                pack_size = class1.getDeclaredField("int_size").getInt(class1);
            }
            catch (Throwable throwable) { }
            bg = new byte[totalFrames][pack_size * pack_size * 4];
            loadBG(bufferedimage);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void onTick()
    {
        imageData = bg[frame];
        frame++;
        if (frame == totalFrames)
        {
            frame = 0;
        }
    }

    private void loadBG(BufferedImage bufferedimage)
    {
        float f = (float)size / (float)pack_size;
        for (int i = 0; i < totalFrames; i++)
        {
            for (int j = 0; j < pack_size; j++)
            {
                for (int k = 0; k < pack_size; k++)
                {
                    int l = bufferedimage.getRGB((int)((float)k * f), i * size + (int)((float)j * f));
                    bg[i][(k + j * pack_size) * 4 + 0] = (byte)(l >> 16 & 0xff);
                    bg[i][(k + j * pack_size) * 4 + 1] = (byte)(l >> 8 & 0xff);
                    bg[i][(k + j * pack_size) * 4 + 2] = (byte)(l & 0xff);
                    bg[i][(k + j * pack_size) * 4 + 3] = (byte)(l >> 24 & 0xff);
                }
            }
        }
    }
}
