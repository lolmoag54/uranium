package net.mcreator.uranium;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * ARBRE TECHNOLOGIQUE URANIUM — UN SEUL FICHIER (MC 26.1 / NeoForge 26.1).
 * Place dans src/main/java/net/mcreator/uranium/. Touche K en jeu + bouton ☢ dans l'inventaire.
 * Aucune modification de la classe principale MCreator.
 * Inspiré de SQ-Nuclear (MIT, (c) 2026 SideQuestChris).
 */
public final class UraniumTechTree {
    private UraniumTechTree() {}

    public record Category(String id, String label, int color) {}

    public record TechSystem(String id, String label, int color, String iconItem, int tier) {}

    public record Ing(String itemId, int count) {}

    public record Recipe(String station, String stationItem, List<Ing> inputs, int out, int outMax, String note,
                         int gw, int gh, List<String> grid) {
        public Recipe(String station, String stationItem, List<Ing> inputs, int out, String note) {
            this(station, stationItem, inputs, out, 0, note, 0, 0, List.of());
        }
    }

    public record Drop(String itemId, int min, int max, int chance) {}

    public record Mob(String id, String name, String icon, String info, List<Drop> drops) {}

    public enum StatusFilter {
        ALL("All"),
        AVAILABLE("Avail"),
        UNLOCKED("Done");

        final String label;
        StatusFilter(String label) { this.label = label; }
    }

    public static final class TechTree {
        public static final String MODID = "uranium";

        // Ordre de progression : Radium (1) -> Uranium (2) -> Plutonium (3)
        public static final TechSystem SYS_RADIUM    = new TechSystem("radium",    "Radium",    0xFF47E5A6, "radiferouscrystal", 1);
        public static final TechSystem SYS_URANIUM   = new TechSystem("uranium",   "Uranium",   0xFF7BE83A, "enricheduranium",   2);
        public static final TechSystem SYS_PLUTONIUM = new TechSystem("plutonium", "Plutonium", 0xFFFF6A4D, "plutoniumdioxide",  3);
        public static final List<TechSystem> SYSTEMS = List.of(SYS_RADIUM, SYS_URANIUM, SYS_PLUTONIUM);

        static final Category MINERAI    = new Category("ores",       "Ores",      0xFF2FC4B2);
        static final Category MATIERE    = new Category("materials",  "Materials", 0xFF6FCF3A);
        static final Category METAL      = new Category("metals",     "Metals",    0xFF9AA4B0);
        static final Category MACHINE    = new Category("machines",   "Machines",  0xFFE08A2B);
        static final Category ENERGIE    = new Category("energy",     "Energy",    0xFFF2D13B);
        static final Category FLUIDE     = new Category("fluids",     "Chemicals", 0xFF3AA0E0);
        static final Category NUCLEAIRE  = new Category("nuclear",    "Nuclear",   0xFFE0443A);
        static final Category EQUIPEMENT = new Category("gear",       "Gear",      0xFFA97BEA);

        static final List<Category> CATEGORIES =
                List.of(MINERAI, MATIERE, METAL, MACHINE, ENERGIE, FLUIDE, NUCLEAIRE, EQUIPEMENT);

        public static final Map<String, TechNode> BY_ID = new LinkedHashMap<>();
        private static final Map<String, List<Recipe>> MANUAL = new HashMap<>();
        public static final List<Mob> MOBS = new ArrayList<>();

        static {
            // =========================================================================
            // 1. SYSTÈME RADIUM (rangées 0..4, colonnes 0..8)
            // =========================================================================
            auto("start", "enricheduranium", "Welcome", "Thanks for using our mod.", 0, 1, NUCLEAIRE, SYS_RADIUM);

            // Branche Radium : Pechblende -> Poudre -> Séparateur -> Concentré -> Unité -> Cristalliseur
            add("pitch_ore", "penchblendeore", "Pitchblende ore", "Radioactive ore.", 1, 0, MINERAI, SYS_RADIUM, "start");
            add("pitch", "penchblende", "Pitchblende", "Radioactive mineral. The crusher can grind it.", 2, 0, MATIERE, SYS_RADIUM, "pitch_ore");
            add("powder", "powderedfertilizedsoil", "Pitchblende powder", "Ground pitchblende.", 3, 0, MATIERE, SYS_RADIUM, "pitch", "crusher");
            add("splitter", "splitter", "Mineral separator", "Splits powder into concentrate and rock. Needs FE, heat and water.", 4, 0, MACHINE, SYS_RADIUM, "powder");
            add("conc", "pitchblendeconcentrate", "Pitchblende concentrate", "Concentrated output of the mineral separator.", 5, 0, NUCLEAIRE, SYS_RADIUM, "splitter");
            add("rock", "rochesteril", "Rocky residue", "Mining waste. May still be slightly radioactive.", 5, 1, MATIERE, SYS_RADIUM, "splitter");

            add("closed", "closedindustrialprocessingunit", "Industrial processing unit", "Heavy industrial machine.", 6, 0, MACHINE, SYS_RADIUM, "conc");
            add("radiferous_conc", "radiferousconcentrate", "Radiferous concentrate", "Pitchblende concentrate refined in the industrial processing unit.", 7, 0, NUCLEAIRE, SYS_RADIUM, "closed");
            add("silicon", "silicon", "Silicon", "Made from sand in the industrial processing unit.", 7, 1, MATIERE, SYS_RADIUM, "closed");
            add("ram", "random_acces_memory", "Random access memory", "Electronic memory component.", 8, 1, MACHINE, SYS_RADIUM, "silicon");

            // Fioles & Cristallisation
            add("empty_vial", "emptyvial", "Empty vial", "Glass sample vial for chemical handling.", 1, 2, MATIERE, SYS_RADIUM, "start");
            add("sterile_vial", "sterilized_water_vial", "Sterilized water vial", "Sterile water vial.", 2, 2, FLUIDE, SYS_RADIUM, "empty_vial");
            add("liquified_conc", "liquified_radiferouscencentrate", "Liquified radiferous concentrate", "Concentrate dissolved in a water vial (brewing stand).", 7, 2, FLUIDE, SYS_RADIUM, "radiferous_conc", "empty_vial");
            add("crystallizer", "crystallizer", "Crystallizer", "Grows crystalline compounds from processed materials.", 6, 2, MACHINE, SYS_RADIUM, "conc", "closed");
            add("crystal_base", "crystaliserbase", "Crystallizer base", "Seed base consumed by the crystallizer.", 7, 3, MATIERE, SYS_RADIUM, "crystallizer");
            add("radiferous_crystal", "radiferouscrystal", "Radiferous crystal", "Radioactive crystal grown from liquified concentrate.", 8, 2, NUCLEAIRE, SYS_RADIUM, "crystallizer", "liquified_conc", "crystal_base");

            // Portail Wasteland
            add("portal", "radioactivewasteland", "Wasteland portal igniter", "Opens the portal to the radioactive wasteland.", 1, 4, NUCLEAIRE, SYS_RADIUM, "start");

            // =========================================================================
            // 2. SYSTÈME URANIUM (rangées 6..18, colonnes 1..11)
            // =========================================================================
            // Minerais de base (colonne 1)
            add("raw", "rawuranium", "Raw uranium ore", "Mine raw uranium ore.", 1, 7, MINERAI, SYS_URANIUM, "start");
            add("raw_deep", "rawuranium_deepslate", "Deepslate uranium ore", "Raw uranium ore found in deepslate.", 1, 8, MINERAI, SYS_URANIUM, "start");
            add("fluorite", "fluorite_ore", "Fluorite ore", "Mine fluorite to get fluoride.", 1, 10, MINERAI, SYS_URANIUM, "start");
            add("fluorite_deep", "deepslate_fluorite_ore", "Deepslate fluorite ore", "Fluorite ore found in deepslate.", 1, 11, MINERAI, SYS_URANIUM, "start");
            add("leadore", "leadore", "Lead ore", "Needed for pellets and the reactor.", 1, 13, MINERAI, SYS_URANIUM, "start");
            add("steel", "steel", "Steel", "Strong alloy.", 1, 15, METAL, SYS_URANIUM, "start");
            add("zicron_ore", "zicron_ore", "Zicron ore", "Source of raw zirconium.", 1, 17, MINERAI, SYS_URANIUM, "start");
            add("solar", "solarpanel", "Solar panel", "Begin renewable energy.", 1, 18, ENERGIE, SYS_URANIUM, "start");

            // Traitement Uranium -> Yellowcake -> UF6 (rangées 7..8)
            add("crusher", "crusher", "Crusher", "Crushes raw uranium ore.", 2, 7, MACHINE, SYS_URANIUM, "raw", "raw_deep");
            add("crushed", "cruched_rawuranium", "Crushed uranium", "Output of the crusher.", 3, 7, MATIERE, SYS_URANIUM, "crusher");
            add("sulfuric", "sulfuricacid_bucket", "Sulfuric acid", "Corrosive liquid bucket used to treat crushed uranium.", 3, 8, FLUIDE, SYS_URANIUM, "start");
            add("yellowcake", "yellowcake", "Yellowcake", "Crushed uranium treated with sulfuric acid.", 4, 7, MATIERE, SYS_URANIUM, "crushed", "sulfuric");

            // Fluor & UF6 (rangées 10..11)
            add("fluoride", "fluoride", "Fluoride", "Found in fluorite ore.", 2, 10, MATIERE, SYS_URANIUM, "fluorite", "fluorite_deep");
            add("fluor", "fluor", "Fluor", "Smelted from fluoride.", 3, 10, MATIERE, SYS_URANIUM, "fluoride");
            add("hexa", "uraniumhexafluoride", "Uranium hexafluoride", "Yellowcake + fluor, ready to enrich.", 5, 8, NUCLEAIRE, SYS_URANIUM, "yellowcake", "fluor");

            // Énergie & Réseau (rangée 18)
            add("cable", "cable", "Cables", "Carry energy between machines.", 2, 18, ENERGIE, SYS_URANIUM, "solar");
            add("battery", "grossebatterie", "Heavy battery", "Stores a large amount of energy.", 3, 18, ENERGIE, SYS_URANIUM, "cable");
            add("wind", "windturbine", "Wind turbine", "Generates energy from the wind.", 4, 18, ENERGIE, SYS_URANIUM, "cable");
            add("processor", "prossesor", "Processor", "Electronic control component.", 3, 17, MACHINE, SYS_URANIUM, "cable", "steel");

            // Plomb, Zicronium & Refroidissement (rangées 13..16)
            add("lead", "lingotdeplomb", "Lead ingot", "Smelted lead.", 2, 13, METAL, SYS_URANIUM, "leadore");
            add("nugget", "pepitedeplomb", "Lead nugget", "Small piece of lead.", 3, 12, METAL, SYS_URANIUM, "lead");
            add("lead_block", "blockdeplomb", "Lead block", "Compact block of lead ingots.", 3, 13, METAL, SYS_URANIUM, "lead");
            add("lead_table", "leadcraftingtable", "Lead crafting table", "Crafting station with its own recipes.", 3, 14, MACHINE, SYS_URANIUM, "lead");
            add("concrete", "reinforcedconcrete", "Reinforced concrete", "Reactor shielding.", 4, 13, METAL, SYS_URANIUM, "lead");

            add("coolantpipe", "coolantpipe", "Coolant pipe", "Carries coolant to the reactor.", 2, 15, MACHINE, SYS_URANIUM, "steel");
            add("waterpump", "waterpump", "Water pump", "Pumps water for cooling.", 2, 16, MACHINE, SYS_URANIUM, "steel");
            add("water_vial", "watervial", "Water vial", "Vial filled with water.", 3, 2, FLUIDE, SYS_URANIUM, "empty_vial", "waterpump");
            add("hammer", "steel_hammer", "Steel hammer", "Used to press pellets.", 3, 15, EQUIPEMENT, SYS_URANIUM, "steel", "lead");

            add("zicron", "zicron", "Zicron", "Raw zirconium.", 2, 17, MATIERE, SYS_URANIUM, "zicron_ore");
            add("zicronium", "zicronium", "Zicronium", "Cladding for fuel rods.", 3, 16, METAL, SYS_URANIUM, "zicron");

            // Centrifugeuse, Enrichissement, Réacteur & Déchets (colonnes 6..11)
            add("centrifuge", "centrifuge", "Centrifuge", "Separates enriched from depleted uranium.", 6, 8, MACHINE, SYS_URANIUM, "hexa", "cable");
            add("enriched", "enricheduranium", "Enriched uranium", "Fissile uranium.", 7, 9, NUCLEAIRE, SYS_URANIUM, "centrifuge");
            add("depleted", "depleteduranium", "Depleted uranium", "Dense by-product of enrichment.", 7, 6, NUCLEAIRE, SYS_URANIUM, "centrifuge");

            add("pellet", "uraniumpellet", "Uranium pellet", "Pressed enriched uranium.", 8, 10, NUCLEAIRE, SYS_URANIUM, "enriched", "hammer");
            add("fuelrod", "fuelrod", "Fuel rod", "Pellets in a zicronium cladding.", 9, 11, NUCLEAIRE, SYS_URANIUM, "pellet", "zicronium");
            add("reactor", "reactor", "Fission reactor", "We need a reactor now?", 10, 12, NUCLEAIRE, SYS_URANIUM, "fuelrod", "concrete");
            add("spent", "spentfuelrod", "Spent fuel rod", "Waste from the reactor.", 11, 11, NUCLEAIRE, SYS_URANIUM, "reactor");

            // Outils & Armures Uranium appauvri (colonne 8, rangées 4..8)
            add("u_pickaxe", "uranium_pickaxe", "Uranium pickaxe", "Uranium pickaxe.", 8, 4, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("u_axe", "uranium_axe", "Uranium axe", "Uranium axe.", 8, 5, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("u_sword", "uranium_sword", "Uranium sword", "Uranium sword.", 8, 6, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("u_shovel", "uranium_shovel", "Uranium shovel", "Uranium shovel.", 8, 7, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("u_hoe", "uranium_hoe", "Uranium hoe", "Uranium hoe.", 8, 8, EQUIPEMENT, SYS_URANIUM, "depleted");

            // Armures Uranium (colonne 8, rangées 14..17)
            add("ua_helmet", "uranium_armor_helmet", "Uranium helmet", "Uranium armor piece.", 8, 14, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("ua_chestplate", "uranium_armor_chestplate", "Uranium chestplate", "Uranium armor piece.", 8, 15, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("ua_leggings", "uranium_armor_leggings", "Uranium leggings", "Uranium armor piece.", 8, 16, EQUIPEMENT, SYS_URANIUM, "depleted");
            add("ua_boots", "uranium_armor_boots", "Uranium boots", "Uranium armor piece.", 8, 17, EQUIPEMENT, SYS_URANIUM, "depleted");

            // Uranium isolé & Armures isolées (colonnes 8..9, rangées 9..13)
            add("insulated", "insulateduranium", "Insulated uranium", "Shielded uranium for insulated armor.", 8, 9, MATIERE, SYS_URANIUM, "enriched");
            add("ia_helmet", "enriched_uranium_armor_armor_helmet", "Insulated helmet", "Insulated uranium armor piece.", 9, 9, EQUIPEMENT, SYS_URANIUM, "insulated");
            add("ia_chestplate", "enriched_uranium_armor_armor_chestplate", "Insulated chestplate", "Insulated uranium armor piece.", 9, 10, EQUIPEMENT, SYS_URANIUM, "insulated");
            add("ia_leggings", "enriched_uranium_armor_armor_leggings", "Insulated leggings", "Insulated uranium armor piece.", 9, 11, EQUIPEMENT, SYS_URANIUM, "insulated");
            add("ia_boots", "enriched_uranium_armor_armor_boots", "Insulated boots", "Insulated uranium armor piece.", 9, 12, EQUIPEMENT, SYS_URANIUM, "insulated");

            // =========================================================================
            // 3. SYSTÈME PLUTONIUM (colonnes 12..18, rangées 8..13)
            // =========================================================================
            add("cooling_pool", "coolingpool", "Cooling pool", "Cools spent fuel rods in water.", 12, 8, MACHINE, SYS_PLUTONIUM, "spent");
            add("cooled", "cooledspentfuelrod", "Cooled spent fuel rod", "Left to decay in a cooling pool.", 13, 8, NUCLEAIRE, SYS_PLUTONIUM, "cooling_pool");

            add("nitric", "nitricacid", "Nitric acid", "Dissolves spent fuel (PUREX process).", 12, 10, FLUIDE, SYS_PLUTONIUM, "spent");
            add("tbp", "tbpsolvent", "TBP solvent", "Tributyl phosphate in kerosene. Separates uranium, plutonium and waste.", 12, 11, FLUIDE, SYS_PLUTONIUM, "spent");
            add("oxalic", "oxalicacid", "Oxalic acid", "Precipitates plutonium as oxalate.", 12, 12, FLUIDE, SYS_PLUTONIUM, "spent");

            add("reprocessing", "reprocessingplant", "Reprocessing plant", "PUREX: rod + nitric acid + TBP give plutonium, uranium and waste.", 14, 10, MACHINE, SYS_PLUTONIUM, "cooled", "nitric", "tbp");
            add("nitrate", "plutoniumnitrate", "Plutonium nitrate", "Plutonium nitrate solution. Precipitate and calcine it.", 15, 11, NUCLEAIRE, SYS_PLUTONIUM, "reprocessing");
            add("waste", "fissionwaste", "Fission waste", "Highly radioactive.", 15, 9, NUCLEAIRE, SYS_PLUTONIUM, "reprocessing");

            add("calcination", "calcinationfurnace", "Calcination furnace", "Calcines plutonium nitrate with oxalic acid.", 16, 11, MACHINE, SYS_PLUTONIUM, "nitrate", "oxalic");
            add("dioxide", "plutoniumdioxide", "Plutonium dioxide", "PuO2 powder. For MOX fuel or cores.", 17, 11, NUCLEAIRE, SYS_PLUTONIUM, "calcination");
            add("mox", "moxfuelrod", "MOX fuel rod", "Reactor fuel: +50% power, runs hotter.", 18, 12, NUCLEAIRE, SYS_PLUTONIUM, "dioxide", "fuelrod");
            add("core", "plutoniumcore", "Plutonium core", "Fissile core.", 18, 10, NUCLEAIRE, SYS_PLUTONIUM, "dioxide");

            // --- Procédés des machines ---
            process("crushed", "crusher", 2, "1 second per item. Uses energy.", ing("rawuranium", 1));
            process("crushed", "crusher", 2, "1 second per item. Uses energy.", ing("rawuranium_deepslate", 1));
            process("powder", "crusher", 0, "", ing("penchblende", 1));
            process("enriched", "centrifuge", 1, "20% chance per cycle (2 s). Every cycle also gives 1 depleted uranium.", ing("uraniumhexafluoride", 1));
            process("depleted", "centrifuge", 1, "1 per cycle (2 s). 20% of the cycles also give 1 enriched uranium.", ing("uraniumhexafluoride", 1));
            process("conc", "splitter", 0, "", ing("powderedfertilizedsoil", 1));
            process("rock", "splitter", 0, "", ing("powderedfertilizedsoil", 1));
            process("cooled", "cooling_pool", 0, "Needs water.", ing("spentfuelrod", 1));
            process("nitrate", "reprocessing", 0, "", ing("cooledspentfuelrod", 1), ing("nitricacid", 1), ing("tbpsolvent", 1));
            process("waste", "reprocessing", 0, "", ing("cooledspentfuelrod", 1), ing("nitricacid", 1), ing("tbpsolvent", 1));
            process("dioxide", "calcination", 0, "", ing("plutoniumnitrate", 1), ing("oxalicacid", 1));
            process("radiferous_conc", "closed", 0, "", ing("pitchblendeconcentrate", 1));
            process("silicon", "closed", 0, "", ing("minecraft:sand", 1));
            process("radiferous_crystal", "crystallizer", 0, "", ing("liquified_radiferouscencentrate", 1), ing("crystaliserbase", 1));
            addManual("liquified_conc", new Recipe("Brewing stand", "minecraft:brewing_stand",
                    List.of(ing("watervial", 1), ing("radiferousconcentrate", 1)), 1, "Brewing recipe."));
            station("hexa", "Brewing stand", "minecraft:brewing_stand", "Brewing recipe.");

            // --- Almanach des mobs (touche M) ---
            mob("poissonchatte", "Mutated catfish", "poissonchatte_spawn_egg", "Aquatic mob.");
            mob("crystalstag", "Crystal stag", "crystalstagspawnegg", "");
        }

        private static void add(String id, String item, String title, String desc, int col, int row, Category cat, TechSystem sys, String... parents) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, cat, sys, List.of(parents), false));
        }

        private static void auto(String id, String item, String title, String desc, int col, int row, Category cat, TechSystem sys) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, cat, sys, List.of(), true));
        }

        private static Ing ing(String item, int count) { return new Ing(item, count); }

        private static Drop drop(String item, int min, int max, int chance) { return new Drop(item, min, max, chance); }

        private static void mob(String id, String name, String icon, String info, Drop... drops) {
            MOBS.add(new Mob(id, name, icon, info, List.of(drops)));
        }

        private static void addManual(String nodeId, Recipe r) {
            MANUAL.computeIfAbsent(nodeId, k -> new ArrayList<>()).add(r);
        }

        private static void process(String nodeId, String machineId, int out, String note, Ing... inputs) {
            TechNode m = BY_ID.get(machineId);
            addManual(nodeId, new Recipe(m == null ? machineId : m.title(), m == null ? null : m.itemId(), List.of(inputs), out, note));
        }

        private static void station(String nodeId, String name, String stationItem, String note) {
            addManual(nodeId, new Recipe(name, stationItem, List.of(), 0, note));
        }

        public static String full(String id) { return full(id, MODID); }

        public static String full(String id, String ns) { return id.contains(":") ? id : ns + ":" + id; }

        public static Item resolve(String itemId) {
            if (itemId == null || itemId.startsWith("#")) return null;
            Identifier rl = Identifier.tryParse(full(itemId));
            return rl == null ? null : BuiltInRegistries.ITEM.getOptional(rl).orElse(null);
        }

        public static TechNode nodeOfItem(String itemId) {
            String f = full(itemId);
            for (TechNode n : BY_ID.values()) if (!n.auto() && full(n.itemId()).equals(f)) return n;
            return null;
        }

        public static List<Recipe> recipesOf(String nodeId) {
            List<Recipe> l = new ArrayList<>();
            List<Recipe> m = MANUAL.get(nodeId);
            if (m != null) l.addAll(m);
            RecipeIndex.load();
            List<Recipe> j = RecipeIndex.BY_NODE.get(nodeId);
            if (j != null) l.addAll(j);
            return l;
        }

        public static List<TechNode> usedIn(String nodeId) {
            RecipeIndex.load();
            Set<String> ids = new LinkedHashSet<>();
            Set<String> a = RecipeIndex.USED_IN.get(nodeId);
            if (a != null) ids.addAll(a);
            TechNode me = BY_ID.get(nodeId);
            if (me != null) {
                String mine = full(me.itemId());
                for (Map.Entry<String, List<Recipe>> e : MANUAL.entrySet()) {
                    for (Recipe r : e.getValue()) {
                        for (Ing in : r.inputs()) if (full(in.itemId()).equals(mine)) ids.add(e.getKey());
                        if (r.stationItem() != null && full(r.stationItem()).equals(mine)) ids.add(e.getKey());
                    }
                }
            }
            ids.remove(nodeId);
            List<TechNode> out = new ArrayList<>();
            for (String id : ids) {
                TechNode n = BY_ID.get(id);
                if (n != null) out.add(n);
            }
            return out;
        }

        public static List<Mob> mobsDropping(String itemId) {
            List<Mob> l = new ArrayList<>();
            String mine = full(itemId);
            for (Mob m : MOBS) {
                for (Drop d : m.drops()) if (full(d.itemId()).equals(mine)) { l.add(m); break; }
            }
            return l;
        }

        public static List<TechNode> childrenOf(String id) {
            List<TechNode> l = new ArrayList<>();
            for (TechNode n : BY_ID.values()) if (n.parents().contains(id)) l.add(n);
            return l;
        }

        public static Set<String> pathTo(String id) {
            Set<String> out = new LinkedHashSet<>();
            Deque<String> todo = new ArrayDeque<>();
            todo.add(id);
            while (!todo.isEmpty()) {
                String c = todo.poll();
                if (!out.add(c)) continue;
                TechNode n = BY_ID.get(c);
                if (n != null) todo.addAll(n.parents());
            }
            return out;
        }

        static int depth(String id, Map<String, Integer> memo, Set<String> visiting) {
            Integer m = memo.get(id);
            if (m != null) return m;
            TechNode n = BY_ID.get(id);
            if (n == null || n.parents().isEmpty() || !visiting.add(id)) return 0;
            int d = 0;
            for (String p : n.parents()) d = Math.max(d, 1 + depth(p, memo, visiting));
            visiting.remove(id);
            memo.put(id, d);
            return d;
        }

        private TechTree() {}
    }

    public record TechNode(String id, String itemId, String title, String desc,
                           int col, int row, Category cat, TechSystem system, List<String> parents, boolean auto) {
        public int color() { return cat.color(); }
        public Item item() { return TechTree.resolve(itemId); }
    }

    static final class RecipeIndex {
        static final String[] RECIPE_FILES = {
            "blocklingo", "lingoblock", "lingopepite", "pepitelingo",
            "enricheduraniumrecipe", "fuelrodrecipe", "nuggettoingot_lead",
            "ingottoblock_lead", "yellowcakerecipe", "fluorrecipe",
            "zicroniumrecipe", "uranium_pellet_recipe", "reinforcedconcreterecipe",
            "leadcraftingtablerecipe", "uranium_pickaxe_recipe", "uranium_axe_recipe",
            "uranium_sword_recipe", "uranium_shovel_recipe", "uranium_hoe_recipe",
            "uranium_armor_helmet_recipe", "uranium_armor_chestplate_recipe",
            "uranium_armor_leggings_recipe", "uranium_armor_boots_recipe",
            "enriched_uranium_armor_armor_helmet_recipe",
            "enriched_uranium_armor_armor_chestplate_recipe",
            "enriched_uranium_armor_armor_leggings_recipe",
            "enriched_uranium_armor_armor_boots_recipe",
            "insulateduraniumrecipe", "coolantpiperecipe", "waterpumprecipe",
            "nitricacidrecipe", "tbpsolventrecipe", "oxalicacidrecipe",
            "moxfuelrodrecipe", "plutoniumcorerecipe", "coolingpoolrecipe",
            "reprocessingplantrecipe", "calcinationfurnacerecipe"
        };

        private static final Map<String, String> TAG_REP = Map.ofEntries(
                Map.entry("minecraft:planks", "minecraft:oak_planks"), Map.entry("minecraft:logs", "minecraft:oak_log"),
                Map.entry("minecraft:logs_that_burn", "minecraft:oak_log"), Map.entry("minecraft:coals", "minecraft:coal"),
                Map.entry("minecraft:sand", "minecraft:sand"), Map.entry("minecraft:wool", "minecraft:white_wool"),
                Map.entry("minecraft:stone_crafting_materials", "minecraft:cobblestone"),
                Map.entry("minecraft:wooden_slabs", "minecraft:oak_slab"),
                Map.entry("c:ingots/iron", "minecraft:iron_ingot"), Map.entry("c:ingots/gold", "minecraft:gold_ingot"),
                Map.entry("c:ingots/copper", "minecraft:copper_ingot"), Map.entry("c:gems/diamond", "minecraft:diamond"),
                Map.entry("c:dusts/redstone", "minecraft:redstone"), Map.entry("c:stones", "minecraft:stone"),
                Map.entry("c:cobblestones", "minecraft:cobblestone"), Map.entry("c:sands", "minecraft:sand"),
                Map.entry("c:glass_blocks", "minecraft:glass"), Map.entry("c:rods/wooden", "minecraft:stick"),
                Map.entry("c:nuggets/iron", "minecraft:iron_nugget"), Map.entry("c:storage_blocks/iron", "minecraft:iron_block"));

        static final Map<String, List<Recipe>> BY_NODE = new HashMap<>();
        static final Map<String, Set<String>> USED_IN = new HashMap<>();
        private static boolean loaded;

        static synchronized void load() {
            if (loaded) return;
            loaded = true;
            Map<String, String> itemToNode = new HashMap<>();
            for (TechNode n : TechTree.BY_ID.values()) if (!n.auto()) itemToNode.put(TechTree.full(n.itemId()), n.id());
            for (String f : RECIPE_FILES) {
                try {
                    parseRecipe(Json.parse(read("data/" + TechTree.MODID + "/recipe/" + f + ".json")), itemToNode);
                } catch (RuntimeException ignored) {}
            }
            for (TechNode ore : TechTree.BY_ID.values()) {
                if (ore.cat() != TechTree.MINERAI) continue;
                try {
                    Object o = Json.parse(read("data/" + TechTree.MODID + "/loot_table/blocks/" + ore.itemId() + ".json"));
                    if (o == null) continue;
                    List<Drop> drops = new ArrayList<>();
                    collectDrops(o, drops);
                    for (Drop d : drops) {
                        String nid = itemToNode.get(TechTree.full(d.itemId(), "minecraft"));
                        if (nid == null || nid.equals(ore.id())) continue;
                        Recipe r = new Recipe("Mining", "minecraft:iron_pickaxe",
                                List.of(new Ing(TechTree.full(ore.itemId()), 1)), d.min(), d.max(), "Mine the ore with a pickaxe.", 0, 0, List.of());
                        BY_NODE.computeIfAbsent(nid, k -> new ArrayList<>()).add(r);
                        USED_IN.computeIfAbsent(ore.id(), k -> new LinkedHashSet<>()).add(nid);
                    }
                } catch (RuntimeException ignored) {}
            }
        }

        private static String read(String path) {
            for (int k = 0; k < 3; k++) {
                try (InputStream in = k == 0 ? UraniumTechTree.class.getResourceAsStream("/" + path)
                        : k == 1 ? UraniumTechTree.class.getClassLoader().getResourceAsStream(path)
                        : Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
                    if (in != null) return new String(in.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException | RuntimeException ignored) {}
            }
            return null;
        }

        private static void parseRecipe(Object root, Map<String, String> itemToNode) {
            Map<String, Object> r = Json.obj(root);
            if (r == null) return;
            String type = Json.str(r.get("type"));
            type = type == null ? "" : type.substring(type.indexOf(':') + 1);
            String[] res = result(r.get("result"));
            if (res == null) return;
            String nodeId = itemToNode.get(res[0]);
            if (nodeId == null) return;
            int out = Integer.parseInt(res[1]);
            String station = "", stationItem = null, note = "";
            int gw = 0, gh = 0;
            List<String> grid = List.of();
            Map<String, Integer> tot = new LinkedHashMap<>();

            if (type.equals("crafting_shaped")) {
                station = "Crafting table";
                stationItem = "minecraft:crafting_table";
                List<Object> pat = Json.list(r.get("pattern"));
                Map<String, Object> key = Json.obj(r.get("key"));
                if (pat == null || key == null) return;
                List<String> rows = new ArrayList<>();
                for (Object o : pat) rows.add(String.valueOf(o));
                gh = rows.size();
                for (String row : rows) gw = Math.max(gw, row.length());
                List<String> cells = new ArrayList<>();
                for (String row : rows) {
                    for (int x = 0; x < gw; x++) {
                        char c = x < row.length() ? row.charAt(x) : ' ';
                        String id = c == ' ' ? "" : ingredient(key.get(String.valueOf(c)));
                        if (id == null) id = "";
                        cells.add(id);
                        if (!id.isEmpty()) tot.merge(id, 1, Integer::sum);
                    }
                }
                grid = cells;
            } else if (type.equals("crafting_shapeless")) {
                station = "Crafting table";
                stationItem = "minecraft:crafting_table";
                List<Object> ings = Json.list(r.get("ingredients"));
                if (ings == null) return;
                for (Object o : ings) {
                    String id = ingredient(o);
                    if (id != null) tot.merge(id, 1, Integer::sum);
                }
            } else if (type.equals("smelting") || type.equals("blasting") || type.equals("smoking") || type.equals("campfire_cooking")) {
                switch (type) {
                    case "smelting" -> { station = "Furnace"; stationItem = "minecraft:furnace"; }
                    case "blasting" -> { station = "Blast furnace"; stationItem = "minecraft:blast_furnace"; }
                    case "smoking" -> { station = "Smoker"; stationItem = "minecraft:smoker"; }
                    default -> { station = "Campfire"; stationItem = "minecraft:campfire"; }
                }
                String id = ingredient(r.get("ingredient"));
                if (id == null) return;
                tot.put(id, 1);
                double ticks = Json.num(r.get("cookingtime"), 0);
                if (ticks > 0) note = "Cooking time: " + Math.round(ticks / 2.0) / 10.0 + " s.";
            } else if (type.equals("stonecutting")) {
                station = "Stonecutter";
                stationItem = "minecraft:stonecutter";
                String id = ingredient(r.get("ingredient"));
                if (id == null) return;
                tot.put(id, 1);
            } else if (type.equals("smithing_transform")) {
                station = "Smithing table";
                stationItem = "minecraft:smithing_table";
                for (String k : new String[] { "template", "base", "addition" }) {
                    String id = ingredient(r.get(k));
                    if (id != null) tot.merge(id, 1, Integer::sum);
                }
            } else {
                return;
            }

            List<Ing> inputs = new ArrayList<>();
            for (Map.Entry<String, Integer> e : tot.entrySet()) inputs.add(new Ing(e.getKey(), e.getValue()));
            BY_NODE.computeIfAbsent(nodeId, k -> new ArrayList<>()).add(new Recipe(station, stationItem, inputs, out, 0, note, gw, gh, grid));
            for (Ing in : inputs) {
                String src = itemToNode.get(in.itemId());
                if (src != null && !src.equals(nodeId)) USED_IN.computeIfAbsent(src, k -> new LinkedHashSet<>()).add(nodeId);
            }
        }

        private static String[] result(Object o) {
            String id;
            int count = 1;
            if (o instanceof String s) {
                id = s;
            } else {
                Map<String, Object> m = Json.obj(o);
                if (m == null) return null;
                id = Json.str(m.get("id"));
                if (id == null) id = Json.str(m.get("item"));
                count = (int) Json.num(m.get("count"), 1);
            }
            return id == null ? null : new String[] { TechTree.full(id, "minecraft"), String.valueOf(Math.max(1, count)) };
        }

        private static String ingredient(Object o) {
            if (o instanceof String s) return s.startsWith("#") ? tag(s.substring(1)) : TechTree.full(s, "minecraft");
            List<Object> l = Json.list(o);
            if (l != null) {
                for (Object e : l) {
                    String x = ingredient(e);
                    if (x != null) return x;
                }
                return null;
            }
            Map<String, Object> m = Json.obj(o);
            if (m != null) {
                if (m.get("item") instanceof String it) return TechTree.full(it, "minecraft");
                if (m.get("tag") instanceof String t) return tag(t);
            }
            return null;
        }

        private static String tag(String t) {
            String full = t.contains(":") ? t : "minecraft:" + t;
            String rep = TAG_REP.get(full);
            return rep != null ? rep : "#" + full;
        }

        private static void collectDrops(Object node, List<Drop> out) {
            Map<String, Object> m = Json.obj(node);
            if (m != null) {
                String type = Json.str(m.get("type")), name = Json.str(m.get("name"));
                if (name != null && type != null && type.endsWith("item")) {
                    int min = 1, max = 1;
                    List<Object> fns = Json.list(m.get("functions"));
                    if (fns != null) {
                        for (Object f : fns) {
                            Map<String, Object> fm = Json.obj(f);
                            String fn = fm == null ? null : Json.str(fm.get("function"));
                            if (fn == null || !fn.endsWith("set_count")) continue;
                            Object c = fm.get("count");
                            Map<String, Object> cm = Json.obj(c);
                            if (cm != null && cm.containsKey("min")) {
                                min = (int) Json.num(cm.get("min"), 1);
                                max = (int) Json.num(cm.get("max"), min);
                            } else if (cm != null && cm.containsKey("value")) {
                                min = max = (int) Json.num(cm.get("value"), 1);
                            } else if (c instanceof Double d) {
                                min = max = (int) Math.round(d);
                            }
                        }
                    }
                    out.add(new Drop(name, min, Math.max(min, max), 100));
                }
                for (Object v : m.values()) collectDrops(v, out);
            } else {
                List<Object> l = Json.list(node);
                if (l != null) for (Object v : l) collectDrops(v, out);
            }
        }

        private RecipeIndex() {}
    }

    static final class Json {
        private final String s;
        private int i;

        private Json(String s) { this.s = s; }

        static Object parse(String text) {
            if (text == null) return null;
            if (text.startsWith("\uFEFF")) text = text.substring(1);
            try { return new Json(text).value(); } catch (RuntimeException e) { return null; }
        }

        private void ws() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }

        private void expect(char c) { if (s.charAt(i++) != c) throw new IllegalStateException("json"); }

        private Object value() {
            ws();
            char c = s.charAt(i);
            if (c == '{') {
                Map<String, Object> m = new LinkedHashMap<>();
                i++; ws();
                if (s.charAt(i) == '}') { i++; return m; }
                while (true) {
                    ws(); String k = string(); ws(); expect(':');
                    m.put(k, value()); ws();
                    char d = s.charAt(i++);
                    if (d == '}') return m;
                    if (d != ',') throw new IllegalStateException("json");
                }
            }
            if (c == '[') {
                List<Object> l = new ArrayList<>();
                i++; ws();
                if (s.charAt(i) == ']') { i++; return l; }
                while (true) {
                    l.add(value()); ws();
                    char d = s.charAt(i++);
                    if (d == ']') return l;
                    if (d != ',') throw new IllegalStateException("json");
                }
            }
            if (c == '"') return string();
            if (s.startsWith("true", i)) { i += 4; return Boolean.TRUE; }
            if (s.startsWith("false", i)) { i += 5; return Boolean.FALSE; }
            if (s.startsWith("null", i)) { i += 4; return null; }
            int st = i;
            while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
            return Double.valueOf(s.substring(st, i));
        }

        private String string() {
            expect('"');
            StringBuilder b = new StringBuilder();
            while (true) {
                char c = s.charAt(i++);
                if (c == '"') return b.toString();
                if (c != '\\') { b.append(c); continue; }
                char e = s.charAt(i++);
                switch (e) {
                    case 'n' -> b.append('\n');
                    case 't' -> b.append('\t');
                    case 'r' -> b.append('\r');
                    case 'b' -> b.append('\b');
                    case 'f' -> b.append('\f');
                    case 'u' -> { b.append((char) Integer.parseInt(s.substring(i, i + 4), 16)); i += 4; }
                    default -> b.append(e);
                }
            }
        }

        @SuppressWarnings("unchecked")
        static Map<String, Object> obj(Object o) { return o instanceof Map ? (Map<String, Object>) o : null; }

        @SuppressWarnings("unchecked")
        static List<Object> list(Object o) { return o instanceof List ? (List<Object>) o : null; }

        static String str(Object o) { return o instanceof String ? (String) o : null; }

        static double num(Object o, double def) { return o instanceof Double ? (Double) o : def; }
    }

    public static final class TechTreeCommon {
        public static final Set<String> CLIENT_UNLOCKED = new HashSet<>();
        public static int CLIENT_NEW = 0;
        static boolean CLIENT_SYNCED = false;

        private static final Codec<Set<String>> SET_CODEC =
                Codec.STRING.listOf().xmap(l -> (Set<String>) new LinkedHashSet<>(l), s -> new ArrayList<>(s));

        public static final AttachmentType<Set<String>> UNLOCKED =
                AttachmentType.<Set<String>>builder(() -> new LinkedHashSet<String>())
                        .serialize(SET_CODEC.fieldOf("v")).copyOnDeath().build();

        public record SyncPayload(List<String> unlocked) implements CustomPacketPayload {
            public static final Type<SyncPayload> TYPE =
                    new Type<>(Identifier.fromNamespaceAndPath(TechTree.MODID, "tech_sync"));
            public static final StreamCodec<ByteBuf, SyncPayload> CODEC =
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(SyncPayload::new, SyncPayload::unlocked);

            @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
        }

        static void sync(ServerPlayer p) {
            PacketDistributor.sendToPlayer(p, new SyncPayload(new ArrayList<>(p.getData(UNLOCKED))));
        }

        @EventBusSubscriber(modid = TechTree.MODID)
        public static final class ModBus {
            @SubscribeEvent
            public static void registerAttachment(RegisterEvent e) {
                e.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, h ->
                        h.register(Identifier.fromNamespaceAndPath(TechTree.MODID, "tech_unlocked"), UNLOCKED));
            }

            @SubscribeEvent
            public static void registerPayload(RegisterPayloadHandlersEvent e) {
                e.registrar("1").playToClient(SyncPayload.TYPE, SyncPayload.CODEC, (payload, ctx) ->
                        ctx.enqueueWork(() -> {
                            Set<String> before = new HashSet<>(CLIENT_UNLOCKED);
                            CLIENT_UNLOCKED.clear();
                            CLIENT_UNLOCKED.addAll(payload.unlocked());
                            if (CLIENT_SYNCED) {
                                for (String id : CLIENT_UNLOCKED) if (!before.contains(id)) CLIENT_NEW++;
                            }
                            CLIENT_SYNCED = true;
                        }));
            }
        }

        @EventBusSubscriber(modid = TechTree.MODID)
        public static final class GameBus {
            @SubscribeEvent
            public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
                if (e.getEntity() instanceof ServerPlayer sp) sync(sp);
            }

            @SubscribeEvent
            public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
                if (e.getEntity() instanceof ServerPlayer sp) sync(sp);
            }

            @SubscribeEvent
            public static void onTick(PlayerTickEvent.Post e) {
                if (!(e.getEntity() instanceof ServerPlayer sp) || sp.tickCount % 20 != 0) return;
                Set<String> have = new LinkedHashSet<>(sp.getData(UNLOCKED));
                boolean changed = false;
                for (TechNode n : TechTree.BY_ID.values()) {
                    if (have.contains(n.id())) continue;
                    Item it = n.item();
                    if (n.auto() || (it != null && sp.getInventory().contains(st -> st.is(it)))) {
                        have.add(n.id());
                        changed = true;
                        sp.sendOverlayMessage(Component.literal("§aTech unlocked: §f" + n.title()));
                    }
                }
                if (changed) {
                    sp.setData(UNLOCKED, have);
                    sync(sp);
                }
            }
        }

        private TechTreeCommon() {}
    }

    public static final class TechTreeClient {
        static final KeyMapping KEY = new KeyMapping("key." + TechTree.MODID + ".tech_tree",
                InputConstants.KEY_K, KeyMapping.Category.MISC);

        private static Button invButton;
        private static Screen invOwner;

        public static void open() { open(null); }

        public static void open(Screen parent) {
            Minecraft.getInstance().setScreen(new TechTreeScreen(parent));
        }

        private static Component buttonLabel() {
            boolean flash = TechTreeCommon.CLIENT_NEW > 0 && (System.currentTimeMillis() / 350L) % 2L == 0L;
            return Component.literal((flash ? "§e" : "§a") + "\u2622");
        }

        private static int[] anchor(Screen s) {
            if (s instanceof CreativeModeInventoryScreen) {
                return new int[] { (s.width - 195) / 2 - 24, (s.height - 136) / 2 + 4 };
            }
            int ry = s.height / 2 - 22;
            for (GuiEventListener c : s.children()) {
                if (c != invButton && c instanceof AbstractWidget w && w.getY() == ry && w.getWidth() <= 24) {
                    return new int[] { w.getX() + w.getWidth() + 6, ry - 1 };
                }
            }
            return new int[] { (s.width - 176) / 2 + 130, ry - 1 };
        }

        @EventBusSubscriber(modid = TechTree.MODID, value = Dist.CLIENT)
        public static final class ModBus {
            @SubscribeEvent
            public static void keys(RegisterKeyMappingsEvent e) { e.register(KEY); }
        }

        @EventBusSubscriber(modid = TechTree.MODID, value = Dist.CLIENT)
        public static final class GameBus {
            @SubscribeEvent
            public static void tick(ClientTickEvent.Post e) {
                Minecraft mc = Minecraft.getInstance();
                while (KEY.consumeClick()) {
                    if (mc.screen == null && mc.player != null) open();
                }
                if (invButton != null) {
                    if (invOwner != null && mc.screen == invOwner) {
                        int[] p = anchor(invOwner);
                        invButton.setPosition(p[0], p[1]);
                        invButton.setMessage(buttonLabel());
                    } else {
                        invButton = null;
                        invOwner = null;
                    }
                }
            }

            @SubscribeEvent
            public static void screenInit(ScreenEvent.Init.Post e) {
                Screen s = e.getScreen();
                if (!(s instanceof InventoryScreen) && !(s instanceof CreativeModeInventoryScreen)) return;
                int[] p = anchor(s);
                Button b = Button.builder(buttonLabel(), btn -> open(s))
                        .bounds(p[0], p[1], 20, 20)
                        .tooltip(Tooltip.create(Component.literal("Uranium Tech Tree")))
                        .build();
                e.addListener(b);
                invButton = b;
                invOwner = s;
            }

            @SubscribeEvent
            public static void logout(ClientPlayerNetworkEvent.LoggingOut e) {
                TechTreeCommon.CLIENT_UNLOCKED.clear();
                TechTreeCommon.CLIENT_NEW = 0;
                TechTreeCommon.CLIENT_SYNCED = false;
            }
        }

        private TechTreeClient() {}
    }

    public static class TechTreeScreen extends Screen {
        private static final int CW = 94, RH = 72, BOX = 34;
        private static final int HEADER = 52, FOOT = 18;
        private static final int STARS = 44;
        private static final float MIN_ZOOM = 0.2f;
        private static final float LOD_ZOOM = 0.42f;
        private static final float LABEL_ZOOM = 0.34f;

        private static final int BG_TOP = 0xFF0A0D10, BG_BOT = 0xFF10151A;
        private static final int PANEL = 0xFF14191F, PANEL_LINE = 0xFF2B343E, SLOT = 0xFF0C1014;
        private static final int TXT = 0xFFECEFF2, TXT2 = 0xFF9FA8B2, TXT3 = 0xFF606A75;
        private static final int LOCKED = 0xFF3A424B, DIM_LINE = 0xFF283038;
        private static final int GOLD = 0xFFF2C94C, GREEN = 0xFF58D26E, RED = 0xFFE0645A;

        private static final class Anim { float hover, sel, dim, unlock = -99f; }

        private static final class Particle { float x, y, vx, vy, life, max; int color; }

        private record Hit(int x0, int y0, int x1, int y1, Runnable run, List<Component> tip) {
            boolean has(int x, int y) { return x >= x0 && x < x1 && y >= y0 && y < y1; }
        }

        private static final class Route {
            final int ax, ay, mid, bx, by, l1, l2, l3;
            final float len;

            Route(int ax, int ay, int bx, int by) {
                this.ax = ax; this.ay = ay; this.bx = bx; this.by = by;
                this.mid = bx - 12;
                this.l1 = Math.abs(mid - ax); this.l2 = Math.abs(by - ay); this.l3 = Math.abs(bx - mid);
                this.len = l1 + l2 + l3;
            }

            void at(float s, float[] out) {
                if (s < l1) { out[0] = ax + Math.signum(mid - ax) * s; out[1] = ay; }
                else if (s < l1 + l2) { out[0] = mid; out[1] = ay + Math.signum(by - ay) * (s - l1); }
                else { out[0] = mid + Math.signum(bx - mid) * (s - l1 - l2); out[1] = by; }
            }
        }

        private final Screen parent;
        private boolean opened;
        private int lastW, lastH;
        private float panX, panY, zoom = 1f, tPanX, tPanY, tZoom = 1f;
        private boolean dragging;
        private float dragDist;
        private int lastMx = -1, lastMy = -1;
        private TechNode hovered;
        private long lastNano;
        private float time;
        private float buildStart;
        private TechSystem activeSystem = null;
        private List<Category> cats = List.of();
        private Category filter;
        private StatusFilter statusFilter = StatusFilter.ALL;
        private String searchQuery = "";
        private boolean searchFocused = false;
        private final float[] tabAnim = new float[4];
        private TechNode selected, shown;
        private float panelAnim, panelScroll, panelContentH;
        private Set<String> path = Set.of();
        private final Map<String, Integer> steps = new HashMap<>();
        private final List<TechNode> stepList = new ArrayList<>();
        private final Map<String, Anim> anims = new HashMap<>();
        private final List<Particle> parts = new ArrayList<>();
        private final Random rnd = new Random();
        private Set<String> known = new HashSet<>();
        private float shownProgress;
        private final float[] starX = new float[STARS], starY = new float[STARS], starV = new float[STARS], starP = new float[STARS];
        private final List<Hit> hits = new ArrayList<>();
        private final Map<String, ItemStack> stacks = new HashMap<>();
        private int py, pTop, pBot, pX, pW;
        private float pA = 1f;
        private String statusBanner = "";
        private float statusBannerTimer = 0f;

        public TechTreeScreen() { this(null); }

        public TechTreeScreen(Screen parent) {
            super(Component.literal("Tech tree"));
            this.parent = parent;
            Random r = new Random(7);
            for (int i = 0; i < STARS; i++) {
                starX[i] = r.nextFloat(); starY[i] = r.nextFloat();
                starV[i] = 0.6f + r.nextFloat() * 1.4f; starP[i] = r.nextFloat() * 6.28f;
            }
        }

        @Override public boolean isPauseScreen() { return false; }

        @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }

        @Override protected void init() {
            if (!opened) {
                Set<Category> used = new LinkedHashSet<>();
                for (Category c : TechTree.CATEGORIES)
                    for (TechNode n : TechTree.BY_ID.values()) if (n.cat() == c) { used.add(c); break; }
                for (TechNode n : TechTree.BY_ID.values()) used.add(n.cat());
                cats = new ArrayList<>(used);
                known = new HashSet<>(TechTreeCommon.CLIENT_UNLOCKED);
                TechTreeCommon.CLIENT_NEW = 0;
                activeSystem = currentHighestSystem();
                buildStart = time;
                fit(true);
                opened = true;
            } else if (width != lastW || height != lastH) {
                fit(false);
            }
            lastW = width;
            lastH = height;
        }

        private void replayBuild() {
            buildStart = time;
            anims.clear();
            parts.clear();
            fit(false);
            notifyBanner("Building tech tree...");
        }

        private void resetView() {
            filter = null;
            statusFilter = StatusFilter.ALL;
            searchQuery = "";
            fit(false);
        }

        private static int[] systemStat(TechSystem sys) {
            int have = 0, total = 0;
            for (TechNode n : TechTree.BY_ID.values()) {
                if (n.system() == sys) {
                    total++;
                    if (unlocked(n)) have++;
                }
            }
            return new int[] { have, total };
        }

        private static boolean isSystemCompleted(TechSystem sys) {
            int[] st = systemStat(sys);
            return st[1] > 0 && st[0] >= st[1];
        }

        private static TechSystem previousSystem(TechSystem sys) {
            if (sys == null) return null;
            for (int i = 0; i < TechTree.SYSTEMS.size(); i++) {
                if (TechTree.SYSTEMS.get(i) == sys && i > 0) {
                    return TechTree.SYSTEMS.get(i - 1);
                }
            }
            return null;
        }

        // Chaîne stricte : Radium -> Uranium -> Plutonium
        private static boolean isSystemUnlocked(TechSystem sys) {
            if (sys == null) return true;
            for (TechSystem s : TechTree.SYSTEMS) {
                if (s == sys) return true;
                if (!isSystemCompleted(s)) return false;
            }
            return true;
        }

        private static TechSystem currentHighestSystem() {
            TechSystem cur = TechTree.SYS_RADIUM;
            for (TechSystem s : TechTree.SYSTEMS) {
                if (isSystemUnlocked(s)) cur = s;
            }
            return cur;
        }

        private void stepPage(int delta) {
            List<TechSystem> order = new ArrayList<>();
            order.add(null);
            order.addAll(TechTree.SYSTEMS);
            int idx = order.indexOf(activeSystem);
            if (idx < 0) idx = 0;
            int nextIdx = Math.max(0, Math.min(order.size() - 1, idx + delta));
            selectSystem(order.get(nextIdx));
        }

        private void notifyBanner(String msg) {
            statusBanner = msg;
            statusBannerTimer = 2.8f;
        }

        private void selectSystem(TechSystem sys) {
            if (sys != null && !isSystemUnlocked(sys)) {
                TechSystem prev = previousSystem(sys);
                int[] st = prev != null ? systemStat(prev) : new int[] { 0, 0 };
                notifyBanner("Finish " + (prev != null ? prev.label() : "previous") + " (" + st[0] + "/" + st[1] + ") to unlock " + sys.label() + "!");
                return;
            }
            activeSystem = sys;
            buildStart = time;
            if (selected != null && sys != null && selected.system() != sys) {
                selectNode(null);
            }
            frameFilter();
        }

        private void fit(boolean instant) {
            frame(visibleNodes(), 0.22f, 1.6f, instant);
        }

        private List<TechNode> visibleNodes() {
            List<TechNode> l = new ArrayList<>();
            for (TechNode n : TechTree.BY_ID.values()) {
                if (!isRevealed(n)) continue;
                if (activeSystem != null && n.system() != activeSystem) continue;
                if (matchesFilter(n)) l.add(n);
            }
            if (!l.isEmpty()) return l;
            for (TechNode n : TechTree.BY_ID.values()) {
                if (!isRevealed(n)) continue;
                if (activeSystem == null || n.system() == activeSystem) l.add(n);
            }
            return l;
        }

        private boolean matchesFilter(TechNode n) {
            if (!isRevealed(n)) return false;
            if (filter != null && n.cat() != filter) return false;
            if (statusFilter == StatusFilter.UNLOCKED && !unlocked(n)) return false;
            if (statusFilter == StatusFilter.AVAILABLE && !available(n)) return false;
            if (!searchQuery.isEmpty()) {
                String q = searchQuery.toLowerCase(Locale.ROOT);
                return n.title().toLowerCase(Locale.ROOT).contains(q)
                        || n.id().toLowerCase(Locale.ROOT).contains(q)
                        || n.itemId().toLowerCase(Locale.ROOT).contains(q)
                        || n.system().label().toLowerCase(Locale.ROOT).contains(q);
            }
            return true;
        }

        private boolean isNodeInActiveSystem(TechNode n) {
            return activeSystem == null || n.system() == activeSystem;
        }

        private void frame(Collection<TechNode> ns, float zMin, float zMax, boolean instant) {
            if (ns.isEmpty()) return;
            int minC = Integer.MAX_VALUE, maxC = 0, minR = Integer.MAX_VALUE, maxR = 0;
            for (TechNode n : ns) {
                minC = Math.min(minC, n.col()); maxC = Math.max(maxC, n.col());
                minR = Math.min(minR, n.row()); maxR = Math.max(maxR, n.row());
            }
            float x0 = minC * CW - 16f, y0 = minR * RH - 14f, x1 = maxC * CW + BOX + 16f, y1 = maxR * RH + BOX + 30f;
            float w = x1 - x0, h = y1 - y0;
            float right = selected != null ? panelW() : 0f;
            float aw = width - right - 30f, ah = height - HEADER - FOOT - 22f;
            float z = Draw.clamp(Math.min(aw / w, ah / h), zMin, zMax);
            tZoom = z;
            tPanX = (w * z <= aw ? 15f + (aw - w * z) / 2f : 15f) - x0 * z;
            tPanY = (h * z <= ah ? HEADER + 11f + (ah - h * z) / 2f : HEADER + 11f) - y0 * z;
            if (instant) { zoom = tZoom; panX = tPanX; panY = tPanY; }
        }

        private static int nx(TechNode n) { return n.col() * CW; }
        private static int ny(TechNode n) { return n.row() * RH; }
        private static boolean unlocked(TechNode n) { return TechTreeCommon.CLIENT_UNLOCKED.contains(n.id()); }

        // Direct next step : système débloqué + tous les parents directs débloqués
        private static boolean available(TechNode n) {
            if (unlocked(n) || !isSystemUnlocked(n.system())) return false;
            for (String p : n.parents()) if (!TechTreeCommon.CLIENT_UNLOCKED.contains(p)) return false;
            return true;
        }

        // N'affiche que ce qui est déjà débloqué ou directement accessible ensuite (pas la suite après)
        private static boolean isRevealed(TechNode n) {
            return unlocked(n) || available(n);
        }

        private static int have(Item it) {
            Minecraft mc = Minecraft.getInstance();
            if (it == null || mc.player == null) return 0;
            Inventory inv = mc.player.getInventory();
            int c = 0;
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack s = inv.getItem(i);
                if (s.is(it)) c += s.getCount();
            }
            return c;
        }

        private Anim anim(TechNode n) { return anims.computeIfAbsent(n.id(), k -> new Anim()); }

        private float appear(TechNode n) {
            float elapsed = time - buildStart;
            return Draw.clamp((elapsed - 0.06f - n.col() * 0.055f - n.row() * 0.015f) / 0.34f);
        }

        private ItemStack stackOf(String itemId) {
            return stacks.computeIfAbsent(itemId, k -> {
                Item it = TechTree.resolve(k);
                return new ItemStack(it != null ? it : Items.BARRIER);
            });
        }

        private ItemStack stack(TechNode n) { return stackOf(n.itemId()); }

        private String itemName(String itemId) {
            TechNode m = TechTree.nodeOfItem(itemId);
            if (m != null) return m.title();
            String f = TechTree.full(itemId, "minecraft");
            String s = f.substring(f.indexOf(':') + 1).replace('_', ' ');
            if (s.isEmpty()) return itemId;
            s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            return f.startsWith("#") ? "Any " + s : s;
        }

        private TechNode nodeAt(double mx, double my) {
            double wx = (mx - panX) / zoom, wy = (my - panY) / zoom;
            for (TechNode n : TechTree.BY_ID.values()) {
                if (!isRevealed(n) || appear(n) <= 0.1f) continue;
                if (wx >= nx(n) && wx < nx(n) + BOX && wy >= ny(n) && wy < ny(n) + BOX) return n;
            }
            return null;
        }

        private int panelW() { return Math.max(148, Math.min(206, Math.round(width * 0.44f))); }

        private int panelX() { return width - Math.round(panelW() * Draw.easeOut(panelAnim)); }

        private boolean overUi(double mx, double my) {
            return my < HEADER || my >= height - FOOT || (panelAnim > 0.02f && mx >= panelX());
        }

        private void step(float dt, int mx, int my) {
            if (statusBannerTimer > 0f) statusBannerTimer = Math.max(0f, statusBannerTimer - dt);

            float k = 1f - (float) Math.exp(-dt * 14f);
            panX += (tPanX - panX) * k;
            panY += (tPanY - panY) * k;
            zoom += (tZoom - zoom) * k;

            hovered = overUi(mx, my) || (dragging && dragDist >= 4f) ? null : nodeAt(mx, my);
            float kh = 1f - (float) Math.exp(-dt * 16f), kd = 1f - (float) Math.exp(-dt * 9f);
            boolean hasActiveFilter = filter != null || statusFilter != StatusFilter.ALL || !searchQuery.isEmpty();
            for (TechNode n : TechTree.BY_ID.values()) {
                Anim a = anim(n);
                a.hover += ((n == hovered ? 1f : 0f) - a.hover) * kh;
                a.sel += ((n == selected ? 1f : 0f) - a.sel) * kh;
                float bySystem = !isNodeInActiveSystem(n) ? 0.88f : 0f;
                float byFilter = hasActiveFilter && !matchesFilter(n) ? 1f : 0f;
                float byPath = selected != null && !path.contains(n.id()) ? 0.55f : 0f;
                a.dim += (Math.max(bySystem, Math.max(byFilter, byPath)) - a.dim) * kd;
            }
            for (int i = 0; i < tabAnim.length; i++) {
                TechSystem s = i == 0 ? null : TechTree.SYSTEMS.get(i - 1);
                boolean on = activeSystem == s;
                tabAnim[i] += ((on ? 1f : 0f) - tabAnim[i]) * kd;
            }
            panelAnim += ((selected != null ? 1f : 0f) - panelAnim) * (1f - (float) Math.exp(-dt * 12f));
            if (selected == null && panelAnim < 0.01f) shown = null;

            Set<String> cur = TechTreeCommon.CLIENT_UNLOCKED;
            if (cur.size() != known.size() || !known.containsAll(cur)) {
                for (String id : cur) {
                    TechNode n = TechTree.BY_ID.get(id);
                    if (n != null && !known.contains(id)) burst(n);
                }
                known = new HashSet<>(cur);
                if (selected != null) computePath();
            }
            TechTreeCommon.CLIENT_NEW = 0;

            long done = TechTree.BY_ID.values().stream().filter(TechTreeScreen::unlocked).count();
            float target = TechTree.BY_ID.isEmpty() ? 0f : done / (float) TechTree.BY_ID.size();
            shownProgress += (target - shownProgress) * (1f - (float) Math.exp(-dt * 5f));

            for (int i = parts.size() - 1; i >= 0; i--) {
                Particle p = parts.get(i);
                p.life -= dt;
                if (p.life <= 0f) { parts.remove(i); continue; }
                p.x += p.vx * dt;
                p.y += p.vy * dt;
                float drag = (float) Math.exp(-dt * 2.2f);
                p.vx *= drag;
                p.vy *= drag;
            }
        }

        private void burst(TechNode n) {
            anim(n).unlock = time;
            float cx = nx(n) + BOX / 2f, cy = ny(n) + BOX / 2f;
            for (int i = 0; i < 24; i++) {
                Particle p = new Particle();
                double a = rnd.nextDouble() * Math.PI * 2.0, v = 30.0 + rnd.nextFloat() * 70.0;
                p.x = cx; p.y = cy;
                p.vx = (float) (Math.cos(a) * v);
                p.vy = (float) (Math.sin(a) * v) - 10f;
                p.max = p.life = 0.6f + rnd.nextFloat() * 0.5f;
                p.color = i % 3 == 0 ? 0xFFFFFFFF : n.color();
                parts.add(p);
            }
        }

        private void selectNode(TechNode n) {
            selected = n;
            if (n == null) { computePath(); return; }
            if (activeSystem != null && n.system() != activeSystem && isSystemUnlocked(n.system())) {
                activeSystem = n.system();
            }
            shown = n;
            panelScroll = 0f;
            computePath();
            revealNode(n);
        }

        private void computePath() {
            steps.clear();
            stepList.clear();
            if (selected == null) { path = Set.of(); return; }
            path = TechTree.pathTo(selected.id());
            Map<String, Integer> memo = new HashMap<>();
            for (String id : path) {
                TechNode m = TechTree.BY_ID.get(id);
                if (m != null && !unlocked(m)) stepList.add(m);
            }
            stepList.sort(Comparator.comparingInt((TechNode m) -> TechTree.depth(m.id(), memo, new HashSet<>()))
                    .thenComparingInt(TechNode::col).thenComparingInt(TechNode::row));
            for (int i = 0; i < stepList.size(); i++) steps.put(stepList.get(i).id(), i + 1);
        }

        private void revealNode(TechNode n) {
            float freeW = width - panelW() - 12f;
            float sxn = tPanX + (nx(n) + BOX / 2f) * tZoom, syn = tPanY + (ny(n) + BOX / 2f) * tZoom;
            float mX = 40f, mTop = HEADER + 36f, mBot = height - FOOT - 40f;
            if (sxn > freeW - mX) tPanX -= sxn - (freeW - mX);
            else if (sxn < mX) tPanX += mX - sxn;
            if (syn > mBot) tPanY -= syn - mBot;
            else if (syn < mTop) tPanY += mTop - syn;
        }

        private void focusNode(TechNode n) {
            if (!isRevealed(n)) return;
            if (activeSystem != null && n.system() != activeSystem && isSystemUnlocked(n.system())) {
                activeSystem = n.system();
            }
            selectNode(n);
            tPanX = (width - panelW()) / 2f - (nx(n) + BOX / 2f) * tZoom;
            tPanY = (HEADER + (height - FOOT)) / 2f - (ny(n) + BOX / 2f) * tZoom;
        }

        void focusItem(String itemId) {
            TechNode n = TechTree.nodeOfItem(itemId);
            if (n != null && isRevealed(n)) focusNode(n);
        }

        private void openAlmanac(String mobId) {
            Minecraft.getInstance().setScreen(new AlmanacScreen(this, mobId));
        }

        private void setFilter(Category c) {
            filter = (c != null && c == filter) ? null : c;
            frameFilter();
        }

        private void cycleStatusFilter() {
            StatusFilter[] vals = StatusFilter.values();
            statusFilter = vals[(statusFilter.ordinal() + 1) % vals.length];
            frameFilter();
        }

        private void frameFilter() {
            if (filter == null && statusFilter == StatusFilter.ALL && searchQuery.isEmpty() && activeSystem == null) {
                fit(false);
                return;
            }
            frame(visibleNodes(), 0.22f, 1.35f, false);
        }

        private void cycleFilter() {
            int i = filter == null ? 0 : cats.indexOf(filter) + 1;
            i = (i + 1) % (cats.size() + 1);
            filter = i == 0 ? null : cats.get(i - 1);
            frameFilter();
        }

        @Override public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
            if (e.button() == 0) {
                searchFocused = false;
                for (int i = hits.size() - 1; i >= 0; i--) {
                    Hit h = hits.get(i);
                    if (h.has(lastMx, lastMy)) { h.run().run(); return true; }
                }
                if (overUi(lastMx, lastMy)) return true;
                dragging = true;
                dragDist = 0f;
                tPanX = panX; tPanY = panY; tZoom = zoom;
                return true;
            }
            return super.mouseClicked(e, doubleClick);
        }

        @Override public boolean mouseReleased(MouseButtonEvent e) {
            if (dragging && dragDist < 4f) selectNode(overUi(lastMx, lastMy) ? null : nodeAt(lastMx, lastMy));
            dragging = false;
            return super.mouseReleased(e);
        }

        @Override public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
            if (dragging) {
                panX += (float) dx;
                panY += (float) dy;
                tPanX = panX;
                tPanY = panY;
                dragDist += (float) (Math.abs(dx) + Math.abs(dy));
                return true;
            }
            return super.mouseDragged(e, dx, dy);
        }

        @Override public boolean mouseScrolled(double mx, double my, double sx, double sy) {
            if (panelAnim > 0.5f && mx >= panelX() && my > HEADER && my < height - FOOT) {
                panelScroll -= (float) sy * 14f;
                return true;
            }
            float z = Draw.clamp(tZoom * (sy > 0 ? 1.15f : 1f / 1.15f), MIN_ZOOM, 2.5f);
            float wx = (float) (mx - tPanX) / tZoom, wy = (float) (my - tPanY) / tZoom;
            tPanX = (float) mx - wx * z;
            tPanY = (float) my - wy * z;
            tZoom = z;
            return true;
        }

        @Override public boolean charTyped(CharacterEvent e) {
            if (searchFocused && searchQuery.length() < 24) {
                String ch = e.codepointAsString();
                if (!ch.isEmpty()) {
                    char c = ch.charAt(0);
                    if (c >= 32 && c != 127) {
                        searchQuery += c;
                        frameFilter();
                        return true;
                    }
                }
            }
            return super.charTyped(e);
        }

        @Override public boolean keyPressed(KeyEvent e) {
            int k = e.key();
            if (searchFocused) {
                if (k == InputConstants.KEY_ESCAPE || k == InputConstants.KEY_RETURN) {
                    searchFocused = false;
                    return true;
                }
                if (k == InputConstants.KEY_BACKSPACE && !searchQuery.isEmpty()) {
                    searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
                    frameFilter();
                    return true;
                }
            }
            if (k == InputConstants.KEY_ESCAPE && selected != null) { selectNode(null); return true; }
            if (k == InputConstants.KEY_R) { resetView(); return true; }
            if (k == InputConstants.KEY_B) { replayBuild(); return true; }
            if (k == InputConstants.KEY_M) { openAlmanac(null); return true; }
            if (k == InputConstants.KEY_K || k == InputConstants.KEY_E) { onClose(); return true; }
            if (k == InputConstants.KEY_TAB) { cycleFilter(); return true; }
            int d = k - InputConstants.KEY_0;
            if (d >= 0 && d <= 9) {
                if (d == 0) { filter = null; frameFilter(); }
                else if (d <= cats.size()) { filter = cats.get(d - 1); frameFilter(); }
                return true;
            }
            return super.keyPressed(e);
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
            long now = System.nanoTime();
            float dt = lastNano == 0L ? 0f : Math.min(0.1f, (now - lastNano) / 1.0e9f);
            lastNano = now;
            time += dt;
            lastMx = mx;
            lastMy = my;
            hits.clear();
            step(dt, mx, my);
            float open = Draw.easeOut(time / 0.30f);

            drawBackground(g, open);
            Draw.push(g, panX, panY, zoom);
            drawLinks(g);
            drawNodes(g);
            drawFx(g);
            Draw.pop(g);
            drawHeader(g, open, mx, my);
            drawLegend(g, open, mx, my);
            drawPageBar(g, open, mx, my);
            drawPanel(g, mx, my);
            drawFooter(g, open);

            if (statusBannerTimer > 0f && !statusBanner.isEmpty()) {
                int bw = font.width(statusBanner) + 18;
                int bx = (width - (selected != null ? panelW() : 0) - bw) / 2;
                int by = HEADER + 8;
                Draw.round(g, bx, by, bx + bw, by + 16, Draw.alpha(GOLD, Math.min(1f, statusBannerTimer)));
                Draw.rect(g, bx + 1, by + 1, bx + bw - 1, by + 15, Draw.alpha(0xFF14191F, Math.min(0.96f, statusBannerTimer)));
                Draw.text(g, font, statusBanner, bx + 9, by + 4, Draw.alpha(GOLD, Math.min(1f, statusBannerTimer)), true);
            }

            List<Component> tip = null;
            for (Hit h : hits) if (h.tip() != null && h.has(mx, my)) { tip = h.tip(); break; }
            if (tip != null) Draw.tooltip(g, font, tip, mx, my);
            else if (hovered != null && hovered != selected) Draw.tooltip(g, font, tip(hovered), mx, my);
        }

        private void drawBackground(GuiGraphicsExtractor g, float open) {
            Draw.vgrad(g, 0, 0, width, height, Draw.alpha(BG_TOP, 0.96f * open), Draw.alpha(BG_BOT, 0.96f * open), 6);
            float st = 24f * zoom;
            int mul = 1;
            while (st * mul < 16f) mul *= 2;
            st *= mul;
            int gx0 = (int) Math.floor(-panX / st), gx1 = (int) Math.ceil((width - panX) / st);
            int gy0 = (int) Math.floor((HEADER - panY) / st), gy1 = (int) Math.ceil((height - FOOT - panY) / st);
            for (int gx = gx0; gx <= gx1; gx++) {
                for (int gy = gy0; gy <= gy1; gy++) {
                    int qx = (int) (panX + gx * st), qy = (int) (panY + gy * st);
                    boolean major = gx % 4 == 0 && gy % 4 == 0;
                    Draw.rect(g, qx, qy, qx + (major ? 2 : 1), qy + (major ? 2 : 1),
                            Draw.alpha(major ? 0xFF3E4A56 : 0xFF212931, open));
                }
            }
            int starTint = activeSystem != null ? activeSystem.color() : 0xFF6FCF3A;
            for (int i = 0; i < STARS; i++) {
                float yy = (starY[i] * height - time * starV[i] * 5f) % height;
                if (yy < 0f) yy += height;
                float xx = starX[i] * width + (float) Math.sin(time * 0.6f + starP[i]) * 5f + panX * 0.04f * starV[i];
                xx = ((xx % width) + width) % width;
                float tw = 0.5f + 0.5f * (float) Math.sin(time * 1.7f + starP[i] * 3f);
                int sz = starV[i] > 1.5f ? 2 : 1, qx = (int) xx, qy = (int) yy;
                Draw.rect(g, qx, qy, qx + sz, qy + sz, Draw.alpha(starTint, (0.05f + 0.16f * tw) * open));
            }
            int c0 = Draw.alpha(0xFF000000, 0f), c1 = Draw.alpha(0xFF000000, 0.42f * open);
            Draw.hgrad(g, 0, HEADER, 40, height - FOOT, c1, c0, 4);
            Draw.hgrad(g, width - 40, HEADER, width, height - FOOT, c0, c1, 4);
            Draw.vgrad(g, 0, height - FOOT - 28, width, height - FOOT, c0, Draw.alpha(0xFF000000, 0.36f * open), 4);
        }

        private void drawLinks(GuiGraphicsExtractor g) {
            float[] pos = new float[2];
            boolean lod = zoom < LOD_ZOOM;
            for (TechNode n : TechTree.BY_ID.values()) {
                if (!isRevealed(n)) continue;
                float ap = appear(n);
                if (ap <= 0f) continue;
                boolean nu = unlocked(n);
                boolean nav = available(n);
                for (String pid : n.parents()) {
                    TechNode p = TechTree.BY_ID.get(pid);
                    if (p == null || !isRevealed(p)) continue;
                    boolean pu = unlocked(p);
                    boolean onPath = selected != null && path.contains(n.id()) && path.contains(pid);
                    float al = ap * (1f - 0.72f * Math.max(anim(n).dim, anim(p).dim));
                    int base = n.color();
                    int col = pu ? (nu ? base : nav ? Draw.mix(base, GOLD, 0.45f) : Draw.dim(base, 0.65f)) : DIM_LINE;
                    if (onPath) col = Draw.mix(col, 0xFFFFFFFF, 0.30f);
                    int th = onPath ? 3 : nav ? 3 : 2;
                    Route r = new Route(nx(p) + BOX, ny(p) + BOX / 2, nx(n), ny(n) + BOX / 2);
                    if (!onScreen(Math.min(r.ax, r.bx) - 6, Math.min(r.ay, r.by) - 6, Math.abs(r.bx - r.ax) + 12, Math.abs(r.by - r.ay) + 12)) continue;
                    if (!lod) route(g, r, th + 2, Draw.alpha(0xFF000000, 0.48f * al));
                    route(g, r, th, Draw.alpha(col, al));
                    if (!lod) Draw.arrow(g, r.bx, r.by, Draw.alpha(col, al));
                    if (pu && (!lod || onPath || nav)) flow(g, r, Draw.mix(col, 0xFFFFFFFF, nav ? 0.7f : 0.55f), al, nu, onPath || nav, pos);
                }
            }
        }

        private static void route(GuiGraphicsExtractor g, Route r, int th, int c) {
            int h0 = th / 2, h1 = th - h0, xl = r.mid - h0, xr = r.mid + h1;
            Draw.rect(g, Math.min(r.ax, xl), r.ay - h0, Math.max(r.ax, xl), r.ay + h1, c);
            Draw.rect(g, xl, Math.min(r.ay, r.by) - h0, xr, Math.max(r.ay, r.by) + h1, c);
            Draw.rect(g, Math.min(xr, r.bx), r.by - h0, Math.max(xr, r.bx), r.by + h1, c);
        }

        private void flow(GuiGraphicsExtractor g, Route r, int c, float al, boolean full, boolean strong, float[] pos) {
            if (r.len < 4f) return;
            if (full) {
                int k = Math.max(1, Math.round(r.len / (strong ? 30f : 46f)));
                float speed = strong ? 64f : 34f;
                for (int i = 0; i < k; i++) {
                    float s = (time * speed + i * (r.len / k)) % r.len;
                    for (int t = 0; t < 3; t++) {
                        float sp = s - t * 3f;
                        if (sp < 0f) continue;
                        r.at(sp, pos);
                        int sz = t == 0 ? 3 : 2, x = Math.round(pos[0]) - sz / 2, y = Math.round(pos[1]) - sz / 2;
                        Draw.rect(g, x, y, x + sz, y + sz, Draw.alpha(c, al * (t == 0 ? 0.95f : 0.5f / t)));
                    }
                }
            } else {
                float off = (time * 16f) % 8f;
                for (float s = off; s < r.len; s += 8f) {
                    r.at(s, pos);
                    int x = Math.round(pos[0]), y = Math.round(pos[1]);
                    Draw.rect(g, x - 1, y - 1, x + 1, y + 1, Draw.alpha(c, al * 0.85f));
                }
            }
        }

        private void drawNodes(GuiGraphicsExtractor g) {
            for (TechNode n : TechTree.BY_ID.values()) {
                if (!isRevealed(n)) continue;
                if (n != hovered && n != selected) drawNode(g, n);
            }
            if (selected != null && selected != hovered && isRevealed(selected)) drawNode(g, selected);
            if (hovered != null && isRevealed(hovered)) drawNode(g, hovered);
        }

        private boolean onScreen(int wx, int wy, int w, int h) {
            float x0 = panX + wx * zoom, y0 = panY + wy * zoom, x1 = x0 + w * zoom, y1 = y0 + h * zoom;
            return x1 > -4f && x0 < width + 4f && y1 > HEADER - 30f && y0 < height + 4f;
        }

        private void drawNode(GuiGraphicsExtractor g, TechNode n) {
            float ap = appear(n);
            if (ap <= 0f || !onScreen(nx(n) - 8, ny(n) - 8, BOX + 16, BOX + 30)) return;
            Anim an = anim(n);
            int x = nx(n), y = ny(n), base = n.color();
            boolean un = unlocked(n), av = available(n);
            boolean lod = zoom < LOD_ZOOM && an.hover < 0.05f && an.sel < 0.05f;
            float al = ap * (1f - 0.72f * an.dim);
            float pulse = 0.5f + 0.5f * (float) Math.sin(time * (av ? 4.2f : 2.6f) + n.col() * 0.6f);
            float ut = time - an.unlock;
            float pop = ut >= 0f && ut < 0.5f ? 1f + 0.22f * (float) Math.sin(ut / 0.5f * Math.PI) : 1f;
            float sc = (0.55f + 0.45f * Draw.easeBack(ap)) * (1f + (av ? 0.05f * pulse : 0f) + 0.14f * an.hover) * pop;

            Draw.pushAt(g, x + BOX / 2f, y + BOX / 2f, sc);

            if (!lod) {
                Draw.round(g, x + 1, y + 3, x + BOX + 1, y + BOX + 3, Draw.alpha(0xFF000000, 0.44f * al));
                if (av) {
                    int ring = 2 + Math.round(2f * pulse);
                    Draw.frame(g, x - ring, y - ring, x + BOX + ring, y + BOX + ring, 1, Draw.alpha(GOLD, (0.45f + 0.45f * pulse) * al));
                    Draw.glow(g, x - 1, y - 1, x + BOX + 1, y + BOX + 1, GOLD, (0.42f + 0.35f * pulse) * al, 4);
                }
                float glow = (un ? 0.30f : av ? 0.25f + 0.30f * pulse : 0f) + 0.35f * an.hover + 0.40f * an.sel;
                if (ut >= 0f && ut < 0.9f) glow += 0.8f * (1f - ut / 0.9f);
                if (glow > 0.01f) Draw.glow(g, x, y, x + BOX, y + BOX, av ? GOLD : un ? base : 0xFFFFFFFF, Math.min(1f, glow) * al, 3);
            }

            int border = un ? base : av ? Draw.mix(base, GOLD, 0.45f + 0.45f * pulse) : LOCKED;
            border = Draw.mix(border, 0xFFFFFFFF, Math.max(an.hover * 0.85f, an.sel * 0.6f));
            int plate = un ? Draw.mix(0xFF161B20, base, 0.16f) : av ? Draw.mix(0xFF1B1E18, GOLD, 0.14f + 0.08f * pulse) : 0xFF111418;
            if (lod) Draw.rect(g, x, y, x + BOX, y + BOX, Draw.alpha(border, al));
            else Draw.rframe(g, x, y, x + BOX, y + BOX, Draw.alpha(border, al));
            Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, Draw.alpha(plate, al));
            if (!lod) {
                Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + 3, Draw.alpha(av ? GOLD : 0xFFFFFFFF, (av ? 0.35f : 0.14f) * al));
                Draw.rect(g, x + 2, y + 2, x + 3, y + BOX - 2, Draw.alpha(0xFFFFFFFF, 0.10f * al));
                Draw.rect(g, x + 2, y + BOX - 4, x + BOX - 2, y + BOX - 2, Draw.alpha(0xFF000000, 0.30f * al));
            }

            int bob = av && !lod ? Math.round((float) Math.sin(time * 3.6f + n.col()) * 1.4f) : 0;
            Draw.item(g, stack(n), x + (BOX - 16) / 2, y + (BOX - 16) / 2 + bob);
            float veil = Math.max(!un && !av ? 0.66f : 0f, Math.max(an.dim * 0.75f, 1f - ap));
            if (veil > 0.01f) Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, Draw.alpha(plate, Math.min(1f, veil)));

            int mx0 = x + BOX - 7;
            if (lod) {
                if (un || av) Draw.rect(g, x + BOX - 8, y - 4, x + BOX + 2, y + 6, Draw.alpha(un ? base : GOLD, al));
            } else if (un) {
                Draw.disc(g, mx0, y - 3, 10, Draw.alpha(0xFF0A0D10, al), Draw.alpha(base, al));
                Draw.check(g, mx0 + 2, y, Draw.alpha(0xFFFFFFFF, al));
            } else if (av) {
                int gold = Draw.mix(0xFF9E7B18, GOLD, 0.45f + 0.55f * pulse);
                Draw.disc(g, mx0 - 1, y - 4, 12, Draw.alpha(0xFF0A0D10, al), Draw.alpha(gold, al));
                Draw.rect(g, mx0 + 4, y - 2, mx0 + 6, y + 3, Draw.alpha(0xFF1A1405, al));
                Draw.rect(g, mx0 + 4, y + 4, mx0 + 6, y + 6, Draw.alpha(0xFF1A1405, al));
            } else {
                Draw.disc(g, mx0, y + BOX - 7, 10, Draw.alpha(0xFF0A0D10, al), Draw.alpha(0xFF262C33, al));
                Draw.lock(g, mx0 + 2, y + BOX - 6, Draw.alpha(0xFF8A939D, al));
            }

            Integer stp = steps.get(n.id());
            if (stp != null && !un) Draw.badge(g, font, x - 6, y - 6, String.valueOf(stp), GOLD, al);

            if (zoom >= LABEL_ZOOM) {
                int tc = un ? TXT : av ? Draw.mix(GOLD, 0xFFFFFFFF, 0.35f + 0.35f * pulse) : 0xFF66707A, ty = y + BOX + 5;
                for (String ln : labelLines(n.title())) {
                    Draw.text(g, font, ln, x + BOX / 2 - font.width(ln) / 2, ty, Draw.alpha(tc, al), (un || av) && !lod);
                    ty += 9;
                }
            }

            if (an.sel > 0.02f) {
                int d = 3 + Math.round(1.5f * (float) Math.sin(time * 4f));
                Draw.brackets(g, x - d, y - d, x + BOX + d, y + BOX + d, 6, Draw.alpha(0xFFFFFFFF, an.sel * al));
            }
            Draw.pop(g);
        }

        private final Map<String, List<String>> labelCache = new HashMap<>();

        private List<String> labelLines(String title) {
            return labelCache.computeIfAbsent(title, t -> {
                List<String> w = wrap(t, CW - 6);
                if (w.size() <= 2) return w;
                StringBuilder rest = new StringBuilder(w.get(1));
                for (int i = 2; i < w.size(); i++) rest.append(' ').append(w.get(i));
                return List.of(w.get(0), font.plainSubstrByWidth(rest.toString(), CW - 6));
            });
        }

        private void drawFx(GuiGraphicsExtractor g) {
            for (TechNode n : TechTree.BY_ID.values()) {
                Anim a = anims.get(n.id());
                if (a == null) continue;
                float t = time - a.unlock;
                if (t < 0f || t > 1.0f) continue;
                int cx = nx(n) + BOX / 2, cy = ny(n) + BOX / 2;
                for (int k = 0; k < 2; k++) {
                    float u = (t - k * 0.14f) / 0.7f;
                    if (u < 0f || u > 1f) continue;
                    int r = Math.round(BOX / 2f + 6f + 34f * Draw.easeOut(u));
                    Draw.frame(g, cx - r, cy - r, cx + r, cy + r, 2, Draw.alpha(n.color(), (1f - u) * 0.9f));
                }
            }
            for (Particle p : parts) {
                float u = Draw.clamp(p.life / p.max);
                int s = u > 0.5f ? 2 : 1, qx = Math.round(p.x), qy = Math.round(p.y);
                Draw.rect(g, qx, qy, qx + s, qy + s, Draw.alpha(p.color, u));
            }
        }

        private void drawHeader(GuiGraphicsExtractor g, float open, int mx, int my) {
            int off = Math.round((1f - open) * -18f);
            Draw.vgrad(g, 0, off, width, HEADER + off, Draw.alpha(0xFF070A0C, 0.98f * open), Draw.alpha(0xFF0D1216, 0.97f * open), 4);
            Draw.rect(g, 0, HEADER - 1 + off, width, HEADER + off, Draw.alpha(PANEL_LINE, open));
            Draw.atom(g, 20, 14 + off, time, 0xFF6FCF3A, open);
            Draw.shimmer(g, font, "URANIUM TECH TREE", 34, 9 + off, TXT, 0xFFB6F25A, time, open);

            long done = TechTree.BY_ID.values().stream().filter(TechTreeScreen::unlocked).count();
            String cnt = done + " / " + TechTree.BY_ID.size();
            int bw = Math.min(92, Math.max(48, width / 6)), bx = width - bw - 14, by = 12 + off;
            int cntX = bx - font.width(cnt) - 8;
            Draw.text(g, font, cnt, cntX, 9 + off, Draw.alpha(TXT2, open), false);
            Draw.round(g, bx - 1, by - 1, bx + bw + 1, by + 8, Draw.alpha(0xFF2B343E, open));
            Draw.rect(g, bx, by, bx + bw, by + 7, Draw.alpha(0xFF0B0E12, open));
            int fw = Math.round(bw * Draw.clamp(shownProgress));
            if (fw > 0) {
                Draw.rect(g, bx, by, bx + fw, by + 7, Draw.alpha(0xFF5DB82E, open));
                Draw.rect(g, bx, by, bx + fw, by + 2, Draw.alpha(0xFFFFFFFF, 0.24f * open));
            }

            int titleEnd = 34 + font.width("URANIUM TECH TREE") + 8;
            int rightEdge = cntX - 8;
            int curX = titleEnd;

            String alLabel = rightEdge - curX > 90 ? "Mobs" : "M";
            int alW = font.width(alLabel) + 10;
            if (curX + alW <= rightEdge) {
                int y = 7 + off;
                boolean hv = mx >= curX && mx < curX + alW && my >= y && my < y + 14;
                Draw.button(g, font, alLabel, curX, y, alW, 14, hv, open);
                hits.add(new Hit(curX, y, curX + alW, y + 14, () -> openAlmanac(null),
                        List.of(Component.literal("Mob almanac"), Component.literal("§8Mobs and their loots (key M)"))));
                curX += alW + 4;
            }

            String stLabel = statusFilter.label;
            int stW = font.width(stLabel) + 10;
            if (curX + stW <= rightEdge) {
                int y = 7 + off;
                boolean hv = mx >= curX && mx < curX + stW && my >= y && my < y + 14;
                Draw.button(g, font, stLabel, curX, y, stW, 14, hv || statusFilter != StatusFilter.ALL, open);
                hits.add(new Hit(curX, y, curX + stW, y + 14, this::cycleStatusFilter,
                        List.of(Component.literal("Status filter: " + statusFilter.name()), Component.literal("§8Click to cycle All / Avail / Done"))));
                curX += stW + 4;
            }

            int srW = Math.min(76, rightEdge - curX);
            if (srW >= 38) {
                int y = 7 + off, x0 = curX, x1 = curX + srW;
                Draw.round(g, x0, y, x1, y + 14, Draw.alpha(searchFocused ? 0xFF6FCF3A : 0xFF2B343E, open));
                Draw.rect(g, x0 + 1, y + 1, x1 - 1, y + 13, Draw.alpha(0xFF0C1014, open));
                String disp = searchQuery.isEmpty() ? "Search..." : font.plainSubstrByWidth(searchQuery, srW - 10);
                int dCol = searchQuery.isEmpty() ? TXT3 : TXT;
                Draw.text(g, font, disp + (searchFocused && (System.currentTimeMillis() / 500L) % 2L == 0L ? "_" : ""),
                        x0 + 5, y + 3, Draw.alpha(dCol, open), false);
                hits.add(new Hit(x0, y, x1, y + 14, () -> searchFocused = true,
                        List.of(Component.literal("Quick search"), Component.literal("§8Click and type to filter revealed nodes (R to reset)"))));
            }

            int ty = 28 + off, th = 16, tGap = 5, tx = 12;
            int totalTabs = TechTree.SYSTEMS.size() + 1;
            for (int i = 0; i < totalTabs; i++) {
                TechSystem sys = i == 0 ? null : TechTree.SYSTEMS.get(i - 1);
                boolean sysUnlocked = isSystemUnlocked(sys);
                boolean sysDone = sys != null && isSystemCompleted(sys);
                int[] sst = sys != null ? systemStat(sys) : new int[] { (int) done, TechTree.BY_ID.size() };
                String lbl = sys == null ? "All" : (i + ". " + sys.label());
                String statTxt = " " + sst[0] + "/" + sst[1];
                int tw = font.width(lbl) + font.width(statTxt) + (sys != null ? 24 : 14);
                if (tx + tw > width - 12) break;

                boolean hv = mx >= tx && mx < tx + tw && my >= ty && my < ty + th;
                float on = i < tabAnim.length ? tabAnim[i] : 0f;
                int accent = sys == null ? 0xFFB6F25A : sys.color();
                int border = !sysUnlocked ? 0xFF282F37 : Draw.mix(hv ? 0xFF4A5A68 : 0xFF2B343E, accent, on);
                int fill = !sysUnlocked ? 0xFF0E1216 : Draw.mix(hv ? 0xFF1D262E : 0xFF13181D, Draw.mix(0xFF141B21, accent, 0.24f), on);

                Draw.round(g, tx, ty, tx + tw, ty + th, Draw.alpha(border, open));
                Draw.round(g, tx + 1, ty + 1, tx + tw - 1, ty + th - 1, Draw.alpha(fill, open));
                if (sysUnlocked) Draw.rect(g, tx + 2, ty + 1, tx + tw - 2, ty + 2, Draw.alpha(0xFFFFFFFF, 0.12f * open));

                int cx = tx + 5;
                if (sys != null) {
                    Draw.item(g, stackOf(sys.iconItem()), cx - 2, ty);
                    cx += 15;
                }
                int textCol = !sysUnlocked ? TXT3 : Draw.mix(TXT2, TXT, Math.max(on, hv ? 0.7f : 0f));
                Draw.text(g, font, lbl, cx, ty + 4, Draw.alpha(textCol, open), sysUnlocked && on > 0.3f);
                int stCol = sysDone ? GREEN : !sysUnlocked ? TXT3 : TXT2;
                Draw.text(g, font, statTxt, cx + font.width(lbl), ty + 4, Draw.alpha(stCol, open), false);

                if (!sysUnlocked) {
                    Draw.lock(g, tx + tw - 10, ty + 4, Draw.alpha(0xFF8A939D, open));
                }

                List<Component> tabTip = new ArrayList<>();
                if (sys == null) {
                    tabTip.add(Component.literal("All systems"));
                    tabTip.add(Component.literal("§7View revealed nodes across unlocked tiers"));
                } else {
                    tabTip.add(Component.literal("System " + i + ": " + sys.label()));
                    tabTip.add(Component.literal("§7Progress: §f" + sst[0] + "/" + sst[1]));
                    if (!sysUnlocked) {
                        TechSystem prev = previousSystem(sys);
                        int[] pst = prev != null ? systemStat(prev) : new int[] { 0, 0 };
                        tabTip.add(Component.literal("§cLocked §7— finish §f" + (prev != null ? prev.label() : "") + " §7(" + pst[0] + "/" + pst[1] + ") first"));
                    } else if (sysDone) {
                        tabTip.add(Component.literal("§aCompleted!"));
                    } else {
                        tabTip.add(Component.literal("§eActive tier §7— finish all nodes to unlock next"));
                    }
                }
                final TechSystem targetSys = sys;
                hits.add(new Hit(tx, ty, tx + tw, ty + th, () -> selectSystem(targetSys), tabTip));
                tx += tw + tGap;
            }
        }

        private void drawLegend(GuiGraphicsExtractor g, float open, int mx, int my) {
            int lx = 10, ly = height - FOOT - 8 - cats.size() * 10;
            if (ly < HEADER + 6) return;
            for (int i = 0; i < cats.size(); i++) {
                Category c = cats.get(i);
                int y = ly + i * 10;
                boolean active = filter == c;
                boolean hv = mx >= lx && mx < lx + 74 && my >= y && my < y + 9;
                int cCol = active ? 0xFFFFFFFF : hv ? TXT : TXT2;
                Draw.rect(g, lx, y + 2, lx + 5, y + 7, Draw.alpha(c.color(), open));
                if (active) Draw.frame1(g, lx - 1, y + 1, lx + 6, y + 8, Draw.alpha(0xFFFFFFFF, open));
                Draw.text(g, font, c.label(), lx + 8, y + 1, Draw.alpha(cCol, open), active);
                final Category fc = c;
                hits.add(new Hit(lx, y, lx + 74, y + 9, () -> setFilter(fc),
                        List.of(Component.literal("Filter: " + c.label()), Component.literal("§8Click to toggle category filter"))));
            }
        }

        private void drawPageBar(GuiGraphicsExtractor g, float open, int mx, int my) {
            List<TechSystem> order = new ArrayList<>();
            order.add(null);
            order.addAll(TechTree.SYSTEMS);
            int idx = Math.max(0, order.indexOf(activeSystem));
            int total = order.size();

            int barY = height - FOOT - 18;
            int rightLimit = width - (selected != null ? panelW() : 0) - 10;
            if (rightLimit < 120) return;

            String resetLbl = "Reset view";
            int rw = font.width(resetLbl) + 10;
            int rx = rightLimit - rw;
            boolean rhv = mx >= rx && mx < rx + rw && my >= barY && my < barY + 14;
            Draw.button(g, font, resetLbl, rx, barY, rw, 14, rhv, open);
            hits.add(new Hit(rx, barY, rx + rw, barY + 14, this::resetView,
                    List.of(Component.literal("Reset view"), Component.literal("§8Reset filters and zoom (R)"))));

            String buildLbl = "Build again";
            int bw = font.width(buildLbl) + 10;
            int bx = rx - bw - 4;
            boolean bhv = mx >= bx && mx < bx + bw && my >= barY && my < barY + 14;
            Draw.button(g, font, buildLbl, bx, barY, bw, 14, bhv, open);
            hits.add(new Hit(bx, barY, bx + bw, barY + 14, this::replayBuild,
                    List.of(Component.literal("Build again"), Component.literal("§8Replay step-by-step build animation (B)"))));

            int navW = Math.min(136, bx - 16);
            if (navW < 70) return;
            int nx0 = (bx - navW) / 2;
            String backLbl = "< Back";
            int backW = font.width(backLbl) + 8;
            boolean backHv = idx > 0 && mx >= nx0 && mx < nx0 + backW && my >= barY && my < barY + 14;
            Draw.button(g, font, backLbl, nx0, barY, backW, 14, backHv, open);
            if (idx > 0) hits.add(new Hit(nx0, barY, nx0 + backW, barY + 14, () -> stepPage(-1), null));

            String pageLbl = (idx + 1) + " / " + total;
            Draw.text(g, font, pageLbl, nx0 + navW / 2 - font.width(pageLbl) / 2, barY + 3, Draw.alpha(TXT2, open), false);

            String nextLbl = "Next >";
            int nextW = font.width(nextLbl) + 8;
            int nextX = nx0 + navW - nextW;
            boolean nextHv = idx < total - 1 && mx >= nextX && mx < nextX + nextW && my >= barY && my < barY + 14;
            Draw.button(g, font, nextLbl, nextX, barY, nextW, 14, nextHv, open);
            if (idx < total - 1) hits.add(new Hit(nextX, barY, nextX + nextW, barY + 14, () -> stepPage(1), null));

            TechSystem curSys = activeSystem != null ? activeSystem : currentHighestSystem();
            if (curSys != null && isSystemCompleted(curSys)) {
                String loopTxt = "Loop closed";
                Draw.text(g, font, loopTxt, rightLimit - font.width(loopTxt), barY - 11, Draw.alpha(GREEN, open), true);
            }
        }

        private void drawFooter(GuiGraphicsExtractor g, float open) {
            int y0 = height - FOOT;
            Draw.rect(g, 0, y0, width, height, Draw.alpha(0xFF07090B, 0.97f * open));
            Draw.rect(g, 0, y0, width, y0 + 1, Draw.alpha(PANEL_LINE, open));
            String[][] hints = { {"Drag", "Move"}, {"Wheel", "Zoom"}, {"Click", "Details"}, {"Esc", "Close"},
                                 {"M", "Mobs"}, {"B", "Build"}, {"R", "Reset"} };
            int x = 12;
            for (String[] h : hints) {
                int kw = font.width(h[0]) + 8, lw = font.width(h[1]);
                if (x + kw + 4 + lw > width - 12) break;
                Draw.key(g, font, h[0], x, y0 + 4, open);
                Draw.text(g, font, h[1], x + kw + 4, y0 + 6, Draw.alpha(TXT3, open), false);
                x += kw + 4 + lw + 14;
            }
        }

        private boolean vis(int y, int h) { return y >= pTop && y + h <= pBot; }

        private void pText(GuiGraphicsExtractor g, String s, int color) {
            if (vis(py, 9)) Draw.text(g, font, s, pX, py, Draw.alpha(color, pA), false);
        }

        private void pSection(GuiGraphicsExtractor g, TechNode n, String title, String sub) {
            if (vis(py, 10)) {
                Draw.rect(g, pX, py + 1, pX + 2, py + 8, Draw.alpha(n.color(), pA));
                Draw.text(g, font, title, pX + 6, py, Draw.alpha(TXT2, pA), true);
                if (!sub.isEmpty()) Draw.text(g, font, sub, pX + 6 + font.width(title) + 6, py, Draw.alpha(TXT3, pA), false);
            }
            py += 13;
        }

        private void pSlot(GuiGraphicsExtractor g, ItemStack st, int x, int y, String badge, boolean hl) {
            Draw.round(g, x, y, x + 18, y + 18, Draw.alpha(hl ? 0xFF566675 : 0xFF252E37, pA));
            Draw.rect(g, x + 1, y + 1, x + 17, y + 17, Draw.alpha(SLOT, pA));
            Draw.rect(g, x + 1, y + 1, x + 17, y + 2, Draw.alpha(0xFF000000, 0.35f * pA));
            Draw.item(g, st, x + 1, y + 1);
            if (pA < 0.98f) Draw.rect(g, x + 1, y + 1, x + 17, y + 17, Draw.alpha(SLOT, 1f - pA));
            if (badge != null) Draw.text(g, font, badge, x + 17 - font.width(badge), y + 9, Draw.alpha(TXT, pA), true);
        }

        private List<Component> nameTip(String itemId, String extra) {
            List<Component> l = new ArrayList<>();
            l.add(Component.literal(itemName(itemId)));
            if (extra != null) l.add(Component.literal(extra));
            return l;
        }

        private void drawPanel(GuiGraphicsExtractor g, int mx, int my) {
            if (shown == null || panelAnim < 0.01f) return;
            TechNode n = shown;
            int pw = panelW(), x0 = panelX(), y0 = HEADER, y1 = height - FOOT;
            float e = Draw.easeOut(panelAnim);
            Draw.rect(g, x0, y0, width, y1, Draw.alpha(PANEL, 0.97f * e));
            Draw.rect(g, x0 - 2, y0, x0, y1, Draw.alpha(0xFF000000, 0.22f * e));
            Draw.rect(g, x0, y0, x0 + 1, y1, Draw.alpha(PANEL_LINE, e));
            Draw.rect(g, x0 + 1, y0, x0 + 3, y1, Draw.alpha(n.color(), 0.90f * e));

            float maxS = Math.max(0f, panelContentH - (y1 - y0));
            panelScroll = Draw.clamp(panelScroll, 0f, maxS);
            pX = x0 + 12;
            pW = pw - 22;
            pTop = y0 + 3;
            pBot = y1 - 3;
            pA = e;
            py = y0 + 9 - Math.round(panelScroll);
            boolean un = unlocked(n), av = available(n);

            if (vis(py, 28)) {
                Draw.round(g, pX, py, pX + 28, py + 28, Draw.alpha(n.color(), pA));
                Draw.rect(g, pX + 1, py + 1, pX + 27, py + 27, Draw.alpha(0xFF10151A, pA));
                Draw.item(g, stack(n), pX + 6, py + 6);
                if (pA < 0.98f) Draw.rect(g, pX + 1, py + 1, pX + 27, py + 27, Draw.alpha(0xFF10151A, 1f - pA));
                Draw.text(g, font, font.plainSubstrByWidth(n.title(), pW - 36), pX + 36, py + 4, Draw.alpha(TXT, pA), true);
                Draw.rect(g, pX + 36, py + 17, pX + 41, py + 22, Draw.alpha(n.color(), pA));
                Draw.text(g, font, n.system().label() + " · " + n.cat().label(), pX + 45, py + 15, Draw.alpha(TXT2, pA), false);
            }
            py += 32;

            if (vis(py, 12)) {
                String st = un ? "UNLOCKED" : av ? "NEXT STEP" : "LOCKED";
                int sc = un ? GREEN : av ? GOLD : RED, w = font.width(st) + 10;
                Draw.round(g, pX, py, pX + w, py + 12, Draw.alpha(sc, 0.78f * pA));
                Draw.round(g, pX + 1, py + 1, pX + w - 1, py + 11, Draw.alpha(Draw.mix(0xFF11161B, sc, 0.16f), pA));
                Draw.text(g, font, st, pX + 5, py + 2, Draw.alpha(sc, pA), false);
                int inv = have(n.item());
                if (inv > 0) Draw.text(g, font, "Have: " + inv, pX + w + 8, py + 2, Draw.alpha(TXT2, pA), false);
            }
            py += 15;

            if (vis(py, 1)) Draw.rect(g, pX, py, pX + pW, py + 1, Draw.alpha(PANEL_LINE, pA));
            py += 6;

            for (String ln : wrap(n.desc(), pW)) {
                pText(g, ln, 0xFFB8C0C8);
                py += 10;
            }
            py += 4;

            pSection(g, n, "HOW TO GET", "");
            if (n.auto()) {
                pText(g, "Unlocked from the start.", TXT2);
                py += 12;
            } else if (!un && !av) {
                String lockMsg = !isSystemUnlocked(n.system())
                        ? "Craft hidden: finish the previous system first."
                        : "Craft hidden: unlock previous step first.";
                for (String ln : wrap(lockMsg, pW)) {
                    pText(g, ln, TXT3);
                    py += 10;
                }
                py += 4;
            } else {
                List<Recipe> rs = TechTree.recipesOf(n.id());
                if (rs.isEmpty()) {
                    String msg = n.cat() == TechTree.MINERAI ? "Found in the world. Mine it with a pickaxe." : "No recipe listed for this one.";
                    for (String ln : wrap(msg, pW)) {
                        pText(g, ln, TXT3);
                        py += 10;
                    }
                    py += 2;
                }
                int shownN = 0;
                for (Recipe r : rs) {
                    if (shownN++ >= 4) break;
                    if (shownN > 1) {
                        if (vis(py, 1)) Draw.rect(g, pX, py, pX + pW, py + 1, Draw.alpha(PANEL_LINE, pA));
                        py += 6;
                    }
                    drawRecipe(g, n, r, mx, my);
                }
                if (av) {
                    for (String ln : wrap("Direct next step: obtain " + n.title() + " to unlock it.", pW)) {
                        pText(g, ln, GOLD);
                        py += 10;
                    }
                }
                py += 4;
            }

            if (!n.parents().isEmpty()) {
                pSection(g, n, "REQUIRES", "");
                for (String pid : n.parents()) needRow(g, TechTree.BY_ID.get(pid), pid, mx, my);
                py += 2;
            }

            pSection(g, n, "USED FOR", "");
            Map<String, TechNode> used = new LinkedHashMap<>();
            for (TechNode k : TechTree.childrenOf(n.id())) if (isRevealed(k)) used.put(k.id(), k);
            for (TechNode k : TechTree.usedIn(n.id())) if (isRevealed(k)) used.putIfAbsent(k.id(), k);
            if (used.isEmpty()) {
                pText(g, "No revealed next step yet.", TXT3);
                py += 12;
            } else {
                int x = pX;
                for (TechNode k : used.values()) {
                    if (x + 18 > pX + pW) { x = pX; py += 20; }
                    if (vis(py, 18)) {
                        boolean hv = mx >= x && mx < x + 18 && my >= py && my < py + 18;
                        pSlot(g, stack(k), x, py, null, hv);
                        hits.add(new Hit(x, py, x + 18, py + 18, () -> focusNode(k),
                                List.of(Component.literal(k.title()), Component.literal("§8Click to show it on the tree"))));
                    }
                    x += 21;
                }
                py += 22;
            }

            int[] pStat = systemStat(TechTree.SYS_PLUTONIUM);
            pSection(g, n, "NEXT TO PLUTONIUM", pStat[0] + "/" + pStat[1]);
            if (isSystemUnlocked(TechTree.SYS_PLUTONIUM)) {
                List<TechNode> pTodo = new ArrayList<>();
                for (TechNode k : TechTree.BY_ID.values()) {
                    if (k.system() == TechTree.SYS_PLUTONIUM && !unlocked(k)) pTodo.add(k);
                }
                if (pTodo.isEmpty()) {
                    pText(g, "Plutonium unlocked!", GREEN);
                    py += 12;
                } else {
                    int x = pX;
                    for (TechNode k : pTodo) {
                        if (x + 18 > pX + pW) { x = pX; py += 20; }
                        if (vis(py, 18)) {
                            boolean canFocus = isRevealed(k);
                            boolean hv = canFocus && mx >= x && mx < x + 18 && my >= py && my < py + 18;
                            pSlot(g, stack(k), x, py, null, hv);
                            if (!canFocus) Draw.rect(g, x + 1, py + 1, x + 17, py + 17, Draw.alpha(0xFF0C1014, 0.65f * pA));
                            hits.add(new Hit(x, py, x + 18, py + 18, canFocus ? () -> focusNode(k) : () -> {},
                                    List.of(Component.literal(k.title()), Component.literal(canFocus ? "§eNext step in Plutonium" : "§8Locked Plutonium node"))));
                        }
                        x += 21;
                    }
                    py += 22;
                }
            } else {
                TechSystem blocker = null;
                for (TechSystem s : TechTree.SYSTEMS) {
                    if (s == TechTree.SYS_PLUTONIUM) break;
                    if (!isSystemCompleted(s)) { blocker = s; break; }
                }
                if (blocker != null) {
                    int[] bStat = systemStat(blocker);
                    for (String ln : wrap("Finish " + blocker.label() + " (" + bStat[0] + "/" + bStat[1] + ") first.", pW)) {
                        pText(g, ln, GOLD);
                        py += 10;
                    }
                    int rem = 0;
                    for (TechNode k : TechTree.BY_ID.values()) {
                        if (k.system() == blocker && !unlocked(k)) {
                            rem++;
                            if (rem <= 6) {
                                pText(g, "· " + font.plainSubstrByWidth(k.title(), pW - 10), TXT2);
                                py += 10;
                            }
                        }
                    }
                    if (rem > 6) {
                        pText(g, "· +" + (rem - 6) + " more in " + blocker.label(), TXT3);
                        py += 10;
                    }
                    py += 2;
                }
            }

            List<Mob> mobs = TechTree.mobsDropping(n.itemId());
            if (!mobs.isEmpty() && (un || av)) {
                pSection(g, n, "DROPPED BY", "");
                for (Mob m : mobs) {
                    if (vis(py, 18)) {
                        boolean hv = mx >= pX && mx < pX + pW && my >= py && my < py + 18;
                        Draw.round(g, pX, py, pX + pW, py + 18, Draw.alpha(hv ? 0xFF1F2931 : 0xFF10151A, pA));
                        pSlot(g, stackOf(m.icon()), pX, py, null, hv);
                        Draw.text(g, font, font.plainSubstrByWidth(m.name(), pW - 28), pX + 24, py + 5, Draw.alpha(TXT, pA), false);
                        hits.add(new Hit(pX, py, pX + pW, py + 18, () -> openAlmanac(m.id()),
                                List.of(Component.literal(m.name()), Component.literal("§8Click to open the almanac"))));
                    }
                    py += 19;
                }
                py += 2;
            }

            panelContentH = (py + Math.round(panelScroll)) - y0 + 8;
            maxS = Math.max(0f, panelContentH - (y1 - y0));
            if (maxS > 0f) {
                int track = y1 - y0 - 6;
                int th = Math.max(12, Math.round(track * (y1 - y0) / panelContentH));
                int ty = y0 + 3 + Math.round((track - th) * (panelScroll / maxS));
                Draw.rect(g, width - 4, y0 + 3, width - 2, y1 - 3, Draw.alpha(0xFF000000, 0.30f * e));
                Draw.rect(g, width - 4, ty, width - 2, ty + th, Draw.alpha(TXT3, e));
            }
        }

        private void drawRecipe(GuiGraphicsExtractor g, TechNode n, Recipe r, int mx, int my) {
            boolean mining = r.station().equals("Mining");
            if (!r.station().isEmpty()) {
                if (vis(py, 18)) {
                    TechNode stn = r.stationItem() == null ? null : TechTree.nodeOfItem(r.stationItem());
                    boolean hv = stn != null && isRevealed(stn) && mx >= pX && mx < pX + pW && my >= py && my < py + 18;
                    Draw.round(g, pX, py, pX + pW, py + 18, Draw.alpha(hv ? 0xFF1F2931 : 0xFF10151A, pA));
                    if (r.stationItem() != null) pSlot(g, stackOf(r.stationItem()), pX, py, null, hv);
                    Draw.text(g, font, font.plainSubstrByWidth(r.station(), pW - 28), pX + 24, py + 5, Draw.alpha(TXT, pA), true);
                    if (stn != null && isRevealed(stn)) {
                        hits.add(new Hit(pX, py, pX + pW, py + 18, () -> focusNode(stn),
                                List.of(Component.literal(stn.title()), Component.literal("§8Click to show it on the tree"))));
                    }
                }
                py += 21;
            }

            boolean shaped = r.gw() > 0 && r.gh() > 0;
            if (!r.inputs().isEmpty() || shaped) {
                int cell = 17;
                int x = pX, rowH = 18;
                if (shaped) {
                    for (int yy = 0; yy < r.gh(); yy++) {
                        for (int xx = 0; xx < r.gw(); xx++) {
                            String id = r.grid().get(yy * r.gw() + xx);
                            int cx = pX + xx * cell, cy = py + yy * cell;
                            if (!vis(cy, 16)) continue;
                            Draw.rect(g, cx, cy, cx + 16, cy + 16, Draw.alpha(0xFF0C1014, pA));
                            Draw.frame1(g, cx, cy, cx + 16, cy + 16, Draw.alpha(0xFF252E37, pA));
                            if (id.isEmpty()) continue;
                            Draw.item(g, stackOf(id), cx, cy);
                            if (pA < 0.98f) Draw.rect(g, cx, cy, cx + 16, cy + 16, Draw.alpha(0xFF0C1014, 1f - pA));
                            if (mx >= cx && mx < cx + 16 && my >= cy && my < cy + 16)
                                Draw.frame(g, cx, cy, cx + 16, cy + 16, 1, Draw.alpha(0xFFFFFFFF, pA * 0.7f));
                            hits.add(new Hit(cx, cy, cx + 16, cy + 16, () -> {}, nameTip(id, null)));
                        }
                    }
                    rowH = r.gh() * cell;
                    x = pX + r.gw() * cell + 4;
                } else {
                    for (Ing in : r.inputs()) {
                        if (x + 18 > pX + pW) { x = pX; py += 20; }
                        if (vis(py, 18)) {
                            boolean hv = mx >= x && mx < x + 18 && my >= py && my < py + 18;
                            pSlot(g, stackOf(in.itemId()), x, py, in.count() > 1 ? String.valueOf(in.count()) : null, hv);
                            hits.add(new Hit(x, py, x + 18, py + 18, () -> {}, nameTip(in.itemId(), null)));
                        }
                        x += 21;
                    }
                }
                if (x + 12 + 18 > pX + pW) { x = pX; py += rowH + 2; rowH = 18; }
                int ry = py + (rowH - 18) / 2;
                if (vis(ry, 18)) {
                    Draw.arrow(g, x + 8, ry + 9, Draw.alpha(TXT2, pA));
                    String badge = r.out() <= 0 ? null : r.outMax() > r.out() ? r.out() + "-" + r.outMax() : r.out() > 1 ? String.valueOf(r.out()) : null;
                    boolean hv = mx >= x + 12 && mx < x + 30 && my >= ry && my < ry + 18;
                    pSlot(g, stack(n), x + 12, ry, badge, hv);
                    hits.add(new Hit(x + 12, ry, x + 30, ry + 18, () -> {}, nameTip(n.itemId(), null)));
                }
                py += rowH + 4;

                if (!mining) {
                    for (Ing in : r.inputs()) {
                        if (vis(py, 12)) {
                            Item it = TechTree.resolve(in.itemId());
                            int owned = have(it), need = in.count();
                            boolean enough = it != null && owned >= need;
                            Draw.push(g, pX, py + 1, 0.625f);
                            Draw.item(g, stackOf(in.itemId()), 0, 0);
                            Draw.pop(g);
                            String cnt = need + "x";
                            Draw.text(g, font, cnt, pX + 14, py + 1, Draw.alpha(enough ? GREEN : TXT, pA), false);
                            int nameX = pX + 14 + font.width(cnt) + 4;
                            Draw.text(g, font, font.plainSubstrByWidth(itemName(in.itemId()), pW - (nameX - pX) - 26), nameX, py + 1,
                                    Draw.alpha(enough ? TXT : TXT2, pA), false);
                            if (enough) {
                                Draw.check(g, pX + pW - 7, py + 3, Draw.alpha(GREEN, pA));
                            } else if (it != null) {
                                String own = owned + "/" + need;
                                Draw.text(g, font, own, pX + pW - font.width(own), py + 1, Draw.alpha(owned > 0 ? GOLD : RED, pA), false);
                            }
                        }
                        py += 12;
                    }
                    py += 2;
                }
            }
            for (String ln : wrap(r.note(), pW)) {
                if (ln.isEmpty()) continue;
                pText(g, ln, TXT3);
                py += 10;
            }
            py += 2;
        }

        private void needRow(GuiGraphicsExtractor g, TechNode p, String pid, int mx, int my) {
            if (vis(py, 18)) {
                boolean pu = p != null && unlocked(p);
                boolean canFocus = p != null && isRevealed(p);
                boolean hv = canFocus && mx >= pX && mx < pX + pW && my >= py && my < py + 18;
                Draw.round(g, pX, py, pX + pW, py + 18, Draw.alpha(hv ? 0xFF1F2931 : 0xFF10151A, pA));
                pSlot(g, p != null ? stack(p) : stackOf("minecraft:barrier"), pX, py, null, hv);
                String nm = font.plainSubstrByWidth(p != null ? p.title() : pid, pW - 24 - 16);
                Draw.text(g, font, nm, pX + 24, py + 5, Draw.alpha(pu ? TXT : TXT2, pA), false);
                if (pu) Draw.check(g, pX + pW - 11, py + 7, Draw.alpha(GREEN, pA));
                else Draw.lock(g, pX + pW - 10, py + 5, Draw.alpha(0xFF6B7580, pA));
                if (canFocus) {
                    hits.add(new Hit(pX, py, pX + pW, py + 18, () -> focusNode(p),
                            List.of(Component.literal(p.title()), Component.literal("§8Click to show it on the tree"))));
                }
            }
            py += 19;
        }

        private List<Component> tip(TechNode n) {
            List<Component> l = new ArrayList<>();
            l.add(Component.literal(n.title()));
            l.add(Component.literal("§8" + n.system().label() + " · " + n.cat().label()));
            l.add(Component.literal("§7" + n.desc()));
            if (unlocked(n)) {
                l.add(Component.literal("§aUnlocked"));
            } else if (available(n)) {
                l.add(Component.literal("§eDirect next step §7— obtain: " + n.title()));
            } else {
                l.add(Component.literal("§cLocked"));
            }
            l.add(Component.literal("§8Click for details"));
            return l;
        }

        private List<String> wrap(String s, int maxW) {
            List<String> out = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String w : s.split(" ")) {
                String t = line.length() == 0 ? w : line + " " + w;
                if (line.length() == 0 || font.width(t) <= maxW) {
                    line.setLength(0);
                    line.append(t);
                } else {
                    out.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
            if (line.length() > 0) out.add(line.toString());
            return out;
        }
    }

    public static class AlmanacScreen extends Screen {
        private static final int HEAD = 34, FOOT = 18;
        private static final int ACCENT = 0xFF2FC4B2;

        private record Zone(int x0, int y0, int x1, int y1, Runnable run, List<Component> tip) {
            boolean has(int x, int y) { return x >= x0 && x < x1 && y >= y0 && y < y1; }
        }

        private final Screen parent;
        private long lastNano;
        private float time, fade = 1f, scroll;
        private int sel, lastMx = -1, lastMy = -1;
        private final float[] rowAnim = new float[64];
        private final List<Zone> zones = new ArrayList<>();
        private final Map<String, ItemStack> stacks = new HashMap<>();
        private final float[] starX = new float[30], starY = new float[30], starV = new float[30], starP = new float[30];

        public AlmanacScreen(Screen parent, String mobId) {
            super(Component.literal("Mob almanac"));
            this.parent = parent;
            for (int i = 0; i < TechTree.MOBS.size(); i++) if (TechTree.MOBS.get(i).id().equals(mobId)) sel = i;
            Random r = new Random(11);
            for (int i = 0; i < starX.length; i++) {
                starX[i] = r.nextFloat(); starY[i] = r.nextFloat();
                starV[i] = 0.6f + r.nextFloat() * 1.4f; starP[i] = r.nextFloat() * 6.28f;
            }
        }

        @Override public boolean isPauseScreen() { return false; }

        @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }

        private ItemStack stackOf(String itemId) {
            return stacks.computeIfAbsent(itemId, k -> {
                Item it = TechTree.resolve(k);
                return new ItemStack(it != null ? it : Items.BARRIER);
            });
        }

        private String itemName(String itemId) {
            TechNode m = TechTree.nodeOfItem(itemId);
            if (m != null) return m.title();
            String f = TechTree.full(itemId, "minecraft");
            String s = f.substring(f.indexOf(':') + 1).replace('_', ' ');
            return s.isEmpty() ? itemId : Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }

        private void select(int i) {
            if (i < 0 || i >= TechTree.MOBS.size() || i == sel) return;
            sel = i;
            fade = 0f;
        }

        private void showOnTree(String itemId) {
            TechTreeScreen t = parent instanceof TechTreeScreen ts ? ts : new TechTreeScreen(null);
            Minecraft.getInstance().setScreen(t);
            t.focusItem(itemId);
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
            long now = System.nanoTime();
            float dt = lastNano == 0L ? 0f : Math.min(0.1f, (now - lastNano) / 1.0e9f);
            lastNano = now;
            time += dt;
            lastMx = mx;
            lastMy = my;
            zones.clear();
            float open = Draw.easeOut(time / 0.30f);
            float k = 1f - (float) Math.exp(-dt * 12f);
            fade += (1f - fade) * k;
            List<Mob> mobs = TechTree.MOBS;
            if (sel >= mobs.size()) sel = Math.max(0, mobs.size() - 1);

            Draw.vgrad(g, 0, 0, width, height, Draw.alpha(TechTreeScreen.BG_TOP, 0.97f * open), Draw.alpha(TechTreeScreen.BG_BOT, 0.97f * open), 6);
            for (int i = 0; i < starX.length; i++) {
                float yy = (starY[i] * height - time * starV[i] * 5f) % height;
                if (yy < 0f) yy += height;
                float xx = starX[i] * width + (float) Math.sin(time * 0.6f + starP[i]) * 5f;
                float tw = 0.5f + 0.5f * (float) Math.sin(time * 1.7f + starP[i] * 3f);
                int sz = starV[i] > 1.5f ? 2 : 1, qx = (int) xx, qy = (int) yy;
                Draw.rect(g, qx, qy, qx + sz, qy + sz, Draw.alpha(ACCENT, (0.05f + 0.16f * tw) * open));
            }

            int off = Math.round((1f - open) * -14f);
            Draw.vgrad(g, 0, off, width, HEAD + off, Draw.alpha(0xFF06080A, 0.98f * open), Draw.alpha(0xFF0C1013, 0.96f * open), 4);
            Draw.rect(g, 0, HEAD - 1 + off, width, HEAD + off, Draw.alpha(TechTreeScreen.PANEL_LINE, open));
            Draw.atom(g, 20, 15 + off, time, ACCENT, open);
            Draw.shimmer(g, font, "MOB ALMANAC", 34, 10 + off, TechTreeScreen.TXT, 0xFF8FF2E4, time, open);
            String cnt = mobs.size() + (mobs.size() == 1 ? " mob" : " mobs");
            Draw.text(g, font, cnt, width - font.width(cnt) - 14, 10 + off, Draw.alpha(TechTreeScreen.TXT2, open), false);

            int lw = Math.min(150, Math.max(110, width * 36 / 100)), lx = 12, top = HEAD + 10, bottom = height - FOOT - 8;

            if (mobs.isEmpty()) {
                String m = "No mob registered yet.";
                Draw.text(g, font, m, (width - font.width(m)) / 2, height / 2 - 4, Draw.alpha(TechTreeScreen.TXT3, open), false);
            }

            int rowH = 28;
            float maxScroll = Math.max(0f, mobs.size() * rowH - (bottom - top));
            scroll = Draw.clamp(scroll, 0f, maxScroll);
            for (int i = 0; i < mobs.size(); i++) {
                Mob mob = mobs.get(i);
                int y = top + i * rowH - Math.round(scroll);
                if (y < top - 2 || y + 24 > bottom + 2) continue;
                boolean hv = mx >= lx && mx < lx + lw && my >= y && my < y + 24;
                if (i < rowAnim.length) rowAnim[i] += ((i == sel ? 1f : hv ? 0.5f : 0f) - rowAnim[i]) * k;
                float a = i < rowAnim.length ? rowAnim[i] : 0f;
                float ap = Draw.clamp((time - 0.12f - i * 0.05f) / 0.3f);
                int x = lx - Math.round((1f - Draw.easeOut(ap)) * 14f);
                float al = open * ap;
                Draw.round(g, x, y, x + lw, y + 24, Draw.alpha(Draw.mix(0xFF2B343E, ACCENT, a), al));
                Draw.round(g, x + 1, y + 1, x + lw - 1, y + 23, Draw.alpha(Draw.mix(0xFF12171B, 0xFF16302E, a), al));
                Draw.rect(g, x + 1, y + 4, x + 3, y + 4 + Math.round(16 * a), Draw.alpha(ACCENT, al));
                Draw.round(g, x + 6, y + 3, x + 24, y + 21, Draw.alpha(0xFF252E37, al));
                Draw.rect(g, x + 7, y + 4, x + 23, y + 20, Draw.alpha(TechTreeScreen.SLOT, al));
                Draw.item(g, stackOf(mob.icon()), x + 7, y + 4);
                if (al < 0.98f) Draw.rect(g, x + 7, y + 4, x + 23, y + 20, Draw.alpha(TechTreeScreen.SLOT, 1f - al));
                Draw.text(g, font, font.plainSubstrByWidth(mob.name(), lw - 34), x + 29, y + 4, Draw.alpha(TechTreeScreen.TXT, al), true);
                String sub = mob.drops().isEmpty() ? "No drops listed" : mob.drops().size() + (mob.drops().size() == 1 ? " drop" : " drops");
                Draw.text(g, font, sub, x + 29, y + 14, Draw.alpha(TechTreeScreen.TXT3, al), false);
                final int idx = i;
                zones.add(new Zone(lx, y, lx + lw, y + 24, () -> select(idx), null));
            }

            int dx = lx + lw + 12, dw = width - dx - 12;
            if (!mobs.isEmpty() && dw > 80) {
                Mob mob = mobs.get(sel);
                float da = open * Draw.easeOut(fade);
                int slide = Math.round((1f - Draw.easeOut(fade)) * 10f);
                Draw.round(g, dx, top, dx + dw, bottom, Draw.alpha(TechTreeScreen.PANEL_LINE, open));
                Draw.round(g, dx + 1, top + 1, dx + dw - 1, bottom - 1, Draw.alpha(TechTreeScreen.PANEL, 0.97f * open));
                int cx = dx + 12 + slide, cy = top + 12;
                Draw.round(g, cx, cy, cx + 40, cy + 40, Draw.alpha(ACCENT, da));
                Draw.rect(g, cx + 1, cy + 1, cx + 39, cy + 39, Draw.alpha(0xFF10151A, da));
                Draw.push(g, cx + 4, cy + 4, 2f);
                Draw.item(g, stackOf(mob.icon()), 0, 0);
                Draw.pop(g);
                if (da < 0.98f) Draw.rect(g, cx + 1, cy + 1, cx + 39, cy + 39, Draw.alpha(0xFF10151A, 1f - da));
                Draw.push(g, cx + 48, cy + 2, 1.5f);
                Draw.text(g, font, font.plainSubstrByWidth(mob.name(), (int) ((dw - 72) / 1.5f)), 0, 0, Draw.alpha(TechTreeScreen.TXT, da), true);
                Draw.pop(g);
                Draw.text(g, font, TechTree.MODID + ":" + mob.id(), cx + 48, cy + 20, Draw.alpha(TechTreeScreen.TXT3, da), false);
                if (!mob.info().isEmpty()) Draw.text(g, font, font.plainSubstrByWidth(mob.info(), dw - 72), cx + 48, cy + 31, Draw.alpha(TechTreeScreen.TXT2, da), false);

                int y = cy + 52;
                Draw.rect(g, cx, y - 4, cx + dw - 24, y - 3, Draw.alpha(TechTreeScreen.PANEL_LINE, da));
                Draw.rect(g, cx, y + 1, cx + 2, y + 8, Draw.alpha(ACCENT, da));
                Draw.text(g, font, "DROPS", cx + 6, y, Draw.alpha(TechTreeScreen.TXT2, da), true);
                y += 15;
                if (mob.drops().isEmpty()) {
                    Draw.text(g, font, "No drops listed yet.", cx, y, Draw.alpha(TechTreeScreen.TXT3, da), false);
                }
                int rowW = dw - 24;
                for (Drop d : mob.drops()) {
                    if (y + 20 > bottom - 4) break;
                    TechNode node = TechTree.nodeOfItem(d.itemId());
                    boolean hv = mx >= cx && mx < cx + rowW && my >= y && my < y + 20;
                    Draw.round(g, cx, y, cx + rowW, y + 20, Draw.alpha(hv && node != null ? 0xFF1F2931 : 0xFF10151A, da));
                    Draw.round(g, cx + 1, y + 1, cx + 19, y + 19, Draw.alpha(0xFF252E37, da));
                    Draw.rect(g, cx + 2, y + 2, cx + 18, y + 18, Draw.alpha(TechTreeScreen.SLOT, da));
                    Draw.item(g, stackOf(d.itemId()), cx + 2, y + 2);
                    if (da < 0.98f) Draw.rect(g, cx + 2, y + 2, cx + 18, y + 18, Draw.alpha(TechTreeScreen.SLOT, 1f - da));
                    String qty = d.max() > d.min() ? d.min() + "-" + d.max() + "x" : d.min() + "x";
                    String pct = d.chance() + "%";
                    int rx = cx + rowW - 6;
                    Draw.text(g, font, pct, rx - font.width(pct), y + 6, Draw.alpha(d.chance() >= 100 ? TechTreeScreen.GREEN : d.chance() >= 30 ? TechTreeScreen.GOLD : TechTreeScreen.RED, da), false);
                    Draw.text(g, font, qty, rx - font.width(pct) - 8 - font.width(qty), y + 6, Draw.alpha(TechTreeScreen.TXT2, da), false);
                    int nameMax = rowW - 28 - font.width(pct) - font.width(qty) - 22;
                    Draw.text(g, font, font.plainSubstrByWidth(itemName(d.itemId()), Math.max(20, nameMax)), cx + 24, y + 6, Draw.alpha(TechTreeScreen.TXT, da), false);
                    List<Component> tip = new ArrayList<>();
                    tip.add(Component.literal(itemName(d.itemId())));
                    tip.add(Component.literal("§7" + qty + " · " + pct + " chance"));
                    if (node != null && isRevealed(node)) tip.add(Component.literal("§8Click to show it on the tree"));
                    final String itemId = d.itemId();
                    zones.add(new Zone(cx, y, cx + rowW, y + 20, node != null && isRevealed(node) ? () -> showOnTree(itemId) : () -> {}, tip));
                    y += 22;
                }
            }

            int y0 = height - FOOT;
            Draw.rect(g, 0, y0, width, height, Draw.alpha(0xFF07090B, 0.97f * open));
            Draw.rect(g, 0, y0, width, y0 + 1, Draw.alpha(TechTreeScreen.PANEL_LINE, open));
            String[][] hints = { {"Click", "Select"}, {"Up", "Previous"}, {"Down", "Next"}, {"Esc", "Back"} };
            int x = 12;
            for (String[] h : hints) {
                int kw = font.width(h[0]) + 8, lw2 = font.width(h[1]);
                if (x + kw + 4 + lw2 > width - 12) break;
                Draw.key(g, font, h[0], x, y0 + 4, open);
                Draw.text(g, font, h[1], x + kw + 4, y0 + 6, Draw.alpha(TechTreeScreen.TXT3, open), false);
                x += kw + 4 + lw2 + 14;
            }

            for (Zone z : zones) {
                if (z.tip() != null && z.has(mx, my)) { Draw.tooltip(g, font, z.tip(), mx, my); break; }
            }
        }

        private static boolean isRevealed(TechNode n) {
            if (TechTreeCommon.CLIENT_UNLOCKED.contains(n.id())) return true;
            if (!TechTreeScreen.isSystemUnlocked(n.system())) return false;
            for (String p : n.parents()) if (!TechTreeCommon.CLIENT_UNLOCKED.contains(p)) return false;
            return true;
        }

        @Override public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
            if (e.button() == 0) {
                for (int i = zones.size() - 1; i >= 0; i--) {
                    Zone z = zones.get(i);
                    if (z.has(lastMx, lastMy)) { z.run().run(); return true; }
                }
                return true;
            }
            return super.mouseClicked(e, doubleClick);
        }

        @Override public boolean mouseScrolled(double mx, double my, double sx, double sy) {
            scroll -= (float) sy * 14f;
            return true;
        }

        @Override public boolean keyPressed(KeyEvent e) {
            int k = e.key();
            if (k == InputConstants.KEY_M || k == InputConstants.KEY_K || k == InputConstants.KEY_E) { onClose(); return true; }
            if (k == InputConstants.KEY_UP) { select(sel - 1); return true; }
            if (k == InputConstants.KEY_DOWN) { select(sel + 1); return true; }
            return super.keyPressed(e);
        }
    }

    static final class Draw {
        static void rect(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int argb) { g.fill(x0, y0, x1, y1, argb); }

        static void text(GuiGraphicsExtractor g, Font f, String s, int x, int y, int argb) { text(g, f, s, x, y, argb, false); }

        static void text(GuiGraphicsExtractor g, Font f, String s, int x, int y, int argb, boolean shadow) {
            if (((argb >>> 24) & 255) < 8) return;
            g.text(f, s, x, y, argb, shadow);
        }

        static void item(GuiGraphicsExtractor g, ItemStack st, int x, int y) { g.item(st, x, y); }

        static void push(GuiGraphicsExtractor g, float tx, float ty, float scale) {
            g.pose().pushMatrix();
            g.pose().translate(tx, ty);
            g.pose().scale(scale, scale);
        }

        static void pushAt(GuiGraphicsExtractor g, float cx, float cy, float s) {
            g.pose().pushMatrix();
            g.pose().translate(cx, cy);
            g.pose().scale(s, s);
            g.pose().translate(-cx, -cy);
        }

        static void pop(GuiGraphicsExtractor g) { g.pose().popMatrix(); }

        static void tooltip(GuiGraphicsExtractor g, Font f, List<Component> lines, int x, int y) {
            g.setComponentTooltipForNextFrame(f, lines, x, y);
        }

        static float clamp(float v) { return v < 0f ? 0f : Math.min(v, 1f); }

        static float clamp(float v, float lo, float hi) { return v < lo ? lo : Math.min(v, hi); }

        static float easeOut(float t) { float u = 1f - clamp(t); return 1f - u * u * u; }

        static float easeBack(float t) { float u = clamp(t) - 1f; return 1f + 2.70158f * u * u * u + 1.70158f * u * u; }

        static int alpha(int argb, float a) {
            int na = Math.round(((argb >>> 24) & 255) * clamp(a));
            return na << 24 | (argb & 0xFFFFFF);
        }

        static int mix(int a, int b, float t) {
            t = clamp(t);
            int aa = Math.round(((a >>> 24) & 255) * (1f - t) + ((b >>> 24) & 255) * t);
            int rr = Math.round(((a >> 16) & 255) * (1f - t) + ((b >> 16) & 255) * t);
            int gg = Math.round(((a >> 8) & 255) * (1f - t) + ((b >> 8) & 255) * t);
            int bb = Math.round((a & 255) * (1f - t) + (b & 255) * t);
            return aa << 24 | rr << 16 | gg << 8 | bb;
        }

        static int dim(int argb, float f) {
            int r = (int) (((argb >> 16) & 255) * f), gg = (int) (((argb >> 8) & 255) * f), b = (int) ((argb & 255) * f);
            return 0xFF000000 | r << 16 | gg << 8 | b;
        }

        static void round(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            if (x1 - x0 < 5 || y1 - y0 < 5) { rect(g, x0, y0, x1, y1, c); return; }
            rect(g, x0 + 2, y0, x1 - 2, y0 + 1, c);
            rect(g, x0 + 1, y0 + 1, x1 - 1, y0 + 2, c);
            rect(g, x0, y0 + 2, x1, y1 - 2, c);
            rect(g, x0 + 1, y1 - 2, x1 - 1, y1 - 1, c);
            rect(g, x0 + 2, y1 - 1, x1 - 2, y1, c);
        }

        static void rframe(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            rect(g, x0 + 2, y0, x1 - 2, y0 + 1, c);
            rect(g, x0 + 1, y0 + 1, x1 - 1, y0 + 2, c);
            rect(g, x0, y0 + 2, x0 + 2, y1 - 2, c);
            rect(g, x1 - 2, y0 + 2, x1, y1 - 2, c);
            rect(g, x0 + 1, y1 - 2, x1 - 1, y1 - 1, c);
            rect(g, x0 + 2, y1 - 1, x1 - 2, y1, c);
        }

        static void frame(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int t, int c) {
            rect(g, x0, y0, x1, y0 + t, c);
            rect(g, x0, y1 - t, x1, y1, c);
            rect(g, x0, y0 + t, x0 + t, y1 - t, c);
            rect(g, x1 - t, y0 + t, x1, y1 - t, c);
        }

        static void frame1(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            rect(g, x0 + 1, y0, x1 - 1, y0 + 1, c);
            rect(g, x0 + 1, y1 - 1, x1 - 1, y1, c);
            rect(g, x0, y0 + 1, x0 + 1, y1 - 1, c);
            rect(g, x1 - 1, y0 + 1, x1, y1 - 1, c);
        }

        static void glow(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int argb, float strength, int layers) {
            for (int i = 1; i <= layers; i++) {
                float f = 1f - (i - 1f) / layers;
                frame1(g, x0 - i, y0 - i, x1 + i, y1 + i, alpha(argb, strength * 0.40f * f * f));
            }
        }

        static void disc(GuiGraphicsExtractor g, int x, int y, int size, int outline, int fill) {
            round(g, x - 1, y - 1, x + size + 1, y + size + 1, outline);
            round(g, x, y, x + size, y + size, fill);
        }

        static void check(GuiGraphicsExtractor g, int x, int y, int c) {
            rect(g, x + 4, y, x + 5, y + 1, c);
            rect(g, x + 3, y + 1, x + 4, y + 2, c);
            rect(g, x, y + 2, x + 1, y + 3, c);
            rect(g, x + 2, y + 2, x + 3, y + 3, c);
            rect(g, x + 1, y + 3, x + 2, y + 4, c);
        }

        static void lock(GuiGraphicsExtractor g, int x, int y, int c) {
            rect(g, x + 2, y, x + 5, y + 1, c);
            rect(g, x + 1, y + 1, x + 2, y + 3, c);
            rect(g, x + 5, y + 1, x + 6, y + 3, c);
            rect(g, x, y + 3, x + 7, y + 8, c);
            rect(g, x + 3, y + 5, x + 4, y + 7, 0xFF0A0D10);
        }

        static void arrow(GuiGraphicsExtractor g, int x, int y, int c) {
            for (int dy = -2; dy <= 2; dy++) rect(g, x - 3, y + dy, x - 3 + (3 - Math.abs(dy)), y + dy + 1, c);
        }

        static int badge(GuiGraphicsExtractor g, Font f, int x, int y, String s, int color, float a) {
            int w = Math.max(11, f.width(s) + 6);
            round(g, x - 1, y - 1, x + w + 1, y + 12, alpha(0xFF0A0D10, a));
            round(g, x, y, x + w, y + 11, alpha(color, a));
            text(g, f, s, x + (w - f.width(s)) / 2, y + 2, alpha(0xFF1A1405, a), false);
            return w;
        }

        static void brackets(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int len, int c) {
            rect(g, x0, y0, x0 + len, y0 + 1, c);
            rect(g, x0, y0 + 1, x0 + 1, y0 + len, c);
            rect(g, x1 - len, y0, x1, y0 + 1, c);
            rect(g, x1 - 1, y0 + 1, x1, y0 + len, c);
            rect(g, x0, y1 - 1, x0 + len, y1, c);
            rect(g, x0, y1 - len, x0 + 1, y1 - 1, c);
            rect(g, x1 - len, y1 - 1, x1, y1, c);
            rect(g, x1 - 1, y1 - len, x1, y1 - 1, c);
        }

        static void key(GuiGraphicsExtractor g, Font f, String s, int x, int y, float a) {
            int w = f.width(s) + 8;
            round(g, x, y, x + w, y + 11, alpha(0xFF34404B, a));
            rect(g, x + 1, y + 1, x + w - 1, y + 9, alpha(0xFF181E24, a));
            text(g, f, s, x + 4, y + 2, alpha(0xFFD0D8E0, a), false);
        }

        static void button(GuiGraphicsExtractor g, Font f, String label, int x, int y, int w, int h, boolean hover, float a) {
            round(g, x, y, x + w, y + h, alpha(hover ? 0xFF566877 : 0xFF34404B, a));
            rect(g, x + 1, y + 1, x + w - 1, y + h - 1, alpha(hover ? 0xFF212D35 : 0xFF161C21, a));
            rect(g, x + 2, y + 1, x + w - 2, y + 2, alpha(0xFFFFFFFF, (hover ? 0.16f : 0.08f) * a));
            text(g, f, label, x + (w - f.width(label)) / 2, y + (h - 8) / 2, alpha(hover ? 0xFFFFFFFF : 0xFFD0D8E0, a), false);
        }

        static void vgrad(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int top, int bottom, int strip) {
            int h = y1 - y0;
            for (int y = 0; y < h; y += strip)
                rect(g, x0, y0 + y, x1, Math.min(y1, y0 + y + strip), mix(top, bottom, (y + strip / 2f) / h));
        }

        static void hgrad(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int left, int right, int strip) {
            int w = x1 - x0;
            for (int x = 0; x < w; x += strip)
                rect(g, x0 + x, y0, Math.min(x1, x0 + x + strip), y1, mix(left, right, (x + strip / 2f) / w));
        }

        static void shimmer(GuiGraphicsExtractor g, Font f, String s, int x, int y, int base, int hi, float t, float a) {
            int cx = x;
            for (int i = 0; i < s.length(); i++) {
                String ch = s.substring(i, i + 1);
                float w = 0.5f + 0.5f * (float) Math.sin(t * 2.4f - i * 0.45f);
                w = w * w * w * w;
                text(g, f, ch, cx, y, alpha(mix(base, hi, w), a), true);
                cx += f.width(ch) + 1;
            }
        }

        static void atom(GuiGraphicsExtractor g, int cx, int cy, float t, int color, float a) {
            for (int k = 0; k < 3; k++) {
                double th = k * Math.PI / 3.0, ct = Math.cos(th), st = Math.sin(th);
                for (int j = 0; j < 12; j++) {
                    double an = j * Math.PI / 6.0, ex = Math.cos(an) * 8.0, ey = Math.sin(an) * 3.0;
                    int px = cx + (int) Math.round(ex * ct - ey * st), py = cy + (int) Math.round(ex * st + ey * ct);
                    rect(g, px, py, px + 1, py + 1, alpha(color, 0.22f * a));
                }
                double an = t * 2.4 + k * 2.1, ex = Math.cos(an) * 8.0, ey = Math.sin(an) * 3.0;
                int px = cx + (int) Math.round(ex * ct - ey * st), py = cy + (int) Math.round(ex * st + ey * ct);
                rect(g, px - 1, py - 1, px + 1, py + 1, alpha(mix(color, 0xFFFFFFFF, 0.5f), a));
            }
            rect(g, cx - 1, cy - 1, cx + 2, cy + 2, alpha(color, a));
        }

        private Draw() {}
    }
}