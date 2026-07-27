package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ProjectSanguineAttributes {
    public static final Holder<Attribute> SANITY_AURA = register(
            "sanity_aura",
            0,
            -1024,
            1024,
            true
    );
    public static final Holder<Attribute> MAX_SANITY = register(
            "max_sanity",
            125,
            0,
            1024,
            true
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Attributes");
    }

    private static Holder<Attribute> register(
            String name, double defaultValue, double minValue, double maxValue, boolean syncedWithClient
    ) {
        Identifier identifier = ProjectSanguine.id(name);

        Attribute entityAttribute = new RangedAttribute(
                identifier.toLanguageKey("attribute.name"),
                defaultValue,
                minValue,
                maxValue
        ).setSyncable(syncedWithClient);

        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, identifier, entityAttribute);
    }
}
