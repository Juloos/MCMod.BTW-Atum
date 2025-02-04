package juloos.btw_atum.mixin;

import juloos.btw_atum.BTWAtumMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin(File par1File) {
        super(par1File);
    }

    @Redirect(method = "loadAllWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/IntegratedServer;convertMapIfNeeded(Ljava/lang/String;)V"))
    private void preventCrashIfReset(IntegratedServer instance, String s) {
        if (!BTWAtumMod.running)
            this.convertMapIfNeeded(s);
    }
}
