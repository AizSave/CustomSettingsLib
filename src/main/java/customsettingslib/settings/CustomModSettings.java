package customsettingslib.settings;

import customsettingslib.components.*;
import customsettingslib.components.settings.*;
import necesse.engine.GlobalData;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.state.MainGame;
import necesse.gfx.gameFont.FontOptions;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CustomModSettings extends ModSettings {
    public static List<CustomModSettings> customModSettingsList = new ArrayList<>();

    public static Object getModSetting(String modID, String settingID) {
        for (CustomModSettings customModSettings : customModSettingsList) {
            if (Objects.equals(customModSettings.mod.id, modID)) {
                return customModSettings.getSetting(settingID);
            }
        }
        return null;
    }

    public static void addOnSavedListener(String modID, Runnable onSaved) {
        for (CustomModSettings customModSettings : customModSettingsList) {
            if (Objects.equals(customModSettings.mod.id, modID)) {
                customModSettings.onSavedListeners.add(onSaved);
                break;
            }
        }
    }

    protected final int position;

    public final LoadedMod mod;

    public final List<Runnable> onSavedListeners = new ArrayList<>();

    public final List<String> serverSettings = new ArrayList<>();

    public final List<SettingsComponents> settingsDisplay = new ArrayList<>();
    public final List<CustomModSetting<?>> settingsList = new ArrayList<>();
    public final Map<String, CustomModSetting<?>> settingsMap = new HashMap<>();
    public final Map<String, Object> serverDataSettings = new HashMap<>();

    public CustomModSettings(Runnable onSaved) {
        this.mod = LoadedMod.getRunningMod();

        if (onSaved != null) this.onSavedListeners.add(onSaved);

        position = customModSettingsList.size();
        customModSettingsList.add(this);
    }

    public CustomModSettings() {
        this(null);
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

    public void addServerSettings(String... serverSettingsIDs) {
        Collections.addAll(serverSettings, serverSettingsIDs);
    }

    public Object getSetting(String settingID) {
        return serverSettings.contains(settingID) && GlobalData.getCurrentState() instanceof MainGame ?
                serverDataSettings.get(settingID) :
                settingsMap.get(settingID).getValue();
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

    public CustomModSettings addParagraph(String key, FontOptions fontOptions, int align, int spaceTop, int spaceBottom) {
        addCustomComponents(new Paragraph(key, fontOptions, align, spaceTop, spaceBottom));
        return this;
    }

    public CustomModSettings addParagraph(String key, FontOptions fontOptions, int align) {
        addCustomComponents(new Paragraph(key, fontOptions, align, 0, 4));
        return this;
    }

    public CustomModSettings addParagraph(String key, int spaceTop, int spaceBottom) {
        addCustomComponents(new Paragraph(key, new FontOptions(12), -1, spaceTop, spaceBottom));
        return this;
    }

    public CustomModSettings addParagraph(String key) {
        addCustomComponents(new Paragraph(key, new FontOptions(12), -1, 4, 6));
        return this;
    }

    public CustomModSettings addSpace(int height) {
        addCustomComponents(new Space(height));
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

    public CustomModSettings addStringSetting(String id, String defaultValue, int maxLength, boolean large) {
        addCustomSetting(new StringSetting(id, defaultValue, maxLength, large));
        return this;
    }

    public CustomModSettings addIntSetting(String id, int defaultValue, int min, int max, IntSetting.DisplayMode displayMode, int shownDecimals) {
        addCustomSetting(new IntSetting(id, defaultValue, min, max, displayMode, shownDecimals));
        return this;
    }

    public CustomModSettings addIntSetting(String id, int defaultValue, int min, int max, IntSetting.DisplayMode displayMode) {
        addIntSetting(id, defaultValue, min, max, displayMode, 0);
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
