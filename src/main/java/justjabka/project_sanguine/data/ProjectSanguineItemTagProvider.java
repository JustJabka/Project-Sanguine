package justjabka.project_sanguine.data;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.registries.ProjectSanguineItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ProjectSanguineItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> REPAIRS_FLOWER_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), ProjectSanguine.id("repairs_flower_armor"));


    public ProjectSanguineItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(ItemTags.HEAD_ARMOR)
                .add(ProjectSanguineItemIds.FLOWER_CROWN);

        builder(REPAIRS_FLOWER_ARMOR)
                .forceAddTag(BlockItemTags.SMALL_FLOWERS.item());
    }
}