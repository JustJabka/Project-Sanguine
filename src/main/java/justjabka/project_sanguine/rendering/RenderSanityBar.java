package justjabka.project_sanguine.rendering;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.managers.SanityManager;
import justjabka.project_sanguine.types.Sanity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class RenderSanityBar {
    private static final Identifier SANITY_SPRITES = ProjectSanguine.id("hud/sanity/");

    public static void render(GuiGraphicsExtractor graphics, Player player, int sw, int sh) {
        GameType gameMode = player.gameMode();
        if (gameMode == null) return;
        if (!gameMode.isSurvival()) return;

        int size = 9;

        int x = sw / 2 - size / 2;
        int y = sh - 40 - size;

        Sanity currentSanity = SanityManager.getSanityStage(player);
        switch (currentSanity) {
            case NORMAL -> {
                return;
            }
            case INSANITY -> {
                if (player.tickCount % 60 != 0) break;
                x += getRandomMovement(player);
                y += getRandomMovement(player);
            }
            case DELIRIUM -> {
                x += getRandomMovement(player);
                y += getRandomMovement(player);
            }
        }

        Identifier currentSanitySprite = SANITY_SPRITES.withSuffix(currentSanity.getSerializedName());
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, currentSanitySprite, x, y, size, size);
    }

    private static int getRandomMovement(Player player) {
        RandomSource random = player.getRandom();
        return random.nextInt(3) - 1;
    }
}
