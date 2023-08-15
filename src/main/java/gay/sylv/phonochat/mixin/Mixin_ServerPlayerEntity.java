package gay.sylv.phonochat.mixin;

import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class Mixin_ServerPlayerEntity implements Duck_ServerPlayerEntity {
	@Unique
	private int broadcastingChannel;
	
	private Mixin_ServerPlayerEntity() {}
	
	@Override
	public int phonochat$getBroadcastingChannel() {
		return broadcastingChannel;
	}
	
	@Override
	public void phonochat$setBroadcastingChannel(int broadcastingChannel) {
		this.broadcastingChannel = broadcastingChannel;
	}
}
