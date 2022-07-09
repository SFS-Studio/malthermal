package com.sifsstudio.malthermal.tile;

import com.sifsstudio.malthermal.capability.Capabilities;
import com.sifsstudio.malthermal.capability.IMultiBlockComponent;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThermalGlassTile extends BaseTile {

    private int structureId = -1;

    public ThermalGlassTile() {
        super(TileEntities.THERMAL_GLASS.get());
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

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT pCompound) {
        CompoundNBT compound =  super.save(pCompound);
        compound.putInt("structureId", this.structureId);
        return compound;
    }

    @Override
    public void load(@Nonnull BlockState pState, @Nonnull CompoundNBT pCompound) {
        this.structureId = pCompound.getInt("structureId");
        super.load(pState, pCompound);
    }

    @Override
    public boolean isNetwork() {
        return false;
    }
}
