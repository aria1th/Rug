package de.rubixdev.rug.mixins.reach;

import de.rubixdev.rug.RugSettings;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow @Final protected ServerPlayerEntity player;

	@Redirect(
            method = "processBlockBreakingAction",
            at =
                    @At(
                            value = "FIELD",
                            target =
                                    "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
    private double changeReachDistance() {
	    if (this.player == null){
		    return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
	    }
	    if (this.player.hasPermissionLevel(2))
		    return Math.pow(Math.sqrt(ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE) + RugSettings.reachDistance - 4.5, 2);
	    return ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE;
    }
}
