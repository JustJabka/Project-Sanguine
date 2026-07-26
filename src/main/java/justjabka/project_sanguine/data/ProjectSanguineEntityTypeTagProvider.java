package justjabka.project_sanguine.data;

import justjabka.project_sanguine.ProjectSanguine;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class ProjectSanguineEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public static final TagKey<EntityType<?>> INCREASES_SANITY = TagKey.create(Registries.ENTITY_TYPE, ProjectSanguine.id("increases_sanity"));


    public ProjectSanguineEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(INCREASES_SANITY)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.PARROT);
    }
}
