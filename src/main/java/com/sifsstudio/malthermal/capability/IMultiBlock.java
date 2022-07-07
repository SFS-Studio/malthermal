package com.sifsstudio.malthermal.capability;

import com.sifsstudio.malthermal.registry.MultiBlockType;
import com.sifsstudio.malthermal.registry.Registries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.stream.Collectors;

public interface IMultiBlock {

    Collection<MultiBlock> getMultiBlocks();

    MultiBlock getMultiBlockById(int id);

    void setMultiBlock(int id, MultiBlock multiBlock);

    void removeMultiBlock(int id);

    void clear();

    default int getAvailableId() {
        return this.getMultiBlocks().stream().max(Comparator.comparingInt(multiBlock -> multiBlock.id)).map(MultiBlock::getId).orElse(-1) + 1;
    }

    default int addMultiBlock(BlockPos controller, List<BlockPos> blocks, MultiBlockType type) {
        int id = this.getAvailableId();
        this.setMultiBlock(id, new MultiBlock(id, blocks, controller, type));
        return id;
    }

    class MultiBlock {
        private final int id;
        private final List<BlockPos> blocks;
        private final BlockPos controller;
        private final MultiBlockType type;

        private MultiBlock(int id, List<BlockPos> blocks, BlockPos controller, MultiBlockType type) {
            this.id = id;
            this.blocks = blocks;
            this.controller = controller;
            this.type = type;
        }

        public static MultiBlock deserializeNBT(CompoundNBT compound) {
            List<BlockPos> blocks = new ArrayList<>();
            ListNBT list = compound.getList("blocks", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                blocks.add(i, NBTUtil.readBlockPos(list.getCompound(i)));
            }
            return new MultiBlock(compound.getInt("id"), blocks, NBTUtil.readBlockPos(compound.getCompound("controller")), Registries.MULTI_BLOCK_TYPE.get().getValue(new ResourceLocation(compound.getString("type"))));
        }

        public List<BlockPos> getBlocks() {
            return blocks;
        }

        public int getId() {
            return id;
        }

        public MultiBlockType getType() {
            return type;
        }

        public CompoundNBT serializeNBT() {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("id", this.id);
            compound.putString("type", Objects.requireNonNull(this.type.getRegistryName()).toString());
            compound.put("controller", NBTUtil.writeBlockPos(this.controller));
            ListNBT blockList = new ListNBT();
            blockList.addAll(this.blocks.stream().map(NBTUtil::writeBlockPos).collect(Collectors.toList()));
            compound.put("blocks", blockList);
            return compound;
        }
    }

}
