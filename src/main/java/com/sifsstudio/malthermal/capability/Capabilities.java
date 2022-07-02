package com.sifsstudio.malthermal.capability;

import com.sifsstudio.malthermal.util.Utilities;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {

    @CapabilityInject(IMultiBlock.class)
    public static final Capability<IMultiBlock> MULTI_BLOCK_CAPABILITY = Utilities.nonnull();
    @CapabilityInject(IMultiBlockComponent.class)
    public static final Capability<IMultiBlockComponent> MULTI_BLOCK_COMPONENT_CAPABILITY = Utilities.nonnull();

    public static void register() {
        CapabilityManager.INSTANCE.register(IMultiBlock.class, new CapabilityMultiBlock.Storage(), CapabilityMultiBlock.Implementation::new);
        CapabilityManager.INSTANCE.register(IMultiBlockComponent.class, new CapabilityMultiBlockComponent.Storage(), CapabilityMultiBlockComponent.Implementation::new);
    }

}
