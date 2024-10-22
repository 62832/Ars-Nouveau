package com.hollingsworth.arsnouveau.api.source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface ISourceCap {


    boolean canAcceptSource(int source);

    boolean canProvideSource(int source);

    int getMaxExtract();

    int getMaxReceive();

    default boolean canExtract() {
        return canProvideSource(1);
    }

    default boolean canReceive() {
        return canAcceptSource(1);
    }

    int getSource();

    int getSourceCapacity();

    default int getMaxSource() {
        return getSourceCapacity();
    }

    /**
     * Force set the amount of source stored, clamped to the max source.
     * Use for source generation or other use-cases without transfer rates.
     */
    void setSource(int source);

    void setMaxSource(int max);

    int receiveSource(final int source, boolean simulate);

    int extractSource(final int source, boolean simulate);

    static Direction getDirTo(BlockPos from, BlockPos to) {
        var x = from.getX() - to.getX();
        var y = from.getY() - to.getY();
        var z = from.getZ() - to.getZ();

        if (Math.abs(y) >= Math.abs(x) && Math.abs(y) >= Math.abs(z)) {
            return y > 0 ? Direction.UP : Direction.DOWN;
        } else if (Math.abs(x) > Math.abs(z)) {
            return x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
}
