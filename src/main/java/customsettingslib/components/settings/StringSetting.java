package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.FormTextInput;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

import java.util.concurrent.atomic.AtomicReference;

public class StringSetting extends CustomModSetting<String> {
    protected int maxLength;
    boolean large;

    public StringSetting(String id, String defaultValue, int maxLength, boolean large) {
        super(id, defaultValue);
        this.maxLength = maxLength;
        this.large = large;
    }

    @Override
    protected boolean isValidValue(Object value) {
        return super.isValidValue(value) && (maxLength == 0 || ((String) value).length() <= maxLength);
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addSafeString(id, value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getSafeString(id, loadData.getSafeString(getOldSaveKey(), defaultValue));
    }

    private final AtomicReference<String> newValue = new AtomicReference<>();

    @Override
    public int addComponents(int y, int n) {
        newValue.set(value);

        int inputWidth = large ? 192 : 128;

        settingsForm.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(16), -1, LEFT_MARGIN + inputWidth + 16, y + 2));
        FormTextInput input = settingsForm.addComponent(new FormTextInput(LEFT_MARGIN, y, FormInputSize.SIZE_20, inputWidth, maxLength));
        input.onChange(e -> {
            FormTextInput formTextInput = (FormTextInput) e.from;
            String text = formTextInput.getText();
            newValue.set(text);
        });
        input.setText(newValue.get());

        input.setActive(isEnabled());

        return 20;
    }

    @Override
    public void onSave() {
        changeValue(newValue.get());
    }
}
