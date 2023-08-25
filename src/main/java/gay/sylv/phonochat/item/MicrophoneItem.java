package gay.sylv.phonochat.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.sylv.phonochat.duck.Duck_ServerPlayerEntity;
import gay.sylv.phonochat.network.c2s.C2SPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class MicrophoneItem extends TrinketItem {
	public MicrophoneItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		// sanity checks
		if (!(entity instanceof PlayerEntity player)) return;
		if (!entity.getWorld().isClient) { // if we're on the server
			if (player instanceof ServerPlayerEntity serverPlayer && !serverPlayer.interactionManager.getGameMode().isCreative()) { // if this player isn't in creative
				// we won't have any syncing issues, so go ahead and do things normally
				int channel = 0;
				//noinspection DataFlowIssue
				if (stack.hasNbt() && stack.getNbt().contains("channel")) {
					channel = stack.getNbt().getInt("channel");
				}
				((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(channel);
			}
		} else {
			ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
			if (interactionManager != null && interactionManager.getCurrentGameMode().isCreative()) {
				// if we're in creative, proceed to sync the microphone state
				ClientPlayNetworking.send(C2SPackets.EQUIP_MIC, PacketByteBufs.empty());
			}
		}
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		// sanity checks
		if (!(entity instanceof PlayerEntity player)) return;
		if (!entity.getWorld().isClient) { // if we're on the server
			if (player instanceof ServerPlayerEntity serverPlayer && !serverPlayer.interactionManager.getGameMode().isCreative()) { // if this player isn't in creative
				// we won't have any syncing issues, so go ahead and do things normally
				((Duck_ServerPlayerEntity) player).phonochat$setBroadcastingChannel(-1);
			}
		} else {
			ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
			if (interactionManager != null && interactionManager.getCurrentGameMode().isCreative()) {
				// if we're in creative, proceed to sync the microphone state
				ClientPlayNetworking.send(C2SPackets.UNEQUIP_MIC, PacketByteBufs.empty());
			}
		}
	}
}
