package gay.sylv.phonochat.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gay.sylv.phonochat.PhonochatMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class PhonochatClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID + "/Client");
	public static final Int2ObjectMap<ObjectList<BlockPos>> LISTENING_CHANNELS = new Int2ObjectArrayMap<>();
	/**
	 * A map of channels listened to slots in the player's inventory
	 */
	public static final Int2ObjectMap<IntList> HEADPHONES_CHANNELS = new Int2ObjectArrayMap<>();
	/**
	 * If the player is wearing headphones.
	 */
	public static boolean hasHeadphones = false;
	
	@Override
	public void onInitializeClient() {
	}
}
