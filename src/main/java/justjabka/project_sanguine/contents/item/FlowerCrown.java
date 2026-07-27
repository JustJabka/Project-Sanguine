package justjabka.project_sanguine.contents.item;

import justjabka.project_sanguine.contents.armor.FlowerArmorMaterial;
import justjabka.project_sanguine.contents.component.SanityProviderComponent;
import justjabka.project_sanguine.registries.ProjectSanguineComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class FlowerCrown extends Item {
    public FlowerCrown(Properties properties) {
        super(properties
                .stacksTo(1)
                .durability(ArmorType.HELMET.getDurability(FlowerArmorMaterial.BASE_DURABILITY))
                .humanoidArmor(FlowerArmorMaterial.INSTANCE, ArmorType.HELMET)
                .component(ProjectSanguineComponents.SANITY_PROVIDER, new SanityProviderComponent(0.1f, 1))
        );
    }
}
