package justjabka.project_sanguine.contents.entity;

import justjabka.project_sanguine.registries.ProjectSanguineEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.level.Level;

public class Arachnophobia extends CaveSpider {
    public Arachnophobia(EntityType<? extends CaveSpider> type, Level level) {
        super(type, level);
    }

    public Arachnophobia(Level level) {
        super(ProjectSanguineEntityTypes.ARACHNOPHOBIA, level);
    }

    public static AttributeSupplier.Builder createArachnophobiaAttributes() {
        return CaveSpider.createCaveSpider()
                .add(Attributes.MAX_HEALTH, 32)
                .add(Attributes.ATTACK_DAMAGE, 4);
    }
}
