package com.hollingsworth.arsnouveau.common.block.tile;

import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.common.block.ManaJar;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PotionJarTile extends TileEntity implements ITickableTileEntity, ITooltipProvider {

    public int amount;
    private Potion potion;
    public PotionJarTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public PotionJarTile() {
        super(BlockRegistry.POTION_JAR_TYPE);
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            // world.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
            return;
        }
        BlockState state = world.getBlockState(pos);
        int fillState = 0;
        if(this.getCurrentFill() > 0 && this.getCurrentFill() < 1000)
            fillState = 1;
        else if(this.getCurrentFill() != 0){
            fillState = (this.getCurrentFill() / 1000) + 1;
        }

        world.setBlockState(pos, state.with(ManaJar.fill, fillState),3);
    }

    public void setPotion(Potion potion){
        this.potion = potion == null ? Potions.EMPTY : potion;
    }

    public @Nonnull Potion getPotion(){
        return potion == null ? Potions.EMPTY : potion;
    }

    public int getColor(){
        return potion == null ? 16253176 : PotionUtils.getPotionColor(potion);
    }

    public int getCurrentFill(){
        return amount;
    }

    public void setFill(int fill){
        amount = fill;
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    public void addAmount(Potion potion, int fill){
        setPotion(potion);
        amount = Math.min(getMaxFill(), amount + fill);
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    @Override
    public List<String> getTooltip() {
        List<String> list = new ArrayList<>();
        if(this.potion != null && this.potion != Potions.EMPTY) {
            ItemStack potionStack = new ItemStack(Items.POTION);
            PotionUtils.addPotionToItemStack(potionStack, potion);
            list.add(potionStack.getDisplayName().getString());

        }
        list.add( (getCurrentFill()*100) / this.getMaxFill() + "% full");
        return list;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.amount = tag.getInt("amount");
        this.potion = PotionUtils.getPotionTypeFromNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        ResourceLocation resourcelocation = Registry.POTION.getKey(potion);
        tag.putInt("amount", this.amount);
        tag.putString("Potion", resourcelocation.toString());
        return super.write(tag);
    }


    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(world.getBlockState(pos),pkt.getNbtCompound());
    }

    public int getMaxFill() {
        return 10000;
    }
}

