package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.entity.Necrophagia;
import justjabka.project_sanguine.contents.entity.projectile.PhantomCharge;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ProjectSanguineEntityTypes {
    public static final EntityType<Necrophagia> NECROPHAGIA = register(
            "necrophagia",
            EntityType.Builder.<Necrophagia>of(Necrophagia::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.95f)
                    .eyeHeight(1.74F)
                    .passengerAttachments(2.0125F)
                    .ridingOffset(-0.7F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
    );

    public static final EntityType<PhantomCharge> PHANTOM_CHARGE = register(
            "phantom_charge",
            EntityType.Builder.<PhantomCharge>of(PhantomCharge::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Entity Types");
        registerAttributes();
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, ProjectSanguine.id(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    @SuppressWarnings("UnusedReturnValue")
    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(NECROPHAGIA, Necrophagia.createAttributes());
    }
}
