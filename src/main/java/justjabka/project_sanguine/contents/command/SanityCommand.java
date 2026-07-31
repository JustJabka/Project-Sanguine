package justjabka.project_sanguine.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import justjabka.project_sanguine.contents.attachment.PlayerData;
import justjabka.project_sanguine.managers.SanityManager;
import justjabka.project_sanguine.registries.ProjectSanguineAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.function.TriFunction;

public class SanityCommand {
    private enum Operation {
        ADD("add", PlayerData::addSanity),
        REMOVE("remove", PlayerData::removeSanity),
        SET("set", PlayerData::setSanity);

        private final String key;
        private final TriFunction<PlayerData, ServerPlayer, Float, PlayerData> function;

        Operation(String key, TriFunction<PlayerData, ServerPlayer, Float, PlayerData> function) {
            this.key = key;
            this.function = function;
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("sanity")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(SanityCommand::getSanity)
                                .then(Commands.literal("ambient")
                                        .executes(context ->
                                                getSanityAura(context, false)
                                        )
                                        .then(Commands.literal("full")
                                                .executes(context ->
                                                        getSanityAura(context, true)
                                                )
                                        )
                                )
                        )
                );

        for (Operation operation : Operation.values()) {
            builder.then(Commands.literal(operation.key)
                    .then(Commands.argument("target", EntityArgument.player())
                            .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                    .executes(context ->
                                            manipulateSanity(context, operation)
                                    )
                            )
                    )
            );
        }

        dispatcher.register(builder);
    }

    private static int getSanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);

        context.getSource().sendSuccess(() -> Component.translatable("commands.sanity.get.success.single",
                player.getDisplayName(),
                data.sanity()
        ), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int getSanityAura(CommandContext<CommandSourceStack> context, boolean full) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");

        CommandSourceStack source = context.getSource();

        source.sendSuccess(() -> Component.translatable("commands.sanity.get.ambient.success.single",
                player.getDisplayName(),
                SanityManager.getPassiveAura(player)
        ), true);

        if (full) {
            ServerLevel level = player.level();

            source.sendSuccess(() -> Component.translatable("commands.sanity.get.ambient_detailed.success.single",
                SanityManager.getInsomniaAura(player),
                SanityManager.getAuraFromEnvironment(player, level),
                SanityManager.getAuraFromEntities(player, level)
            ), true);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int manipulateSanity(CommandContext<CommandSourceStack> context, Operation operation) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        float value = FloatArgumentType.getFloat(context, "value");

        PlayerData data = player.getAttachedOrCreate(ProjectSanguineAttachments.PLAYER_DATA);
        player.setAttached(
                ProjectSanguineAttachments.PLAYER_DATA,
                operation.function.apply(data, player, value)
        );

        String translationKey = "commands.sanity.%s.success.single".formatted(operation.key);
        context.getSource().sendSuccess(() -> Component.translatable(translationKey,
                value,
                player.getDisplayName()
        ), true);

        return Command.SINGLE_SUCCESS;
    }
}
