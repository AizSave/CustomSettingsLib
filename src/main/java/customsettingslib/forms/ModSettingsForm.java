package customsettingslib.forms;

import customsettingslib.patches.SettingsFormPatches;
import customsettingslib.settings.CustomModSettings;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormTextButton;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModSettingsForm extends Form {
    protected boolean started = false;
    public static List<CustomModSettingsForm> customModSettingsForms = new ArrayList<>();

    public ModSettingsForm(int width, int height) {
        super("modSettings", width, height);
    }

    public void start() {
        if (!started) {
            FormContentBox modSettingsButtons = this.addComponent(new FormContentBox(0, 0, this.getWidth(), this.getHeight() - 40));

            int contentHeight = 8 + CustomModSettings.customModSettingsList.size() * 40;
            int buttonWidth = modSettingsButtons.getWidth() - 8 - (contentHeight > modSettingsButtons.getHeight() ? 12 : 0);

            for (int i = 0; i < CustomModSettings.customModSettingsList.size(); i++) {
                CustomModSettings customModSetting = CustomModSettings.customModSettingsList.get(i);
                CustomModSettingsForm customModSettingsForm = SettingsFormPatches.settingsForm.addComponent(new CustomModSettingsForm(customModSetting, 400, 600));
                customModSettingsForm.setPosMiddle(
                        WindowManager.getWindow().getWidth() / 2,
                        WindowManager.getWindow().getHeight() / 2
                );
                customModSettingsForms.add(customModSettingsForm);
                modSettingsButtons.addComponent(
                        new FormTextButton(customModSetting.mod.name, 4, 4 + i * 40, buttonWidth)
                                .onClicked(event -> customModSettingsForm.makeCurrent(customModSetting, SettingsFormPatches.settingsForm))
                );
            }

            modSettingsButtons.setContentBox(new Rectangle(0, 0, this.getWidth(), contentHeight));

            FormLocalTextButton backButton = this.addComponent(
                    new FormLocalTextButton("ui", "backbutton", 4, this.getHeight() - 40, getWidth() - 8
                    )
            );
            backButton.onClicked(e -> SettingsFormPatches.settingsForm.subMenuBackPressed());
        }
    }
}
