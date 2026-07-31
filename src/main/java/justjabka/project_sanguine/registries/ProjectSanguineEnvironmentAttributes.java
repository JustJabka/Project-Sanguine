package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.AttributeRange;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Set;

public class ProjectSanguineEnvironmentAttributes {
    private static final float THE_NETHER_BIOMES_AURA = -0.05f;
    private static final float THE_END_BIOMES_AURA = 0.03f;
    private static final float FLOWER_BIOMES_AURA = 0.08f;
    private static final float WARM_OCEAN_BIOME_AURA = 0.05f;
    private static final float LUSH_CAVE_BIOME_AURA = 0.05f;
    private static final float DEEP_DARK_BIOME_AURA = -0.08f;
    private static final float PALE_GARDEN_BIOME_AURA = -0.07f;

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
        modifyVanillaEnvironmentAttributes();
    }

    private static void modifyVanillaEnvironmentAttributes() {
        ProjectSanguine.LOGGER.info("Modifying Vanilla's Environment Attributes");

        final Set<ResourceKey<Biome>> FLOWER_BIOMES = Set.of(
                Biomes.FLOWER_FOREST,
                Biomes.CHERRY_GROVE,
                Biomes.MEADOW,
                Biomes.SUNFLOWER_PLAINS
        );

        BiomeModifications.create(ProjectSanguine.id("sanity_aura"))
                // Dimension Aura
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInTheNether(), context ->
                        context.getAttributes().set(SANITY_AURA, THE_NETHER_BIOMES_AURA))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInTheEnd(), context ->
                        context.getAttributes().set(SANITY_AURA, THE_END_BIOMES_AURA))

                // Biome Positive Aura
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(FLOWER_BIOMES), context ->
                        context.getAttributes().set(SANITY_AURA, FLOWER_BIOMES_AURA))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.WARM_OCEAN), context ->
                        context.getAttributes().set(SANITY_AURA, WARM_OCEAN_BIOME_AURA))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.LUSH_CAVES), context ->
                        context.getAttributes().set(SANITY_AURA, LUSH_CAVE_BIOME_AURA))
                
                // Biome Negative Aura
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.DEEP_DARK), context ->
                        context.getAttributes().set(SANITY_AURA, DEEP_DARK_BIOME_AURA))
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.PALE_GARDEN), context ->
                        context.getAttributes().set(SANITY_AURA, PALE_GARDEN_BIOME_AURA));
    }

    private static <Value> EnvironmentAttribute<Value> register(final String id, final EnvironmentAttribute.Builder<Value> attributeBuilder) {
        EnvironmentAttribute<Value> attribute = attributeBuilder.build();
        Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, ProjectSanguine.id(id), attribute);
        return attribute;
    }
}
