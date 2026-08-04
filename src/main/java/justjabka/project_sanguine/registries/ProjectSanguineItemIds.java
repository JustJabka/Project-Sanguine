package justjabka.project_sanguine.registries;

import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ProjectSanguineItemIds {
    public static final ResourceKey<Item> FLOWER_CROWN = create("flower_crown");
    public static final ResourceKey<Item> RITUAL_KNIFE = create("ritual_knife");

    private static @NonNull ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, ProjectSanguine.id(name));
    }
}
