package com.sifsstudio.malthermal.tile;

import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractThermalTankCoreTile extends BaseTile {
    public AbstractThermalTankCoreTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public boolean isNetwork() {
        return true;
    }
}
