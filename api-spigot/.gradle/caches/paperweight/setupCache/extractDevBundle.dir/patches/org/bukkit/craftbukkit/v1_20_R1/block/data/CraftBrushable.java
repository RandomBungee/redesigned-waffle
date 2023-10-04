package org.bukkit.craftbukkit.v1_20_R1.block.data;

import org.bukkit.block.data.Brushable;

public abstract class CraftBrushable extends CraftBlockData implements Brushable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DUSTED = getInteger("dusted");

    @Override
    public int getDusted() {
        return get(CraftBrushable.DUSTED);
    }

    @Override
    public void setDusted(int dusted) {
        set(CraftBrushable.DUSTED, dusted);
    }

    @Override
    public int getMaximumDusted() {
        return getMax(CraftBrushable.DUSTED);
    }
}
