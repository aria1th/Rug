package de.rubixdev.rug.mixins;

import de.rubixdev.rug.RugSettings;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PressurePlateBlock.class)
public class PressurePlateBlockMixin {
    @Redirect(
            method = "getRedstoneOutput(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I",
            require = 0,
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/block/PressurePlateBlock;getEntityCount(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Box;Ljava/lang/Class;)I"))
    private int getEntityCountModified(World world, Box box, Class<? extends Entity> aClass) {
        int i = getEntityCount(world, box, aClass);
        return i > 0 ? 15 : 0;
    }

    private static int getEntityCount(World world, Box box, Class<? extends Entity> entityClass) {
        return world.getEntitiesByClass(entityClass, box, EntityPredicates.EXCEPT_SPECTATOR.and((entity) -> {
                    return !entity.canAvoidTraps()
                            || (RugSettings.itemFramesActivatePressurePlates
                                    && entity.getType() == EntityType.ITEM_FRAME);
                }))
                .size();
    }
}
