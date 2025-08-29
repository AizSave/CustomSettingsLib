package customsettingsui.settings;

import customsettingsui.components.settings.SelectionSetting;

import java.awt.*;

public class CustomModSettingsGetter {
    protected final CustomModSettings customModSettings;

    protected CustomModSettingsGetter(CustomModSettings customModSettings) {
        this.customModSettings = customModSettings;
    }

    public Object get(String settingID) {
        return customModSettings.settingsMap.get(settingID).getValue();
    }

    public boolean getBoolean(String settingID) {
        return (boolean) get(settingID);
    }

    public String getString(String settingID) {
        return (String) get(settingID);
    }

    public int getInt(String settingID) {
        return (int) get(settingID);
    }

    public float getFloat(String settingID, int decimals) {
        return (float) get(settingID) / decimals;
    }

    public Color getColor(String settingID) {
        return (Color) get(settingID);
    }

    public Object getSelection(String settingID) {
        SelectionSetting.Option[] options = ((SelectionSetting) customModSettings.settingsMap.get(settingID)).options;
        return options[getInt(settingID)].object;
    }
}