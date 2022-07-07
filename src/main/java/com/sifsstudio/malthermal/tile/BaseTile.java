package com.sifsstudio.malthermal.tile;

import net.minecraft.block.BlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class BaseTile extends TileEntity {
    public BaseTile(TileEntityType<?> type) {
        super(type);
    }

    public abstract boolean isNetwork();

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        if (this.isNetwork()) {
            return new SUpdateTileEntityPacket(this.worldPosition, -1, this.serializeNBT());
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

    public void notifyClient() {
        if (this.isNetwork()) {
            if (this.level != null && !this.level.isClientSide) {
                BlockState state = this.getBlockState();
                this.level.sendBlockUpdated(this.worldPosition, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                this.setChanged();
            }
        }
    }
}
