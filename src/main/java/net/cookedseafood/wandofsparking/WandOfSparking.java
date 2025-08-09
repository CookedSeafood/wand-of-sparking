package net.cookedseafood.wandofsparking;

import net.cookedseafood.wandofsparking.command.WandOfSparkingCommand;
import net.cookedseafood.wandofsparking.data.WandOfSparkingConfig;
import net.cookedseafood.wandofsparking.entity.projectile.SparkingEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WandOfSparking implements ModInitializer {
    public static final String MOD_ID = "wand-of-sparking";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final byte VERSION_MAJOR = 1;
    public static final byte VERSION_MINOR = 0;
    public static final byte VERSION_PATCH = 0;

    public static final String MOD_NAMESPACE = "wand_of_sparking";
    public static final String WAND_OF_SPARKING_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "wand_of_sparking").toString();
    public static final String SPARKING_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "sparking").toString();

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> WandOfSparkingCommand.register(dispatcher, registryAccess));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> WandOfSparkingConfig.reload());

        ServerTickEvents.END_SERVER_TICK.register(server -> SparkingEntity.tickAll());

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (Hand.OFF_HAND.equals(hand)) {
                return ActionResult.PASS;
            }

            ItemStack stack = player.getMainHandStack();
            if (!WAND_OF_SPARKING_CUSTOM_ID.equals(stack.getCustomId())) {
                return ActionResult.PASS;
            }

            return usedBy(player, (ServerWorld)world) ? ActionResult.SUCCESS : ActionResult.FAIL;
        });
    }

    public static boolean usedBy(LivingEntity entity, ServerWorld world) {
        if (!entity.consumMana(WandOfSparkingConfig.manaConsumption)) {
            return false;
        }

        SparkingEntity sparking = new SparkingEntity(world);
        sparking.setPos(entity.getEyePos());
        sparking.setVelocity(entity.getRotationVector().multiply(WandOfSparkingConfig.movementSpeed));
        sparking.setFuse(WandOfSparkingConfig.dissipationFuse);
        sparking.setOwner(entity);
        return true;
    }
}
