package gay.sylv.phonochat;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroups implements Initializable {
	static final ItemGroups INSTANCE = new ItemGroups();
	
	public static final ItemGroup PHONOCHAT = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Items.MICROPHONE))
			.entries((displayContext, entries) -> entries.add(Items.MICROPHONE))
			.build();
	
	private ItemGroups() {}
}
