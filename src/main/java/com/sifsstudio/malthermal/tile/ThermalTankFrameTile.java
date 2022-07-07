package com.sifsstudio.malthermal.tile;

import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.capability.IMultiBlockComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThermalTankFrameTile extends BaseTile {

    private int structureId = -1;

    public ThermalTankFrameTile() {
        super(TileEntities.THERMAL_TANK_FRAME.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (Capabilities.MULTI_BLOCK_COMPONENT_CAPABILITY.equals(cap)) {
            return LazyOptional.of(() -> new IMultiBlockComponent() {
                @Override
                public int getId() {
                    return structureId;
                }

                @Override
                public void setId(int id) {
                    structureId = id;
                }
            }).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = super.serializeNBT();
        compound.putInt("structureId", this.structureId);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.structureId = nbt.getInt("structureId");
        super.deserializeNBT(nbt);
    }

    @Override
    public boolean isNetwork() {
        return true;
    }
}
