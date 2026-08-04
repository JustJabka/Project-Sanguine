package justjabka.project_sanguine.contents.item;

import justjabka.project_sanguine.rendering.screens.RuneCastingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

public class RitualKnife extends Item {
    public RitualKnife(Properties properties) {
        super(properties
                .stacksTo(1)
                .sword(ToolMaterial.IRON, 1, -2.0f)
                .durability(100)
        );
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand){
        if(level.isClientSide()){
            Minecraft.getInstance().setScreenAndShow(new RuneCastingScreen(Component.empty(), player));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
