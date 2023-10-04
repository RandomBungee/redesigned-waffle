package org.bukkit.craftbukkit.v1_20_R1.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkSensor;

public class CraftSculkSensor<T extends SculkSensorBlockEntity> extends CraftBlockEntityState<T> implements SculkSensor {

    public CraftSculkSensor(World world, T tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public int getLastVibrationFrequency() {
        return getSnapshot().getLastVibrationFrequency();
    }

    @Override
    public void setLastVibrationFrequency(int lastVibrationFrequency) {
        Preconditions.checkArgument(0 <= lastVibrationFrequency && lastVibrationFrequency <= 15, "Vibration frequency must be between 0-15");
        getSnapshot().lastVibrationFrequency = lastVibrationFrequency;
    }
    // Paper start
    @Override
    public int getListenerRange() {
        return this.getSnapshot().getListener().getListenerRadius();
    }

    @Override
    public void setListenerRange(int range) {
        Preconditions.checkArgument(range > 0, "Vibration listener range must be greater than 0");
        this.getSnapshot().rangeOverride = range;
    }
    // Paper end
}
