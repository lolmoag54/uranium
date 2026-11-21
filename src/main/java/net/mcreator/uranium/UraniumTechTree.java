package net.mcreator.uranium;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
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
 *
 * v2 :
 *  - animations : apparition en cascade, caméra fluide, survol, halo des nœuds disponibles,
 *    flux d'énergie sur les liens, explosion de particules au déblocage ;
 *  - catégories : onglets filtrants (clic, Tab ou touches 0-9) avec progression par catégorie ;
 *  - aide au craft : clic sur un nœud = panneau de détails (prérequis, stock dans l'inventaire,
 *    "sert à", feuille de route numérotée sur l'arbre) ; recette explicite facultative (voir recipe()) ;
 *  - rendu retravaillé (coins arrondis, halos, ombres, dégradés) ;
 *  - bouton d'accès dans l'inventaire (survie + créatif).
 */
public final class UraniumTechTree {
    private UraniumTechTree() {}

    /** Catégorie d'un nœud : nom affiché dans les onglets + couleur d'accent. */
    public record Category(String id, String label, int color) {}

    /** Ingrédient d'une recette d'aide : itemId (avec ou sans "modid:") + quantité. */
    public record Ing(String itemId, int count) {}

    /** Recette d'aide (facultative) : poste de craft (texte libre), ingrédients, quantité produite. */
    public record Recipe(String station, List<Ing> inputs, int out) {}

    /**
     * Définition de l'arbre (construite à partir de tes éléments MCreator).
     *  - itemId : nom d'enregistrement de l'item/bloc (sans "modid:")
     *  - col / row : position dans la grille ; parents : nœuds requis avant (dessine les liens)
     *  - auto : débloqué d'office (nœud de départ)
     *  - catégorie : MATIERE, MACHINE, ENERGIE, FLUIDE, NUCLEAIRE, METAL (couleur + onglet)
     */
    public static final class TechTree {
        public static final String MODID = "uranium"; // = ID du mod dans Workspace settings

        // Catégories : le 7e paramètre de add(...) choisit celle du nœud (nom = onglet, couleur = accent).
        static final Category MATIERE   = new Category("materials", "Materials", 0xFF6FCF3A);
        static final Category MACHINE   = new Category("machines",  "Machines",  0xFFE08A2B);
        static final Category ENERGIE   = new Category("energy",    "Energy",    0xFFF2D13B);
        static final Category FLUIDE    = new Category("fluids",    "Fluids",    0xFF3AA0E0);
        static final Category NUCLEAIRE = new Category("nuclear",   "Nuclear",   0xFFE0443A);
        static final Category METAL     = new Category("metals",    "Metals",    0xFF9AA4B0);
        /** Ordre des onglets (seules les catégories utilisées par au moins un nœud sont affichées). */
        static final List<Category> CATEGORIES = List.of(MATIERE, METAL, MACHINE, ENERGIE, FLUIDE, NUCLEAIRE);

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

            // --- Aide au craft : recettes explicites (FACULTATIF) ---
            // Sans recette, le panneau de détails affiche les nœuds parents sous "Needs".
            // Pour montrer la vraie recette (poste, quantités), ajoute une ligne par nœud, par exemple :
            //   recipe("hexa", "Crafting table", 1, ing("yellowcake", 1), ing("fluor", 1));
            //   recipe("concrete", "Crafting table", 2, ing("lingotdeplomb", 4), ing("minecraft:iron_ingot", 2));
        }

        private static void add(String id, String item, String title, String desc, int col, int row, Category cat, String... parents) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, cat, List.of(parents), false, null));
        }

        private static void auto(String id, String item, String title, String desc, int col, int row, Category cat) {
            BY_ID.put(id, new TechNode(id, item, title, desc, col, row, cat, List.of(), true, null));
        }

        private static Ing ing(String item, int count) { return new Ing(item, count); }

        /** Attache une recette d'aide à un nœud déjà défini (poste de craft, quantité produite, ingrédients). */
        private static void recipe(String nodeId, String station, int out, Ing... inputs) {
            TechNode n = BY_ID.get(nodeId);
            if (n != null) BY_ID.put(nodeId, n.withRecipe(new Recipe(station, List.of(inputs), out)));
        }

        /** null si l'item n'existe pas (id à corriger plus haut) : l'écran n'a alors pas d'icône mais ne plante pas. */
        public static Item resolve(String itemId) {
            Identifier rl = Identifier.tryParse(itemId.contains(":") ? itemId : MODID + ":" + itemId);
            return rl == null ? null : BuiltInRegistries.ITEM.getOptional(rl).orElse(null);
        }

        /** Nœuds qui ont {@code id} pour parent direct ("sert à"). */
        public static List<TechNode> childrenOf(String id) {
            List<TechNode> l = new ArrayList<>();
            for (TechNode n : BY_ID.values()) if (n.parents().contains(id)) l.add(n);
            return l;
        }

        /** {@code id} + tous ses ancêtres (parents, grands-parents...) : le chemin complet pour y arriver. */
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

        /** Plus longue chaîne de parents jusqu'à une racine (sert à ordonner la feuille de route). */
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

    /** Un nœud de l'arbre. itemId = item "preuve" (l'avoir dans l'inventaire débloque le nœud) ; auto = débloqué d'office (itemId sert alors d'icône). */
    public record TechNode(String id, String itemId, String title, String desc,
                           int col, int row, Category cat, List<String> parents, boolean auto, Recipe recipe) {

        /** Couleur d'accent = celle de la catégorie. */
        public int color() { return cat.color(); }

        /** null si l'item n'existe pas (id à corriger plus haut) : l'écran n'a alors pas d'icône mais ne plante pas. */
        public Item item() { return TechTree.resolve(itemId); }

        public TechNode withRecipe(Recipe r) {
            return new TechNode(id, itemId, title, desc, col, row, cat, parents, auto, r);
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

        /** Techs débloquées depuis la dernière ouverture de l'écran (fait clignoter le bouton d'inventaire). */
        public static int CLIENT_NEW = 0;
        /** Faux tant que la 1re synchro de la session n'est pas arrivée (pour ne pas compter l'existant comme "nouveau"). */
        static boolean CLIENT_SYNCED = false;

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


    /** Touche pour ouvrir l'écran (K par défaut, modifiable dans les contrôles) + bouton ☢ dans l'inventaire. */
    public static final class TechTreeClient {
        static final KeyMapping KEY = new KeyMapping("key." + TechTree.MODID + ".tech_tree",
                InputConstants.KEY_K, KeyMapping.Category.MISC);

        /** Bouton actuellement posé sur un écran d'inventaire + l'écran qui le porte. */
        private static Button invButton;
        private static Screen invOwner;

        /** À appeler aussi depuis une procédure MCreator / un item livre si tu veux. */
        public static void open() { open(null); }

        /** Ouvre l'arbre ; à la fermeture on revient sur {@code parent} (ex. l'inventaire), ou au jeu si null. */
        public static void open(Screen parent) {
            Minecraft.getInstance().setScreen(new TechTreeScreen(parent));
        }

        /** Libellé du bouton : ☢ vert ; jaune clignotant quand une techno vient d'être débloquée. */
        private static Component buttonLabel() {
            boolean flash = TechTreeCommon.CLIENT_NEW > 0 && (System.currentTimeMillis() / 350L) % 2L == 0L;
            return Component.literal((flash ? "§e" : "§a") + "\u2622");
        }

        /**
         * Position du bouton :
         *  - inventaire survie : juste à droite du bouton "livre de recettes" (même ordonnée height/2 - 22),
         *    donc il suit la fenêtre quand le livre s'ouvre ou se ferme ;
         *  - inventaire créatif (195 x 136, centré) : à gauche du cadre.
         */
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
                // bouton d'inventaire : suit le décalage du livre de recettes, clignote s'il y a du nouveau
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

            /** Ajoute le bouton ☢ sur l'inventaire (survie) et sur l'inventaire créatif. */
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

            /** On quitte le monde : on oublie l'état de la session (le serveur renverra tout à la prochaine connexion). */
            @SubscribeEvent
            public static void logout(ClientPlayerNetworkEvent.LoggingOut e) {
                TechTreeCommon.CLIENT_UNLOCKED.clear();
                TechTreeCommon.CLIENT_NEW = 0;
                TechTreeCommon.CLIENT_SYNCED = false;
            }
        }

        private TechTreeClient() {}
    }


    /**
     * Écran d'arbre technologique.
     *   glisser = déplacer · molette = zoom · clic sur un nœud = détails + aide au craft
     *   R = recentrer · onglets / Tab / 0-9 = catégorie · Échap = fermer (d'abord le panneau)
     * Tout est animé (voir step()). Tout le dessin passe par Draw, la seule classe qui touche à l'API de rendu.
     */
    public static class TechTreeScreen extends Screen {
        private static final int CW = 84, RH = 66, BOX = 34;        // grille des nœuds
        private static final int HEADER = 52, FOOT = 18;            // bandeaux haut / bas
        private static final int STARS = 42;                        // particules d'ambiance
        // palette
        private static final int BG_TOP = 0xFF0A0D10, BG_BOT = 0xFF11161B;
        private static final int PANEL = 0xFF13181D, PANEL_LINE = 0xFF27303A, SLOT = 0xFF0E1216;
        private static final int TXT = 0xFFE6EAEE, TXT2 = 0xFF9AA4AE, TXT3 = 0xFF5C6670;
        private static final int LOCKED = 0xFF3A424B, DIM_LINE = 0xFF2A323A;
        private static final int GOLD = 0xFFF2C94C, GREEN = 0xFF58D26E, RED = 0xFFE0645A;

        /** Animation par nœud : tout est lissé (survol, sélection, atténuation) + instant du dernier déblocage. */
        private static final class Anim { float hover, sel, dim, unlock = -99f; }

        private static final class Particle { float x, y, vx, vy, life, max; int color; }

        /** Zone cliquable de l'interface (reconstruite à chaque image) avec infobulle facultative. */
        private record Hit(int x0, int y0, int x1, int y1, Runnable run, List<Component> tip) {
            boolean has(int x, int y) { return x >= x0 && x < x1 && y >= y0 && y < y1; }
        }

        /** Lien en équerre (horizontal → vertical → horizontal) : géométrie + position le long du tracé. */
        private static final class Route {
            final int ax, ay, mid, bx, by, l1, l2, l3;
            final float len;

            Route(int ax, int ay, int bx, int by) {
                this.ax = ax; this.ay = ay; this.bx = bx; this.by = by;
                this.mid = bx - 12;
                this.l1 = Math.abs(mid - ax); this.l2 = Math.abs(by - ay); this.l3 = Math.abs(bx - mid);
                this.len = l1 + l2 + l3;
            }

            /** Point situé à la distance s du départ → out[0], out[1]. */
            void at(float s, float[] out) {
                if (s < l1) { out[0] = ax + Math.signum(mid - ax) * s; out[1] = ay; }
                else if (s < l1 + l2) { out[0] = mid; out[1] = ay + Math.signum(by - ay) * (s - l1); }
                else { out[0] = mid + Math.signum(bx - mid) * (s - l1 - l2); out[1] = by; }
            }
        }

        private final Screen parent;
        private boolean opened;
        // caméra : valeurs affichées + cibles (le mouvement est lissé vers la cible)
        private float panX, panY, zoom = 1f, tPanX, tPanY, tZoom = 1f;
        // souris
        private boolean dragging;
        private float dragDist;
        private int lastMx = -1, lastMy = -1;
        private TechNode hovered;
        // temps : secondes depuis l'ouverture ; lastNano = horloge du dernier rendu
        private long lastNano;
        private float time;
        // catégories (onglets)
        private List<Category> cats = List.of();
        private Category filter;
        private final float[] chipAnim = new float[16];
        // sélection + panneau de détails + feuille de route
        private TechNode selected, shown;
        private float panelAnim, panelScroll, panelContentH;
        private Set<String> path = Set.of();
        private final Map<String, Integer> steps = new HashMap<>();
        private final List<TechNode> stepList = new ArrayList<>();
        // animations
        private final Map<String, Anim> anims = new HashMap<>();
        private final List<Particle> parts = new ArrayList<>();
        private final Random rnd = new Random();
        private Set<String> known = new HashSet<>();
        private float shownProgress;
        private final float[] starX = new float[STARS], starY = new float[STARS], starV = new float[STARS], starP = new float[STARS];
        // zones cliquables + cache d'icônes
        private final List<Hit> hits = new ArrayList<>();
        private final Map<String, ItemStack> stacks = new HashMap<>();
        // curseur de mise en page du panneau
        private int py, pTop, pBot, pX, pW;
        private float pA = 1f;

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

        /** Échap / bouton retour : on revient à l'écran d'où l'on vient (inventaire) ou au jeu. */
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
                fit(true);
                opened = true;
            } else {
                fit(false);
            }
        }

        /** Cadre tout l'arbre dans la zone libre (entre l'en-tête et le pied de page), en douceur. */
        private void fit(boolean instant) {
            int maxC = 0, maxR = 0;
            for (TechNode n : TechTree.BY_ID.values()) { maxC = Math.max(maxC, n.col()); maxR = Math.max(maxR, n.row()); }
            float w = maxC * CW + BOX, h = maxR * RH + BOX + 14;
            float aw = width - 40f, ah = height - HEADER - FOOT - 18f;
            float z = Draw.clamp(Math.min(aw / w, ah / h), 0.3f, 1.6f);
            tZoom = z;
            tPanX = (width - w * z) / 2f;
            tPanY = HEADER + 8f + (ah - h * z) / 2f;
            if (instant) { zoom = tZoom; panX = tPanX; panY = tPanY; }
        }

        // ---------- état du joueur ----------
        private static int nx(TechNode n) { return n.col() * CW; }
        private static int ny(TechNode n) { return n.row() * RH; }
        private static boolean unlocked(TechNode n) { return TechTreeCommon.CLIENT_UNLOCKED.contains(n.id()); }

        private static boolean available(TechNode n) {
            if (unlocked(n)) return false;
            for (String p : n.parents()) if (!TechTreeCommon.CLIENT_UNLOCKED.contains(p)) return false;
            return true;
        }

        /** Nombre d'exemplaires de l'item dans l'inventaire du joueur (aide au craft). */
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

        /** Apparition en cascade de gauche à droite : 0 → 1. */
        private float appear(TechNode n) {
            return Draw.clamp((time - 0.10f - n.col() * 0.04f - n.row() * 0.012f) / 0.38f);
        }

        private ItemStack stackOf(String itemId) {
            return stacks.computeIfAbsent(itemId, k -> {
                Item it = TechTree.resolve(k);
                return new ItemStack(it != null ? it : Items.BARRIER);
            });
        }

        private ItemStack stack(TechNode n) { return stackOf(n.itemId()); }

        /** Nom affiché pour un ingrédient : titre du nœud correspondant, sinon l'id mis en forme ("iron_ingot" → "Iron ingot"). */
        private String itemName(String itemId) {
            for (TechNode m : TechTree.BY_ID.values()) if (m.itemId().equals(itemId)) return m.title();
            String s = itemId.substring(itemId.indexOf(':') + 1).replace('_', ' ');
            return s.isEmpty() ? itemId : Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }

        private TechNode nodeAt(double mx, double my) {
            double wx = (mx - panX) / zoom, wy = (my - panY) / zoom;
            for (TechNode n : TechTree.BY_ID.values())
                if (wx >= nx(n) && wx < nx(n) + BOX && wy >= ny(n) && wy < ny(n) + BOX) return n;
            return null;
        }

        // ---------- mise en page ----------
        private int panelW() { return Math.max(132, Math.min(184, Math.round(width * 0.40f))); }

        private int panelX() { return width - Math.round(panelW() * Draw.easeOut(panelAnim)); }

        /** Vrai si la souris est sur l'en-tête, le pied de page ou le panneau (pas sur l'arbre). */
        private boolean overUi(double mx, double my) {
            return my < HEADER || my >= height - FOOT || (panelAnim > 0.02f && mx >= panelX());
        }

        // ---------- animations : appelé une fois par image ----------
        private void step(float dt, int mx, int my) {
            // caméra : on glisse vers la cible (déplacement, zoom, recentrage)
            float k = 1f - (float) Math.exp(-dt * 14f);
            panX += (tPanX - panX) * k;
            panY += (tPanY - panY) * k;
            zoom += (tZoom - zoom) * k;

            hovered = overUi(mx, my) || (dragging && dragDist >= 4f) ? null : nodeAt(mx, my);
            float kh = 1f - (float) Math.exp(-dt * 16f), kd = 1f - (float) Math.exp(-dt * 9f);
            for (TechNode n : TechTree.BY_ID.values()) {
                Anim a = anim(n);
                a.hover += ((n == hovered ? 1f : 0f) - a.hover) * kh;
                a.sel += ((n == selected ? 1f : 0f) - a.sel) * kh;
                float byCat = filter != null && n.cat() != filter ? 1f : 0f;          // onglet actif
                float byPath = selected != null && !path.contains(n.id()) ? 0.55f : 0f; // feuille de route
                a.dim += (Math.max(byCat, byPath) - a.dim) * kd;
            }
            for (int i = 0; i < chipAnim.length; i++) {
                boolean on = i == 0 ? filter == null : i - 1 < cats.size() && cats.get(i - 1) == filter;
                chipAnim[i] += ((on ? 1f : 0f) - chipAnim[i]) * kd;
            }
            panelAnim += ((selected != null ? 1f : 0f) - panelAnim) * (1f - (float) Math.exp(-dt * 12f));
            if (selected == null && panelAnim < 0.01f) shown = null;

            // déblocage pendant que l'écran est ouvert → explosion + mise à jour de la feuille de route
            Set<String> cur = TechTreeCommon.CLIENT_UNLOCKED;
            if (cur.size() != known.size() || !known.containsAll(cur)) {
                for (String id : cur) {
                    TechNode n = TechTree.BY_ID.get(id);
                    if (n != null && !known.contains(id)) burst(n);
                }
                known = new HashSet<>(cur);
                if (selected != null) computePath();
            }
            TechTreeCommon.CLIENT_NEW = 0; // l'écran est ouvert : plus rien de "nouveau"

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

        /** Gerbe de particules + onde de choc autour d'un nœud qui vient d'être débloqué. */
        private void burst(TechNode n) {
            anim(n).unlock = time;
            float cx = nx(n) + BOX / 2f, cy = ny(n) + BOX / 2f;
            for (int i = 0; i < 24; i++) {
                Particle p = new Particle();
                double a = rnd.nextDouble() * Math.PI * 2.0, v = 30.0 + rnd.nextFloat() * 70.0;
                p.x = cx;
                p.y = cy;
                p.vx = (float) (Math.cos(a) * v);
                p.vy = (float) (Math.sin(a) * v) - 10f;
                p.max = p.life = 0.6f + rnd.nextFloat() * 0.5f;
                p.color = i % 3 == 0 ? 0xFFFFFFFF : n.color();
                parts.add(p);
            }
        }

        // ---------- sélection, feuille de route, caméra ----------
        private void selectNode(TechNode n) {
            selected = n;
            if (n == null) { computePath(); return; }
            shown = n;
            panelScroll = 0f;
            computePath();
            revealNode(n);
        }

        /** Chemin complet vers le nœud sélectionné + étapes encore à franchir, numérotées dans l'ordre. */
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

        /** Décale la caméra pour que le nœud ne soit ni sous le panneau ni sous les bandeaux. */
        private void revealNode(TechNode n) {
            float freeW = width - panelW() - 12f;
            float sxn = tPanX + (nx(n) + BOX / 2f) * tZoom, syn = tPanY + (ny(n) + BOX / 2f) * tZoom;
            float mX = 40f, mTop = HEADER + 36f, mBot = height - FOOT - 40f;
            if (sxn > freeW - mX) tPanX -= sxn - (freeW - mX);
            else if (sxn < mX) tPanX += mX - sxn;
            if (syn > mBot) tPanY -= syn - mBot;
            else if (syn < mTop) tPanY += mTop - syn;
        }

        /** Sélectionne un nœud et le centre dans la zone libre (clic sur une icône du panneau). */
        private void focusNode(TechNode n) {
            selectNode(n);
            tPanX = (width - panelW()) / 2f - (nx(n) + BOX / 2f) * tZoom;
            tPanY = (HEADER + (height - FOOT)) / 2f - (ny(n) + BOX / 2f) * tZoom;
        }

        private void setFilter(Category c) { filter = (c != null && c == filter) ? null : c; }

        private void cycleFilter() {
            int i = filter == null ? 0 : cats.indexOf(filter) + 1;
            i = (i + 1) % (cats.size() + 1);
            filter = i == 0 ? null : cats.get(i - 1);
        }

        // ---------- entrées ----------
        @Override public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
            if (e.button() == 0) {
                for (int i = hits.size() - 1; i >= 0; i--) {
                    Hit h = hits.get(i);
                    if (h.has(lastMx, lastMy)) { h.run().run(); return true; }
                }
                if (overUi(lastMx, lastMy)) return true;
                dragging = true;
                dragDist = 0f;
                tPanX = panX; tPanY = panY; tZoom = zoom; // fige l'animation de caméra en cours
                return true;
            }
            return super.mouseClicked(e, doubleClick);
        }

        @Override public boolean mouseReleased(MouseButtonEvent e) {
            // un "clic" = relâché sans avoir vraiment glissé : on sélectionne le nœud (ou on referme le panneau)
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
                panelScroll -= (float) sy * 14f; // défilement du panneau (borné au dessin)
                return true;
            }
            float z = Draw.clamp(tZoom * (sy > 0 ? 1.15f : 1f / 1.15f), 0.3f, 2.5f);
            float wx = (float) (mx - tPanX) / tZoom, wy = (float) (my - tPanY) / tZoom; // zoom centré sur la souris
            tPanX = (float) mx - wx * z;
            tPanY = (float) my - wy * z;
            tZoom = z;
            return true;
        }

        @Override public boolean keyPressed(KeyEvent e) {
            int k = e.key();
            if (k == InputConstants.KEY_ESCAPE && selected != null) { selectNode(null); return true; }
            if (k == InputConstants.KEY_R) { fit(false); return true; }
            if (k == InputConstants.KEY_K || k == InputConstants.KEY_E) { onClose(); return true; }
            if (k == InputConstants.KEY_TAB) { cycleFilter(); return true; }
            int d = k - InputConstants.KEY_0;
            if (d >= 0 && d <= 9) {
                if (d == 0) filter = null;
                else if (d <= cats.size()) filter = cats.get(d - 1);
                return true;
            }
            return super.keyPressed(e);
        }

        // =====================================================================
        //  RENDU
        // =====================================================================
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
            drawPanel(g, mx, my);
            drawFooter(g, open);

            // infobulle : zones du panneau d'abord, sinon nœud survolé
            List<Component> tip = null;
            for (Hit h : hits) if (h.tip() != null && h.has(mx, my)) { tip = h.tip(); break; }
            if (tip != null) Draw.tooltip(g, font, tip, mx, my);
            else if (hovered != null && hovered != selected) Draw.tooltip(g, font, tip(hovered), mx, my);
        }

        private void drawBackground(GuiGraphicsExtractor g, float open) {
            Draw.vgrad(g, 0, 0, width, height, Draw.alpha(BG_TOP, 0.96f * open), Draw.alpha(BG_BOT, 0.96f * open), 6);
            // grille de points ancrée dans le monde (suit le déplacement ET le zoom)
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
                    Draw.rect(g, qx, qy, qx + 1, qy + 1, Draw.alpha(major ? 0xFF3C4853 : 0xFF222A32, open));
                }
            }
            // particules d'ambiance : lucioles vertes qui montent doucement (légère parallaxe)
            for (int i = 0; i < STARS; i++) {
                float yy = (starY[i] * height - time * starV[i] * 5f) % height;
                if (yy < 0f) yy += height;
                float xx = starX[i] * width + (float) Math.sin(time * 0.6f + starP[i]) * 5f + panX * 0.04f * starV[i];
                xx = ((xx % width) + width) % width;
                float tw = 0.5f + 0.5f * (float) Math.sin(time * 1.7f + starP[i] * 3f);
                int sz = starV[i] > 1.5f ? 2 : 1, qx = (int) xx, qy = (int) yy;
                Draw.rect(g, qx, qy, qx + sz, qy + sz, Draw.alpha(0xFF6FCF3A, (0.05f + 0.16f * tw) * open));
            }
            // vignette : bords sombres
            int c0 = Draw.alpha(0xFF000000, 0f), c1 = Draw.alpha(0xFF000000, 0.40f * open);
            Draw.hgrad(g, 0, HEADER, 40, height - FOOT, c1, c0, 4);
            Draw.hgrad(g, width - 40, HEADER, width, height - FOOT, c0, c1, 4);
            Draw.vgrad(g, 0, height - FOOT - 28, width, height - FOOT, c0, Draw.alpha(0xFF000000, 0.35f * open), 4);
        }

        // ---------- liens ----------
        private void drawLinks(GuiGraphicsExtractor g) {
            float[] pos = new float[2];
            for (TechNode n : TechTree.BY_ID.values()) {
                float ap = appear(n);
                if (ap <= 0f) continue;
                boolean nu = unlocked(n);
                for (String pid : n.parents()) {
                    TechNode p = TechTree.BY_ID.get(pid);
                    if (p == null) continue;
                    boolean pu = unlocked(p);
                    boolean onPath = selected != null && path.contains(n.id()) && path.contains(pid);
                    float al = ap * (1f - 0.72f * Math.max(anim(n).dim, anim(p).dim));
                    int base = n.color();
                    int col = pu ? (nu ? base : Draw.dim(base, 0.65f)) : DIM_LINE;
                    if (onPath) col = Draw.mix(col, 0xFFFFFFFF, 0.30f);
                    int th = onPath ? 3 : 2;
                    Route r = new Route(nx(p) + BOX, ny(p) + BOX / 2, nx(n), ny(n) + BOX / 2);
                    if (!onScreen(Math.min(r.ax, r.bx) - 6, Math.min(r.ay, r.by) - 6, Math.abs(r.bx - r.ax) + 12, Math.abs(r.by - r.ay) + 12)) continue;
                    route(g, r, th + 2, Draw.alpha(0xFF000000, 0.45f * al)); // contour sombre : lisibilité aux croisements
                    route(g, r, th, Draw.alpha(col, al));
                    Draw.arrow(g, r.bx, r.by, Draw.alpha(col, al));
                    if (pu) flow(g, r, Draw.mix(col, 0xFFFFFFFF, 0.55f), al, nu, onPath, pos);
                }
            }
        }

        /** Trait en équerre d'épaisseur th, sans chevauchement (l'alpha reste correct aux coins). */
        private static void route(GuiGraphicsExtractor g, Route r, int th, int c) {
            int h0 = th / 2, h1 = th - h0, xl = r.mid - h0, xr = r.mid + h1;
            Draw.rect(g, Math.min(r.ax, xl), r.ay - h0, Math.max(r.ax, xl), r.ay + h1, c);
            Draw.rect(g, xl, Math.min(r.ay, r.by) - h0, xr, Math.max(r.ay, r.by) + h1, c);
            Draw.rect(g, Math.min(xr, r.bx), r.by - h0, Math.max(xr, r.bx), r.by + h1, c);
        }

        /** Animation du lien : "électrons" qui circulent (maillon acquis) ou pointillés qui avancent (prochain maillon). */
        private void flow(GuiGraphicsExtractor g, Route r, int c, float al, boolean full, boolean strong, float[] pos) {
            if (r.len < 4f) return;
            if (full) {
                int k = Math.max(1, Math.round(r.len / (strong ? 30f : 46f)));
                float speed = strong ? 64f : 34f;
                for (int i = 0; i < k; i++) {
                    float s = (time * speed + i * (r.len / k)) % r.len;
                    for (int t = 0; t < 3; t++) {                      // tête + traînée
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

        // ---------- nœuds ----------
        private void drawNodes(GuiGraphicsExtractor g) {
            // le nœud survolé / sélectionné est dessiné en dernier (au-dessus des voisins)
            for (TechNode n : TechTree.BY_ID.values()) if (n != hovered && n != selected) drawNode(g, n);
            if (selected != null && selected != hovered) drawNode(g, selected);
            if (hovered != null) drawNode(g, hovered);
        }

        /** Faux si le rectangle (repère du monde) est entièrement hors de l'écran : inutile de le dessiner. */
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
            float al = ap * (1f - 0.72f * an.dim);                       // opacité globale du nœud
            float pulse = 0.5f + 0.5f * (float) Math.sin(time * 2.6f + n.col() * 0.6f);
            float ut = time - an.unlock;                                  // secondes depuis le déblocage
            float pop = ut >= 0f && ut < 0.5f ? 1f + 0.22f * (float) Math.sin(ut / 0.5f * Math.PI) : 1f;
            float sc = (0.55f + 0.45f * Draw.easeBack(ap)) * (1f + 0.12f * an.hover) * pop;

            Draw.pushAt(g, x + BOX / 2f, y + BOX / 2f, sc);

            // ombre portée
            Draw.round(g, x + 1, y + 3, x + BOX + 1, y + BOX + 3, Draw.alpha(0xFF000000, 0.40f * al));
            // halo : fixe si débloqué, qui respire si disponible ; renforcé au survol / sélection / déblocage
            float glow = (un ? 0.30f : av ? 0.10f + 0.28f * pulse : 0f) + 0.35f * an.hover + 0.40f * an.sel;
            if (ut >= 0f && ut < 0.9f) glow += 0.8f * (1f - ut / 0.9f);
            if (glow > 0.01f) Draw.glow(g, x, y, x + BOX, y + BOX, un || av ? base : 0xFFFFFFFF, Math.min(1f, glow) * al, 3);

            // cadre + plaque
            int border = un ? base : av ? Draw.dim(base, 0.55f + 0.35f * pulse) : LOCKED;
            border = Draw.mix(border, 0xFFFFFFFF, Math.max(an.hover * 0.85f, an.sel * 0.6f));
            int plate = un ? Draw.mix(0xFF161B20, base, 0.16f) : av ? 0xFF171C22 : 0xFF111418;
            Draw.rframe(g, x, y, x + BOX, y + BOX, Draw.alpha(border, al));
            Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, Draw.alpha(plate, al));
            Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + 4, Draw.alpha(0xFFFFFFFF, 0.06f * al));
            Draw.rect(g, x + 2, y + BOX - 4, x + BOX - 2, y + BOX - 2, Draw.alpha(0xFF000000, 0.22f * al));

            // icône (flotte doucement si disponible) + voile (verrouillé / atténué / apparition)
            int bob = av ? Math.round((float) Math.sin(time * 3f + n.col()) * 1.2f) : 0;
            Draw.item(g, stack(n), x + (BOX - 16) / 2, y + (BOX - 16) / 2 + bob);
            float veil = Math.max(!un && !av ? 0.66f : 0f, Math.max(an.dim * 0.75f, 1f - ap));
            if (veil > 0.01f) Draw.rect(g, x + 2, y + 2, x + BOX - 2, y + BOX - 2, Draw.alpha(plate, Math.min(1f, veil)));

            // médaillons d'état : ✓ débloqué, ! disponible, cadenas verrouillé
            int mx0 = x + BOX - 7;
            if (un) {
                Draw.disc(g, mx0, y - 3, 10, Draw.alpha(0xFF0A0D10, al), Draw.alpha(base, al));
                Draw.check(g, mx0 + 2, y, Draw.alpha(0xFFFFFFFF, al));
            } else if (av) {
                int gold = Draw.mix(0xFF8A6D17, GOLD, 0.4f + 0.6f * pulse);
                Draw.disc(g, mx0, y - 3, 10, Draw.alpha(0xFF0A0D10, al), Draw.alpha(gold, al));
                Draw.rect(g, mx0 + 4, y - 1, mx0 + 6, y + 3, Draw.alpha(0xFF1A1405, al));
                Draw.rect(g, mx0 + 4, y + 4, mx0 + 6, y + 5, Draw.alpha(0xFF1A1405, al));
            } else {
                Draw.disc(g, mx0, y + BOX - 7, 10, Draw.alpha(0xFF0A0D10, al), Draw.alpha(0xFF262C33, al));
                Draw.lock(g, mx0 + 2, y + BOX - 6, Draw.alpha(0xFF8A939D, al));
            }
            // numéro d'étape de la feuille de route
            Integer stp = steps.get(n.id());
            if (stp != null && !un) Draw.badge(g, font, x - 6, y - 6, String.valueOf(stp), GOLD, al);

            // titre sous le nœud
            String t = font.plainSubstrByWidth(n.title(), CW - 6);
            int tc = un ? TXT : av ? 0xFFB4BCC4 : 0xFF66707A;
            Draw.text(g, font, t, x + BOX / 2 - font.width(t) / 2, y + BOX + 5, Draw.alpha(tc, al), un || av);

            // coins de visée qui respirent autour du nœud sélectionné
            if (an.sel > 0.02f) {
                int d = 3 + Math.round(1.5f * (float) Math.sin(time * 4f));
                Draw.brackets(g, x - d, y - d, x + BOX + d, y + BOX + d, 6, Draw.alpha(0xFFFFFFFF, an.sel * al));
            }
            Draw.pop(g);
        }

        /** Ondes de choc + particules des déblocages (dans le repère du monde). */
        private void drawFx(GuiGraphicsExtractor g) {
            for (TechNode n : TechTree.BY_ID.values()) {
                Anim a = anims.get(n.id());
                if (a == null) continue;
                float t = time - a.unlock;
                if (t < 0f || t > 1.0f) continue;
                int cx = nx(n) + BOX / 2, cy = ny(n) + BOX / 2;
                for (int k = 0; k < 2; k++) {                           // deux ondes décalées
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

        // ---------- en-tête : titre, progression, onglets de catégories ----------
        private void drawHeader(GuiGraphicsExtractor g, float open, int mx, int my) {
            int off = Math.round((1f - open) * -18f);                   // glisse depuis le haut à l'ouverture
            Draw.vgrad(g, 0, off, width, HEADER + off, Draw.alpha(0xFF06080A, 0.98f * open), Draw.alpha(0xFF0C1013, 0.96f * open), 4);
            Draw.rect(g, 0, HEADER - 1 + off, width, HEADER + off, Draw.alpha(PANEL_LINE, open));
            Draw.atom(g, 20, 15 + off, time, 0xFF6FCF3A, open);
            Draw.shimmer(g, font, "URANIUM TECH TREE", 34, 10 + off, TXT, 0xFFB6F25A, time, open);

            // progression globale (à droite) : compteur + barre animée
            long done = TechTree.BY_ID.values().stream().filter(TechTreeScreen::unlocked).count();
            String cnt = done + " / " + TechTree.BY_ID.size();
            int bw = Math.min(120, width / 4), bx = width - bw - 14, by = 13 + off;
            Draw.text(g, font, cnt, bx - font.width(cnt) - 8, 10 + off, Draw.alpha(TXT2, open), false);
            Draw.round(g, bx - 1, by - 1, bx + bw + 1, by + 8, Draw.alpha(0xFF2A333C, open));
            Draw.rect(g, bx, by, bx + bw, by + 7, Draw.alpha(0xFF0D1114, open));
            int fw = Math.round(bw * Draw.clamp(shownProgress));
            if (fw > 0) {
                Draw.rect(g, bx, by, bx + fw, by + 7, Draw.alpha(0xFF5DB82E, open));
                Draw.rect(g, bx, by, bx + fw, by + 2, Draw.alpha(0xFFFFFFFF, 0.22f * open));
                int s0 = bx + Math.round(((time * 0.55f) % 1.8f - 0.3f) * fw);   // reflet qui balaie la barre
                s0 = Math.max(s0, bx);
                int s1 = Math.min(s0 + 10, bx + fw);
                if (s1 > s0) Draw.rect(g, s0, by, s1, by + 7, Draw.alpha(0xFFFFFFFF, 0.28f * open));
            }

            // onglets : Tout + une pastille par catégorie. La catégorie active s'élargit pour montrer "débloqués/total",
            // chaque pastille porte une fine barre de progression ; mode compact si l'écran est étroit.
            int n = cats.size() + 1, cy = 30 + off, ch = 15, gap = 5;
            boolean compact = false;
            for (int pass = 0; pass < 2; pass++) {
                int sum = 12;
                for (int i = 0; i < n; i++) sum += chipW(i, compact) + gap;
                if (sum <= width - 4 || pass == 1) break;
                compact = true;
                gap = 3;
            }
            int cx = 12;
            for (int i = 0; i < n; i++) {
                Category c = i == 0 ? null : cats.get(i - 1);
                int w = chipW(i, compact);
                if (cx + w > width - 8) break;
                int[] stat = chipStat(c);
                boolean hv = mx >= cx && mx < cx + w && my >= cy && my < cy + ch;
                float on = i < chipAnim.length ? chipAnim[i] : 0f;
                int col = c == null ? 0xFFB6F25A : c.color();
                int edge = Draw.mix(hv ? 0xFF3E4A55 : 0xFF2A333C, col, on);
                int fill = Draw.mix(hv ? 0xFF1C242B : 0xFF141A1F, Draw.mix(0xFF141A1F, col, 0.22f), on);
                Draw.round(g, cx, cy, cx + w, cy + ch, Draw.alpha(edge, open));
                Draw.round(g, cx + 1, cy + 1, cx + w - 1, cy + ch - 1, Draw.alpha(fill, open));
                int tx = cx + (compact ? 4 : 6);
                if (c != null && !compact) {
                    Draw.rect(g, tx, cy + 4, tx + 5, cy + 9, Draw.alpha(col, open));
                    tx += 9;
                }
                String label = c == null ? "All" : c.label();
                Draw.text(g, font, label, tx, cy + 2, Draw.alpha(Draw.mix(TXT2, TXT, Math.max(on, hv ? 0.6f : 0f)), open), false);
                if (on > 0.3f)
                    Draw.text(g, font, stat[0] + "/" + stat[1], tx + font.width(label) + 6, cy + 2, Draw.alpha(TXT3, open * on), false);
                int barW = w - 6, frac = stat[1] == 0 ? 0 : Math.round(barW * (stat[0] / (float) stat[1]));
                Draw.rect(g, cx + 3, cy + ch - 4, cx + 3 + barW, cy + ch - 2, Draw.alpha(0xFF0B0E11, open));
                Draw.rect(g, cx + 3, cy + ch - 4, cx + 3 + frac, cy + ch - 2, Draw.alpha(col, 0.9f * open));
                final Category fc = c;
                hits.add(new Hit(cx, cy, cx + w, cy + ch, () -> setFilter(fc), null));
                cx += w + gap;
            }
        }

        /** {débloqués, total} pour une catégorie (null = tout l'arbre). */
        private int[] chipStat(Category c) {
            int have = 0, all = 0;
            for (TechNode n : TechTree.BY_ID.values()) if (c == null || n.cat() == c) { all++; if (unlocked(n)) have++; }
            return new int[] { have, all };
        }

        /** Largeur d'un onglet (s'élargit en douceur quand il devient actif, pour afficher son compteur). */
        private int chipW(int i, boolean compact) {
            Category c = i == 0 ? null : cats.get(i - 1);
            int[] st = chipStat(c);
            int w = (compact ? 8 : 12) + font.width(c == null ? "All" : c.label()) + (c == null || compact ? 0 : 9);
            float on = i < chipAnim.length ? chipAnim[i] : 0f;
            return w + Math.round((6 + font.width(st[0] + "/" + st[1])) * on);
        }

        // ---------- pied de page : raccourcis ----------
        private void drawFooter(GuiGraphicsExtractor g, float open) {
            int y0 = height - FOOT;
            Draw.rect(g, 0, y0, width, height, Draw.alpha(0xFF07090B, 0.97f * open));
            Draw.rect(g, 0, y0, width, y0 + 1, Draw.alpha(PANEL_LINE, open));
            String[][] hints = { {"Drag", "Move"}, {"Wheel", "Zoom"}, {"Click", "Details"},
                                 {"Esc", "Close"}, {"R", "Recenter"}, {"Tab", "Category"} };
            int x = 12;
            for (String[] h : hints) {
                int kw = font.width(h[0]) + 8, lw = font.width(h[1]);
                if (x + kw + 4 + lw > width - 12) break;
                Draw.key(g, font, h[0], x, y0 + 4, open);
                Draw.text(g, font, h[1], x + kw + 4, y0 + 6, Draw.alpha(TXT3, open), false);
                x += kw + 4 + lw + 14;
            }
        }

        // =====================================================================
        //  PANNEAU DE DÉTAILS = AIDE AU CRAFT
        // =====================================================================
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

        /** Case d'objet 18x18 : icône + quantité facultative. */
        private void pSlot(GuiGraphicsExtractor g, ItemStack st, int x, int y, int count, boolean hl) {
            Draw.round(g, x, y, x + 18, y + 18, Draw.alpha(hl ? 0xFF3E4A55 : 0xFF222B33, pA));
            Draw.rect(g, x + 1, y + 1, x + 17, y + 17, Draw.alpha(SLOT, pA));
            Draw.item(g, st, x + 1, y + 1);
            if (pA < 0.98f) Draw.rect(g, x + 1, y + 1, x + 17, y + 17, Draw.alpha(SLOT, 1f - pA));
            if (count > 1) {
                String c = String.valueOf(count);
                Draw.text(g, font, c, x + 17 - font.width(c), y + 9, Draw.alpha(TXT, pA), true);
            }
        }

        private void drawPanel(GuiGraphicsExtractor g, int mx, int my) {
            if (shown == null || panelAnim < 0.01f) return;
            TechNode n = shown;
            int pw = panelW(), x0 = panelX(), y0 = HEADER, y1 = height - FOOT;
            float e = Draw.easeOut(panelAnim);
            Draw.rect(g, x0, y0, width, y1, Draw.alpha(PANEL, 0.97f * e));
            Draw.rect(g, x0 - 2, y0, x0, y1, Draw.alpha(0xFF000000, 0.18f * e));
            Draw.rect(g, x0, y0, x0 + 1, y1, Draw.alpha(PANEL_LINE, e));
            Draw.rect(g, x0 + 1, y0, x0 + 3, y1, Draw.alpha(n.color(), 0.85f * e));   // liseré = couleur de la catégorie

            float maxS = Math.max(0f, panelContentH - (y1 - y0));
            panelScroll = Draw.clamp(panelScroll, 0f, maxS);
            pX = x0 + 12;
            pW = pw - 22;
            pTop = y0 + 3;
            pBot = y1 - 3;
            pA = e;
            py = y0 + 9 - Math.round(panelScroll);
            boolean un = unlocked(n), av = available(n);

            // --- icône, titre, catégorie ---
            if (vis(py, 28)) {
                Draw.round(g, pX, py, pX + 28, py + 28, Draw.alpha(n.color(), pA));
                Draw.rect(g, pX + 1, py + 1, pX + 27, py + 27, Draw.alpha(0xFF11161B, pA));
                Draw.item(g, stack(n), pX + 6, py + 6);
                if (pA < 0.98f) Draw.rect(g, pX + 1, py + 1, pX + 27, py + 27, Draw.alpha(0xFF11161B, 1f - pA));
                Draw.text(g, font, font.plainSubstrByWidth(n.title(), pW - 36), pX + 36, py + 4, Draw.alpha(TXT, pA), true);
                Draw.rect(g, pX + 36, py + 17, pX + 41, py + 22, Draw.alpha(n.color(), pA));
                Draw.text(g, font, n.cat().label(), pX + 45, py + 15, Draw.alpha(TXT2, pA), false);
            }
            py += 32;

            // --- état + stock dans l'inventaire ---
            if (vis(py, 12)) {
                String st = un ? "UNLOCKED" : av ? "AVAILABLE" : "LOCKED";
                int sc = un ? GREEN : av ? GOLD : RED, w = font.width(st) + 10;
                Draw.round(g, pX, py, pX + w, py + 12, Draw.alpha(sc, 0.75f * pA));
                Draw.round(g, pX + 1, py + 1, pX + w - 1, py + 11, Draw.alpha(Draw.mix(0xFF11161B, sc, 0.16f), pA));
                Draw.text(g, font, st, pX + 5, py + 2, Draw.alpha(sc, pA), false);
                int inv = have(n.item());
                if (inv > 0) Draw.text(g, font, "Have: " + inv, pX + w + 8, py + 2, Draw.alpha(TXT2, pA), false);
            }
            py += 15;
            if (vis(py, 1)) Draw.rect(g, pX, py, pX + pW, py + 1, Draw.alpha(PANEL_LINE, pA));
            py += 6;

            // --- description ---
            for (String ln : wrap(n.desc(), pW)) {
                pText(g, ln, 0xFFB4BCC4);
                py += 10;
            }
            py += 4;

            // --- AIDE AU CRAFT : recette explicite (si définie) + prérequis avec état et stock ---
            Recipe r = n.recipe();
            pSection(g, n, "CRAFT HELP", n.auto() ? "free" : r != null ? "recipe" : "needs");
            if (n.auto()) {
                pText(g, "Unlocked from the start.", TXT2);
                py += 12;
            } else {
                if (r != null) drawRecipe(g, n, r, mx, my);
                if (!n.parents().isEmpty()) {
                    if (r != null) {
                        pText(g, "Also needs", TXT3);
                        py += 12;
                    }
                    for (String pid : n.parents()) needRow(g, TechTree.BY_ID.get(pid), pid, mx, my);
                }
                if (av) {
                    for (String ln : wrap("Ready: get " + n.title() + " to unlock it.", pW)) {
                        pText(g, ln, GOLD);
                        py += 10;
                    }
                }
                py += 4;
            }

            // --- SERT À ---
            pSection(g, n, "USED FOR", "");
            List<TechNode> kids = TechTree.childrenOf(n.id());
            if (kids.isEmpty()) {
                pText(g, "Nothing depends on it yet.", TXT3);
                py += 12;
            } else {
                int x = pX;
                for (TechNode k : kids) {
                    if (x + 18 > pX + pW) { x = pX; py += 20; }
                    if (vis(py, 18)) {
                        boolean hv = mx >= x && mx < x + 18 && my >= py && my < py + 18;
                        pSlot(g, stack(k), x, py, 0, hv);
                        hits.add(new Hit(x, py, x + 18, py + 18, () -> focusNode(k),
                                List.of(Component.literal(k.title()), Component.literal("§8Click to show it on the tree"))));
                    }
                    x += 21;
                }
                py += 22;
            }

            // --- FEUILLE DE ROUTE : étapes à franchir, dans l'ordre (numérotées aussi sur l'arbre) ---
            if (!stepList.isEmpty()) {
                py += 2;
                pSection(g, n, "ROADMAP", stepList.size() + (stepList.size() > 1 ? " steps left" : " step left"));
                for (int i = 0; i < stepList.size(); i++) {
                    TechNode m = stepList.get(i);
                    if (vis(py, 12)) {
                        boolean hv = mx >= pX && mx < pX + pW && my >= py && my < py + 12;
                        int bwid = Draw.badge(g, font, pX, py, String.valueOf(i + 1), GOLD, pA);
                        Draw.text(g, font, font.plainSubstrByWidth(m.title(), pW - bwid - 8), pX + bwid + 6, py + 2,
                                Draw.alpha(hv || m == n ? TXT : TXT2, pA), false);
                        hits.add(new Hit(pX, py, pX + pW, py + 12, () -> focusNode(m),
                                List.of(Component.literal(m.title()), Component.literal("§8Click to show it on the tree"))));
                    }
                    py += 14;
                }
            }

            // --- hauteur du contenu + barre de défilement ---
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

        /** Recette explicite : [ingrédients] → [résultat], avec le poste de craft. */
        private void drawRecipe(GuiGraphicsExtractor g, TechNode n, Recipe r, int mx, int my) {
            pText(g, "Recipe: " + r.station(), TXT2);
            py += 12;
            int x = pX;
            for (Ing in : r.inputs()) {
                if (x + 18 > pX + pW) { x = pX; py += 20; }
                recipeSlot(g, in.itemId(), in.count(), x, mx, my);
                x += 21;
            }
            if (x + 12 + 18 > pX + pW) { x = pX; py += 20; }
            if (vis(py, 18)) Draw.arrow(g, x + 8, py + 9, Draw.alpha(TXT2, pA));
            recipeSlot(g, n.itemId(), r.out(), x + 12, mx, my);
            py += 24;
        }

        private void recipeSlot(GuiGraphicsExtractor g, String itemId, int count, int x, int mx, int my) {
            if (!vis(py, 18)) return;
            boolean hv = mx >= x && mx < x + 18 && my >= py && my < py + 18;
            pSlot(g, stackOf(itemId), x, py, count, hv);
            hits.add(new Hit(x, py, x + 18, py + 18, () -> { }, List.of(Component.literal(itemName(itemId)))));
        }

        /** Ligne "prérequis" : icône, nom, ✓ si acquis ou cadenas ; cliquable pour le retrouver sur l'arbre. */
        private void needRow(GuiGraphicsExtractor g, TechNode p, String pid, int mx, int my) {
            if (vis(py, 18)) {
                boolean pu = p != null && unlocked(p);
                boolean hv = mx >= pX && mx < pX + pW && my >= py && my < py + 18;
                Draw.round(g, pX, py, pX + pW, py + 18, Draw.alpha(hv ? 0xFF1D262D : 0xFF10151A, pA));
                pSlot(g, p != null ? stack(p) : stackOf("minecraft:barrier"), pX, py, 0, hv);
                String nm = font.plainSubstrByWidth(p != null ? p.title() : pid, pW - 24 - 16);
                Draw.text(g, font, nm, pX + 24, py + 5, Draw.alpha(pu ? TXT : TXT2, pA), false);
                if (pu) Draw.check(g, pX + pW - 11, py + 7, Draw.alpha(GREEN, pA));
                else Draw.lock(g, pX + pW - 10, py + 5, Draw.alpha(0xFF6B7580, pA));
                if (p != null) {
                    hits.add(new Hit(pX, py, pX + pW, py + 18, () -> focusNode(p),
                            List.of(Component.literal(p.title()), Component.literal("§8Click to show it on the tree"))));
                }
            }
            py += 19;
        }

        // ---------- textes ----------
        private List<Component> tip(TechNode n) {
            List<Component> l = new ArrayList<>();
            l.add(Component.literal(n.title()));
            l.add(Component.literal("§8" + n.cat().label()));
            l.add(Component.literal("§7" + n.desc()));
            if (unlocked(n)) l.add(Component.literal("§aUnlocked"));
            else if (available(n)) l.add(Component.literal("§eAvailable §7— obtain: " + n.title()));
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
            l.add(Component.literal("§8Click for craft help"));
            return l;
        }

        /** Retour à la ligne par mots, selon la largeur réelle de la police. */
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

    /**
     * SEUL fichier qui touche à l'API de rendu. Écrit pour MC 26.1 / NeoForge 26.1
     * (GuiGraphicsExtractor, pile de matrices 2D). Le reste du fichier ne passe que par ici,
     * et uniquement par fill / text / item / pose / tooltip : si une version change l'API, tout se corrige dans cette classe.
     */
    static final class Draw {
        // ---------- primitives : les seuls appels à l'API de rendu ----------
        static void rect(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int argb) { g.fill(x0, y0, x1, y1, argb); }

        static void text(GuiGraphicsExtractor g, Font f, String s, int x, int y, int argb) { text(g, f, s, x, y, argb, false); }

        /** Un alpha quasi nul ferait afficher le texte en opaque côté Minecraft : dans ce cas on ne dessine rien. */
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

        /** Met à l'échelle s autour du point (cx, cy) : effet "pop" des nœuds. */
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

        // ---------- maths / couleurs ----------
        static float clamp(float v) { return v < 0f ? 0f : Math.min(v, 1f); }

        static float clamp(float v, float lo, float hi) { return v < lo ? lo : Math.min(v, hi); }

        static float easeOut(float t) { float u = 1f - clamp(t); return 1f - u * u * u; }

        /** Ease-out avec léger dépassement (effet ressort à l'apparition). */
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

        // ---------- formes (rectangles qui ne se chevauchent pas : l'alpha reste propre) ----------
        /** Rectangle aux coins arrondis (rayon 2 px). */
        static void round(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            if (x1 - x0 < 5 || y1 - y0 < 5) { rect(g, x0, y0, x1, y1, c); return; }
            rect(g, x0 + 2, y0, x1 - 2, y0 + 1, c);
            rect(g, x0 + 1, y0 + 1, x1 - 1, y0 + 2, c);
            rect(g, x0, y0 + 2, x1, y1 - 2, c);
            rect(g, x0 + 1, y1 - 2, x1 - 1, y1 - 1, c);
            rect(g, x0 + 2, y1 - 1, x1 - 2, y1, c);
        }

        /** Cadre de 2 px aux coins extérieurs arrondis. */
        static void rframe(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            rect(g, x0 + 2, y0, x1 - 2, y0 + 1, c);
            rect(g, x0 + 1, y0 + 1, x1 - 1, y0 + 2, c);
            rect(g, x0, y0 + 2, x0 + 2, y1 - 2, c);
            rect(g, x1 - 2, y0 + 2, x1, y1 - 2, c);
            rect(g, x0 + 1, y1 - 2, x1 - 1, y1 - 1, c);
            rect(g, x0 + 2, y1 - 1, x1 - 2, y1, c);
        }

        /** Contour d'épaisseur t. */
        static void frame(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int t, int c) {
            rect(g, x0, y0, x1, y0 + t, c);
            rect(g, x0, y1 - t, x1, y1, c);
            rect(g, x0, y0 + t, x0 + t, y1 - t, c);
            rect(g, x1 - t, y0 + t, x1, y1 - t, c);
        }

        /** Contour de 1 px sans les 4 coins (aspect arrondi). */
        private static void frame1(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int c) {
            rect(g, x0 + 1, y0, x1 - 1, y0 + 1, c);
            rect(g, x0 + 1, y1 - 1, x1 - 1, y1, c);
            rect(g, x0, y0 + 1, x0 + 1, y1 - 1, c);
            rect(g, x1 - 1, y0 + 1, x1, y1 - 1, c);
        }

        /** Halo : contours concentriques de plus en plus transparents autour d'un rectangle. */
        static void glow(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int argb, float strength, int layers) {
            for (int i = 1; i <= layers; i++) {
                float f = 1f - (i - 1f) / layers;
                frame1(g, x0 - i, y0 - i, x1 + i, y1 + i, alpha(argb, strength * 0.40f * f * f));
            }
        }

        /** Médaillon arrondi (contour sombre + fond) pour les icônes d'état des nœuds. */
        static void disc(GuiGraphicsExtractor g, int x, int y, int size, int outline, int fill) {
            round(g, x - 1, y - 1, x + size + 1, y + size + 1, outline);
            round(g, x, y, x + size, y + size, fill);
        }

        /** Coche pixel-art 5x4. */
        static void check(GuiGraphicsExtractor g, int x, int y, int c) {
            rect(g, x + 4, y, x + 5, y + 1, c);
            rect(g, x + 3, y + 1, x + 4, y + 2, c);
            rect(g, x, y + 2, x + 1, y + 3, c);
            rect(g, x + 2, y + 2, x + 3, y + 3, c);
            rect(g, x + 1, y + 3, x + 2, y + 4, c);
        }

        /** Cadenas pixel-art 7x8. */
        static void lock(GuiGraphicsExtractor g, int x, int y, int c) {
            rect(g, x + 2, y, x + 5, y + 1, c);
            rect(g, x + 1, y + 1, x + 2, y + 3, c);
            rect(g, x + 5, y + 1, x + 6, y + 3, c);
            rect(g, x, y + 3, x + 7, y + 8, c);
            rect(g, x + 3, y + 5, x + 4, y + 7, 0xFF0A0D10); // trou de serrure
        }

        /** Pointe de flèche (3 px de long, 5 de haut) tournée vers la droite, dont la pointe est en (x, y). */
        static void arrow(GuiGraphicsExtractor g, int x, int y, int c) {
            for (int dy = -2; dy <= 2; dy++) rect(g, x - 3, y + dy, x - 3 + (3 - Math.abs(dy)), y + dy + 1, c);
        }

        /** Pastille numérotée (feuille de route). Renvoie sa largeur. */
        static int badge(GuiGraphicsExtractor g, Font f, int x, int y, String s, int color, float a) {
            int w = Math.max(11, f.width(s) + 6);
            round(g, x - 1, y - 1, x + w + 1, y + 12, alpha(0xFF0A0D10, a));
            round(g, x, y, x + w, y + 11, alpha(color, a));
            text(g, f, s, x + (w - f.width(s)) / 2, y + 2, alpha(0xFF1A1405, a), false);
            return w;
        }

        /** Quatre équerres de visée autour d'un rectangle. */
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

        /** Touche de clavier stylisée (pied de page). */
        static void key(GuiGraphicsExtractor g, Font f, String s, int x, int y, float a) {
            int w = f.width(s) + 8;
            round(g, x, y, x + w, y + 11, alpha(0xFF2F3943, a));
            rect(g, x + 1, y + 1, x + w - 1, y + 9, alpha(0xFF1A2027, a));
            text(g, f, s, x + 4, y + 2, alpha(0xFFC9D1D9, a), false);
        }

        /** Dégradé vertical par bandes de `strip` px. */
        static void vgrad(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int top, int bottom, int strip) {
            int h = y1 - y0;
            for (int y = 0; y < h; y += strip)
                rect(g, x0, y0 + y, x1, Math.min(y1, y0 + y + strip), mix(top, bottom, (y + strip / 2f) / h));
        }

        /** Dégradé horizontal par bandes de `strip` px. */
        static void hgrad(GuiGraphicsExtractor g, int x0, int y0, int x1, int y1, int left, int right, int strip) {
            int w = x1 - x0;
            for (int x = 0; x < w; x += strip)
                rect(g, x0 + x, y0, Math.min(x1, x0 + x + strip), y1, mix(left, right, (x + strip / 2f) / w));
        }

        /** Titre dont l'éclat parcourt les lettres. */
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

        /** Petit atome animé : noyau + 3 électrons qui parcourent des orbites inclinées (tracées en pointillés discrets). */
        static void atom(GuiGraphicsExtractor g, int cx, int cy, float t, int color, float a) {
            for (int k = 0; k < 3; k++) {
                double th = k * Math.PI / 3.0, ct = Math.cos(th), st = Math.sin(th);
                for (int j = 0; j < 12; j++) {                               // orbite
                    double an = j * Math.PI / 6.0, ex = Math.cos(an) * 8.0, ey = Math.sin(an) * 3.0;
                    int px = cx + (int) Math.round(ex * ct - ey * st), py = cy + (int) Math.round(ex * st + ey * ct);
                    rect(g, px, py, px + 1, py + 1, alpha(color, 0.22f * a));
                }
                double an = t * 2.4 + k * 2.1, ex = Math.cos(an) * 8.0, ey = Math.sin(an) * 3.0;   // électron
                int px = cx + (int) Math.round(ex * ct - ey * st), py = cy + (int) Math.round(ex * st + ey * ct);
                rect(g, px - 1, py - 1, px + 1, py + 1, alpha(mix(color, 0xFFFFFFFF, 0.5f), a));
            }
            rect(g, cx - 1, cy - 1, cx + 2, cy + 2, alpha(color, a));       // noyau
        }

        private Draw() {}
    }
}
