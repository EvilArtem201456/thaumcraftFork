package thaumcraft;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class WorldGenObelisk extends WorldGenerator
{
    public WorldGenObelisk()
    {
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < 3; l++)
        {
            int j1 = (i + random.nextInt(8)) - random.nextInt(8);
            int l1 = (j + random.nextInt(8)) - random.nextInt(8);
            int j2 = (k + random.nextInt(8)) - random.nextInt(8);
            if (world.isAirBlock(j1, l1, j2) && !world.isBlockOpaqueCube(j1 - 2, l1, j2 - 2) && !world.isBlockOpaqueCube(j1 + 2, l1, j2 - 2) && !world.isBlockOpaqueCube(j1 + 2, l1, j2 - 2) && !world.isBlockOpaqueCube(j1 + 2, l1, j2 + 2) && world.isBlockOpaqueCube(j1, l1 - 1, j2) && world.isBlockOpaqueCube(j1 - 2, l1 - 1, j2 - 2) && world.isBlockOpaqueCube(j1 + 2, l1 - 1, j2 - 2) && world.isBlockOpaqueCube(j1 + 2, l1 - 1, j2 - 2) && world.isBlockOpaqueCube(j1 + 2, l1 - 1, j2 + 2))
            {
                if (ModLoader.getMinecraftInstance().inGameHasFocus)
                {
                    Container container = ModLoader.getMinecraftInstance().thePlayer.inventorySlots;
                    for (int i4 = 0; i4 < container.inventorySlots.size(); i4++)
                    {
                        if (!container.getSlot(i4).getHasStack())
                        {
                            continue;
                        }
                        ItemStack itemstack = ModLoader.getMinecraftInstance().thePlayer.inventorySlots.getSlot(i4).getStack();
                        if (itemstack.itemID != mod_ThaumCraft.thaumDetector.shiftedIndex)
                        {
                            continue;
                        }
                        ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("You detect a great mystical disturbance in the distance.");
                        break;
                    }
                }
                for (int l2 = 0; l2 < 200; l2++)
                {
                    int j4 = world.rand.nextInt(6) - world.rand.nextInt(6);
                    int j5 = world.rand.nextInt(6) - world.rand.nextInt(6);
                    if (!world.isBlockOpaqueCube(j1 + j4, l1 - 1, j2 + j5) || world.getBlockId(j1 + j4, l1 - 1, j2 + j5) == Block.obsidian.blockID)
                    {
                        continue;
                    }
                    world.setBlock(j1 + j4, l1 - 1, j2 + j5, Block.obsidian.blockID);
                    for (int i6 = 0; i6 < 10; i6++)
                    {
                        if (!world.isBlockOpaqueCube(j1 + j4, l1 + i6, j2 + j5))
                        {
                            world.setBlock(j1 + j4, l1 + i6, j2 + j5, 0);
                        }
                    }
                }

                world.setBlockAndMetadata(j1, l1, j2, mod_ThaumCraft.thaumCrucible.blockID, 9);
                world.setBlock(j1 - 2, l1 - 1, j2 - 2, Block.obsidian.blockID);
                world.setBlock(j1 - 2, l1 - 1, j2 + 2, Block.obsidian.blockID);
                world.setBlock(j1 + 2, l1 - 1, j2 - 2, Block.obsidian.blockID);
                world.setBlock(j1 + 2, l1 - 1, j2 + 2, Block.obsidian.blockID);
                int i3 = 0;
                do
                {
                    if (i3 >= 4)
                    {
                        break;
                    }
                    world.setBlockAndMetadata(j1 - 2, l1 + i3, j2 - 2, mod_ThaumCraft.thaumCrucible.blockID, 3 + i3);
                    if (world.rand.nextInt(15) < i3)
                    {
                        break;
                    }
                    i3++;
                }
                while (true);
                i3 = 0;
                do
                {
                    if (i3 >= 4)
                    {
                        break;
                    }
                    world.setBlockAndMetadata(j1 + 2, l1 + i3, j2 + 2, mod_ThaumCraft.thaumCrucible.blockID, 3 + i3);
                    if (world.rand.nextInt(15) < i3)
                    {
                        break;
                    }
                    i3++;
                }
                while (true);
                i3 = 0;
                do
                {
                    if (i3 >= 4)
                    {
                        break;
                    }
                    world.setBlockAndMetadata(j1 - 2, l1 + i3, j2 + 2, mod_ThaumCraft.thaumCrucible.blockID, 3 + i3);
                    if (world.rand.nextInt(15) < i3)
                    {
                        break;
                    }
                    i3++;
                }
                while (true);
                i3 = 0;
                do
                {
                    if (i3 >= 4)
                    {
                        break;
                    }
                    world.setBlockAndMetadata(j1 + 2, l1 + i3, j2 - 2, mod_ThaumCraft.thaumCrucible.blockID, 3 + i3);
                    if (world.rand.nextInt(18) < i3)
                    {
                        break;
                    }
                    i3++;
                }
                while (true);
                return true;
            }
        }

        int i1 = 0;
        do
        {
            if (i1 >= 5)
            {
                break;
            }
            int k1 = (i + random.nextInt(8)) - random.nextInt(8);
            int i2 = (j + random.nextInt(8)) - random.nextInt(8);
            int k2 = (k + random.nextInt(8)) - random.nextInt(8);
            if (world.isAirBlock(k1, i2, k2) && !world.isAirBlock(k1, i2 - 1, k2) && world.isBlockOpaqueCube(k1, i2 - 1, k2) && world.getBlockId(k1, i2 - 1, k2) != Block.leaves.blockID && world.getBlockId(k1, i2 - 1, k2) != Block.wood.blockID)
            {
                if (ModLoader.getMinecraftInstance().inGameHasFocus)
                {
                    Container container1 = ModLoader.getMinecraftInstance().thePlayer.inventorySlots;
                    for (int k4 = 0; k4 < container1.inventorySlots.size(); k4++)
                    {
                        if (!container1.getSlot(k4).getHasStack())
                        {
                            continue;
                        }
                        ItemStack itemstack1 = ModLoader.getMinecraftInstance().thePlayer.inventorySlots.getSlot(k4).getStack();
                        if (itemstack1.itemID != mod_ThaumCraft.thaumDetector.shiftedIndex)
                        {
                            continue;
                        }
                        ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("You detect a minor mystical disturbance in the distance.");
                        break;
                    }
                }
                for (int j3 = 0; j3 < 4; j3++)
                {
                    world.setBlockAndMetadata(k1, i2 + j3, k2, mod_ThaumCraft.thaumCrucible.blockID, 3 + j3);
                }

                world.setBlock(k1, i2 - 1, k2, Block.obsidian.blockID);
                for (int k3 = 0; k3 < 10; k3++)
                {
                    int l4 = world.rand.nextInt(2) - world.rand.nextInt(2);
                    int k5 = world.rand.nextInt(2) - world.rand.nextInt(2);
                    if (!world.isAirBlock(k1 + l4, i2 - 1, k2 + k5))
                    {
                        world.setBlock(k1 + l4, i2 - 1, k2 + k5, Block.obsidian.blockID);
                    }
                }

                for (int l3 = 0; l3 < 10; l3++)
                {
                    int i5 = world.rand.nextInt(3) - world.rand.nextInt(3);
                    int l5 = world.rand.nextInt(3) - world.rand.nextInt(3);
                    if (!world.isAirBlock(k1 + i5, i2 - 1, k2 + l5))
                    {
                        world.setBlock(k1 + i5, i2 - 1, k2 + l5, Block.obsidian.blockID);
                    }
                }

                break;
            }
            i1++;
        }
        while (true);
        return true;
    }
}
