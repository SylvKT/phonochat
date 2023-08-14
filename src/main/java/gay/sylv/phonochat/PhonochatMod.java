package gay.sylv.phonochat;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhonochatMod implements ModInitializer {
	public static final String MOD_ID = "phonochat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Phonochat initialized!");
		
		Items.INSTANCE.initialize();
		ItemGroups.INSTANCE.initialize();
	}
}
