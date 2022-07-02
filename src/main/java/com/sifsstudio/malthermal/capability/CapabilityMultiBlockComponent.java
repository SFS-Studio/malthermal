package com.sifsstudio.malthermal.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CapabilityMultiBlockComponent {

    protected static class Storage implements Capability.IStorage<IMultiBlockComponent> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IMultiBlockComponent> capability, IMultiBlockComponent instance, Direction side) {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("id", instance.getId());
            return compound;
        }

        @Override
        public void readNBT(Capability<IMultiBlockComponent> capability, IMultiBlockComponent instance, Direction side, INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
            instance.setId(compound.getInt("id"));
        }
    }

    protected static class Implementation implements IMultiBlockComponent {
        private int id = -1;

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }
    }

}
