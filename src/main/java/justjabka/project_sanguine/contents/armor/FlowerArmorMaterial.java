package justjabka.project_sanguine.contents.armor;

import justjabka.project_sanguine.ProjectSanguine;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class FlowerArmorMaterial {
    public static final int BASE_DURABILITY = 3;
    public static final ResourceKey<EquipmentAsset> FLOWER_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, ProjectSanguine.id("flower"));

    public static final TagKey<Item> REPAIRS_FLOWER_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), ProjectSanguine.id("repairs_flower_armor"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 0,
                    ArmorType.CHESTPLATE, 1,
                    ArmorType.LEGGINGS, 1,
                    ArmorType.BOOTS, 0
            ),
            5,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0.0F,
            0.0F,
            REPAIRS_FLOWER_ARMOR,
            FLOWER_ARMOR_MATERIAL_KEY
    );
}
