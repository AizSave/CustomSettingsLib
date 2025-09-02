package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.FormTextInput;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

import java.util.concurrent.atomic.AtomicReference;

public class StringSetting extends CustomModSetting<String> {
    protected int maxLength;

    public StringSetting(String id, String defaultValue, int maxLength) {
        super(id, defaultValue);
        this.maxLength = maxLength;
    }

    @Override
    protected boolean isValidValue(Object value) {
        return super.isValidValue(value) && (maxLength == 0 || ((String) value).length() <= maxLength);
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addSafeString(getSaveKey(), value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getSafeString(getSaveKey(), defaultValue);
    }

    private final AtomicReference<String> newValue = new AtomicReference<>();

    @Override
    public int addComponents(FormContentBox form, int y, int n) {
        newValue.set(value);

        form.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(16), -1, 208, y + 2));
        FormTextInput input = form.addComponent(new FormTextInput(4, y, FormInputSize.SIZE_20, 200, maxLength));
        input.onChange(e -> {
            FormTextInput formTextInput = (FormTextInput) e.from;
            String text = formTextInput.getText();
            newValue.set(text);
        });
        input.setText(newValue.get());

        return 20;
    }

    @Override
    public void onSave() {
        changeValue(newValue.get());
    }
}
