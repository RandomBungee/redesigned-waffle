/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R1.block.impl;

public final class CraftReed extends org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData implements org.bukkit.block.data.Ageable {

    public CraftReed() {
        super();
    }

    public CraftReed(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_20_R1.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.SugarCaneBlock.class, "age");

    @Override
    public int getAge() {
        return get(CraftReed.AGE);
    }

    @Override
    public void setAge(int age) {
        set(CraftReed.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftReed.AGE);
    }
}
