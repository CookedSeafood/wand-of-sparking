package net.cookedseafood.wandofsparking.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.wandofsparking.WandOfSparking;
import net.cookedseafood.wandofsparking.data.WandOfSparkingConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class WandOfSparkingCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("wandofsparking")
            .then(
                CommandManager.literal("reload")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeReload((ServerCommandSource)context.getSource()))
            )
            .then(
                CommandManager.literal("version")
                .executes(context -> executeVersion((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeReload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Reloading Magician Staff!"), true);
        return WandOfSparkingConfig.reload();
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Magician Staff " + WandOfSparking.VERSION_MAJOR + "." + WandOfSparking.VERSION_MINOR + "." + WandOfSparking.VERSION_PATCH), false);
        return 0;
    }
}
