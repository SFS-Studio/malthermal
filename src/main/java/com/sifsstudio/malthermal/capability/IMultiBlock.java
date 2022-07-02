package com.sifsstudio.malthermal.capability;

import com.sifsstudio.malthermal.registry.MultiBlockType;
import com.sifsstudio.malthermal.registry.Registries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface IMultiBlock {

    class MultiBlock {
        private final int id;
        private final List<BlockPos> blocks;
        private final MultiBlockType type;

        public MultiBlock(int id, MultiBlockType type) {
            this.id = id;
            this.blocks = new ArrayList<>();
            this.type = type;
        }

        private MultiBlock(int id, List<BlockPos> blocks, MultiBlockType type) {
            this.id = id;
            this.blocks = blocks;
            this.type = type;
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
            ListNBT blockList = new ListNBT();
            blockList.addAll(this.blocks.stream().map(NBTUtil::writeBlockPos).collect(Collectors.toList()));
            compound.put("blocks", blockList);
            return compound;
        }

        public static MultiBlock deserializeNBT(CompoundNBT compound) {
            List<BlockPos> blocks = new ArrayList<>();
            ListNBT list = compound.getList("blocks", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                blocks.add(i, NBTUtil.readBlockPos(list.getCompound(i)));
            }
            return new MultiBlock(compound.getInt("id"), blocks, Registries.MULTI_BLOCK_TYPE.get().getValue(new ResourceLocation(compound.getString("type"))));
        }
    }

    Collection<MultiBlock> getMultiBlocks();
    MultiBlock getMultiBlockById(int id);
    void setMultiBlock(int id, MultiBlock multiBlock);
    void removeMultiBlock(int id);
    void clear();

}
