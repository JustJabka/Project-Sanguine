package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.component.SanityProviderComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ProjectSanguineComponents {
    public static final DataComponentType<SanityProviderComponent> SANITY_PROVIDER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ProjectSanguine.id("sanity_provider"),
            DataComponentType.<SanityProviderComponent>builder().persistent(SanityProviderComponent.CODEC).build()
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Components");
    }
}
