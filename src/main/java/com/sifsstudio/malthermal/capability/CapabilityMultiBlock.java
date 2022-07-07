package com.sifsstudio.malthermal.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CapabilityMultiBlock {
    protected static class Storage implements Capability.IStorage<IMultiBlock> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IMultiBlock> capability, IMultiBlock instance, Direction side) {
            ListNBT list = new ListNBT();
            list.addAll(instance.getMultiBlocks().stream().map(IMultiBlock.MultiBlock::serializeNBT).collect(Collectors.toList()));
            return list;
        }

        @Override
        public void readNBT(Capability<IMultiBlock> capability, IMultiBlock instance, Direction side, INBT nbt) {
            ListNBT list = (ListNBT) nbt;
            instance.clear();
            for (int i = 0; i < list.size(); i++) {
                IMultiBlock.MultiBlock multiBlock = IMultiBlock.MultiBlock.deserializeNBT(list.getCompound(i));
                instance.setMultiBlock(multiBlock.getId(), multiBlock);
            }
        }
    }

    protected static class Implementation implements IMultiBlock {

        private final HashMap<Integer, MultiBlock> multiBlockMap = new HashMap<>();

        @Override
        public Collection<MultiBlock> getMultiBlocks() {
            return this.multiBlockMap.values();
        }

        @Override
        public MultiBlock getMultiBlockById(int id) {
            return this.multiBlockMap.get(id);
        }

        @Override
        public void setMultiBlock(int id, MultiBlock multiBlock) {
            this.multiBlockMap.put(id, multiBlock);
        }

        @Override
        public void removeMultiBlock(int id) {
            this.multiBlockMap.remove(id);
        }

        @Override
        public void clear() {
            this.multiBlockMap.clear();
        }
    }

    public static class WorldCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

        private final IMultiBlock multiBlock = new Implementation();
        private final Capability.IStorage<IMultiBlock> storage = Capabilities.MULTI_BLOCK_CAPABILITY.getStorage();

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (Capabilities.MULTI_BLOCK_CAPABILITY.equals(cap)) {
                return LazyOptional.of(() -> this.multiBlock).cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT compoundNBT = new CompoundNBT();
            INBT list = storage.writeNBT(Capabilities.MULTI_BLOCK_CAPABILITY, this.multiBlock, null);
            if (list == null) {
                list = new ListNBT();
            }
            compoundNBT.put("multiBlocks", list);
            return compoundNBT;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            INBT list = nbt.get("multiBlocks");
            storage.readNBT(Capabilities.MULTI_BLOCK_CAPABILITY, multiBlock, null, list);
        }
    }
}
