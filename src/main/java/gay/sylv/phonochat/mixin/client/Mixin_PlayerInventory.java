package gay.sylv.phonochat.mixin.client;

import gay.sylv.phonochat.event.handler.PortableRadioHandler;
import io.github.foundationgames.phonos.item.PhonosItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class Mixin_PlayerInventory implements Inventory {
	@Shadow
	public abstract ItemStack getStack(int slot);
	
	@Shadow
	@Final
	public PlayerEntity player;
	
	private Mixin_PlayerInventory() {}
	
	@Inject(method = "setStack", at = @At("HEAD"))
	private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {
		if (this.player.getWorld().isClient) {
			if (this.getStack(slot).isOf(PhonosItems.PORTABLE_RADIO)) {
				PortableRadioHandler.onRemoveStack(slot, this.getStack(slot));
			}
			if (stack.isOf(PhonosItems.PORTABLE_RADIO)) {
				PortableRadioHandler.onAddStack(slot, stack);
			}
		}
	}
	
	@Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
	private void onAddStack(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (this.player.getWorld().isClient) {
			if (stack.isOf(PhonosItems.PORTABLE_RADIO)) {
				PortableRadioHandler.onAddStack(slot, stack);
			}
		}
	}
	
	@Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
	private void onRemoveStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = this.getStack(slot);
		if (this.player.getWorld().isClient) {
			if (amount >= stack.getCount() && stack.isOf(PhonosItems.PORTABLE_RADIO)) {
				PortableRadioHandler.onRemoveStack(slot, stack);
			}
		}
	}
	
	@Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
	private void onRemoveStack(int slot, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = this.getStack(slot);
		if (this.player.getWorld().isClient) {
			if (stack.isOf(PhonosItems.PORTABLE_RADIO)) {
				PortableRadioHandler.onRemoveStack(slot, stack);
			}
		}
	}
}
