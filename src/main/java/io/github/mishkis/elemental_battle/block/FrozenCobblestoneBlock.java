package io.github.mishkis.elemental_battle.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class FrozenCobblestoneBlock extends MultifaceGrowthBlock {
    public static final MapCodec<FrozenCobblestoneBlock> CODEC = FrozenCobblestoneBlock.createCodec(FrozenCobblestoneBlock::new);

    public FrozenCobblestoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<FrozenCobblestoneBlock> getCodec() {
        return CODEC;
    }

    @Override
    public boolean canGrowWithDirection(BlockView world, BlockState state, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return false;
    }

    @Override
    public LichenGrower getGrower() {
        return null;
    }
}
