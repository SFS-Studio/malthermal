package com.sifsstudio.malthermal.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseTile extends TileEntity {
    public BaseTile(TileEntityType<?> type) {
        super(type);
    }

    public abstract boolean isNetwork();

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        if (this.isNetwork()) {
            return this.serializeNBT();
        } else {
            return super.getUpdateTag();
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        if (this.isNetwork()) {
            return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
        } else {
            return super.getUpdatePacket();
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (this.isNetwork()) {
            this.deserializeNBT(pkt.getTag());
        }
    }

    public void notifyChanged() {
        if (this.isNetwork()) {
            if (this.level != null && !this.level.isClientSide) {
                BlockState state = this.getBlockState();
                this.level.sendBlockUpdated(this.worldPosition, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                this.setChanged();
            }
        }
    }

    // Override to rename parameters & annotate

    @Override
    public void load(@Nonnull BlockState pState, @Nonnull CompoundNBT pCompound) {
        super.load(pState, pCompound);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT pCompound) {
        return super.save(pCompound);
    }
}
