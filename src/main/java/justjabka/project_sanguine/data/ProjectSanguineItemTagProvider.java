package justjabka.project_sanguine.data;

import justjabka.project_sanguine.registries.ProjectSanguineItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ProjectSanguineItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ProjectSanguineItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(ItemTags.HEAD_ARMOR)
                .add(ProjectSanguineItemIds.FLOWER_CROWN);
    }
}