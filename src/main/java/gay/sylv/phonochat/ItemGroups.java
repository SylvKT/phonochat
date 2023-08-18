package gay.sylv.phonochat;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static gay.sylv.phonochat.PhonochatMod.MOD_ID;

public final class ItemGroups implements Initializable {
	static final ItemGroups INSTANCE = new ItemGroups();
	
	public static final ItemGroup PHONOCHAT = FabricItemGroup.builder()
			.displayName(Text.translatable("phonochat.phonochat_group"))
			.icon(() -> new ItemStack(Items.MICROPHONE))
			.entries((displayContext, entries) -> entries.add(Items.MICROPHONE))
			.build();
	
	private ItemGroups() {}
	
	@Override
	public void initialize() {
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "phonochat"), PHONOCHAT);
	}
}
