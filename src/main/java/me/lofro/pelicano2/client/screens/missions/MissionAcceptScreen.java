package me.lofro.pelicano2.client.screens.missions;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lofro.pelicano2.global.network.PacketIDS;
import me.lofro.pelicano2.global.utils.ModConstants;
import me.lofro.pelicano2.server.missions.missions.Missions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MissionAcceptScreen extends Screen {
    //TODO ARREGLAR TAMAÑOS GUI 4 Y CENTRAR BOTON EN GUI SIN GIF

    private static Identifier SOURCE_IMAGE;

    // Información del sprite sheet
    private static int FRAME_COUNT;
    private static final int FRAME_WIDTH = 1177;
    private static final int FRAME_HEIGHT = 711;
    private static final int FRAME_SPACING = 10;

    // Tiempo
    private static final int TICKS_PER_FRAME = 1; // ajustar velocidad
    private int tickCounter = 0;
    private int currentFrame = 0;

    // Páginas resultantes (una por textura "page")
    private static final List<Identifier> PAGE_IDS = new ArrayList<>();
    private static final List<Integer> PAGE_HEIGHTS = new ArrayList<>();
    private static int framesPerPage = 0;
    private static boolean pagesLoaded = false;

    private final Text titleText;
    private final Text descriptionText;

    private List<OrderedText> wrappedDescription;

    private final Missions mission;

    public MissionAcceptScreen(Missions mission) {
        super(Text.literal("Mission Assign Screen"));

        this.mission = mission;

        this.titleText = mission.getTitle();
        this.descriptionText = mission.getDescription();
    }

    @Override
    protected void init() {
        super.init();

        if (!pagesLoaded && !this.mission.isNPC()) {
            try {
                if (this.mission.getNpcTexture().getPath().equals("guide")) {
                    FRAME_COUNT = 120;
                    SOURCE_IMAGE = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/guide_talking.png");
                    splitGuideSpriteSheetIntoPages();
                } else {
                    FRAME_COUNT = 100;
                    SOURCE_IMAGE = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/nicte.png");
                    splitNicteSpriteSheetIntoPages();
                }
                pagesLoaded = true;
            } catch (IOException e) {
                System.err.println("Error cargando sprite sheet: " + e.getMessage());
                e.printStackTrace();
            }
        }

        var guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();

        int maxTextWidth = switch (guiScale) {
            case 1 -> this.width / 7;
            case 2 -> this.width / 5;
            case 3 -> (int) (this.width / 3.5);
            default -> (int) (this.width / 2.5);
        };

        this.wrappedDescription = this.textRenderer.wrapLines(this.descriptionText, maxTextWidth);

        // botón (lo que ya tenías)
        int buttonWidth = 100;
        int buttonHeight = 31;

        var proportion = switch (guiScale) {
            case 1 -> 4;
            case 2, 3 -> 5;
            default -> 6;
        };

        int x = !mission.isNPC() ? (this.width / 2) + (this.width / proportion) : guiScale != 4 ? (this.width / 2) - 10 * proportion : (this.width / 2) - 8 * proportion; // ajustes de pantalla. no se ni que hice aqui
        int y = (this.height / 2) + (this.height / proportion);

        Identifier acceptTexture = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/accept_button.png");

        this.addDrawableChild(new net.minecraft.client.gui.widget.TexturedButtonWidget(
                x, y,
                buttonWidth, buttonHeight,
                0, 0, buttonHeight,
                acceptTexture,
                buttonWidth, buttonHeight,
                (btn) -> {
                    PlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        PacketByteBuf packetByteBuf = PacketByteBufs.create();
                        packetByteBuf.writeEnumConstant(mission);
                        ClientPlayNetworking.send(PacketIDS.ACCEPT_MISSION_C2S, packetByteBuf);
                    }
                    this.close();
                }
        ));
    }

    private void splitGuideSpriteSheetIntoPages() throws IOException {
        // Asegurarse de que haya contexto GL (estamos en la thread cliente cuando se abre la pantalla).
        int maxTexSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        if (maxTexSize <= 0) {
            // fallback seguro
            maxTexSize = 16384;
        }
        // stride (alto con separación)
        final int stride = FRAME_HEIGHT + FRAME_SPACING;

        // cuántos frames caben por página
        framesPerPage = Math.max(1, (maxTexSize + FRAME_SPACING) / stride);

        // Leer la imagen completa (sprite sheet vertical)
        MinecraftClient mc = MinecraftClient.getInstance();
        try (InputStream is = mc.getResourceManager().getResource(SOURCE_IMAGE).get().getInputStream()) {
            NativeImage big = NativeImage.read(is);

            // calcular páginas
            int pages = (FRAME_COUNT + framesPerPage - 1) / framesPerPage;

            for (int p = 0; p < pages; p++) {
                int firstFrameIndex = p * framesPerPage;
                int framesThisPage = Math.min(framesPerPage, FRAME_COUNT - firstFrameIndex);
                int pageHeight = framesThisPage * FRAME_HEIGHT + Math.max(0, framesThisPage - 1) * FRAME_SPACING;

                // Crear imagen de la página y copiar pixel por pixel de la fuente
                NativeImage pageImage = new NativeImage(FRAME_WIDTH, pageHeight, true);

                for (int f = 0; f < framesThisPage; f++) {
                    int globalFrameIndex = firstFrameIndex + f;
                    int srcY = globalFrameIndex * stride; // y de origen en la big image
                    int dstY = f * stride; // y en la página donde pegamos el frame

                    for (int y = 0; y < FRAME_HEIGHT; y++) {
                        for (int x = 0; x < FRAME_WIDTH; x++) {
                            int color = big.getColor(x, srcY + y); // copia el color tal cual
                            pageImage.setColor(x, dstY + y, color);
                        }
                    }
                }

                // Registrar la textura en TextureManager con un id único
                Identifier pageId = new Identifier(ModConstants.MOD_ID, "gui/guide_talking_page_" + p);
                NativeImageBackedTexture nibt = new NativeImageBackedTexture(pageImage);
                mc.getTextureManager().registerTexture(pageId, nibt);

                PAGE_IDS.add(pageId);
                PAGE_HEIGHTS.add(pageHeight);
                // NOTA: no cierro pageImage aquí porque la textura la usa internamente.
            }

            // cerrar la big image
            big.close();
        }
    }

    private void splitNicteSpriteSheetIntoPages() throws IOException {
        int maxTexSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        if (maxTexSize <= 0) {
            maxTexSize = 16384;
        }

        final int stride = FRAME_HEIGHT + FRAME_SPACING;
        framesPerPage = Math.max(1, (maxTexSize + FRAME_SPACING) / stride);

        MinecraftClient mc = MinecraftClient.getInstance();
        try (InputStream is = mc.getResourceManager().getResource(SOURCE_IMAGE).get().getInputStream()) {
            NativeImage big = NativeImage.read(is);

            int pages = (FRAME_COUNT + framesPerPage - 1) / framesPerPage;

            for (int p = 0; p < pages; p++) {
                int firstFrameIndex = p * framesPerPage;
                int framesThisPage = Math.min(framesPerPage, FRAME_COUNT - firstFrameIndex);
                int pageHeight = framesThisPage * FRAME_HEIGHT + Math.max(0, framesThisPage - 1) * FRAME_SPACING;

                NativeImage pageImage = new NativeImage(FRAME_WIDTH, pageHeight, true);

                for (int f = 0; f < framesThisPage; f++) {
                    int globalFrameIndex = firstFrameIndex + f;
                    int srcY = globalFrameIndex * stride;
                    int dstY = f * stride;

                    for (int y = 0; y < FRAME_HEIGHT; y++) {
                        for (int x = 0; x < FRAME_WIDTH; x++) {
                            int color = big.getColor(x, srcY + y);
                            pageImage.setColor(x, dstY + y, color);
                        }
                    }
                }

                Identifier pageId = new Identifier(ModConstants.MOD_ID, "gui/nicte_page_" + p);
                NativeImageBackedTexture nibt = new NativeImageBackedTexture(pageImage);
                mc.getTextureManager().registerTexture(pageId, nibt);

                PAGE_IDS.add(pageId);
                PAGE_HEIGHTS.add(pageHeight);
            }

            big.close();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        context.fill(0, 0, this.width, this.height, 0x88000000);
        RenderSystem.disableBlend();

        if (!mission.isNPC()) {
            if (!pagesLoaded || PAGE_IDS.isEmpty()) {
                context.drawCenteredTextWithShadow(this.textRenderer, "Sprite sheet no cargada",
                        this.width / 2, this.height / 2, 0xFFFFFF);
                super.render(context, mouseX, mouseY, delta);
                return;
            }

            // --- Frame actual ---
            int pageIndex = currentFrame / framesPerPage;
            pageIndex = Math.max(0, Math.min(pageIndex, PAGE_IDS.size() - 1));
            Identifier pageId = PAGE_IDS.get(pageIndex);
            int pageHeight = PAGE_HEIGHTS.get(pageIndex);
            int indexInPage = currentFrame - pageIndex * framesPerPage;
            int yOffset = indexInPage * (FRAME_HEIGHT + FRAME_SPACING);

            float scaleX = (float)(this.width) / FRAME_WIDTH;
            float scaleY = (float)(this.height) / FRAME_HEIGHT;
            float scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int)(FRAME_WIDTH * scale);
            int scaledHeight = (int)(FRAME_HEIGHT * scale);

            int drawX = (this.width / 4) - (scaledWidth / 2);
            int drawY = (this.height / 2) - (scaledHeight / 2);

            RenderSystem.setShaderTexture(0, pageId);
            context.drawTexture(
                    pageId,
                    drawX, drawY,
                    scaledWidth, scaledHeight,
                    0, yOffset,
                    FRAME_WIDTH, FRAME_HEIGHT,
                    FRAME_WIDTH, pageHeight
            );
        }

        var guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();

        var textProportion = switch (guiScale) {
            case 1 -> 5.5F;
            case 2 -> 2.75F;
            case 3 -> 1.75F;
            default -> 1.5F;
        };

        Pair<Integer, Integer> textSeparator = switch (guiScale) {
            case 1 -> new Pair<>(460, 400);
            case 2 -> new Pair<>(300, 260);
            case 3 -> new Pair<>(200, 170);
            case 4 -> new Pair<>(160, 140);
            default -> new Pair<>(200, 200);
        };

        // --- Dibujar textos encima del botón ---
        this.children().stream()
                .filter(c -> c instanceof net.minecraft.client.gui.widget.TexturedButtonWidget)
                .findFirst()
                .ifPresent(btn -> {
                    var button = (net.minecraft.client.gui.widget.TexturedButtonWidget) btn;
                    int buttonX = button.getX();
                    int buttonY = button.getY();
                    int centerX = buttonX + (button.getWidth() / 2);

                    // --- Título ---
                    context.getMatrices().push();
                    context.getMatrices().scale(textProportion, textProportion, textProportion);
                    context.drawCenteredTextWithShadow(
                            this.textRenderer,
                            this.titleText,
                            (int) (centerX / textProportion),
                            (int) ((buttonY - textSeparator.getLeft()) / textProportion), // más arriba todavía
                            0xFFFFFF
                    );
                    context.getMatrices().pop();

                    // --- Descripción multilínea ---
                    if (wrappedDescription != null) {
                        context.getMatrices().push();
                        context.getMatrices().scale(textProportion / 1.75F, textProportion / 1.75F, textProportion / 1.75F);

                        int lineY = (int) ((float) (buttonY - textSeparator.getRight()) / (textProportion / 1.75F)); // posición inicial
                        for (OrderedText line : wrappedDescription) {
                            context.drawCenteredTextWithShadow(
                                    this.textRenderer,
                                    line,
                                    (int) ((float) centerX / (textProportion / 1.75F)),
                                    lineY,
                                    0xAAAAAA
                            );
                            lineY += this.textRenderer.fontHeight + 2; // salto de línea
                        }

                        context.getMatrices().pop();
                    }
                });

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        if (!mission.isNPC()) {
            tickCounter++;
            if (tickCounter >= TICKS_PER_FRAME) {
                tickCounter = 0;
                currentFrame = (currentFrame + 1) % FRAME_COUNT;
            }
        }
        super.tick();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void close() {
        pagesLoaded = false;

        PAGE_IDS.clear();

        PAGE_HEIGHTS.clear();

        super.close();
    }

}