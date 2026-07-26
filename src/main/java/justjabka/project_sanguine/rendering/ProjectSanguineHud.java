package justjabka.project_sanguine.rendering;

import com.mojang.blaze3d.platform.Window;
import justjabka.project_sanguine.ProjectSanguine;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;

public class ProjectSanguineHud {

    public static void initialize() {
        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing.
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                ProjectSanguine.id("before_chat"),
                ProjectSanguineHud::renderSanityBar
        );
    }

    private static void renderSanityBar(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Font font = minecraft.font;

        if (player == null) return;

        Window window = minecraft.getWindow();
        int sw = window.getGuiScaledWidth();
        int sh = window.getGuiScaledHeight();

        RenderSanityBar.render(graphics, player, sw, sh);
    }
}
