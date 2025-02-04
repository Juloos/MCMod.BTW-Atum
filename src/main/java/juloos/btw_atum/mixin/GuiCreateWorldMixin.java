package juloos.btw_atum.mixin;

import btw.world.util.difficulty.Difficulties;
import juloos.btw_atum.BTWAtumMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.logging.Level;

@Mixin(GuiCreateWorld.class)
public abstract class GuiCreateWorldMixin extends GuiScreen {
    @Shadow private boolean createClicked;

    @Shadow private String gameMode;

    @Shadow private boolean lockDifficulty;

    @Shadow private GuiTextField textboxWorldName;

    @Shadow public String generatorOptionsToUse;

    @Shadow private boolean commandsAllowed;

    @Shadow private boolean isHardcore;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void createDesiredWorld(CallbackInfo ci) {
        if (BTWAtumMod.running) {
            BTWAtumMod.loading = true;
            isHardcore = BTWAtumMod.difficulty == 4;
            if (FabricLoader.getInstance().isModLoaded("nightmare_mode"))
                this.lockDifficulty = true;
            this.createLevel();
        }
    }

    @Unique
    private void createLevel() {
        if (this.createClicked)
            return;

        this.createClicked = true;
        long l = (new Random()).nextLong();
        String string = BTWAtumMod.seed;
        if (!StringUtils.isEmpty(string)) {
            try {
                long m = Long.parseLong(string);
                if (m != 0L)
                    l = m;
            } catch (NumberFormatException var7) {
                l = string.hashCode();
            }
        }
        if (BTWAtumMod.seed == null || BTWAtumMod.seed.isEmpty() || BTWAtumMod.seed.trim().equals("0"))
            BTWAtumMod.rsgAttempts++;
        else
            BTWAtumMod.ssgAttempts++;

        if (!(Difficulties.DIFFICULTY_LIST.get(BTWAtumMod.difficulty)).canSwitchDifficulty())
            this.lockDifficulty = true;
        WorldSettings settings = new WorldSettings(l, EnumGameType.getByName(this.gameMode), BTWAtumMod.structures, this.isHardcore, WorldType.worldTypes[BTWAtumMod.generatorType], Difficulties.DIFFICULTY_LIST.get(BTWAtumMod.difficulty), this.lockDifficulty);
        settings.func_82750_a(this.generatorOptionsToUse);
        if (BTWAtumMod.bonusChest && !this.isHardcore)
            settings.enableBonusChest();
        if (this.commandsAllowed && !this.isHardcore)
            settings.enableCommands();
        BTWAtumMod.saveProperties();
        BTWAtumMod.log(Level.INFO, (BTWAtumMod.seed == null || BTWAtumMod.seed.isEmpty() || BTWAtumMod.seed.trim().equals("0") ? "Resetting a random seed" : "Resetting the set seed" + " \"" + l + "\""));
        this.textboxWorldName.setText((BTWAtumMod.seed == null || BTWAtumMod.seed.isEmpty() || BTWAtumMod.seed.trim().equals("0")) ? "RSG Speedrun #" + BTWAtumMod.rsgAttempts : "SSG Speedrun #" + BTWAtumMod.ssgAttempts);
        this.mc.launchIntegratedServer(this.textboxWorldName.getText().trim(), this.textboxWorldName.getText().trim(), settings);
    }
}
