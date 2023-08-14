package gay.sylv.phonochat.event.handler;

import gay.sylv.phonochat.Items;
import gay.sylv.phonochat.network.c2s.C2SPackets;
import gay.sylv.phonochat.network.c2s.ShiftScrollC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Handles shift-scrolling when holding the microphone item.
 * (credit to Hexcasting for the idea)
 * @author sylv
 */
@Environment(EnvType.CLIENT)
public class ShiftScrollHandler {
	public static boolean onScroll(double delta) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		
		// basic test conditions (if we're a player, we're sneaking, and not in spectator)
		if (player != null && player.isSneaking() && !player.isSpectator()) {
			if (player.getMainHandStack().isOf(Items.MICROPHONE)) {
				int offset = delta < 0.0 ? 1 : -1; // The amount to change the phonos channel by.
				var buf = PacketByteBufs.create();
				var packet = new ShiftScrollC2SPacket((byte) offset);
				packet.write(buf);
				ClientPlayNetworking.send(C2SPackets.SHIFT_SCROLL, buf);
				return true;
			}
		}
		
		return false;
	}
}
