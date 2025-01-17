package de.rubixdev.rug.mixins.reach;

import de.rubixdev.rug.RugSettings;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @ModifyConstant(
            method =
                    "method_17696(Lnet/minecraft/block/Block;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Boolean;",
            allow = 1,
            require = 0,
            constant = @Constant(doubleValue = 64.0))
    private static double changeReachDistance(double baseReachDistance) {
        return Math.pow(Math.sqrt(baseReachDistance) + RugSettings.reachDistance - 4.5, 2);
    }
}
