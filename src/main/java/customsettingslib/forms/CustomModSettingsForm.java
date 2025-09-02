package customsettingslib.forms;

import customsettingslib.components.CustomModSetting;
import customsettingslib.patches.SettingsFormPatches;
import customsettingslib.settings.CustomModSettings;
import necesse.engine.Settings;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormFlow;
import necesse.gfx.forms.components.FormLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;
import necesse.gfx.gameFont.FontOptions;

import java.awt.*;

public class CustomModSettingsForm extends Form {

    public CustomModSettingsForm(CustomModSettings customModSettings, int width, int height) {
        super(customModSettings.mod.id + "customsettingsform", width, height);
    }

    protected void resetComponents(CustomModSettings customModSettings) {
        this.clearComponents();

        this.addComponent(new FormLabel(customModSettings.mod.getModNameString(), new FontOptions(16), -1, 4, 6, getWidth() - 8));

        FormContentBox modSettings = this.addComponent(new FormContentBox(0, 26, this.getWidth(), this.getHeight() - 80 - 28));

        FormFlow settingsFlow = new FormFlow(12);
        for (int i = 0; i < customModSettings.settingsDisplay.size(); i++) {
            settingsFlow.next(customModSettings.settingsDisplay.get(i).addComponents(modSettings, settingsFlow.next(4), i));
            settingsFlow.next(4);
        }

        modSettings.setContentBox(new Rectangle(0, 0, this.getWidth(), settingsFlow.next()));

        int trueHeight = Math.max(200, settingsFlow.next() + 80 + 28);
        if (trueHeight < this.getHeight() || (this.getHeight() < 600 && trueHeight > this.getHeight())) {
            this.setHeight(Math.min(trueHeight, 600));
            modSettings.setHeight(modSettings.getHeight() - (600 - this.getHeight()));
            this.setPosMiddle(
                    WindowManager.getWindow().getWidth() / 2,
                    WindowManager.getWindow().getHeight() / 2
            );
        }

        FormLocalTextButton restoreButton = this.addComponent(
                new FormLocalTextButton("settingsui", "restoredefaultbindall", 4, this.getHeight() - 80, this.getWidth() - 8)
        );
        restoreButton.onClicked(e -> {
            for (CustomModSetting<?> value : customModSettings.settingsList) {
                value.restoreToDefault();
            }
            Settings.saveClientSettings();
            resetComponents(customModSettings);
        });

        FormLocalTextButton saveButton = this.addComponent(
                new FormLocalTextButton("ui", "savebutton", 4, this.getHeight() - 40, this.getWidth() / 2 - 6)
        );
        saveButton.onClicked(e -> {
            for (CustomModSetting<?> value : customModSettings.settingsList) {
                value.onSave();
            }
            Settings.saveClientSettings();
        });

        FormLocalTextButton backButton = this.addComponent(
                new FormLocalTextButton("ui", "backbutton", this.getWidth() / 2 + 2, this.getHeight() - 40, this.getWidth() / 2 - 6)
        );
        backButton.onClicked(e -> SettingsFormPatches.settingsForm.makeCurrent(SettingsFormPatches.modSettingsForm));
    }

    public void makeCurrent(CustomModSettings customModSettings, SettingsForm settingsForm) {
        resetComponents(customModSettings);
        settingsForm.makeCurrent(this);
    }
}
