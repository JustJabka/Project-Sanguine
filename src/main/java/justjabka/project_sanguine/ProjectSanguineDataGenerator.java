package justjabka.project_sanguine;

import justjabka.project_sanguine.data.ProjectSanguineEntityTypeTagProvider;
import justjabka.project_sanguine.data.ProjectSanguineItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ProjectSanguineDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ProjectSanguineEntityTypeTagProvider::new);
		pack.addProvider(ProjectSanguineItemTagProvider::new);
	}
}
