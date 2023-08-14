package gay.sylv.phonochat.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static gay.sylv.phonochat.plugin.PhonochatVoicechatPlugin.BROADCASTING_CHANNELS;

public class MicrophoneItem extends TrinketItem {
	public MicrophoneItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		// sanity checks
		if (!(entity instanceof PlayerEntity player)) return;
		if (entity.getWorld().isClient) return;
		
		int channel = stack.getOrCreateNbt().getInt("channel");
		((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(channel);
		BROADCASTING_CHANNELS.put(channel, BROADCASTING_CHANNELS.get(channel) + 1);
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		// sanity checks
		if (!(entity instanceof PlayerEntity player)) return;
		if (entity.getWorld().isClient) return;
		
		((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(-1);
		
		int channel = stack.getOrCreateNbt().getInt("channel");
		BROADCASTING_CHANNELS.put(channel, BROADCASTING_CHANNELS.get(channel) - 1);
		if (BROADCASTING_CHANNELS.get(channel) == 0) {
			BROADCASTING_CHANNELS.remove(channel);
		}
	}
}
