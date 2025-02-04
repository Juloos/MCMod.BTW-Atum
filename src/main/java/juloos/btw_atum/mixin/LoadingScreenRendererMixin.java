package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingScreenRenderer.class)
public abstract class LoadingScreenRendererMixin {
    @Shadow private Minecraft mc;

    @Inject(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 1, shift = At.Shift.AFTER))
    public void renderSeed(int percentage, CallbackInfo ci) {
        if (BTWAtumMod.running && BTWAtumMod.seed != null && !BTWAtumMod.seed.isEmpty() && this.mc.getIntegratedServer() != null && this.mc.theWorld == null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int x = scaledResolution.getScaledWidth();
            int y = scaledResolution.getScaledHeight();
            String string = BTWAtumMod.seed;
            this.mc.fontRenderer.drawStringWithShadow(string, (x - this.mc.fontRenderer.getStringWidth(string)) / 2, y / 2 - 4 - 40, 0xFFFFFF);
        }
    }
}
