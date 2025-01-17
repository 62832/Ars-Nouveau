package com.hollingsworth.arsnouveau.common.block.tile;

import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.event.EventQueue;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.MirrorWeave;
import com.hollingsworth.arsnouveau.common.event.timed.SkyweaveVisibilityEvent;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class SkyBlockTile extends MirrorWeaveTile implements ITickable, IDispellable {

    private boolean showFacade;
    public int previousLight;

    public SkyBlockTile(BlockPos pos, BlockState state) {
        super(BlockRegistry.SKYWEAVE_TILE.get(), pos, state);
    }

    @Override
    public void tick() {
        if(showFacade && !level.isClientSide) {
            if(getBlockState().getValue(MirrorWeave.LIGHT_LEVEL) != this.mimicState.getLightEmission(level, worldPosition)){
                level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MirrorWeave.LIGHT_LEVEL, this.mimicState.getLightEmission(level, worldPosition)));
            }
            return;
        }
        if(!level.isClientSide && level.isDay()){
            if(getBlockState().getValue(MirrorWeave.LIGHT_LEVEL) != 15){
                previousLight = getBlockState().getValue(MirrorWeave.LIGHT_LEVEL);
                level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MirrorWeave.LIGHT_LEVEL, 15));
            }
        }
        if(!level.isClientSide && !level.isDay()){
            if(getBlockState().getValue(MirrorWeave.LIGHT_LEVEL) != previousLight){
                level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MirrorWeave.LIGHT_LEVEL, this.mimicState.getLightEmission(level, worldPosition)));
            }
        }
    }


    public void setShowFacade(boolean showFacade){
        if(this.showFacade == showFacade){
            return;
        }

        int ticks = 1;
        for(Direction d : Direction.values()){
            BlockPos offset = getBlockPos().relative(d);
            if(level.getBlockEntity(offset) instanceof SkyBlockTile neighbor){
                if(this.showFacade() == neighbor.showFacade()) {
                    ticks++;
                    EventQueue.getServerInstance().addEvent(new SkyweaveVisibilityEvent(neighbor, ticks, showFacade));
                }
            }
        }
        this.showFacade = showFacade;
        this.updateBlock();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("showFacade", showFacade);
        tag.putInt("previousLight", previousLight);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        showFacade = pTag.getBoolean("showFacade");
        previousLight = pTag.getInt("previousLight");
    }

    public boolean showFacade() {
        return showFacade;
    }

    @Override
    public boolean onDispel(@NotNull LivingEntity caster) {
        this.setShowFacade(!this.showFacade());
        return true;
    }
}
