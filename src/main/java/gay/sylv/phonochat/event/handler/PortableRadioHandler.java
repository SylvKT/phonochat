package gay.sylv.phonochat.event.handler;

import io.github.foundationgames.phonos.item.PhonosItems;
import io.github.foundationgames.phonos.item.PortableRadioItem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

import static gay.sylv.phonochat.client.PhonochatClient.HEADPHONES_CHANNELS;
import static gay.sylv.phonochat.client.PhonochatClient.LISTENING_CHANNELS;
import static gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin.sendListeningTo;
import static gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin.sendNotListening;

/**
 * Handles voicechat behavior when using a portable radio.
 */
@Environment(EnvType.CLIENT)
public final class PortableRadioHandler {
	private PortableRadioHandler() {}
	
	public static void onAddStack(int slot, ItemStack stack) {
		int channel = ((PortableRadioItem) PhonosItems.PORTABLE_RADIO).getChannel(stack);
		if (!HEADPHONES_CHANNELS.containsKey(channel)) {
//			PhonochatClient.LOGGER.info("channel entry doesn't exist, creating one for channel " + channel);
			HEADPHONES_CHANNELS.put(channel, IntArrayList.of(slot));
			if (!LISTENING_CHANNELS.containsKey(channel)) {
//				PhonochatClient.LOGGER.info("sending listening to for channel " + channel);
				sendListeningTo(channel);
			}
		} else if (!HEADPHONES_CHANNELS.get(channel).contains(slot)) {
//			PhonochatClient.LOGGER.info("adding channel " + channel);
			HEADPHONES_CHANNELS.get(channel).add(slot);
		}
	}
	
	public static void onRemoveStack(int slot, ItemStack stack) {
		int channel = ((PortableRadioItem) PhonosItems.PORTABLE_RADIO).getChannel(stack);
		IntList channels = HEADPHONES_CHANNELS.get(channel);
		if (channels == null) return;
		channels.removeInt(channels.indexOf(slot));
		if (channels.size() == 0) {
//			PhonochatClient.LOGGER.info("removing channel " + channel);
			HEADPHONES_CHANNELS.remove(channel);
			if (!LISTENING_CHANNELS.containsKey(channel)) {
//				PhonochatClient.LOGGER.info("channels empty, sending not listening for channel " + channel);
				sendNotListening(channel);
			}
		}
	}
}
