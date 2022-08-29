package de.rubixdev.rug.mixins;

import de.rubixdev.rug.RugSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @Shadow
    @Final
    public static DirectionProperty FACING;

    @Inject(
            method = "dispense",
            at =
                    @At(
                            value = "INVOKE_ASSIGN",
                            target =
                                    "Lnet/minecraft/block/entity/DispenserBlockEntity;setStack(ILnet/minecraft/item/ItemStack;)V"),
            cancellable = true)
    private void tryDispense(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        if (!RugSettings.renewableCalcite) return;
        BlockPos facingPos = pos.offset(world.getBlockState(pos).get(FACING));
        BlockState state = world.getBlockState(facingPos);
        if (state.isOf(Blocks.COBBLESTONE)) {
            BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);
            DispenserBlockEntity dispenserBlockEntity = blockPointerImpl.getBlockEntity();
            int slot = dispenserBlockEntity.chooseNonEmptySlot(world.random);
            if (slot == -1) {
                return;
            }
            ItemStack stack = dispenserBlockEntity.getStack(slot);
            if (stack.isOf(Items.BONE_MEAL)) {
                stack.decrement(1);
                world.setBlockState(facingPos, Blocks.CALCITE.getDefaultState());
                ci.cancel();
            }
        }
    }
}
