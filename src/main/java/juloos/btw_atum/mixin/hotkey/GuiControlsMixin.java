package juloos.btw_atum.mixin.hotkey;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiControls.class)
public abstract class GuiControlsMixin extends GuiScreen {
    @Override
    public void onGuiClosed() {
        BTWAtumMod.saveProperties();
    }
}
