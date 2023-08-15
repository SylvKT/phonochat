package gay.sylv.phonochat.plugin;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.PlayerDisconnectedEvent;
import gay.sylv.phonochat.PhonochatMod;
import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
import gay.sylv.phonochat.network.c2s.C2SPackets;
import gay.sylv.phonochat.network.c2s.ListeningToC2SPacket;
import gay.sylv.phonochat.network.c2s.NotListeningC2SPacket;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PhonochatVoicechatPlugin implements VoicechatPlugin {
	/**
	 * Map of every listening player to their listening channels.
	 */
	public static final Object2ObjectMap<UUID, IntList> LISTENING_PLAYERS = new Object2ObjectOpenHashMap<>();
	/**
	 * Map of each channel that is broadcasting to the amount of broadcasting channels.
	 */
	public static final Int2IntMap BROADCASTING_CHANNELS = new Int2IntArrayMap();
	
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
		registration.registerEvent(PlayerDisconnectedEvent.class, this::removeOldPlayer);
	}
	
	/**
	 * Handles players who have the microphone item.
	 * @see MicrophonePacketEvent
	 */
	private void handleMicrophone(MicrophonePacketEvent event) {
		// "sanity" checks (why do they still call it that)
		if (event.getSenderConnection() == null) return;
		if (!((event.getSenderConnection().getPlayer().getPlayer()) instanceof ServerPlayerEntity player)) return;
		if (((Duck_ServerPlayerEntity) player).phonochat$getBroadcastingChannel() == -1) return;
		
		int broadcastingChannel = ((Duck_ServerPlayerEntity) player).phonochat$getBroadcastingChannel();
		VoicechatServerApi api = event.getVoicechat();
		for (UUID uuid : LISTENING_PLAYERS.keySet()) {
			IntList channels = LISTENING_PLAYERS.get(uuid);
			if (channels.contains(broadcastingChannel)) {
				if (PhonochatMod.HEAR_SELF || !uuid.equals(event.getSenderConnection().getPlayer().getUuid())) {
					api.sendStaticSoundPacketTo(api.getConnectionOf(uuid), event.getPacket().staticSoundPacketBuilder().build());
				}
			}
		}
	}
	
	private void removeOldPlayer(PlayerDisconnectedEvent event) {
		LISTENING_PLAYERS.remove(event.getPlayerUuid());
	}
	
	public static void sendListeningTo(int channel) {
		var buf = PacketByteBufs.create();
		var packet = new ListeningToC2SPacket(channel);
		packet.write(buf);
		ClientPlayNetworking.send(C2SPackets.LISTENING_TO, buf);
	}
	
	public static void sendNotListening(int channel) {
		var buf = PacketByteBufs.create();
		var packet = new NotListeningC2SPacket(channel);
		packet.write(buf);
		ClientPlayNetworking.send(C2SPackets.NOT_LISTENING, buf);
	}
}
