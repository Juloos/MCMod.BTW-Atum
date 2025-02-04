package juloos.btw_atum.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationManager.class)
public abstract class ServerConfigurationManagerMixin {
    @Inject(method = "initializeConnectionToPlayer", at = @At(value = "INVOKE", target = "Lbtw/AddonHandler;serverPlayerConnectionInitialized(Lnet/minecraft/src/NetServerHandler;Lnet/minecraft/src/EntityPlayerMP;)V"), cancellable = true)
    private void preventCrash(CallbackInfo ci) {
        if (MinecraftServer.getServer() == null)
            ci.cancel();
    }
}
