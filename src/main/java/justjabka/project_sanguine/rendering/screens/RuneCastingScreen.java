package justjabka.project_sanguine.rendering.screens;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Ease;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerSkin;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RuneCastingScreen extends Screen {

    private final Player player;
    public RuneCastingScreen(Component title, Player player) {
        super(title);
        this.player = player;
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

    @Override
    public boolean isInGameUi(){
        return true;
    }

    private long createTime;
    private int previousCursorMode;
    @Override
    protected void init() {
        createTime = Util.getMillis();
        previousCursorMode = GLFW.glfwGetInputMode(
                minecraft.getWindow().handle(),
                GLFW.GLFW_CURSOR
        );
        GLFW.glfwSetInputMode(
                minecraft.getWindow().handle(),
                GLFW.GLFW_CURSOR,
                GLFW.GLFW_CURSOR_HIDDEN
        );
    }

    @Override
    public void removed(){
        GLFW.glfwSetInputMode(
                minecraft.getWindow().handle(),
                GLFW.GLFW_CURSOR,
                previousCursorMode
        );
    }

    @Override
    public void tick(){
    }

    private static final Identifier HAND_TEXTURE_PATH = Identifier.fromNamespaceAndPath(
            ProjectSanguine.MOD_ID,
            "textures/gui/sprites/rune_casting/hand.png"
    );
    private static final Identifier CURSOR_TEXTURE_PATH = Identifier.fromNamespaceAndPath(
            ProjectSanguine.MOD_ID,
            "textures/gui/sprites/rune_casting/cursor.png"
    );

    private static final int HAND_SIZE_X = 256;
    private static final int HAND_SIZE_Y = 256;

    private static final int HAND_ANIMATION_TIME_MILLIS = 200;
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        long currentTime = Util.getMillis();
        float progress = Math.clamp((currentTime - createTime) / (float)HAND_ANIMATION_TIME_MILLIS, 0f, 1f);

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                HAND_TEXTURE_PATH,
                width / 2 - HAND_SIZE_X / 2,
                height / 2 - HAND_SIZE_Y / 2,
                0,
                0,
                HAND_SIZE_X,
                HAND_SIZE_Y,
                256,
                256,
                Color.WHITE.getRGB()
        );
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CURSOR_TEXTURE_PATH,
                mouseX,
                mouseY,
                0,
                0,
                8,
                8,
                8,
                8,
                Color.WHITE.getRGB()
        );
    }
}
