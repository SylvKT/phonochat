package gay.sylv.phonochat.network.c2s;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import gay.sylv.phonochat.Initializable;
import gay.sylv.phonochat.Items;
import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
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
import net.minecraft.util.Pair;

import java.util.Optional;

import static gay.sylv.phonochat.PhonochatMod.MOD_ID;

/**
 * Registers receivers and handles server-side C2S-related packets.
 */
public final class C2SPackets implements Initializable {
	public static final C2SPackets INSTANCE = new C2SPackets();
	public static final Identifier SHIFT_SCROLL = new Identifier(MOD_ID, "shift_scroll");
	public static final Identifier LISTENING_TO = new Identifier(MOD_ID, "listening_to");
	public static final Identifier NOT_LISTENING = new Identifier(MOD_ID, "not_listening");
	public static final Identifier UNEQUIP_MIC = new Identifier(MOD_ID, "unequip_mic");
	public static final Identifier EQUIP_MIC = new Identifier(MOD_ID, "equip_mic");
	
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
		ServerPlayNetworking.registerGlobalReceiver(NOT_LISTENING, (server, player, handler, buf, packetSender) -> {
			int channel = buf.readInt();
			IntList channels = PhonochatVoicechatPlugin.LISTENING_PLAYERS.get(player.getUuid());
			
			if (channels != null) {
				channels.removeInt(channels.indexOf(channel));
				if (channels.size() == 0) {
					PhonochatVoicechatPlugin.LISTENING_PLAYERS.remove(player.getUuid());
				}
			}
		});
		ServerPlayNetworking.registerGlobalReceiver(UNEQUIP_MIC, (server, player, handler, buf, responseSender) -> ((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(-1));
		ServerPlayNetworking.registerGlobalReceiver(EQUIP_MIC, (server, player, handler, buf, responseSender) -> {
			Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);
			if (trinketComponent.isEmpty()) return;
			ItemStack stack = null;
			for (Pair<SlotReference, ItemStack> slotId : trinketComponent.get().getEquipped(Items.MICROPHONE)) {
				stack = slotId.getRight();
			}
			int channel = 0;
			if (stack == null) return;
			//noinspection DataFlowIssue
			if (stack.hasNbt() && stack.getNbt().contains("channel")) {
				channel = stack.getNbt().getInt("channel");
			}
			((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(channel);
		});
	}
	
	private static void sendMicrophoneOverlay(ServerPlayerEntity player, int channel) {
		player.sendMessage(Text.translatable("gui.phonochat.microphone.overlay", channel), true);
	}
}
