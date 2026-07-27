package justjabka.project_sanguine.contents.armor;

import justjabka.project_sanguine.ProjectSanguine;
import justjabka.project_sanguine.data.ProjectSanguineItemTagProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class FlowerArmorMaterial {
    public static final int BASE_DURABILITY = 5;
    public static final ResourceKey<EquipmentAsset> FLOWER_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, ProjectSanguine.id("flower"));

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
            ProjectSanguineItemTagProvider.REPAIRS_FLOWER_ARMOR,
            FLOWER_ARMOR_MATERIAL_KEY
    );
}
