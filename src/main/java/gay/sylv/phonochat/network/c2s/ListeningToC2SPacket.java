package gay.sylv.phonochat.network.c2s;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public class ListeningToC2SPacket implements Packet<ServerPlayPacketListener> {
	private final int channel;
	
	public ListeningToC2SPacket(int channel) {
		this.channel = channel;
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(channel);
	}
	
	@Override
	public void apply(ServerPlayPacketListener listener) {
	}
}
