package customsettingsui.settings;

import customsettingsui.components.CustomModSetting;
import customsettingsui.components.SettingsComponents;
import customsettingsui.components.TextSeparator;
import customsettingsui.components.settings.*;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModLoader;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CustomModSettings extends ModSettings {
    public static List<CustomModSettings> customModSettingsList = new ArrayList<>();

    public static Object getModSetting(String modID, String settingID) {
        for (CustomModSettings customModSettings : customModSettingsList) {
            if(Objects.equals(customModSettings.mod.id, modID)) {
                return customModSettings.settingsMap.get(settingID).getValue();
            }
        }
        return null;
    }

    protected final int position;

    public final LoadedMod mod;
    public final List<SettingsComponents> settingsDisplay = new ArrayList<>();
    public final List<CustomModSetting<?>> settingsList = new ArrayList<>();
    protected final Map<String, CustomModSetting<?>> settingsMap = new HashMap<>();

    public CustomModSettings() {
        this.mod = LoadedMod.getRunningMod();
        position = customModSettingsList.size();
        customModSettingsList.add(this);
    }

    @Override
    public void addSaveData(SaveData saveData) {
        for (CustomModSetting<?> setting : settingsList) {
            setting.addSaveData(saveData);
        }
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        for (CustomModSetting<?> setting : settingsList) {
            setting.applyLoadData(loadData);
        }
    }

    public CustomModSettingsGetter getGetter() {
        return new CustomModSettingsGetter(this);
    }

    public CustomModSettings addCustomComponents(SettingsComponents settingsComponents) {
        settingsDisplay.add(settingsComponents);
        return this;
    }

    public CustomModSettings addTextSeparator(String key) {
        addCustomComponents(new TextSeparator(key));
        return this;
    }

    public CustomModSettings addCustomSetting(CustomModSetting<?> customModSetting) {
        settingsDisplay.add(customModSetting);
        settingsList.add(customModSetting);
        settingsMap.put(customModSetting.id, customModSetting);
        return this;
    }

    public CustomModSettings addBooleanSetting(String id, boolean defaultValue) {
        addCustomSetting(new BooleanSetting(id, defaultValue));
        return this;
    }

    public CustomModSettings addStringSetting(String id, String defaultValue, int maxLength) {
        addCustomSetting(new StringSetting(id, defaultValue, maxLength));
        return this;
    }

    public CustomModSettings addIntSetting(String id, int defaultValue, int min, int max, IntSetting.DisplayMode displayMode) {
        addCustomSetting(new IntSetting(id, defaultValue, min, max, displayMode));
        return this;
    }

    public CustomModSettings addSelectionSetting(String id, int defaultValue, SelectionSetting.Option... options) {
        addCustomSetting(new SelectionSetting(id, defaultValue, options));
        return this;
    }

    public CustomModSettings addColorSetting(String id, Color defaultValue) {
        addCustomSetting(new ColorSetting(id, defaultValue));
        return this;
    }

}
