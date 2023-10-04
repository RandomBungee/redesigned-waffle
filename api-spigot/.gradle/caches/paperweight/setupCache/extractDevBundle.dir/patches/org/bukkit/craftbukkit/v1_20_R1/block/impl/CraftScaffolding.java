/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R1.block.impl;

public final class CraftScaffolding extends org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.Scaffolding, org.bukkit.block.data.Waterlogged {

    public CraftScaffolding() {
        super();
    }

    public CraftScaffolding(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.type.CraftScaffolding

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BOTTOM = getBoolean(net.minecraft.world.level.block.ScaffoldingBlock.class, "bottom");
    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger(net.minecraft.world.level.block.ScaffoldingBlock.class, "distance");

    @Override
    public boolean isBottom() {
        return get(CraftScaffolding.BOTTOM);
    }

    @Override
    public void setBottom(boolean bottom) {
        set(CraftScaffolding.BOTTOM, bottom);
    }

    @Override
    public int getDistance() {
        return get(CraftScaffolding.DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        set(CraftScaffolding.DISTANCE, distance);
    }

    @Override
    public int getMaximumDistance() {
        return getMax(CraftScaffolding.DISTANCE);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.ScaffoldingBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(CraftScaffolding.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(CraftScaffolding.WATERLOGGED, waterlogged);
    }
}
