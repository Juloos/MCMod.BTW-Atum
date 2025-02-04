package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin extends Gui {
    @Shadow @Final private Minecraft mc;

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    private void getRightText(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        if (BTWAtumMod.running && this.mc.gameSettings.showDebugInfo)
            this.drawString(this.mc.fontRenderer, "Resetting" + (BTWAtumMod.seed == null || BTWAtumMod.seed.isEmpty() || BTWAtumMod.seed.trim().equals("0") ? " a random seed" : (" the seed: \"" + BTWAtumMod.seed + "\"")), 2, 114, 14737632);
    }
}
