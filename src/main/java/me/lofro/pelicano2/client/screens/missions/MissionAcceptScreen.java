package me.lofro.pelicano2.client.screens.missions;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lofro.pelicano2.global.utils.ModConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MissionAcceptScreen extends Screen {
    private static final Identifier SOURCE_IMAGE = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/guide_talking.png");

    // Información del sprite sheet
    private static final int FRAME_COUNT = 120;
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

    public MissionAcceptScreen(Text title, Text description) {
        super(Text.literal("Mission Screen"));
        this.titleText = title;
        this.descriptionText = description;
    }

    @Override
    protected void init() {
        super.init();
        // Cargar y partir la imagen solo una vez por lifecycle del cliente.
        if (!pagesLoaded) {
            try {
                splitSpriteSheetIntoPages();
                pagesLoaded = true;
            } catch (IOException e) {
                System.err.println("Error cargando sprite sheet: " + e.getMessage());
                e.printStackTrace();
            }
        }

        int buttonWidth = 100;
        int buttonHeight = 31;

        var guiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();

        var proportion = switch (guiScale) {
            case 1: {
                yield 4;
            }
            case 2, 3: {
                yield 5;
            }
            default: {
                yield 6;
            }
        };

        int x = (this.width / 2) + (this.width / proportion); // margen derecho
        int y = (this.height / 2) + (this.height / proportion); // margen inferior

        Identifier acceptTexture = new Identifier(ModConstants.MOD_ID, "textures/gui/missions/accept_button.png");

        this.addDrawableChild(new net.minecraft.client.gui.widget.TexturedButtonWidget(
                x, y,
                buttonWidth, buttonHeight,
                0, 0, buttonHeight,
                acceptTexture,
                buttonWidth, buttonHeight,
                (btn) -> {
                    MinecraftClient.getInstance().player.sendMessage(Text.literal("¡Misión aceptada!"));
                    this.close();
                }
        ));
    }

    /**
     * Carga la imagen original desde resources y la divide en páginas
     * respetando el límite máximo de tamaño de textura de la GPU.
     */
    private void splitSpriteSheetIntoPages() throws IOException {
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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        context.fill(0, 0, this.width, this.height, 0x88000000);
        RenderSystem.disableBlend();

        if (!pagesLoaded || PAGE_IDS.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Sprite sheet no cargada").getString(),
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

        // --- Dibujar frame escalado ---
        RenderSystem.setShaderTexture(0, pageId);
        context.drawTexture(
                pageId,
                drawX, drawY,
                scaledWidth, scaledHeight,
                0, yOffset,
                FRAME_WIDTH, FRAME_HEIGHT,
                FRAME_WIDTH, pageHeight
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter >= TICKS_PER_FRAME) {
            tickCounter = 0;
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
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
}