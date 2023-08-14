package gay.sylv.phonochat.network.c2s;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public class ShiftScrollC2SPacket implements Packet<ServerPlayPacketListener> {
	private final byte offset;
	
	public ShiftScrollC2SPacket(byte offset) {
		this.offset = offset;
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		buf.writeByte(offset);
	}
	
	@Override
	public void apply(ServerPlayPacketListener listener) {
	}
}
