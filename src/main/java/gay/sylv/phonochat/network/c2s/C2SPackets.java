package gay.sylv.phonochat.network.c2s;

import gay.sylv.phonochat.Initializable;
import gay.sylv.phonochat.Items;
import gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin;
import io.github.foundationgames.phonos.radio.RadioStorage;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static gay.sylv.phonochat.PhonochatMod.MOD_ID;

/**
 * Registers receivers and handles server-side C2S-related packets.
 */
public final class C2SPackets implements Initializable {
	public static final C2SPackets INSTANCE = new C2SPackets();
	public static final Identifier SHIFT_SCROLL = new Identifier(MOD_ID, "shift_scroll");
	public static final Identifier LISTENING_TO = new Identifier(MOD_ID, "listening_to");
	public static final Identifier NOT_LISTENING = new Identifier(MOD_ID, "not_listening");
	
	private C2SPackets() {}
	
	@Override
	public void initialize() {
		ServerPlayNetworking.registerGlobalReceiver(SHIFT_SCROLL, (server, player, handler, buf, packetSender) -> {
			if (!player.getMainHandStack().isOf(Items.MICROPHONE)) return;
			int offset = buf.readByte();
			ItemStack handStack = player.getMainHandStack();
			NbtCompound nbt = handStack.getOrCreateNbt();
			int oldChannel = nbt.getInt("channel");
			int newChannel = oldChannel + offset;
			int channel = oldChannel; // the final channel we're going to use and display
			
			if (newChannel >= 0 && newChannel < RadioStorage.CHANNEL_COUNT) { // are we in the range of available channels
				channel = newChannel; // we're using the new channel
				nbt.putInt("channel", channel);
			}
			sendMicrophoneOverlay(player, channel); // send overlay regardless
		});
		ServerPlayNetworking.registerGlobalReceiver(LISTENING_TO, ((server, player, handler, buf, packetSender) -> {
			int channel = buf.readInt();
			if (!PhonochatVoicechatPlugin.LISTENING_PLAYERS.containsKey(player.getUuid())) {
				PhonochatVoicechatPlugin.LISTENING_PLAYERS.put(player.getUuid(), IntArrayList.of(channel));
			} else {
				PhonochatVoicechatPlugin.LISTENING_PLAYERS.get(player.getUuid()).add(channel);
			}
		}));
		ServerPlayNetworking.registerGlobalReceiver(NOT_LISTENING, ((server, player, handler, buf, packetSender) -> {
			int channel = buf.readInt();
			IntList channels = PhonochatVoicechatPlugin.LISTENING_PLAYERS.get(player.getUuid());
			
			if (channels != null) {
				channels.removeInt(channels.indexOf(channel));
				if (channels.size() == 0) {
					PhonochatVoicechatPlugin.LISTENING_PLAYERS.remove(player.getUuid());
				}
			}
		}));
	}
	
	private static void sendMicrophoneOverlay(ServerPlayerEntity player, int channel) {
		player.sendMessage(Text.translatable("gui.phonochat.microphone.overlay", channel), true);
	}
}
