package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.contents.item.FlowerCrown;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ProjectSanguineItems {
    public static final Item FLOWER_CROWN = register(ProjectSanguineItemIds.FLOWER_CROWN, FlowerCrown::new, new Item.Properties());

    public static void initialize() {
        ProjectSanguine.LOGGER.info("Initializing Items");
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
}
