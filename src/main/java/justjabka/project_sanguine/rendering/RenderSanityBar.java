package justjabka.project_sanguine.rendering;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import justjabka.project_sanguine.types.Sanity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class RenderSanityBar {
    private static final Identifier SANITY_SPRITES = ProjectSanguine.id("hud/sanity/");

    public static void render(GuiGraphicsExtractor graphics, Player player, int sw, int sh) {
        int size = 9;

        int x = sw / 2 - size / 2;
        int y = sh - 40 - size;

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        float sanity = data.sanity();

        Sanity currentSanity = Sanity.getSanityFromValue(sanity);
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
