package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeathMixin(CallbackInfo ci) {
        BTWAtumMod.running = false;
    }
}
