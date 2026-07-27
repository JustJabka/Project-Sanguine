package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.Set;

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
        modifyVanillaAttributes();
    }

    private static void modifyVanillaAttributes() {
        final Set<EntityType<? extends LivingEntity>> SPECIFIC_POSITIVE_AURA = Set.of(
                EntityTypes.BEE,
                EntityTypes.WOLF,
                EntityTypes.CAT,
                EntityTypes.PARROT
        );

        FabricDefaultAttributeRegistry.MODIFY.register(context -> {
            // Add sanity aura for all living entities
            context.modifyAll((type, builder) ->
                    builder.add(ProjectSanguineAttributes.SANITY_AURA));

            // Add max sanity for players
            context.modify(EntityTypes.PLAYER, ((type, builder) ->
                    builder.add(ProjectSanguineAttributes.MAX_SANITY)));

            // Add positive aura for pets
            context.modify(SPECIFIC_POSITIVE_AURA, (type, builder) ->
                    builder.add(ProjectSanguineAttributes.SANITY_AURA, 0.1));

            // Add negative aura for monsters
            context.modify(
                    type -> type.getCategory() == MobCategory.MONSTER,
                    (type, builder) -> builder.add(ProjectSanguineAttributes.SANITY_AURA, -0.1)
            );


            // Add negative aura for bosses
            context.modify(
                    type -> {
                        Holder<EntityType<?>> holder = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type);
                        return holder.is(ConventionalEntityTypeTags.BOSSES);
                    }, (type, builder) ->
                            builder.add(ProjectSanguineAttributes.SANITY_AURA, -2));

            // So cool that it's above even bosses
            context.modify(EntityTypes.WARDEN, (type, builder) ->
                    builder.add(ProjectSanguineAttributes.SANITY_AURA, -2));

            // Who is he?😨
            context.modify(EntityTypes.ELDER_GUARDIAN, (type, builder) ->
                    builder.add(ProjectSanguineAttributes.SANITY_AURA, -0.5));
        });
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
