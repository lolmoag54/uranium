package net.mcreator.uranium;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * ARBRE TECHNOLOGIQUE URANIUM — UN SEUL FICHIER (MC 26.1 / NeoForge 26.1).
 * Place dans src/main/java/net/mcreator/uranium/. Touche K en jeu.
 * Aucune modification de la classe principale MCreator.
 * Inspiré de SQ-Nuclear (MIT, (c) 2026 SideQuestChris).
 */
public final class UraniumTechTree {
    private UraniumTechTree() {}

    /**
     * Définition de l'arbre (construite à partir de tes éléments MCreator).
     *  - itemId : nom d'enregistrement de l'item/bloc (sans "modid:")
     *  - col / row : position dans la grille ; parents : nœuds requis avant (dessine les liens)
     *  - auto : débloqué d'office (nœud de départ)
     */
    public static final class TechTree {
        public static final String MODID = "uranium"; // = ID du mod dans Workspace settings

        static final int MATIERE = 0xFF6FCF3A, MACHINE = 0xFFE08A2B, ENERGIE = 0xFFF2D13B,
                         FLUIDE = 0xFF3AA0E0, NUCLEAIRE = 0xFFE0443A, METAL = 0xFF9AA4B0;

        public static final Map<String, TechNode> BY_ID = new LinkedHashMap<>();

        static {
            auto("start", "enricheduranium", "Welcome", "Thanks for using our mod.", 0, 2, NUCLEAIRE);

            // --- Ligne uranium ---
            add("raw", "rawuranium", "Raw uranium ore", "Mine raw uranium ore.", 1, 1, MATIERE, "start");
            add("crusher", "crusher", "Crusher", "Crushes raw uranium ore.", 2, 1, MACHINE, "raw");
            add("crushed", "cruched_rawuranium", "Crushed uranium", "Output of the crusher.", 3, 1, MATIERE, "crusher");
            add("yellowcake", "yellowcake", "Yellowcake", "Crushed uranium treated with sulfuric acid.", 4, 1, MATIERE, "crushed");
            add("hexa", "uraniumhexafluoride", "Uranium hexafluoride", "Yellowcake + fluor, ready to enrich.", 5, 1, NUCLEAIRE, "yellowcake", "fluor");
            add("centrifuge", "centrifuge", "Centrifuge", "Separates enriched from depleted uranium.", 6, 1, MACHINE, "hexa", "cable");
            add("enriched", "enricheduranium", "Enriched uranium", "Fissile uranium.", 7, 0, NUCLEAIRE, "centrifuge");
            add("depleted", "depleteduranium", "Depleted uranium", "Dense by-product of enrichment.", 7, 2, NUCLEAIRE, "centrifuge");

            // --- Fluor ---
            add("fluoride", "fluoride", "Fluoride", "Found in fluorite ore.", 1, 0, MATIERE, "start");
            add("fluor", "fluor", "Fluor", "Smelted from fluoride.", 2, 0, MATIERE, "fluoride");

            // --- Énergie ---
            add("solar", "solarpanel", "Solar panel", "Begin renewable energy.", 1, 3, ENERGIE, "start");
            add("cable", "cable", "Cables", "Carry energy between machines.", 2, 3, ENERGIE, "solar");
            add("battery", "grossebatterie", "Heavy battery", "Stores a large amount of energy.", 3, 3, ENERGIE, "cable");

            // --- Plomb / métal ---
            add("leadore", "leadore", "Lead ore", "Needed for pellets and the reactor.", 1, 5, METAL, "start");
            add("lead", "lingotdeplomb", "Lead ingot", "Smelted lead.", 2, 5, METAL, "leadore");
            add("concrete", "reinforcedconcrete", "Reinforced concrete", "Reactor shielding.", 3, 5, METAL, "lead");
            add("hammer", "steel_hammer", "Steel hammer", "Used to press pellets.", 3, 6, METAL, "lead");
            add("zicron", "zicron", "Zicron", "Raw zirconium.", 4, 7, METAL, "start");
            add("zicronium", "zicronium", "Zicronium", "Cladding for fuel rods.", 5, 7, METAL, "zicron");

            // --- Combustible / réacteur ---
            add("pellet", "uraniumpellet", "Uranium pellet", "Pressed enriched uranium.", 8, 0, NUCLEAIRE, "enriched", "hammer");
            add("fuelrod", "fuelrod", "Fuel rod", "Pellets in a zicronium cladding.", 9, 0, NUCLEAIRE, "pellet", "zicronium");
            add("reactor", "reactor", "Fission reactor", "We need a reactor now?", 10, 1, NUCLEAIRE, "fuelrod", "concrete");
            add("spent", "spentfuelrod", "Spent fuel rod", "Waste from the reactor.", 11, 1, NUCLEAIRE, "reactor");
        }

        private static void add(String id, String item, String title, String desc, int col, int row, int color, String... parents) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, color, List.of(parents), false));
        }

        private static void auto(String id, String item, String title, String desc, int col, int row, int color) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, color, List.of(), true));
        }

        private TechTree() {}
    }

    /** Un nœud de l'arbre. itemId = item "preuve" (l'avoir dans l'inventaire débloque le nœud) ; auto = débloqué d'office (itemId sert alors d'icône). */
    public record TechNode(String id, String itemId, String title, String desc,
                           int col, int row, int color, List<String> parents, boolean auto) {

        /** null si l'item n'existe pas (id à corriger dans TechTree.java) : l'écran n'a alors pas d'icône mais ne plante pas. */
        public Item item() {
            Identifier rl = Identifier.tryParse(itemId.contains(":") ? itemId : TechTree.MODID + ":" + itemId);
            return rl == null ? null : BuiltInRegistries.ITEM.getOptional(rl).orElse(null);
        }
    }

    /**
     * Logique serveur + réseau. Même principe que SQ-Nuclear (Discovered) :
     * un attachment sauvegardé sur le joueur, un scan de l'inventaire, un paquet de synchro.
     * Tout s'enregistre via des events : AUCUNE modification de la classe principale MCreator.
     */
    public static final class TechTreeCommon {

        /** Copie côté client des nœuds débloqués (remplie par le paquet). */
        public static final Set<String> CLIENT_UNLOCKED = new HashSet<>();

        private static final Codec<Set<String>> SET_CODEC =
                Codec.STRING.listOf().xmap(l -> (Set<String>) new LinkedHashSet<>(l), s -> new ArrayList<>(s));

        public static final AttachmentType<Set<String>> UNLOCKED =
                AttachmentType.<Set<String>>builder(() -> new LinkedHashSet<String>())
                        .serialize(SET_CODEC.fieldOf("v")).copyOnDeath().build();

        // ---------- Paquet serveur -> client ----------
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

        // ---------- Enregistrements (bus MOD) ----------
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
                            CLIENT_UNLOCKED.clear();
                            CLIENT_UNLOCKED.addAll(payload.unlocked());
                        }));
            }
        }

        // ---------- Événements de jeu (bus GAME) ----------
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

            /** Toutes les secondes : un item "preuve" dans l'inventaire débloque le nœud. */
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

    /** Touche pour ouvrir l'écran (K par défaut, modifiable dans les contrôles). */
    public static final class TechTreeClient {
        static final KeyMapping KEY = new KeyMapping("key." + TechTree.MODID + ".tech_tree",
                InputConstants.KEY_K, KeyMapping.Category.MISC);

        /** À appeler aussi depuis une procédure MCreator / un item livre si tu veux. */
        public static void open() {
            Minecraft.getInstance().setScreen(new TechTreeScreen());
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
            }
        }

        private TechTreeClient() {}
    }

    /**
     * Écran d'arbre technologique : déplacer (clic gauche + glisser), zoom (molette), R = recentrer.
     * Même idée que la vue "plant" de SQ-Nuclear (pan/zoom + nœuds en grille + liens),
     * mais en ~150 lignes et uniquement avec fill() pour les liens.
     */
    public static class TechTreeScreen extends Screen {
        private static final int CW = 84, RH = 66, BOX = 34;
        private static final int BG = 0xF00E0E10, PANEL = 0xFF17171A, LOCKED = 0xFF3A3A3A, DIM_LINE = 0xFF2A2A2E;

        private float panX, panY, zoom = 1f;
        private boolean dragging;

        public TechTreeScreen() { super(Component.literal("Tech tree")); }

        @Override public boolean isPauseScreen() { return false; }

        @Override protected void init() { fit(); }

        private void fit() {
            int maxC = 0, maxR = 0;
            for (TechNode n : TechTree.BY_ID.values()) { maxC = Math.max(maxC, n.col()); maxR = Math.max(maxR, n.row()); }
            float w = maxC * CW + BOX, h = maxR * RH + BOX + 14;
            zoom = Math.max(0.4f, Math.min(1.6f, Math.min((width - 60) / w, (height - 90) / h)));
            panX = (width - w * zoom) / 2f;
            panY = (height - h * zoom) / 2f + 10;
        }

        private static int nx(TechNode n) { return n.col() * CW; }
        private static int ny(TechNode n) { return n.row() * RH; }
        private static boolean unlocked(TechNode n) { return TechTreeCommon.CLIENT_UNLOCKED.contains(n.id()); }

        private static boolean available(TechNode n) {
            if (unlocked(n)) return false;
            for (String p : n.parents()) if (!TechTreeCommon.CLIENT_UNLOCKED.contains(p)) return false;
            return true;
        }

        private static int dim(int argb, float f) {
            int r = (int) (((argb >> 16) & 255) * f), gg = (int) (((argb >> 8) & 255) * f), b = (int) ((argb & 255) * f);
            return 0xFF000000 | r << 16 | gg << 8 | b;
        }

        private TechNode nodeAt(double mx, double my) {
            double wx = (mx - panX) / zoom, wy = (my - panY) / zoom;
            for (TechNode n : TechTree.BY_ID.values())
                if (wx >= nx(n) && wx < nx(n) + BOX && wy >= ny(n) && wy < ny(n) + BOX) return n;
            return null;
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt) {
            Draw.rect(g, 0, 0, width, height, BG);
            // grille de points qui suit le déplacement
            int step = Math.max(12, (int) (24 * zoom));
            for (int x = (int) (panX % step); x < width; x += step)
                for (int y = (int) (panY % step); y < height; y += step) Draw.rect(g, x, y, x + 1, y + 1, 0xFF202024);

            Draw.push(g, panX, panY, zoom);
            // liens
            for (TechNode n : TechTree.BY_ID.values()) {
                for (String pid : n.parents()) {
                    TechNode p = TechTree.BY_ID.get(pid);
                    if (p == null) continue;
                    int col = unlocked(p) ? (unlocked(n) ? n.color() : dim(n.color(), 0.6f)) : DIM_LINE;
                    link(g, nx(p) + BOX, ny(p) + BOX / 2, nx(n), ny(n) + BOX / 2, col);
                }
            }
            // nœuds
            TechNode hover = nodeAt(mx, my);
            for (TechNode n : TechTree.BY_ID.values()) drawNode(g, n, n == hover);
            Draw.pop(g);

            // en-tête / pied
            long done = TechTree.BY_ID.values().stream().filter(TechTreeScreen::unlocked).count();
            Draw.text(g, font, "URANIUM TECH TREE", 14, 12, 0xFFD8D8D8);
            Draw.text(g, font, done + " / " + TechTree.BY_ID.size(), 14, 24, 0xFF8C8C8C);
            Draw.text(g, font, "Drag: move   Wheel: zoom   R: recenter   Esc: close",
                    14, height - 16, 0xFF6C6C6C);

            if (hover != null) Draw.tooltip(g, font, tip(hover), mx, my);
        }

        /** Lien en équerre (horizontal → vertical → horizontal), 2 px d'épaisseur. */
        private void link(GuiGraphicsExtractor g, int ax, int ay, int bx, int by, int c) {
            int mid = bx - 12;
            Draw.rect(g, Math.min(ax, mid), ay - 1, Math.max(ax, mid), ay + 1, c);
            Draw.rect(g, mid - 1, Math.min(ay, by), mid + 1, Math.max(ay, by) + 1, c);
            Draw.rect(g, Math.min(mid, bx), by - 1, Math.max(mid, bx), by + 1, c);
        }

        private void drawNode(GuiGraphicsExtractor g, TechNode n, boolean hover) {
            int x = nx(n), y = ny(n);
            boolean un = unlocked(n), av = available(n);
            int border = un ? n.color() : av ? dim(n.color(), 0.6f) : LOCKED;
            if (hover) border = 0xFFFFFFFF;
            Draw.rect(g, x, y, x + BOX, y + BOX, border);
            Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, PANEL);
            Item it = n.item();
            Draw.item(g, new ItemStack(it != null ? it : Items.BARRIER), x + (BOX - 16) / 2, y + (BOX - 16) / 2);
            if (!un && !av) Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, 0xB00E0E10);
            if (un) Draw.rect(g, x + BOX - 8, y, x + BOX, y + 8, n.color()); // coin = débloqué
            String t = font.plainSubstrByWidth(n.title(), CW - 6);
            Draw.text(g, font, t, x + BOX / 2 - font.width(t) / 2, y + BOX + 4, un ? 0xFFE8E8E8 : av ? 0xFFA8A8A8 : 0xFF5A5A5A);
        }

        private List<Component> tip(TechNode n) {
            List<Component> l = new ArrayList<>();
            l.add(Component.literal(n.title()));
            l.add(Component.literal("§7" + n.desc()));
            if (unlocked(n)) l.add(Component.literal("§aUnlocked"));
            else if (available(n)) l.add(Component.literal("§eAvailable §7— obtain: " + n.itemId()));
            else {
                StringBuilder sb = new StringBuilder("§cLocked §7— requires: ");
                boolean first = true;
                for (String p : n.parents()) {
                    if (TechTreeCommon.CLIENT_UNLOCKED.contains(p)) continue;
                    TechNode pn = TechTree.BY_ID.get(p);
                    sb.append(first ? "" : ", ").append(pn != null ? pn.title() : p);
                    first = false;
                }
                l.add(Component.literal(sb.toString()));
            }
            return l;
        }

        // ---------- entrées ----------
        @Override public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
            if (e.button() == 0) { dragging = true; return true; }
            return super.mouseClicked(e, doubleClick);
        }

        @Override public boolean mouseReleased(MouseButtonEvent e) {
            dragging = false;
            return super.mouseReleased(e);
        }

        @Override public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
            if (dragging) { panX += (float) dx; panY += (float) dy; return true; }
            return super.mouseDragged(e, dx, dy);
        }

        @Override public boolean mouseScrolled(double mx, double my, double sx, double sy) {
            float z = Math.max(0.35f, Math.min(2.5f, zoom * (sy > 0 ? 1.15f : 1f / 1.15f)));
            float wx = (float) (mx - panX) / zoom, wy = (float) (my - panY) / zoom; // zoom centré sur la souris
            panX = (float) mx - wx * z;
            panY = (float) my - wy * z;
            zoom = z;
            return true;
        }

        @Override public boolean keyPressed(KeyEvent e) {
            if (e.key() == InputConstants.KEY_R) { fit(); return true; }
            return super.keyPressed(e);
        }
    }

    /**
     * SEUL fichier qui touche à l'API de rendu. Écrit pour MC 26.1 / NeoForge 26.1
     * (GuiGraphicsExtractor, pile de matrices 2D).
     */
    static final class Draw {
        static void rect(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int argb) { g.fill(x0, y0, x1, y1, argb); }

        static void text(GuiGraphicsExtractor g, Font f, String s, int x, int y, int argb) { g.text(f, s, x, y, argb, false); }

        static void item(GuiGraphicsExtractor g, ItemStack st, int x, int y) { g.item(st, x, y); }

        static void push(GuiGraphicsExtractor g, float tx, float ty, float scale) {
            g.pose().pushMatrix();
            g.pose().translate(tx, ty);
            g.pose().scale(scale, scale);
        }

        static void pop(GuiGraphicsExtractor g) { g.pose().popMatrix(); }

        static void tooltip(GuiGraphicsExtractor g, Font f, List<Component> lines, int x, int y) {
            g.setComponentTooltipForNextFrame(f, lines, x, y);
        }

        private Draw() {}
    }
}
