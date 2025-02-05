package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import juloos.btw_atum.screen.AutoResetOptionScreen;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class GuiMainMenuMixin extends GuiScreen {
    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    private void initGui(CallbackInfo ci) {
        if (BTWAtumMod.running && !BTWAtumMod.loading) {
            this.mc.displayGuiScreen(new GuiCreateWorld(this));
            ci.cancel();
        }
    }
    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("TAIL"))
    private void initGuiAddButton(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(69, this.width / 2 - 124, this.height / 4 + 48, 20, 20, ""));
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        TextureAtlasSprite texture = ((TextureMap) this.mc.getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite("gold_boots");
        drawTexturedModelRectFromIcon(this.width / 2 - 124 + 2, this.height / 4 + 48 + 2, texture, 16, 16);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void buttonClicked(GuiButton button, CallbackInfo ci) {
        if (BTWAtumMod.loading)
            // fixes being able to click title screen buttons during the blink
            ci.cancel();
        if (button.id == 69) {
            if (isShiftKeyDown()) {
                this.mc.displayGuiScreen(new AutoResetOptionScreen(null));
            } else {
                BTWAtumMod.running = true;
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            ci.cancel();
        }
    }
}
