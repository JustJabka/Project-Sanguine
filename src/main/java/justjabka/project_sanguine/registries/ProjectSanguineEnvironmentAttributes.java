package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeRange;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;

public class ProjectSanguineEnvironmentAttributes {
    public static final EnvironmentAttribute<Float> SANITY_AURA = register(
            "gameplay/sanity_aura",
            EnvironmentAttribute.builder(AttributeTypes.FLOAT)
                    .defaultValue(0f)
                    .valueRange(AttributeRange.ofFloat(-1024f,1024f))
                    .spatiallyInterpolated()
                    .syncable()
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Environment Attributes");
    }

    private static <Value> EnvironmentAttribute<Value> register(final String id, final EnvironmentAttribute.Builder<Value> attributeBuilder) {
        EnvironmentAttribute<Value> attribute = attributeBuilder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, ProjectSanguine.id(id), attribute);
        return attribute;
    }
}
