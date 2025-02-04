package juloos.btw_atum.mixin.hotkey;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin {
    @Shadow public KeyBinding[] keyBindings;

    @Inject(method = "loadOptions", at = @At("TAIL"))
    private void addResetKey(CallbackInfo ci) {
        List<KeyBinding> newKeys = new ArrayList<>(Arrays.asList(this.keyBindings));
        newKeys.add(15, BTWAtumMod.resetKey);
        this.keyBindings = newKeys.toArray(new KeyBinding[0]);
    }
}
