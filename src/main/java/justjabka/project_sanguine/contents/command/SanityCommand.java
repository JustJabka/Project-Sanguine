package justjabka.project_sanguine.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
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
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                        .executes(SanityCommand::setSanity)
                                )
                        )
                )
                .then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(SanityCommand::getSanity)
                        )
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                        .executes(SanityCommand::addSanity)
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                        .executes(SanityCommand::removeSanity)
                                )
                        )
                )
        );
    }

    private static int setSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        float value = FloatArgumentType.getFloat(context, "value");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.setSanity(player, value)
        );

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.set.success.single", value, player.getDisplayName()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int getSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.get.success.single", player.getDisplayName(), data.sanity()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int addSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        float value = FloatArgumentType.getFloat(context, "value");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.addSanity(player, value)
        );

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.add.success.single", value, player.getDisplayName()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int removeSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        float value = FloatArgumentType.getFloat(context, "value");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                data.removeSanity(player, value)
        );

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.remove.success.single", value, player.getDisplayName()), true);

        return Command.SINGLE_SUCCESS;
    }
}
