package gay.sylv.phonochat;

import gay.sylv.phonochat.item.MicrophoneItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static gay.sylv.phonochat.PhonochatMod.MOD_ID;

@SuppressWarnings("unused")
public final class Items implements Initializable {
	static final Items INSTANCE = new Items();
	
	public static final MicrophoneItem MICROPHONE = Registry.register(
			Registries.ITEM, new Identifier(MOD_ID, "microphone"),
			new MicrophoneItem(
					new FabricItemSettings()
			)
	);
	
	private Items() {}
}
