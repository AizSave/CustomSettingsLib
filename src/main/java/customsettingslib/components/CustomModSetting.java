package customsettingslib.components;

import necesse.engine.GlobalData;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.state.MainMenu;

abstract public class CustomModSetting<T> extends SettingsComponents {
    public final String id;
    protected final T defaultValue;
    protected final LoadedMod mod;

    protected T value;

    public CustomModSetting(String id, T defaultValue, LoadedMod mod) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.mod = mod;

        this.value = defaultValue;
    }

    public CustomModSetting(String id, T defaultValue) {
        this(id, defaultValue, LoadedMod.getRunningMod());
    }

    public void restoreToDefault() {
        this.value = defaultValue;
    }

    protected Class<?> getValueClass() {
        return defaultValue.getClass();
    }

    public abstract void addSaveData(SaveData saveData);

    public abstract void applyLoadData(LoadData loadData);

    protected String getOldSaveKey() {
        return mod.id + "_" + id;
    }

    public Object getValue() {
        return value;
    }

    protected boolean isValidValue(Object value) {
        return value.getClass() == getValueClass();
    }

    public abstract void onSave();

    protected void changeValue(T value) {
        if (isValidValue(value)) {
            this.value = value;
        }
    }

    public boolean isEnabled() {
        return !customModSettings.serverSettings.contains(id) || GlobalData.getCurrentState() instanceof MainMenu;
    }
}
