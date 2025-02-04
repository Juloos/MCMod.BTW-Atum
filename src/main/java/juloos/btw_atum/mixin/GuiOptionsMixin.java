package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public abstract class GuiOptionsMixin extends GuiScreen {
    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("TAIL"))
    public void addAutoResetButton(CallbackInfo ci) {
        if (BTWAtumMod.running)
            this.buttonList.add(new GuiButton(1238, 0, this.height - 20, 100, 20, "Stop Resets & Quit"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void buttonClicked(GuiButton button, CallbackInfo ci) {
        if (button.id == 1238) {
            BTWAtumMod.running = false;
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
