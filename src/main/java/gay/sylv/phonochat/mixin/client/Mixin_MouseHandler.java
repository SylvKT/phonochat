package gay.sylv.phonochat.mixin.client;

import gay.sylv.phonochat.event.handler.ShiftScrollHandler;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public class Mixin_MouseHandler {
	@Inject(method = "onMouseScroll", cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;client:Lnet/minecraft/client/MinecraftClient;", ordinal = 2, shift = At.Shift.BEFORE))
	private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci, double delta) {
		boolean cancel = ShiftScrollHandler.onScroll(delta);
		if (cancel) ci.cancel();
	}
}
