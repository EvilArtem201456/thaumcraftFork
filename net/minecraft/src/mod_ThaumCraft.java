package net.minecraft.src;

import forge.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import net.minecraft.client.Minecraft;
import thaumcraft.*;

public class mod_ThaumCraft extends BaseMod
{
    String soundNames[] =
    {
        "shock1.ogg", "shock2.ogg", "shock3.ogg", "elecloop.ogg", "fireloop.ogg", "bubbling.ogg", "whispers1.ogg", "whispers2.ogg", "zap1.ogg", "zap2.ogg",
        "zap3.ogg"
    };
    public static mod_ThaumCraft proxy;
    public static GuiApportWand gaw;
    public static GuiHotbarEffects ghe;
    private static boolean firstUpdate = true;
    public static int thaumRange = 15;
    public static boolean lowGfx = false;
    private long Time;
    private long secondTicker;
    private boolean prevOnGround;
    private int stompCooldown;
    private boolean playedFire;
    private boolean startedJump;
    private float fallCount;
    private int hbpTicker[] =
    {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    private int hbpTickerType[] =
    {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public static List recipeList = CraftingManager.getInstance().getRecipeList();
    public static Map smeltList;
    static EnumToolMaterial toolMatThaumium = EnumHelper.addToolMaterial("THAUMIUM", 3, 400, 7F, 2, 22);
    static EnumArmorMaterial armorMatThaumium = EnumHelper.addArmorMaterial("THAUMIUM", 25, new int[]
            {
                2, 6, 5, 2
            }, 25);
    static EnumArmorMaterial armorMatSpecial = EnumHelper.addArmorMaterial("SPECIAL", 25, new int[]
            {
                1, 3, 2, 1
            }, 10);
    public static Item thaumDetector;
    public static Item thaumGrenade;
    public static Item thaumReagent;
    public static Item thaumRune;
    public static Item thaumTemplate;
    public static Item thaumTinker;
    public static Item thaumMold;
    public static Item thaumSymbolItem;
    public static Item portableHole;
    public static Item fabric;
    public static Item eTreeItems;
    public static Item apportWand;
    public static Item fireWand;
    public static Item lightningWand;
    public static Item talismanBlank;
    public static Item talismanVoid;
    public static Item talismanHealth;
    public static Item talismanVigor;
    public static Item thaumiumIngot;
    public static Item helmetThaumium;
    public static Item plateThaumium;
    public static Item bootsThaumium;
    public static Item legsThaumium;
    public static Item shovelThaumium;
    public static Item pickThaumium;
    public static Item swordThaumium;
    public static Item axeThaumium;
    public static Item hoeThaumium;
    public static Item axeWoodsman;
    public static Item bootsStriding;
    public static Item bootsSeven;
    public static Item bootsStomp;
    public static Block thaumCrucible;
    public static Block thaumConduit;
    public static Block thaumProcessor;
    public static Block thaumSymbol;
    public static Block thaumEffects;
    public static int thaumCrucibleTop = 0;
    public static int thaumCrucibleSide = 1;
    public static int thaumSeeingCrucibleTop = 22;
    public static int thaumSeeingCrucibleSide = 23;
    public static int thaumThaumiumCrucibleTop = 25;
    public static int thaumThaumiumCrucibleSide = 26;
    public static int generatorFrame = 28;
    public static int generatorGizmo = 30;
    public static int thaumInfuserTop = 2;
    public static int thaumInfuserSide = 3;
    public static int thaumDuplicatorSide = 4;
    public static int thaumDuplicatorTop = 5;
    public static int thaumConduitSide = 7;
    public static int thaumCondenserSide = 16;
    public static int thaumCondenserTop = 17;
    public static int thaumBoreOut = 18;
    public static int thaumBoreSide = 19;
    public static int thaumObeliskTop = 32;
    public static int thaumObeliskCapSide = 33;
    public static int thaumObeliskMiddle2Side = 34;
    public static int thaumObeliskMiddle1Side = 35;
    public static int thaumObeliskBaseSide = 36;
    public static int thaumObeliskAltarSide = 37;
    public static int thaumObeliskAltarTop = 38;
    public static int trunkTop = 160;
    public static int trunkSide = 161;
    public static int trunkFace = 162;
    public static int thaumDetectorOnSprite = 64;
    public static int thaumDetectorOffSprite = 65;
    public static int thaumGrenadeSprite = 66;
    public static int portableHoleSprite = 68;
    public static int fabricSprite = 69;
    public static int thaumReagentSprite = 80;
    public static int thaumTinkerSprite = 98;
    public static int thaumMoldSprite = 99;
    public static int apportWandSprite = 100;
    public static int thaumRuneSprite = 112;
    public static int thaumSymbolSprite = 128;
    public static int eTreeItemSprite = 70;
    public static int eVTSprite = 192;
    public static int crucibleRenderID;
    public static int processorRenderID;
    public static int thaumConduitRenderID;
    public static int symbolRenderID;
    public static int effectsRenderID;
    public static int thaumCubeSideFX;
    public static int thaumPHSideFX;
    public static int thaumCrucibleSludgeFX;
    public static int thaumSludgeFX;
    public static int thaumTemplateSprite = 96;
    public long chunkCheckInterval;
    public long lastChunkCheck;

    public mod_ThaumCraft()
    {
        prevOnGround = true;
        stompCooldown = 0;
        playedFire = false;
        startedJump = false;
        fallCount = 0.0F;
        proxy = this;
        ModLoader.SetInGameHook(this, true, false);
        chunkCheckInterval = 500L;
        lastChunkCheck = 0L;
        gaw = new GuiApportWand();
        ghe = new GuiHotbarEffects();
        ThaumCraft_Properties thaumcraft_properties = new ThaumCraft_Properties();
        try
        {
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/ThaumCraft.cfg").toString());
            boolean flag = file.createNewFile();
            if (flag)
            {
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                thaumcraft_properties.store(fileoutputstream, "ThaumCraft Properties File");
                fileoutputstream.close();
            }
            thaumcraft_properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/ThaumCraft.cfg").toString()));
            thaumSymbol = new BlockSymbol(Integer.parseInt(thaumcraft_properties.safeGetProperty("BLOCKID.thaumSymbol", file, Integer.toString(244))));
            thaumCrucible = new BlockCrucible(Integer.parseInt(thaumcraft_properties.safeGetProperty("BLOCKID.thaumCrucible", file, Integer.toString(243))));
            thaumConduit = new BlockConduit(Integer.parseInt(thaumcraft_properties.safeGetProperty("BLOCKID.thaumConduit", file, Integer.toString(242))));
            thaumProcessor = new BlockProcessor(Integer.parseInt(thaumcraft_properties.safeGetProperty("BLOCKID.thaumInfuser", file, Integer.toString(246))), false);
            thaumEffects = new BlockEffects(Integer.parseInt(thaumcraft_properties.safeGetProperty("BLOCKID.thaumEffects", file, Integer.toString(241))));
            thaumSymbolItem = new ItemSymbol(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumSymbol", file, Integer.toString(2430))));
            thaumDetector = new ItemThaumDetector(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumDetector", file, Integer.toString(2431))));
            thaumRune = new ItemRune(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumRune", file, Integer.toString(2432))));
            thaumReagent = new ItemReagents(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumReagent", file, Integer.toString(2433))));
            thaumTemplate = new ItemTemplate(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumTemplate", file, Integer.toString(2434))));
            thaumGrenade = new ItemThaumGrenade(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumGrenade", file, Integer.toString(2435))));
            portableHole = new ItemPortableHole(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.portableHole", file, Integer.toString(2436))));
            fabric = new ItemFabric(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.fabric", file, Integer.toString(2437))));
            thaumTinker = new ItemTinker(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumTinker", file, Integer.toString(2438))));
            thaumMold = new ItemMold(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumMold", file, Integer.toString(2439))));
            eTreeItems = new ItemETree(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.eTree", file, Integer.toString(2440))));
            apportWand = new ItemAWand(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.apportwand", file, Integer.toString(2441))));
            lightningWand = new ItemLightningWand(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.lightningwand", file, Integer.toString(2456))));
            fireWand = new ItemFireWand(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.firewand", file, Integer.toString(2457))));
            thaumiumIngot = (new ItemOrichalcumIngot(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumiumIngot", file, Integer.toString(2446))))).setIconCoord(0, 11).setItemName("thaumiumIngot");
            helmetThaumium = (new ItemThaumiumArmor(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumiumHelm", file, Integer.toString(2442))), armorMatThaumium, 5, 0)).setIconCoord(1, 11).setItemName("helmetThaumium");
            plateThaumium = (new ItemThaumiumArmor(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumiumChest", file, Integer.toString(2443))), armorMatThaumium, 5, 1)).setIconCoord(2, 11).setItemName("chestplateThaumium");
            legsThaumium = (new ItemThaumiumArmor(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumiumLegs", file, Integer.toString(2444))), armorMatThaumium, 5, 2)).setIconCoord(3, 11).setItemName("leggingsThaumium");
            bootsThaumium = (new ItemThaumiumArmor(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.thaumiumBoots", file, Integer.toString(2445))), armorMatThaumium, 5, 3)).setIconCoord(4, 11).setItemName("bootsThaumium");
            bootsStriding = (new ItemStridingBoots(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.stridingBoots", file, Integer.toString(2458))), armorMatSpecial, 5, 3)).setIconCoord(7, 6).setItemName("bootsStriding");
            bootsSeven = (new ItemSevenBoots(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.sevenBoots", file, Integer.toString(2459))), armorMatSpecial, 5, 3)).setIconCoord(8, 6).setItemName("bootsSeven");
            bootsStomp = (new ItemStompBoots(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.stompBoots", file, Integer.toString(2460))), armorMatSpecial, 5, 3)).setIconCoord(9, 6).setItemName("bootsStomp");
            shovelThaumium = (new ItemThaumiumShovel(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.shovelThaumium", file, Integer.toString(2447))), toolMatThaumium)).setIconCoord(6, 11).setItemName("shovelThaumium");
            pickThaumium = (new ItemThaumiumPickaxe(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.pickThaumium", file, Integer.toString(2448))), toolMatThaumium)).setIconCoord(7, 11).setItemName("pickThaumium");
            axeThaumium = (new ItemThaumiumAxe(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.axeThaumium", file, Integer.toString(2449))), toolMatThaumium)).setIconCoord(8, 11).setItemName("axeThaumium");
            swordThaumium = (new ItemThaumiumSword(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.swordThaumium", file, Integer.toString(2450))), toolMatThaumium)).setIconCoord(5, 11).setItemName("swordThaumium");
            hoeThaumium = (new ItemThaumiumHoe(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.hoeThaumium", file, Integer.toString(2451))), toolMatThaumium)).setIconCoord(9, 11).setItemName("hoeThaumium");
            axeWoodsman = (new ItemWoodsmansAxe(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.axeWoodsman", file, Integer.toString(2461))), toolMatThaumium)).setIconCoord(15, 11).setItemName("axeWoodsman");
            talismanBlank = (new ItemTalismanBlank(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.blanktalisman", file, Integer.toString(2452))))).setIconCoord(10, 11).setItemName("blanktalisman");
            talismanVoid = (new ItemTalismanVoid(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.voidtalisman", file, Integer.toString(2453))))).setIconIndex(192).setItemName("voidtalisman");
            talismanHealth = (new ItemTalismanHealth(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.healthtalisman", file, Integer.toString(2454))))).setIconCoord(12, 11).setItemName("healthtalisman");
            talismanVigor = (new ItemTalismanVigor(Integer.parseInt(thaumcraft_properties.safeGetProperty("ITEMID.vigortalisman", file, Integer.toString(2455))))).setIconCoord(13, 11).setItemName("vigortalisman");
            thaumRange = Integer.parseInt(thaumcraft_properties.safeGetProperty("TRANSFER.RANGE", file, Integer.toString(15)));
            lowGfx = Boolean.parseBoolean(thaumcraft_properties.safeGetProperty("LOWER.GFX", file, "false").toLowerCase());
            thaumcraft_properties.safeGetProperty("SMELTING.Nikolite", file, Integer.toString(6));
            thaumcraft_properties.safeGetProperty("SMELTING.Red_Alloy_Ingot", file, Integer.toString(29));
            thaumcraft_properties.safeGetProperty("SMELTING.Blue_Alloy_Ingot", file, Integer.toString(37));
            thaumcraft_properties.safeGetProperty("SMELTING.Marble", file, Integer.toString(1));
            thaumcraft_properties.safeGetProperty("SMELTING.Basalt", file, Integer.toString(1));
            thaumcraft_properties.safeGetProperty("SMELTING.Basalt_Cobblestone", file, Integer.toString(1));
            thaumcraft_properties.safeGetProperty("SMELTING.BigCat_Claw", file, Integer.toString(9));
            thaumcraft_properties.safeGetProperty("SMELTING.Croc_Hide", file, Integer.toString(9));
            thaumcraft_properties.safeGetProperty("SMELTING.Fish_Egg", file, Integer.toString(9));
            Enumeration enumeration = thaumcraft_properties.keys();
            String s = "";
            smeltList = new HashMap();
            do
            {
                if (s.startsWith("SMELTING."))
                {
                    String s1 = s.substring(s.indexOf(".") + 1);
                    System.out.println((new StringBuilder()).append("[ThaumCraft] Adding SMELTING item: ").append(s1).toString());
                    smeltList.put(s1.replaceAll("_", " "), Integer.valueOf(Integer.parseInt(thaumcraft_properties.getProperty(s))));
                }
                if (!enumeration.hasMoreElements())
                {
                    break;
                }
                s = (String)enumeration.nextElement();
            }
            while (true);
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        ModLoader.RegisterEntityID(thaumcraft.EntityThaumSlime.class, "Thaumic Slime", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityHiddenTG.class, "Singularity", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityHiddenPH.class, "PortableHole", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityTravelingTrunk.class, "Trunk", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityEldritchTree.class, "Eldritch Tree", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityGrub.class, "Eldritch Grub", ModLoader.getUniqueEntityId());
        ModLoader.RegisterEntityID(thaumcraft.EntityWispWild.class, "Wisp", ModLoader.getUniqueEntityId());
        ModLoader.AddSpawn(thaumcraft.EntityThaumSlime.class, 2, 0, 5, EnumCreatureType.monster);
        ModLoader.AddSpawn(thaumcraft.EntityWispWild.class, 1, 0, 1, EnumCreatureType.monster);
        BiomeGenBase abiomegenbase[] = new BiomeGenBase[1];
        abiomegenbase[0] = BiomeGenBase.hell;
        ModLoader.AddSpawn(thaumcraft.EntityWispWild.class, 32, 1, 1, EnumCreatureType.monster, abiomegenbase);
        abiomegenbase = new BiomeGenBase[5];
        abiomegenbase[0] = BiomeGenBase.forest;
        abiomegenbase[1] = BiomeGenBase.swampland;
        abiomegenbase[2] = BiomeGenBase.river;
        abiomegenbase[3] = BiomeGenBase.extremeHills;
        abiomegenbase[4] = BiomeGenBase.taiga;
        ModLoader.AddSpawn(thaumcraft.EntityEldritchTree.class, 2, 0, 1, EnumCreatureType.monster, abiomegenbase);
        thaumDetector.setItemName("thaumdetector");
        thaumDetector.iconIndex = thaumDetectorOffSprite;
        ModLoader.AddName(thaumDetector, "Vis Detector");
        thaumGrenade.setItemName("thaumgrenade");
        thaumGrenade.iconIndex = thaumGrenadeSprite;
        ModLoader.AddName(thaumGrenade, "Thaumic Grenade");
        thaumReagent.setItemName("thaumreagents");
        thaumReagent.iconIndex = thaumReagentSprite;
        ModLoader.AddName(thaumReagent, "Thaum Infused Reagent");
        ModLoader.AddName(talismanBlank, "Blank Talisman");
        ModLoader.AddName(talismanHealth, "Talisman of Health");
        ModLoader.AddName(talismanVigor, "Talisman of Vigor");
        ModLoader.AddRecipe(new ItemStack(talismanBlank, 1, 0), new Object[]
                {
                    " I ", "ICI", " I ", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('C'), new ItemStack(thaumReagent, 1, 6)
                });
        ModLoader.AddName(thaumiumIngot, "Thaumium Ingot");
        ModLoader.AddName(shovelThaumium, "Thaumium Shovel");
        ModLoader.AddName(pickThaumium, "Thaumium Pickaxe");
        ModLoader.AddName(axeThaumium, "Thaumium Axe");
        ModLoader.AddName(hoeThaumium, "Thaumium Hoe");
        ModLoader.AddName(swordThaumium, "Thaumium Sword");
        ModLoader.AddName(axeWoodsman, "Woodsman's Axe");
        ModLoader.AddRecipe(new ItemStack(axeWoodsman, 1), new Object[]
                {
                    "RA", "RW", " W", Character.valueOf('A'), axeThaumium, Character.valueOf('R'), new ItemStack(thaumReagent, 1, 13), Character.valueOf('W'), new ItemStack(eTreeItems, 1, 3)
                });
        ModLoader.AddRecipe(new ItemStack(helmetThaumium, 1), new Object[]
                {
                    "III", "I I", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1)
                });
        ModLoader.AddRecipe(new ItemStack(plateThaumium, 1), new Object[]
                {
                    "I I", "III", "III", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1)
                });
        ModLoader.AddRecipe(new ItemStack(legsThaumium, 1), new Object[]
                {
                    "III", "I I", "I I", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1)
                });
        ModLoader.AddRecipe(new ItemStack(bootsThaumium, 1), new Object[]
                {
                    "I I", "I I", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1)
                });
        ModLoader.AddRecipe(new ItemStack(bootsStriding, 1), new Object[]
                {
                    "N N", "F F", "G G", Character.valueOf('N'), new ItemStack(thaumReagent, 1, 5), Character.valueOf('G'), Item.ingotGold, Character.valueOf('F'), new ItemStack(fabric, 1, 1)
                });
        ModLoader.AddRecipe(new ItemStack(bootsSeven, 1), new Object[]
                {
                    "R R", "B B", "T T", Character.valueOf('R'), new ItemStack(thaumReagent, 1, 11), Character.valueOf('T'), thaumiumIngot, Character.valueOf('B'), new ItemStack(bootsStriding, 1, -1)
                });
        ModLoader.AddRecipe(new ItemStack(bootsStomp, 1), new Object[]
                {
                    "R R", "B B", "I I", Character.valueOf('R'), new ItemStack(thaumReagent, 1, 12), Character.valueOf('I'), Block.blockSteel, Character.valueOf('B'), new ItemStack(bootsSeven, 1, -1)
                });
        ModLoader.AddRecipe(new ItemStack(shovelThaumium, 1), new Object[]
                {
                    "I", "S", "S", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(pickThaumium, 1), new Object[]
                {
                    "III", " S ", " S ", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(axeThaumium, 1), new Object[]
                {
                    "II", "IS", " S", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(axeThaumium, 1), new Object[]
                {
                    "II", "SI", "S ", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(hoeThaumium, 1), new Object[]
                {
                    "II", "S ", "S ", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(hoeThaumium, 1), new Object[]
                {
                    "II", " S", " S", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        ModLoader.AddRecipe(new ItemStack(swordThaumium, 1), new Object[]
                {
                    "I", "I", "S", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('S'), Item.stick
                });
        eTreeItems.setItemName("etreeitems");
        eTreeItems.iconIndex = eTreeItemSprite;
        ModLoader.AddName(eTreeItems, "Eldritch Tree Items");
        fabric.setItemName("fabric");
        fabric.iconIndex = fabricSprite;
        ModLoader.AddName(fabric, "Fabric");
        ModLoader.AddRecipe(new ItemStack(fabric, 4, 0), new Object[]
                {
                    "SWS", "WWW", "SWS", Character.valueOf('W'), Block.cloth, Character.valueOf('S'), Item.silk
                });
        thaumTinker.setItemName("thaumtinker");
        thaumTinker.iconIndex = thaumTinkerSprite;
        ModLoader.AddName(thaumTinker, "Arcane Tinkering Tools");
        ModLoader.AddShapelessRecipe(new ItemStack(thaumTinker, 1), new Object[]
                {
                    Item.shovelStone, Item.axeStone, Item.hoeStone, Item.pickaxeStone, new ItemStack(thaumReagent, 1, 6)
                });
        thaumMold.setItemName("thaummold");
        thaumMold.iconIndex = thaumMoldSprite;
        ModLoader.AddName(thaumMold, "Infusion Mold");
        ModLoader.AddShapelessRecipe(new ItemStack(thaumMold, 1), new Object[]
                {
                    Item.bowlEmpty, Item.bowlEmpty, new ItemStack(thaumReagent, 1, 6)
                });
        apportWand.setItemName("apportwand");
        apportWand.iconIndex = apportWandSprite;
        ModLoader.AddName(apportWand, "Apportation Wand");
        lightningWand.setItemName("lightningwand");
        lightningWand.iconIndex = apportWandSprite + 1;
        ModLoader.AddName(lightningWand, "Lightning Wand");
        fireWand.setItemName("firewand");
        fireWand.iconIndex = apportWandSprite + 2;
        ModLoader.AddName(fireWand, "Fire Wand");
        thaumRune.setItemName("thaumrune");
        thaumRune.iconIndex = thaumRuneSprite;
        ModLoader.AddName(thaumRune, "Thaumic Rune");
        thaumTemplate.setItemName("thaumtemplate");
        thaumTemplate.iconIndex = thaumTemplateSprite;
        ModLoader.AddName(thaumTemplate, "Thaumic Template");
        ModLoader.AddRecipe(new ItemStack(thaumTemplate, 1, 0), new Object[]
                {
                    " G ", "G G", " G ", Character.valueOf('G'), Item.ingotGold
                });
        ModLoader.AddRecipe(new ItemStack(thaumTemplate, 1, 1), new Object[]
                {
                    " G ", "G G", " G ", Character.valueOf('G'), Item.goldNugget
                });
        portableHole.setItemName("thaumportablehole");
        portableHole.iconIndex = portableHoleSprite;
        ModLoader.AddName(portableHole, "Portable Hole");
        ModLoader.AddRecipe(new ItemStack(portableHole), new Object[]
                {
                    " E ", "EGE", " E ", Character.valueOf('G'), new ItemStack(thaumGrenade, 1, 1), Character.valueOf('E'), new ItemStack(fabric, 1, 1)
                });
        processorRenderID = ModLoader.getUniqueBlockModelID(this, true);
        ModLoader.RegisterBlock(thaumProcessor);
        Item.itemsList[thaumProcessor.blockID] = (new ItemBlockProcessor(thaumProcessor.blockID - 256, thaumProcessor)).setItemName("thaumprocessor");
        ModLoader.AddName(thaumProcessor, "Thaumic Processor");
        TileEntityProcessorRenderer tileentityprocessorrenderer = new TileEntityProcessorRenderer();
        ModLoader.RegisterTileEntity(thaumcraft.TileEntityProcessor.class, "Thaumic Processor", tileentityprocessorrenderer);
        ModLoader.AddRecipe(new ItemStack(thaumProcessor, 1, 0), new Object[]
                {
                    "SSS", "SES", "SPS", Character.valueOf('S'), Block.stone, Character.valueOf('P'), Block.pistonBase, Character.valueOf('E'), Item.enderPearl
                });
        ModLoader.AddRecipe(new ItemStack(thaumProcessor, 1, 1), new Object[]
                {
                    "SSS", "OCO", "OPO", Character.valueOf('O'), Block.obsidian, Character.valueOf('S'), Block.stone, Character.valueOf('P'), Block.pistonBase, Character.valueOf('C'),
                    new ItemStack(thaumReagent, 1, 6)
                });
        ModLoader.AddRecipe(new ItemStack(thaumProcessor, 1, 2), new Object[]
                {
                    "EWE", "WGW", "EWE", Character.valueOf('E'), Item.enderPearl, Character.valueOf('W'), new ItemStack(eTreeItems, 1, 3), Character.valueOf('G'), Block.blockGold
                });
        ModLoader.AddRecipe(new ItemStack(thaumProcessor, 1, 3), new Object[]
                {
                    "TGT", "TGT", "TGT", Character.valueOf('G'), Item.ingotGold, Character.valueOf('T'), Block.thinGlass
                });
        ModLoader.AddRecipe(new ItemStack(thaumProcessor, 1, 4), new Object[]
                {
                    "WLW", "LBL", "WGW", Character.valueOf('W'), new ItemStack(eTreeItems, 1, 3), Character.valueOf('L'), new ItemStack(thaumReagent, 1, 11), Character.valueOf('G'), new ItemStack(thaumConduit, 1, 1), Character.valueOf('B'),
                    Block.blockGold
                });
        thaumSymbolItem.setItemName("thaumsymbol");
        thaumSymbolItem.iconIndex = thaumSymbolSprite;
        ModLoader.AddName(thaumSymbolItem, "Thaumic Symbol");
        symbolRenderID = ModLoader.getUniqueBlockModelID(this, true);
        ModLoader.RegisterBlock(thaumSymbol);
        ModLoader.AddName(thaumSymbol, "Thaumic Symbol");
        TileEntitySymbolRenderer tileentitysymbolrenderer = new TileEntitySymbolRenderer();
        ModLoader.RegisterTileEntity(thaumcraft.TileEntitySymbol.class, "Thaumic Symbol", tileentitysymbolrenderer);
        crucibleRenderID = ModLoader.getUniqueBlockModelID(this, true);
        ModLoader.RegisterBlock(thaumCrucible);
        Item.itemsList[thaumCrucible.blockID] = (new ItemBlockCrucible(thaumCrucible.blockID - 256, thaumCrucible)).setItemName("thaumcrucible");
        ModLoader.AddName(thaumCrucible, "Thaumic Crucible");
        TileEntityCrucibleRenderer tileentitycruciblerenderer = new TileEntityCrucibleRenderer();
        ModLoader.RegisterTileEntity(thaumcraft.TileEntityCrucible.class, "Thaumic Crucible", tileentitycruciblerenderer);
        ModLoader.AddRecipe(new ItemStack(thaumCrucible, 1, 0), new Object[]
                {
                    "S S", "SES", " F ", Character.valueOf('S'), Block.stone, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Block.stoneOvenIdle
                });
        ModLoader.AddRecipe(new ItemStack(thaumCrucible, 1, 1), new Object[]
                {
                    "IEI", "IVI", " C ", Character.valueOf('I'), Item.ingotIron, Character.valueOf('E'), new ItemStack(thaumSymbolItem, 1, 9), Character.valueOf('V'), new ItemStack(thaumReagent, 1, 6), Character.valueOf('C'),
                    new ItemStack(thaumCrucible, 1, 0)
                });
        ModLoader.AddRecipe(new ItemStack(thaumCrucible, 1, 8), new Object[]
                {
                    "IVI", "IVI", " C ", Character.valueOf('I'), new ItemStack(thaumiumIngot, 1), Character.valueOf('V'), new ItemStack(thaumCrucible, 1, 7), Character.valueOf('C'), new ItemStack(thaumCrucible, 1, 1)
                });
        ModLoader.AddRecipe(new ItemStack(thaumCrucible, 1, 2), new Object[]
                {
                    " T ", "SCS", " O ", Character.valueOf('S'), Block.stone, Character.valueOf('T'), new ItemStack(thaumTemplate, 1, 0), Character.valueOf('C'), new ItemStack(thaumReagent, 1, 6), Character.valueOf('O'),
                    Block.obsidian
                });
        thaumConduitRenderID = ModLoader.getUniqueBlockModelID(this, true);
        ModLoader.RegisterBlock(thaumConduit);
        Item.itemsList[thaumConduit.blockID] = (new ItemBlockConduit(thaumConduit.blockID - 256, thaumConduit)).setItemName("thaumconduit");
        ModLoader.AddName(thaumConduit, "Thaumic Conduit");
        ModLoader.RegisterTileEntity(thaumcraft.TileEntityConduit.class, "Thaumic Conduit");
        ModLoader.AddRecipe(new ItemStack(thaumConduit, 1, 0), new Object[]
                {
                    " C ", "CGC", " C ", Character.valueOf('C'), new ItemStack(thaumConduit, 1, 1), Character.valueOf('G'), Item.ingotGold
                });
        ModLoader.AddRecipe(new ItemStack(thaumConduit, 8, 1), new Object[]
                {
                    " G ", "GRG", " G ", Character.valueOf('G'), Block.thinGlass, Character.valueOf('R'), Item.redstone
                });
        ModLoader.AddRecipe(new ItemStack(thaumConduit, 1, 2), new Object[]
                {
                    "CLC", Character.valueOf('C'), new ItemStack(thaumConduit, 1, 1), Character.valueOf('L'), Block.lever
                });
        ModLoader.AddShapelessRecipe(new ItemStack(thaumConduit, 1, 3), new Object[]
                {
                    new ItemStack(thaumConduit, 1, 0), new ItemStack(thaumCrucible, 1, 7)
                });
        ModLoader.AddRecipe(new ItemStack(thaumConduit, 1, 10), new Object[]
                {
                    "PPP", "PHP", "PDP", Character.valueOf('P'), new ItemStack(eTreeItems, 1, 3), Character.valueOf('H'), new ItemStack(eTreeItems, 1, 2), Character.valueOf('D'), new ItemStack(thaumReagent, 1, 7)
                });
        effectsRenderID = ModLoader.getUniqueBlockModelID(this, true);
        ModLoader.RegisterBlock(thaumEffects);
        ModLoader.AddName(thaumEffects, "Thaumic Effects");
        TileEntityEffectsRenderer tileentityeffectsrenderer = new TileEntityEffectsRenderer();
        ModLoader.RegisterTileEntity(thaumcraft.TileEntityEffects.class, "Thaumic Effects", tileentityeffectsrenderer);
        ModLoader.AddLocalization("tile.thaumprocessor.infuser.name", "Vis Infuser");
        ModLoader.AddLocalization("tile.thaumprocessor.duplicator.name", "Duplicator");
        ModLoader.AddLocalization("tile.thaumprocessor.borecore.name", "Arcane Bore");
        ModLoader.AddLocalization("tile.thaumprocessor.borefocus.name", "Arcane Focus");
        ModLoader.AddLocalization("tile.thaumprocessor.generator.name", "Thaumic Generator");
        ModLoader.AddLocalization("tile.thaumcrucible.crucible.name", "Vis Crucible");
        ModLoader.AddLocalization("tile.thaumcrucible.eyecrucible.name", "Crucible of Eyes");
        ModLoader.AddLocalization("tile.thaumcrucible.oricrucible.name", "Thaumium Crucible");
        ModLoader.AddLocalization("tile.thaumcrucible.condenser.name", "Vis Condenser");
        ModLoader.AddLocalization("tile.thaumcrucible.obbase.name", "Obelisk Piece");
        ModLoader.AddLocalization("tile.thaumcrucible.obmiddle1.name", "Obelisk Piece");
        ModLoader.AddLocalization("tile.thaumcrucible.obmiddle2.name", "Obelisk Piece");
        ModLoader.AddLocalization("tile.thaumcrucible.obcap.name", "Obelisk Piece");
        ModLoader.AddLocalization("tile.thaumcrucible.altar.name", "Obsidian Altar");
        ModLoader.AddLocalization("tile.thaumcrucible.viscube.name", "Solidified Vis");
        ModLoader.AddLocalization("tile.thaumconduit.pipe.name", "Vis Conduit");
        ModLoader.AddLocalization("tile.thaumconduit.node.name", "Vis Node");
        ModLoader.AddLocalization("tile.thaumconduit.valve.name", "Vis Valve");
        ModLoader.AddLocalization("tile.thaumconduit.pnode.name", "Portable Vis Node");
        ModLoader.AddLocalization("tile.thaumconduit.trunknormal.name", "Traveling Trunk");
        ModLoader.AddLocalization("tile.thaumconduit.trunkgreedy.name", "Greedy Trunk");
        ModLoader.AddLocalization("tile.thaumconduit.trunkroomy.name", "Roomy Trunk");
        ModLoader.AddLocalization("tile.thaumconduit.trunkangry.name", "Angry Trunk");
        ModLoader.AddLocalization("item.thaumsymbol.iron.name", "Iron Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.stone.name", "Stone Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.gold.name", "Gold Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.lapis.name", "Lapis Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.mossy.name", "Verdant Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.earth.name", "Earth Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.ice.name", "Frozen Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.blazing.name", "Blazing Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.eye.name", "Seeing Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.void.name", "Void Symbol");
        ModLoader.AddLocalization("item.thaumsymbol.shock.name", "Shocking Symbol");
        ModLoader.AddLocalization("item.thaumtemplate.symbol.name", "Blank Symbol");
        ModLoader.AddLocalization("item.thaumtemplate.rune.name", "Blank Rune");
        ModLoader.AddLocalization("item.thaumtemplate.device.name", "Arcane Tinkering Tools");
        ModLoader.AddLocalization("item.thaumtemplate.reagent.name", "Infusion Mold");
        ModLoader.AddLocalization("item.thaumrune.iron.name", "Iron Rune");
        ModLoader.AddLocalization("item.thaumrune.gold.name", "Gold Rune");
        ModLoader.AddLocalization("item.thaumrune.diamond.name", "Diamond Rune");
        ModLoader.AddLocalization("item.thaumrune.black.name", "Flint Rune");
        ModLoader.AddLocalization("item.thaumrune.red.name", "Redstone Rune");
        ModLoader.AddLocalization("item.thaumrune.lapis.name", "Lapis Rune");
        ModLoader.AddLocalization("item.blanktalisman.inert.name", "Inert Talisman Core");
        ModLoader.AddLocalization("item.blanktalisman.charged.name", "Charged Talisman Core");
        ModLoader.AddLocalization("item.voidtalisman.iron.name", "Iron Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.gold.name", "Gold Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.diamond.name", "Diamond Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.black.name", "Flint Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.red.name", "Redstone Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.lapis.name", "Lapis Talisman of the Void");
        ModLoader.AddLocalization("item.voidtalisman.blank.name", "Talisman of the Void");
        ModLoader.AddLocalization("item.thaumgrenade.grenade0.name", "Unstable Arcane Singularity");
        ModLoader.AddLocalization("item.thaumgrenade.grenade1.name", "Stabilized Arcane Singularity");
        ModLoader.AddLocalization("item.thaumreagents.coal0.name", "Glimmering Alumentum");
        ModLoader.AddLocalization("item.thaumreagents.coal1.name", "Glowing Alumentum");
        ModLoader.AddLocalization("item.thaumreagents.coal2.name", "Incandescent Alumentum");
        ModLoader.AddLocalization("item.thaumreagents.dust0.name", "Glimmering Nitor");
        ModLoader.AddLocalization("item.thaumreagents.dust1.name", "Glowing Nitor");
        ModLoader.AddLocalization("item.thaumreagents.dust2.name", "Incandescent Nitor");
        ModLoader.AddLocalization("item.thaumreagents.crystal.name", "Vis Crystal");
        ModLoader.AddLocalization("item.thaumreagents.crystalfire.name", "Fiery Vis Crystal");
        ModLoader.AddLocalization("item.thaumreagents.crystalice.name", "Icy Vis Crystal");
        ModLoader.AddLocalization("item.thaumreagents.crystalvenom.name", "Verdant Vis Crystal");
        ModLoader.AddLocalization("item.thaumreagents.crystalelec.name", "Electrified Vis Crystal");
        ModLoader.AddLocalization("item.thaumreagents.distsouls.name", "Distilled Souls");
        ModLoader.AddLocalization("item.thaumreagents.disthate.name", "Distilled Hate");
        ModLoader.AddLocalization("item.thaumreagents.distempty.name", "Distilled Emptiness");
        ModLoader.AddLocalization("item.thaumreagents.disthunger.name", "Distilled Hunger");
        ModLoader.AddLocalization("item.fabric.fabric.name", "Fabric");
        ModLoader.AddLocalization("item.fabric.enchantedfabric.name", "Enchanted Fabric");
        ModLoader.AddLocalization("item.etreeitems.husk.name", "Grub Husk");
        ModLoader.AddLocalization("item.etreeitems.branch.name", "Eldritch Log");
        ModLoader.AddLocalization("item.etreeitems.heartwood.name", "Eldritch Heartwood");
        ModLoader.AddLocalization("item.etreeitems.planks.name", "Eldritch Timber");
        ModLoader.AddLocalization("item.helmetThaumium.name", "Thaumium Helm");
        ModLoader.AddLocalization("item.chestplateThaumium.name", "Thaumium Chestplate");
        ModLoader.AddLocalization("item.leggingsThaumium.name", "Thaumium Leggings");
        ModLoader.AddLocalization("item.bootsThaumium.name", "Thaumium Boots");
        ModLoader.AddLocalization("item.bootsSeven.name", "Seven League Boots");
        ModLoader.AddLocalization("item.bootsStriding.name", "Boots of Striding");
        ModLoader.AddLocalization("item.bootsStomp.name", "Boots of the Meteor");
        thaumCubeSideFX = ModLoader.getUniqueSpriteIndex("/terrain.png");
        thaumPHSideFX = ModLoader.getUniqueSpriteIndex("/terrain.png");
        thaumCrucibleSludgeFX = ModLoader.getUniqueSpriteIndex("/terrain.png");
        thaumSludgeFX = ModLoader.getUniqueSpriteIndex("/terrain.png");
        RecipesCrucible.smelting().addSmelting(Item.stick.shiftedIndex, 0.25F);
        RecipesCrucible.smelting().addSmelting(Item.clay.shiftedIndex, 1.0F);
        RecipesCrucible.smelting().addSmelting(Item.clay.shiftedIndex, 1.0F);
        RecipesCrucible.smelting().addSmelting(Item.brick.shiftedIndex, 2.0F);
        RecipesCrucible.smelting().addSmelting(Item.flint.shiftedIndex, 1.0F);
        RecipesCrucible.smelting().addSmelting(Item.coal.shiftedIndex, 2.0F);
        RecipesCrucible.smelting().addSmelting(Item.snowball.shiftedIndex, 0.1F);
        RecipesCrucible.smelting().addSmelting(Item.dyePowder.shiftedIndex, 0, 4F);
        RecipesCrucible.smelting().addSmelting(Item.dyePowder.shiftedIndex, 2, 4F);
        RecipesCrucible.smelting().addSmelting(Item.dyePowder.shiftedIndex, 3, 25F);
        RecipesCrucible.smelting().addSmelting(Item.dyePowder.shiftedIndex, 4, 9F);
        RecipesCrucible.smelting().addSmelting(Item.ingotIron.shiftedIndex, 5F);
        RecipesCrucible.smelting().addSmelting(Item.seeds.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.feather.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.bone.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.melon.shiftedIndex, 2.0F);
        RecipesCrucible.smelting().addSmelting(Item.melonSeeds.shiftedIndex, 2.0F);
        RecipesCrucible.smelting().addSmelting(Item.beefCooked.shiftedIndex, 5F);
        RecipesCrucible.smelting().addSmelting(Item.beefRaw.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.chickenCooked.shiftedIndex, 5F);
        RecipesCrucible.smelting().addSmelting(Item.chickenRaw.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.porkCooked.shiftedIndex, 5F);
        RecipesCrucible.smelting().addSmelting(Item.porkRaw.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.fishCooked.shiftedIndex, 5F);
        RecipesCrucible.smelting().addSmelting(Item.fishRaw.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.netherStalkSeeds.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.rottenFlesh.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.silk.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.leather.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.wheat.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.reed.shiftedIndex, 4F);
        RecipesCrucible.smelting().addSmelting(Item.gunpowder.shiftedIndex, 10F);
        RecipesCrucible.smelting().addSmelting(Item.lightStoneDust.shiftedIndex, 9F);
        RecipesCrucible.smelting().addSmelting(Item.redstone.shiftedIndex, 6F);
        RecipesCrucible.smelting().addSmelting(Item.bucketMilk.shiftedIndex, 9F);
        RecipesCrucible.smelting().addSmelting(Item.bucketWater.shiftedIndex, 9F);
        RecipesCrucible.smelting().addSmelting(Item.bucketLava.shiftedIndex, 9F);
        RecipesCrucible.smelting().addSmelting(Item.egg.shiftedIndex, 6F);
        RecipesCrucible.smelting().addSmelting(Item.ingotGold.shiftedIndex, 25F);
        RecipesCrucible.smelting().addSmelting(Item.slimeBall.shiftedIndex, 25F);
        RecipesCrucible.smelting().addSmelting(Item.appleRed.shiftedIndex, 6F);
        RecipesCrucible.smelting().addSmelting(Item.diamond.shiftedIndex, 64F);
        RecipesCrucible.smelting().addSmelting(Item.enderPearl.shiftedIndex, 64F);
        RecipesCrucible.smelting().addSmelting(Item.record13.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordCat.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.record11.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordCat.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordChirp.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordFar.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordMall.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordMellohi.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordStal.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordStrad.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.recordWard.shiftedIndex, 100F);
        RecipesCrucible.smelting().addSmelting(Item.blazeRod.shiftedIndex, 36F);
        RecipesCrucible.smelting().addSmelting(Item.ghastTear.shiftedIndex, 64F);
        RecipesCrucible.smelting().addSmelting(Item.spiderEye.shiftedIndex, 9F);
        RecipesCrucible.smelting().addSmelting(Item.saddle.shiftedIndex, 64F);
        RecipesCrucible.smelting().addSmelting(Block.cobblestone.blockID, 0.1F);
        RecipesCrucible.smelting().addSmelting(Block.planks.blockID, 0.5F);
        RecipesCrucible.smelting().addSmelting(Block.cobblestoneMossy.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.sand.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.dirt.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.grass.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.glass.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.ice.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.gravel.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.stone.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.waterlily.blockID, 3F);
        RecipesCrucible.smelting().addSmelting(Block.web.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.netherBrick.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.netherStalk.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.whiteStone.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.stoneBrick.blockID, 1, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.stoneBrick.blockID, 2, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.netherrack.blockID, 1.0F);
        RecipesCrucible.smelting().addSmelting(Block.slowSand.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.oreCoal.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.wood.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.leaves.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.tallGrass.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.deadBush.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.cactus.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.sapling.blockID, 2.0F);
        RecipesCrucible.smelting().addSmelting(Block.mycelium.blockID, 3F);
        RecipesCrucible.smelting().addSmelting(Block.oreIron.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.plantYellow.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.plantRed.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.mushroomBrown.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.mushroomRed.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.vine.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.pumpkin.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.reed.blockID, 4F);
        RecipesCrucible.smelting().addSmelting(Block.oreRedstone.blockID, 9F);
        RecipesCrucible.smelting().addSmelting(Block.oreLapis.blockID, 9F);
        RecipesCrucible.smelting().addSmelting(Block.oreGold.blockID, 25F);
        RecipesCrucible.smelting().addSmelting(Block.obsidian.blockID, 25F);
        RecipesCrucible.smelting().addSmelting(Block.oreDiamond.blockID, 64F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 0, 75F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 1, 75F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 2, 100F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 3, 100F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 4, 75F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 5, 150F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 6, 75F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 7, 100F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 8, 100F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 9, 100F);
        RecipesCrucible.smelting().addSmelting(thaumSymbolItem.shiftedIndex, 10, 100F);
        RecipesCrucible.smelting().addSmelting(thaumDetector.shiftedIndex, 50F);
        RecipesCrucible.smelting().addSmelting(thaumGrenade.shiftedIndex, 0, 35F);
        RecipesCrucible.smelting().addSmelting(thaumGrenade.shiftedIndex, 1, 50F);
        for (int i = 0; i < 6; i++)
        {
            RecipesCrucible.smelting().addSmelting(thaumRune.shiftedIndex, i, 35F);
        }

        for (int j = 0; j < 7; j++)
        {
            RecipesCrucible.smelting().addSmelting(talismanVoid.shiftedIndex, j, 250F);
        }

        RecipesCrucible.smelting().addSmelting(talismanVigor.shiftedIndex, 250F);
        RecipesCrucible.smelting().addSmelting(talismanHealth.shiftedIndex, 250F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 0, 2.0F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 1, 4F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 2, 8F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 3, 26F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 4, 53F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 5, 81F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 6, 35F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 11, 50F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 12, 50F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 13, 50F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 14, 50F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 7, 15F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 8, 25F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 9, 81F);
        RecipesCrucible.smelting().addSmelting(thaumReagent.shiftedIndex, 10, 35F);
        RecipesCrucible.smelting().addSmelting(thaumiumIngot.shiftedIndex, 40F);
        RecipesCrucible.smelting().addSmelting(fabric.shiftedIndex, 1, 35F);
        RecipesCrucible.smelting().addSmelting(eTreeItems.shiftedIndex, 0, 25F);
        RecipesCrucible.smelting().addSmelting(eTreeItems.shiftedIndex, 1, 16F);
        RecipesCrucible.smelting().addSmelting(eTreeItems.shiftedIndex, 2, 64F);
        RecipesCrucible.smelting().addSmelting(eTreeItems.shiftedIndex, 3, 20F);
        RecipesCrucible.smelting().addSmelting(thaumCrucible.blockID, 7, 225F);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ingotIron, 1), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 0), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Block.stone, 1), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 1), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ingotGold, 1), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 2), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.dyePowder, 1, 4), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 3), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 13), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 4), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumGrenade, 1, 1), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 5), 200, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Block.dirt), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 6), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 14), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 7), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 12), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 8), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.spiderEye, 1), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 9), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 11), new ItemStack(thaumTemplate, 1, 0), new ItemStack(thaumSymbolItem, 1, 10), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ingotIron, 1), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 0), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ingotGold, 1), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 1), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.diamond, 1), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 2), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.flint, 1), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 3), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.redstone, 1), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 4), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.dyePowder, 1, 4), new ItemStack(thaumTemplate, 1, 1), new ItemStack(thaumRune, 1, 5), 50, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumSymbolItem, 1, 5), new ItemStack(talismanBlank, 1, 1), new ItemStack(talismanVoid, 1, 6), 200, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(eTreeItems, 1, 2), new ItemStack(talismanBlank, 1, 1), new ItemStack(talismanHealth, 1), 150, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 10), new ItemStack(talismanBlank, 1, 1), new ItemStack(talismanVigor, 1), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.compass, 1), new ItemStack(thaumTinker, 1), new ItemStack(thaumDetector, 1), 50, 2);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 2), new ItemStack(thaumReagent, 1, 5), new ItemStack(thaumGrenade, 1, 0), 35, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumGrenade, 1, 0), new ItemStack(thaumMold, 1), new ItemStack(thaumGrenade, 1, 1), 50, 5);
        RecipesProcessor.infusing().addInfusing(new ItemStack(eTreeItems, 1, 3), new ItemStack(portableHole, 1), new ItemStack(apportWand, 1), 100, 5);
        RecipesProcessor.infusing().addInfusing(new ItemStack(eTreeItems, 1, 3), new ItemStack(thaumReagent, 1, 12), new ItemStack(fireWand, 1), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(eTreeItems, 1, 3), new ItemStack(thaumReagent, 1, 11), new ItemStack(lightningWand, 1), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Block.glass, 1), new ItemStack(thaumTinker, 1), new ItemStack(thaumCrucible, 1, 7), 250, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumConduit, 1, 10), new ItemStack(thaumReagent, 1, 8), new ItemStack(thaumConduit, 1, 13), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumConduit, 1, 10), new ItemStack(thaumReagent, 1, 9), new ItemStack(thaumConduit, 1, 12), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumConduit, 1, 10), new ItemStack(thaumReagent, 1, 10), new ItemStack(thaumConduit, 1, 11), 100, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.coal, 1), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 0), 2, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.coal, 1, 1), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 0), 6, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 0), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 1), 14, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 1), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 2), 25, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.lightStoneDust, 1), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 3), 16, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 3), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 4), 25, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumReagent, 1, 4), new ItemStack(thaumMold, 1), new ItemStack(thaumReagent, 1, 5), 25, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Block.slowSand, 1), new ItemStack(Item.glassBottle, 1), new ItemStack(thaumReagent, 1, 7), 25, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Block.netherrack, 1), new ItemStack(Item.glassBottle, 1), new ItemStack(thaumReagent, 1, 8), 25, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ghastTear, 1), new ItemStack(Item.glassBottle, 1), new ItemStack(thaumReagent, 1, 9), 10, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(thaumGrenade, 1, 0), new ItemStack(Item.glassBottle, 1), new ItemStack(thaumReagent, 1, 10), 10, 9999);
        RecipesProcessor.infusing().addInfusing(new ItemStack(fabric, 1, 0), new ItemStack(thaumTinker, 1), new ItemStack(fabric, 1, 1), 50, 1);
        RecipesProcessor.infusing().addInfusing(new ItemStack(eTreeItems, 1, 1), new ItemStack(thaumTinker, 1), new ItemStack(eTreeItems, 1, 3), 50, 2);
        RecipesProcessor.infusing().addInfusing(new ItemStack(Item.ingotIron, 1), new ItemStack(thaumReagent, 1, 6), new ItemStack(thaumiumIngot, 1), 5, 9999);
    }

    public int AddFuel(int i, int j)
    {
        if (i == thaumReagent.shiftedIndex && j == 0)
        {
            return 3200;
        }
        if (i == thaumReagent.shiftedIndex && j == 1)
        {
            return 6400;
        }
        return i != thaumReagent.shiftedIndex || j != 2 ? 0 : 12800;
    }

    public void RegisterAnimation(Minecraft minecraft)
    {
        ModLoader.addAnimation(new FxAnimatedTexture("/thaumcraft/tcubeanim.png", thaumCubeSideFX));
        ModLoader.addAnimation(new FxAnimatedTexture("/thaumcraft/tphanim.png", thaumPHSideFX));
        ModLoader.addAnimation(new FxAnimatedTexture("/thaumcraft/tcrucibleanim.png", thaumCrucibleSludgeFX));
        ModLoader.addAnimation(new FxAnimatedTexture("/thaumcraft/tcpuddleanim.png", thaumSludgeFX));
    }

    public void RenderInvBlock(RenderBlocks renderblocks, Block block, int i, int j)
    {
        if (j == crucibleRenderID)
        {
            ((BlockCrucible)block).renderMyBlockInv(ModLoader.getMinecraftInstance().theWorld, block, i, j, renderblocks);
        }
        else if (j == thaumConduitRenderID)
        {
            ((BlockConduit)block).renderMyBlockInv(ModLoader.getMinecraftInstance().theWorld, block, i, j, renderblocks);
        }
        else if (j == processorRenderID)
        {
            ((BlockProcessor)block).renderMyBlockInv(ModLoader.getMinecraftInstance().theWorld, block, i, j, renderblocks);
        }
        super.RenderInvBlock(renderblocks, block, i, j);
    }

    public boolean RenderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l)
    {
        if (l == crucibleRenderID)
        {
            return ((BlockCrucible)block).renderMyBlock(ModLoader.getMinecraftInstance().theWorld, renderblocks, i, j, k, block);
        }
        if (l == thaumConduitRenderID)
        {
            return ((BlockConduit)block).renderMyBlock(ModLoader.getMinecraftInstance().theWorld, renderblocks, i, j, k, block);
        }
        if (l == processorRenderID)
        {
            return ((BlockProcessor)block).renderMyBlock(ModLoader.getMinecraftInstance().theWorld, renderblocks, i, j, k, block);
        }
        if (l == effectsRenderID)
        {
            return ((BlockEffects)block).renderMyBlock(ModLoader.getMinecraftInstance().theWorld, renderblocks, i, j, k, block);
        }
        else
        {
            return false;
        }
    }

    public boolean OnTickInGame(float f, Minecraft minecraft)
    {
        if (firstUpdate)
        {
            firstUpdate = false;
            ThaumCraftCore.WorldDir = null;
            ThaumCraftCore.LoadChunkData();
            ThaumCraftCore.LoadAWandData();
            initSounds(minecraft);
        }
        if (System.currentTimeMillis() - chunkCheckInterval >= lastChunkCheck)
        {
            lastChunkCheck = System.currentTimeMillis();
            Iterator iterator = ThaumCraftCore.loadedChunks.iterator();
            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }
                SaveChunkPos savechunkpos = (SaveChunkPos)iterator.next();
                if (!minecraft.theWorld.chunkProvider.chunkExists(savechunkpos.x, savechunkpos.z))
                {
                    minecraft.theWorld.chunkProvider.loadChunk(savechunkpos.x, savechunkpos.z);
                }
            }
            while (true);
        }
        for (int i = 0; i < 9; i++)
        {
            if (hbpTicker[i] <= 0)
            {
                continue;
            }
            if (hbpTickerType[i] != 2)
            {
                ghe.renderParticles1(i, hbpTicker[i], hbpTickerType[i]);
            }
            else
            {
                ghe.renderParticles2(i, hbpTicker[i], hbpTickerType[i]);
            }
            hbpTicker[i]--;
        }

        if (Time == 0L)
        {
            Time = System.currentTimeMillis();
        }
        double d = System.currentTimeMillis() - Time;
        if (d > 250D)
        {
            Container container = minecraft.thePlayer.inventorySlots;
            for (int k = 0; k < container.inventorySlots.size(); k++)
            {
                if (!container.getSlot(k).getHasStack())
                {
                    continue;
                }
                ItemStack itemstack = minecraft.thePlayer.inventorySlots.getSlot(k).getStack();
                if (itemstack.itemID == thaumDetector.shiftedIndex)
                {
                    itemstack.setItemDamage(0);
                }
            }
        }
        if (d > 750D)
        {
            Time = System.currentTimeMillis();
            Container container1 = minecraft.thePlayer.inventorySlots;
            for (int l = 36; l < container1.inventorySlots.size(); l++)
            {
                if (!container1.getSlot(l).getHasStack())
                {
                    continue;
                }
                ItemStack itemstack1 = minecraft.thePlayer.inventorySlots.getSlot(l).getStack();
                if (itemstack1.itemID != thaumDetector.shiftedIndex)
                {
                    continue;
                }
                Chunk chunk = minecraft.theWorld.getChunkFromBlockCoords(MathHelper.floor_double(minecraft.thePlayer.posX), MathHelper.floor_double(minecraft.thePlayer.posZ));
                if (chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0)
                {
                    itemstack1.setItemDamage(1);
                    minecraft.theWorld.playSoundEffect(minecraft.thePlayer.posX, minecraft.thePlayer.posY, minecraft.thePlayer.posZ, "note.bassattack", 0.1F, 8F);
                }
            }
        }
        if (stompCooldown > 0)
        {
            stompCooldown--;
        }
        int j = 0;
        try
        {
            j = ((Integer)ModLoader.getPrivateValue(net.minecraft.src.EntityLiving.class, minecraft.thePlayer, 69)).intValue();
        }
        catch (Exception exception) { }
        if (minecraft.thePlayer.inventory.armorItemInSlot(0) != null && (minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsStriding.shiftedIndex || minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsSeven.shiftedIndex || minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsStomp.shiftedIndex))
        {
            float f1 = 0.3F;
            float f2 = 0.04F;
            float f3 = 0.85F;
            int j1 = 2;
            if (minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsSeven.shiftedIndex)
            {
                f1 = 0.75F;
                j1 = 3;
                f2 = 0.075F;
                f3 = 1.55F;
            }
            else if (minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsStomp.shiftedIndex)
            {
                f1 = 0.8F;
                j1 = 3;
                f2 = 0.075F;
                f3 = 1.6F;
            }
            if (minecraft.thePlayer.movementInput.jump && fallCount == 0.0F && j == 10 && !minecraft.thePlayer.movementInput.sneak && !startedJump)
            {
                minecraft.thePlayer.motionY += f1;
                if (minecraft.thePlayer.motionY > (double)f3)
                {
                    minecraft.thePlayer.motionY = f3;
                }
                minecraft.thePlayer.addExhaustion(0.075F * (float)j1);
                playedFire = false;
                startedJump = true;
            }
            if (minecraft.thePlayer.movementInput.moveForward > 0.0F && minecraft.thePlayer.onGround)
            {
                minecraft.thePlayer.motionX *= 1.1000000238418579D;
                minecraft.thePlayer.motionZ *= 1.1000000238418579D;
                if (minecraft.thePlayer.isSprinting())
                {
                    minecraft.thePlayer.motionX *= 1.0499999523162842D;
                    minecraft.thePlayer.motionZ *= 1.0499999523162842D;
                }
            }
            minecraft.thePlayer.jumpMovementFactor = f2;
            fallCount += minecraft.thePlayer.fallDistance - fallCount / (float)j1;
            minecraft.thePlayer.fallDistance = fallCount / (float)j1;
            if (minecraft.thePlayer.inventory.armorItemInSlot(0).getItem().shiftedIndex == bootsStomp.shiftedIndex && startedJump)
            {
                if (stompCooldown == 0 && minecraft.thePlayer.movementInput.sneak && !minecraft.thePlayer.onGround && minecraft.thePlayer.motionY < 0.0D)
                {
                    minecraft.thePlayer.motionY *= 1.0900000333786011D;
                    if (!playedFire)
                    {
                        minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "fire.ignite", 1.0F, minecraft.theWorld.rand.nextFloat() * 0.4F + 0.8F);
                    }
                    minecraft.theWorld.spawnParticle("flame", minecraft.thePlayer.posX + (double)((minecraft.theWorld.rand.nextFloat() - minecraft.theWorld.rand.nextFloat()) * 0.4F), minecraft.thePlayer.posY - 1.5D, minecraft.thePlayer.posZ + (double)((minecraft.theWorld.rand.nextFloat() - minecraft.theWorld.rand.nextFloat()) * 0.4F), 0.0D, 0.0D, 0.0D);
                    playedFire = true;
                }
                if (stompCooldown == 0 && minecraft.thePlayer.movementInput.sneak && minecraft.thePlayer.motionY < 0.0D && minecraft.thePlayer.onGround && !prevOnGround && j < 1 && fallCount > 3F)
                {
                    minecraft.theWorld.createExplosion(minecraft.thePlayer, minecraft.thePlayer.posX, minecraft.thePlayer.posY, minecraft.thePlayer.posZ, 2.0F);
                    List list = minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(minecraft.thePlayer, AxisAlignedBB.getBoundingBoxFromPool(minecraft.thePlayer.posX - 2D, minecraft.thePlayer.posY - 2D, minecraft.thePlayer.posZ - 2D, minecraft.thePlayer.posX + 3D, minecraft.thePlayer.posY + 3D, minecraft.thePlayer.posZ + 3D));
                    for (int k1 = 0; k1 < list.size(); k1++)
                    {
                        if (list.get(k1) instanceof EntityLiving)
                        {
                            ((EntityLiving)list.get(k1)).setFire(3);
                        }
                    }

                    if (minecraft.thePlayer.inventory.armorItemInSlot(0) != null)
                    {
                        minecraft.thePlayer.inventory.armorItemInSlot(0).damageItem(1, minecraft.thePlayer);
                    }
                    stompCooldown = 50;
                }
            }
            prevOnGround = minecraft.thePlayer.onGround;
        }
        if (minecraft.thePlayer.onGround)
        {
            fallCount = 0.0F;
            startedJump = false;
        }
        if (System.currentTimeMillis() >= secondTicker && !minecraft.isGamePaused)
        {
            secondTicker = System.currentTimeMillis() + 1000L;
            Container container2 = minecraft.thePlayer.inventorySlots;
            for (int i1 = 36; i1 < container2.inventorySlots.size(); i1++)
            {
                if (!container2.getSlot(i1).getHasStack())
                {
                    continue;
                }
                ItemStack itemstack2 = minecraft.thePlayer.inventorySlots.getSlot(i1).getStack();
                if ((itemstack2.itemID == talismanHealth.shiftedIndex || itemstack2.itemID == talismanVigor.shiftedIndex) && itemstack2.getItemDamage() == itemstack2.getMaxDamage() - 1)
                {
                    itemstack2 = ThaumCraftCore.rechargeItem(itemstack2, minecraft.thePlayer);
                    if (itemstack2.getItemDamage() == 0)
                    {
                        minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "random.orb", 0.5F, 1.0F + minecraft.theWorld.rand.nextFloat());
                        hbpTicker[i1 - 36] = 18;
                        hbpTickerType[i1 - 36] = 2;
                        continue;
                    }
                }
                if (itemstack2.itemID == talismanHealth.shiftedIndex && minecraft.thePlayer.getEntityHealth() < minecraft.thePlayer.getMaxHealth() && itemstack2.getItemDamage() < itemstack2.getMaxDamage() - 1)
                {
                    minecraft.thePlayer.setEntityHealth(minecraft.thePlayer.getEntityHealth() + 1);
                    minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "mob.silverfish.step", 1.0F, 0.5F * ((minecraft.theWorld.rand.nextFloat() - minecraft.theWorld.rand.nextFloat()) * 0.6F + 2.0F));
                    itemstack2.damageItem(10, minecraft.thePlayer);
                    hbpTicker[i1 - 36] = 18;
                    hbpTickerType[i1 - 36] = 0;
                    continue;
                }
                if (itemstack2.itemID != talismanVigor.shiftedIndex)
                {
                    continue;
                }
                if (minecraft.thePlayer.getAir() < 150 && itemstack2.getItemDamage() < itemstack2.getMaxDamage() - 1)
                {
                    minecraft.thePlayer.setAir(300);
                    minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "random.breath", 0.8F, 0.5F * ((minecraft.theWorld.rand.nextFloat() - minecraft.theWorld.rand.nextFloat()) * 0.6F + 2.0F));
                    itemstack2.damageItem(10, minecraft.thePlayer);
                    hbpTicker[i1 - 36] = 18;
                    hbpTickerType[i1 - 36] = 1;
                    continue;
                }
                if (minecraft.thePlayer.getFoodStats().needFood() && itemstack2.getItemDamage() < itemstack2.getMaxDamage() - 1)
                {
                    minecraft.thePlayer.getFoodStats().setFoodLevel(minecraft.thePlayer.getFoodStats().getFoodLevel() + 1);
                    minecraft.theWorld.playSoundAtEntity(minecraft.thePlayer, "random.breath", 0.8F, 0.5F * ((minecraft.theWorld.rand.nextFloat() - minecraft.theWorld.rand.nextFloat()) * 0.6F + 2.0F));
                    itemstack2.damageItem(10, minecraft.thePlayer);
                    hbpTicker[i1 - 36] = 18;
                    hbpTickerType[i1 - 36] = 1;
                }
            }
        }
        if (ThaumCraftCore.aWand != null && ThaumCraftCore.aWand.apportBlock > 0 && minecraft.inGameHasFocus)
        {
            gaw.render(new ItemStack(Block.blocksList[ThaumCraftCore.aWand.apportBlock], 1, ThaumCraftCore.aWand.apportMeta));
        }
        return true;
    }

    public String getVersion()
    {
        return "1.2.5";
    }

    public void load()
    {
        MinecraftForgeClient.preloadTexture("/thaumcraft/main.png");
        MinecraftForgeClient.preloadTexture("/thaumcraft/lightning_l_purple.png");
        MinecraftForgeClient.preloadTexture("/thaumcraft/lightning_s_black.png");
        MinecraftForgeClient.preloadTexture("/thaumcraft/particles.png");
        MinecraftForge.setToolClass(pickThaumium, "pickaxe", 3);
        MinecraftForge.setToolClass(shovelThaumium, "shovel", 3);
        MinecraftForge.setToolClass(axeThaumium, "axe", 3);
    }

    public void AddRenderer(Map map)
    {
        map.put(thaumcraft.EntityThaumSlime.class, new EntityThaumSlimeRenderer(new ModelSlime(16), new ModelSlime(0), 0.5F));
        map.put(thaumcraft.EntityTravelingTrunk.class, new EntityTrunkRenderer(new ModelTrunk(), 0.5F));
        map.put(thaumcraft.EntityEldritchTree.class, new EntityEldritchTreeRenderer(new ModelETree(0), 0.5F));
        map.put(thaumcraft.EntityHiddenTG.class, new EntityTGRenderer());
        map.put(thaumcraft.EntityWispWild.class, new EntityWispRenderer());
    }

    public void GenerateSurface(World world, Random random, int i, int j)
    {
        GenerateObelisk(world, random, i, j);
    }

    public void GenerateObelisk(World world, Random random, int i, int j)
    {
        int k = i + 8;
        int l = random.nextInt(128);
        int i1 = j + 8;
        try
        {
            if (ThaumCraftCore.inAura(k, l, i1))
            {
                (new WorldGenObelisk()).generate(world, random, k, l, i1);
            }
        }
        catch (NullPointerException nullpointerexception) { }
    }

    public void ModsLoaded()
    {
        System.out.print("[Thaumcraft] Adding support for Forge ore dictionary...");
        System.out.print("[CHAT]<Coder> Hey, support DELETED!!");
        System.out.print("[Thaumcraft] Oh, okay!");
        System.out.println("Done!");
        System.out.print("[Thaumcraft] Adding support for TMI...");
        try
        {
            ThaumCraftCore.hideTMIItems();
        }
        catch (Exception exception)
        {
            System.out.print("TMI not found. ");
        }
        System.out.println("Done!");
    }

    private void initSounds(Minecraft minecraft)
    {
        File file = null;
        File file1 = new File(Minecraft.getMinecraftDir(), "/mods/");
        File afile[] = file1.listFiles();
        Arrays.sort(afile);
        for (int i = 0; i < afile.length;)
        {
            File file2 = afile[i];
            if (!file2.isFile() || !file2.getName().endsWith(".zip") || !file2.isFile())
            {
                continue;
            }
            try
            {
                FileInputStream fileinputstream = new FileInputStream(file2);
                ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
                Object obj = null;
                do
                {
                    ZipEntry zipentry = zipinputstream.getNextEntry();
                    if (zipentry == null)
                    {
                        break;
                    }
                    String s3 = zipentry.getName();
                    if (zipentry.isDirectory() || !s3.startsWith("mod_ThaumCraft") || !s3.endsWith(".class"))
                    {
                        continue;
                    }
                    file = file2;
                    break;
                }
                while (true);
                zipinputstream.close();
                fileinputstream.close();
                continue;
            }
            catch (Exception exception)
            {
                i++;
            }
        }

        String s = "resources/";
        String s1 = "newsound/tcsound/";
        File file3 = new File(minecraft.mcDataDir, (new StringBuilder()).append(s).append(s1).toString());
        if (!file3.exists())
        {
            file3.mkdir();
        }
        String as[] = soundNames;
        for (int j = 0; j < as.length; j++)
        {
            String s2 = as[j];
            File file4 = new File(minecraft.mcDataDir, (new StringBuilder()).append(s).append(s1).append(s2).toString());
            if (file4.exists())
            {
                minecraft.installResource((new StringBuilder()).append(s1).append(s2).toString(), file4);
                continue;
            }
            System.out.print((new StringBuilder()).append("[ThaumCraft] Soundfile ").append(s2).append(" not found, attempting to move to resources folder... ").toString());
            if (file != null && extract(s2, file4, file))
            {
                System.out.println("success!");
                minecraft.installResource((new StringBuilder()).append(s1).append(s2).toString(), file4);
            }
            else
            {
                System.out.println("failed!");
            }
        }
    }

    private boolean extract(String s, File file, File file1)
    {
        try
        {
            ZipFile zipfile = new ZipFile(file1);
            for (Enumeration enumeration = zipfile.entries(); enumeration.hasMoreElements();)
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
                if (zipentry.getName().equals((new StringBuilder()).append("sound/").append(s).toString()))
                {
                    BufferedInputStream bufferedinputstream = new BufferedInputStream(zipfile.getInputStream(zipentry));
                    BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
                    int i;
                    do
                    {
                        i = bufferedinputstream.read();
                        if (i != -1)
                        {
                            bufferedoutputstream.write(i);
                        }
                    }
                    while (i != -1);
                    bufferedoutputstream.close();
                    bufferedinputstream.close();
                    zipfile.close();
                    return true;
                }
            }

            zipfile.close();
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return false;
    }
}
