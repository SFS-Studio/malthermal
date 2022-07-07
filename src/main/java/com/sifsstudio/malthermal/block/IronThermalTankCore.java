package com.sifsstudio.malthermal.block;

import com.sifsstudio.malthermal.tile.IronThermalTankCoreTile;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IronThermalTankCore extends AbstractThermalTankCore {
    protected IronThermalTankCore() {
        super(Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nonnull IBlockReader world) {
        return new IronThermalTankCoreTile();
    }
}
