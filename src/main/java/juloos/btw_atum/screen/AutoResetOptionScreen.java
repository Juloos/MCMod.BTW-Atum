package juloos.btw_atum.screen;

import btw.world.util.difficulty.Difficulties;
import btw.world.util.difficulty.Difficulty;
import juloos.btw_atum.BTWAtumMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;
import org.jetbrains.annotations.Nullable;

public class AutoResetOptionScreen extends GuiScreen {
    private final GuiScreen parent;
    private final String title;
    private GuiTextField seedField;
    private String seed;
    private Difficulty difficulty;
    private int generatorType;
    private boolean structures;
    private boolean bonusChest;

    public AutoResetOptionScreen(@Nullable GuiScreen parent) {
        super();
        title = "Autoreset Options";
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.difficulty = Difficulties.DIFFICULTY_LIST.get(BTWAtumMod.difficulty);
        this.seedField = new GuiTextField(this.mc.fontRenderer, this.width / 2 - 100, this.height - 160, 200, 20);
        this.seedField.setText(BTWAtumMod.seed == null ? "" : BTWAtumMod.seed);
        this.seedField.setFocused(true);
        this.seed = BTWAtumMod.seed;
        this.generatorType = BTWAtumMod.generatorType;
        this.structures = BTWAtumMod.structures;
        this.bonusChest = BTWAtumMod.bonusChest;
        if (FabricLoader.getInstance().isModLoaded("nightmare_mode"))
            this.buttonList.add(new GuiButton(340, this.width / 2 + 5, this.height - 100, 150, 20, I18n.getString("selectWorld.difficulty") + ": " + (difficulty.ID == 2 ? "Nightmare" : (difficulty.ID == 0 ? "Bad Dream" : difficulty.NAME))));
        else
            this.buttonList.add(new GuiButton(340, this.width / 2 + 5, this.height - 100, 150, 20, I18n.getString("selectWorld.difficulty") + (difficulty.NAME.contains("restricted") ? ": Locked " : ": ") + I18n.getString(difficulty.getLocalizedName())));
        this.buttonList.add(new GuiButton(341, this.width / 2 - 155, this.height - 100, 150, 20, I18n.getString("selectWorld.mapType") + " " + I18n.getString(WorldType.worldTypes[generatorType].getTranslateName())));
        this.buttonList.add(new GuiButton(342, this.width / 2 - 155, this.height - 64, 150, 20, I18n.getString("selectWorld.mapFeatures") + " " + structures));
        this.buttonList.add(new GuiButton(344, this.width / 2 + 5, this.height - 64, 150, 20, I18n.getString("selectWorld.bonusItems") + " " + bonusChest));
        this.buttonList.add(new GuiButton(345, this.width / 2 - 155, this.height - 28, 150, 20, I18n.getString("gui.done")));
        this.buttonList.add(new GuiButton(343, this.width / 2 + 5, this.height - 28, 150, 20, I18n.getString("gui.cancel")));
    }

    @Override
    public void updateScreen() {
        seedField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.mc.fontRenderer, this.title, this.width / 2, this.height - 210, -1);
        this.drawString(this.mc.fontRenderer, "Seed (Leave empty for a random Seed)", this.width / 2 - 100, this.height - 180, -6250336);
        this.seedField.drawTextBox();
        super.drawScreen(mouseX, mouseY, delta);
    }

    @Override
    public void keyTyped(char character, int code) {
        if (this.seedField.isFocused()) {
            this.seedField.textboxKeyTyped(character, code);
            this.seed = this.seedField.getText();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 340:
                if (FabricLoader.getInstance().isModLoaded("nightmare_mode")) {
                    difficulty = Difficulties.DIFFICULTY_LIST.get(((difficulty.ID / 2 + 1) % 2) * 2);
                    button.displayString = I18n.getString("selectWorld.difficulty") + ": " + (difficulty.ID == 2 ? "Nightmare" : (difficulty.ID == 0 ? "Bad Dream" : difficulty.NAME));
                } else {
                    difficulty = Difficulties.DIFFICULTY_LIST.get((difficulty.ID + 1) % Difficulties.DIFFICULTY_LIST.size());
                    button.displayString = I18n.getString("selectWorld.difficulty") + (difficulty.NAME.contains("restricted") ? ": Locked " : ": ") + I18n.getString(difficulty.getLocalizedName());
                }
                break;
            case 341:
                ++generatorType;
                if (generatorType >= WorldType.worldTypes.length) {
                    generatorType = 0;
                }
                while (WorldType.worldTypes[this.generatorType] == null || !WorldType.worldTypes[this.generatorType].getCanBeCreated()) {
                    ++this.generatorType;
                    if (this.generatorType < WorldType.worldTypes.length) continue;
                    this.generatorType = 0;
                }
                button.displayString = I18n.getString("selectWorld.mapType") + " " + I18n.getString(WorldType.worldTypes[generatorType].getTranslateName());
                break;
            case 342:
                structures = !structures;
                button.displayString = I18n.getString("selectWorld.mapFeatures") + " " + structures;
                break;
            case 344:
                bonusChest = !bonusChest;
                button.displayString = I18n.getString("selectWorld.bonusItems") + " " + bonusChest;
                break;
            case 345:
                BTWAtumMod.seed = seed;
                BTWAtumMod.difficulty = difficulty.ID;
                BTWAtumMod.structures = structures;
                BTWAtumMod.bonusChest = bonusChest;
                BTWAtumMod.generatorType = generatorType;
                BTWAtumMod.saveProperties();
                this.mc.displayGuiScreen(parent);
                break;
            case 343:
                this.mc.displayGuiScreen(parent);
                break;
        }
    }
}
