package customsettingslib;

import customsettingslib.components.settings.IntSetting;
import customsettingslib.components.settings.SelectionSetting;
import customsettingslib.settings.CustomModSettings;
import customsettingslib.settings.CustomModSettingsGetter;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;

import java.awt.*;

@ModEntry
public class SettingsModEntry {
    public static CustomModSettingsGetter settingsGetter;

    public ModSettings initSettings() {
        CustomModSettings customModSettings = new CustomModSettings()
                .addTextSeparator("section1")
                .addBooleanSetting("boolean1", false)
                .addBooleanSetting("boolean2", false)
                .addBooleanSetting("boolean3", false)
                .addBooleanSetting("boolean4", false)
                .addStringSetting("string", "", 0, false)

                .addTextSeparator("section2")
                .addSelectionSetting("selection", 0,
                        new SelectionSetting.Option("option1"),
                        new SelectionSetting.Option("option2"),
                        new SelectionSetting.Option("option3")
                )
                .addIntSetting("int_bar1", 0, 0, 100, IntSetting.DisplayMode.BAR)
                .addIntSetting("int_bar2", 0, 0, 200, IntSetting.DisplayMode.BAR)
                .addIntSetting("int_input", 0, -10000, 10000, IntSetting.DisplayMode.INPUT)
                .addIntSetting("int_input-bar", 0, -100, 100, IntSetting.DisplayMode.INPUT_BAR)

                .addTextSeparator("section3")
                .addBooleanSetting("boolean5", false)
                .addColorSetting("color", new Color(255, 255, 255));

        customModSettings.addServerSettings("boolean3", "boolean4", "int_bar1", "int_input");

        settingsGetter = customModSettings.getGetter();
        return customModSettings;
    }
}
