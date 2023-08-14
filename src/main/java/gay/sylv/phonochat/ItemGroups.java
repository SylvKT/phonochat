package gay.sylv.phonochat;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ItemGroups implements Initializable {
	static final ItemGroups INSTANCE = new ItemGroups();
	
	public static final ItemGroup PHONOCHAT = FabricItemGroup.builder()
			.displayName(Text.translatable("phonochat.phonochat_group"))
			.icon(() -> new ItemStack(Items.MICROPHONE))
			.entries((displayContext, entries) -> entries.add(Items.MICROPHONE))
			.build();
	
	private ItemGroups() {}
}
