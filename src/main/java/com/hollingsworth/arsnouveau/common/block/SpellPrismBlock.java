package com.hollingsworth.arsnouveau.common.block;

import com.hollingsworth.arsnouveau.api.block.IPrismaticBlock;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

public class SpellPrismBlock extends ModBlock implements IPrismaticBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public SpellPrismBlock(Properties properties) {
        super(properties);
    }

    public SpellPrismBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    public static Position getDispensePosition(BlockSource coords) {
        Direction direction = coords.getBlockState().getValue(FACING);
        double d0 = coords.x() + 0.3D * direction.getStepX();
        double d1 = coords.y() + 0.3D * direction.getStepY();
        double d2 = coords.z() + 0.3D * direction.getStepZ();
        return new PositionImpl(d0, d1, d2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onHit(ServerLevel world, BlockPos pos, EntityProjectileSpell spell) {
        Position iposition = getDispensePosition(new BlockSourceImpl(world, pos));
        Direction direction = world.getBlockState(pos).getValue(DispenserBlock.FACING);
        spell.setPos(iposition.x(), iposition.y(), iposition.z());
        spell.prismRedirect++;
        if(spell.prismRedirect >= 3){
            ANCriteriaTriggers.rewardNearbyPlayers(ANCriteriaTriggers.PRISMATIC, world, pos, 10);
        }
        if (spell.spellResolver == null) {
            spell.remove(Entity.RemovalReason.DISCARDED);
            return;
        }
        float acceleration = (spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentAccelerate.INSTANCE) - spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentDecelerate.INSTANCE) * 0.5F);
        float velocity = Math.max(0.1f, 0.5f + 0.1f * Math.min(2, acceleration));

        spell.shoot(direction.getStepX(), (direction.getStepY()), direction.getStepZ(), velocity, 0);
        BlockUtil.updateObservers(world, pos);
    }
}
