package de.rubixdev.rug.mixins.reach;

import de.rubixdev.rug.RugSettings;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 900)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    @Final
    public static double MAX_BREAK_SQUARED_DISTANCE;

    @Shadow
    public ServerPlayerEntity player;

    @Redirect(
            method = "onPlayerInteractEntity",
            at =
                    @At(
                            value = "FIELD",
                            target =
                                    "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double changeAttackRangeForEntities() {
        if (this.player == null) {
            return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
        }
        if (this.player.hasPermissionLevel(RugSettings.reachPermissionLevel))
            return Math.pow(
                    Math.sqrt(ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE) + RugSettings.reachDistance - 4.5,
                    2);
        return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
    }

    @Redirect(
            method = "onPlayerInteractBlock",
            at =
                    @At(
                            value = "FIELD",
                            target =
                                    "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double changeAttackRangeForBlocks() {
        if (player == null) {
            return MAX_BREAK_SQUARED_DISTANCE;
        }
        if (player.hasPermissionLevel(RugSettings.reachPermissionLevel))
            return Math.pow(Math.sqrt(MAX_BREAK_SQUARED_DISTANCE) + RugSettings.reachDistance - 4.5, 2);
        return MAX_BREAK_SQUARED_DISTANCE;
    }

    @ModifyConstant(method = "onPlayerInteractBlock", allow = 1, require = 1, constant = @Constant(doubleValue = 64.0))
    private double changeReachDistance(final double baseReachDistance) {
        if (player == null) {
            return MAX_BREAK_SQUARED_DISTANCE;
        }
        if (player.hasPermissionLevel(RugSettings.reachPermissionLevel))
            return Math.pow(Math.sqrt(MAX_BREAK_SQUARED_DISTANCE) + RugSettings.reachDistance - 4.5, 2);
        return MAX_BREAK_SQUARED_DISTANCE;
    }
}
