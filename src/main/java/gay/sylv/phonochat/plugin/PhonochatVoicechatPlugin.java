package gay.sylv.phonochat.plugin;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import gay.sylv.phonochat.PhonochatMod;
import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PhonochatVoicechatPlugin implements VoicechatPlugin {
	/**
	 * @return the unique ID for this voice chat plugin
	 */
	@Override
	public String getPluginId() {
		return PhonochatMod.MOD_ID;
	}
	
	/**
	 * Called when the voice chat initializes the plugin.
	 *
	 * @param api the voice chat API
	 */
	@Override
	public void initialize(VoicechatApi api) {
		PhonochatMod.LOGGER.info("Phonochat voice chat plugin initialized!");
	}
	
	/**
	 * Called once by the voice chat to register all events.
	 *
	 * @param registration the event registration
	 */
	@Override
	public void registerEvents(EventRegistration registration) {
		registration.registerEvent(MicrophonePacketEvent.class, this::handleMicrophone);
	}
	
	/**
	 * Handles players who have the microphone item.
	 * @see MicrophonePacketEvent
	 */
	private void handleMicrophone(MicrophonePacketEvent event) {
		// "sanity" checks (why do they still call it that)
		if (event.getSenderConnection() == null) return;
		if (!((event.getSenderConnection().getPlayer()) instanceof ServerPlayerEntity player)) return;
		if (((Duck_ServerPlayerEntity) player).phonochat$getBroadcastingChannel() == -1) return;
		
		int broadcastingChannel = ((Duck_ServerPlayerEntity) player).phonochat$getBroadcastingChannel();
		VoicechatServerApi api = event.getVoicechat();
	}
}
