package com.rubixdev.rug.mixins;

import com.rubixdev.rug.RugSettings;
import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(AbstractPlantStemBlock.class)
public abstract class AbstractPlantStemBlockMixin extends AbstractPlantPartBlock {
    @Shadow @Final public static IntProperty AGE;

    @Shadow @Final private double growthChance;

    @Shadow protected abstract boolean chooseStemState(BlockState state);

    protected AbstractPlantStemBlockMixin(Settings settings, Direction growthDirection, VoxelShape outlineShape, boolean tickWater) {
        super(settings, growthDirection, outlineShape, tickWater);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        } else if (RugSettings.zeroTickPlants) {
            if (state.get(AGE) < 25 && random.nextDouble() < this.growthChance) {
                BlockPos blockPos = pos.offset(this.growthDirection);
                if (this.chooseStemState(world.getBlockState(blockPos))) {
                    world.setBlockState(blockPos, state.cycle(AGE));
                }
            }
        }
    }
}
