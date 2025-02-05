package juloos.btw_atum.mixin.hotkey;

import emi.shims.java.com.unascribed.retroemi.REMIScreen;
import juloos.btw_atum.BTWAtumMod;
import juloos.btw_atum.screen.AutoResetOptionScreen;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public WorldClient theWorld;

    @Shadow public abstract void loadWorld(WorldClient par1WorldClient);

    @Shadow public abstract void displayGuiScreen(GuiScreen par1GuiScreen);

    @Shadow public GuiScreen currentScreen;

    @Inject(method = "loadWorld(Lnet/minecraft/src/WorldClient;Ljava/lang/String;)V", at = @At("TAIL"))
    private void allowResettingAfterWorldJoin(CallbackInfo ci) {
        if (this.theWorld != null)
            BTWAtumMod.loading = false;
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 0))
    public void atum_tick(CallbackInfo ci) {
        boolean isPressed;
        try {
            isPressed = Keyboard.isKeyDown(BTWAtumMod.resetKey.keyCode);
        } catch (Exception e) {
            isPressed = Mouse.isButtonDown(100 + BTWAtumMod.resetKey.keyCode);
        }
        if (!isPressed || BTWAtumMod.loading ||
                this.currentScreen instanceof GuiControls ||
                this.currentScreen instanceof AutoResetOptionScreen ||
                this.currentScreen instanceof GuiCreateWorld ||
                this.currentScreen instanceof GuiFlatPresets ||
                this.currentScreen instanceof GuiRenameWorld ||
                this.currentScreen instanceof GuiScreenAddServer ||
                this.currentScreen instanceof GuiScreenConfigureWorld ||
                this.currentScreen instanceof GuiScreenServerList ||
                this.currentScreen instanceof REMIScreen ||

                this.currentScreen instanceof GuiChat ||
                this.currentScreen instanceof GuiCommandBlock ||
                this.currentScreen instanceof GuiEditSign ||
                this.currentScreen instanceof GuiScreenBook ||
                this.currentScreen instanceof GuiRepair ||
                this.currentScreen instanceof GuiInventory ||
                this.currentScreen instanceof GuiContainerCreative ||
                this.currentScreen instanceof GuiScreenHorseInventory
        ) return;

        BTWAtumMod.running = true;
        if (this.theWorld != null) {
            this.theWorld.sendQuittingDisconnectingPacket();
            this.loadWorld(null);
            Thread.yield();
        }
        this.displayGuiScreen(null);
    }
}
