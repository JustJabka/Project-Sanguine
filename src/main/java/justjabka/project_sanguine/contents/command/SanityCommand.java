package justjabka.project_sanguine.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SanityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sanity")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(SanityCommand::setSanity)
                                )
                        )
                )
        );
    }

    private static int setSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        int value = IntegerArgumentType.getInteger(context, "value");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.setSanity(value)
        );

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.set.success.single", value, player.getDisplayName()), true);

        return Command.SINGLE_SUCCESS;
    }
}
