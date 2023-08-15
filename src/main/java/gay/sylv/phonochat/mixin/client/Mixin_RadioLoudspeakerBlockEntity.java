package gay.sylv.phonochat.mixin.client;

import gay.sylv.phonochat.PhonochatMod;
import gay.sylv.phonochat.client.PhonochatClient;
import io.github.foundationgames.phonos.block.entity.RadioLoudspeakerBlockEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static gay.sylv.phonochat.client.PhonochatClient.HEADPHONES_CHANNELS;
import static gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin.sendListeningTo;
import static gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin.sendNotListening;

@Mixin(value = RadioLoudspeakerBlockEntity.class, remap = false)
public class Mixin_RadioLoudspeakerBlockEntity extends BlockEntity {
	@Shadow
	private int channel;
	
	@Unique
	private int oldChannel;
	
	private Mixin_RadioLoudspeakerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	// BEGIN SPAGHETTI
	
	@Override
	public void markRemoved() {
		super.markRemoved();
//		PhonochatClient.LOGGER.info("removing channel " + this.channel + " from pos " + this.pos);
		ObjectList<BlockPos> channelPosList = PhonochatClient.LISTENING_CHANNELS.get(this.channel);
		if (channelPosList != null) {
			channelPosList.remove(this.pos);
			if (channelPosList.size() == 0) {
//				PhonochatClient.LOGGER.info("channel entry list " + this.channel + " empty, removing");
				PhonochatClient.LISTENING_CHANNELS.remove(this.channel);
				if (!HEADPHONES_CHANNELS.containsKey(this.channel)) {
					sendNotListening(this.channel);
				}
			}
		}
	}
	
	@Inject(method = "method_49211", at = @At("TAIL"), remap = true)
	private void onTick(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (!world.isClient) return;
		
		if (this.channel != this.oldChannel) { // if we changed channels
			ObjectList<BlockPos> channelPosList = PhonochatClient.LISTENING_CHANNELS.get(this.oldChannel);
			if (channelPosList != null) { // check if the channel pos list exists
//				PhonochatClient.LOGGER.info("removing channel " + this.oldChannel + " at " + pos);
				channelPosList.remove(pos); // remove us from the old list
				
				if (channelPosList.size() == 0) {
//					PhonochatClient.LOGGER.info("channel entry list " + this.oldChannel + " empty, removing");
					PhonochatClient.LISTENING_CHANNELS.remove(this.oldChannel);
					if (!HEADPHONES_CHANNELS.containsKey(this.oldChannel)) {
						sendNotListening(this.oldChannel);
					}
				}
			}
		}
		this.oldChannel = this.channel;
		
		var client = MinecraftClient.getInstance();
		assert client.player != null;
		if (client.player.getPos().isInRange(pos.toCenterPos(), PhonochatMod.CHAT_RADIUS)) {
			if (!PhonochatClient.LISTENING_CHANNELS.containsKey(this.channel)) {
//				PhonochatClient.LOGGER.info("channel entry doesn't exist, creating one for channel " + this.channel);
				PhonochatClient.LISTENING_CHANNELS.put(this.channel, ObjectArrayList.of(pos));
				if (!HEADPHONES_CHANNELS.containsKey(this.channel)) {
					sendListeningTo(this.channel);
				}
			} else if (!PhonochatClient.LISTENING_CHANNELS.get(this.channel).contains(pos)) {
//				PhonochatClient.LOGGER.info("adding channel " + this.channel + " at " + pos);
				ObjectList<BlockPos> channelPosList = PhonochatClient.LISTENING_CHANNELS.get(this.channel);
				channelPosList.add(pos);
			}
		} else {
			ObjectList<BlockPos> channelPosList = PhonochatClient.LISTENING_CHANNELS.get(this.channel);
			if (channelPosList != null && channelPosList.contains(pos)) {
//				PhonochatClient.LOGGER.info("removing channel " + this.channel + " from pos " + pos);
				channelPosList.remove(pos);
				if (channelPosList.size() == 0) {
//					PhonochatClient.LOGGER.info("channel entry list " + this.channel + " empty, removing");
					PhonochatClient.LISTENING_CHANNELS.remove(this.channel);
					if (!HEADPHONES_CHANNELS.containsKey(this.channel)) {
						sendNotListening(this.channel);
					}
				}
			}
		}
	}
	
	// END SPAGHETTI
}
