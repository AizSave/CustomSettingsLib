package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import customsettingslib.components.vanillaimproved.SwitchableFormLocalSlider;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

import java.util.concurrent.atomic.AtomicReference;

public class IntSetting extends CustomModSetting<Integer> {
    protected int min;
    protected int max;
    protected DisplayMode displayMode;

    public IntSetting(String id, int defaultValue, int min, int max, DisplayMode displayMode) {
        super(id, defaultValue);
        this.min = min;
        this.max = max;
        this.displayMode = displayMode;
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addInt(id, value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getInt(id, loadData.getInt(getOldSaveKey(), defaultValue));
    }

    @Override
    protected boolean isValidValue(Object value) {
        return super.isValidValue(value) && inBounds((Integer) value);
    }

    protected boolean inBounds(int value) {
        return min <= value && value <= max;
    }

    private final AtomicReference<Integer> newValue = new AtomicReference<>();

    @Override
    public int addComponents(int y, int n) {
        newValue.set(value);

        int width = getWidth();

        boolean isEnabled = isEnabled();

        if (displayMode == DisplayMode.BAR) {
            boolean onlyBar = min == 0 && max == 100;
            final AtomicReference<FormLabel> preview = new AtomicReference<>();

            SwitchableFormLocalSlider slider = (SwitchableFormLocalSlider) settingsForm.addComponent(new SwitchableFormLocalSlider("settingsui", id, LEFT_MARGIN, y, newValue.get(), min, max, width - (onlyBar ? 0 : 80)), 15)
                    .onChanged((e) -> {
                        newValue.set(e.from.getValue());
                        if (!onlyBar && preview.get() != null) preview.get().setText(newValue.get().toString());
                    });

            if (!onlyBar)
                preview.set(settingsForm.addComponent(new FormLabel(newValue.get().toString(), new FontOptions(16), 0, width - 32, y + (slider.getTotalHeight() - 16) / 2, 64)));

            slider.setActive(isEnabled);

            return slider.getTotalHeight();
        } else if (displayMode == DisplayMode.INPUT) {
            settingsForm.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(16), -1, LEFT_MARGIN + 128 + 16, y + 2));
            FormTextInput input = settingsForm.addComponent(new FormTextInput(LEFT_MARGIN, y, FormInputSize.SIZE_20, 128, Math.max(String.valueOf(max).length(), String.valueOf(min).length())));
            input.onChange(e -> {
                FormTextInput formTextInput = (FormTextInput) e.from;
                String text = formTextInput.getText();
                try {
                    int number = Integer.parseInt(text);
                    if (number < min) {
                        number = min;
                        formTextInput.setText(String.valueOf(number));
                    }
                    if (number > max) {
                        number = max;
                        formTextInput.setText(String.valueOf(number));
                    }
                    newValue.set(number);
                } catch (RuntimeException ignored) {
                    formTextInput.setText(newValue.get().toString());
                    formTextInput.setCaretEnd();
                }
            });

            input.setText(newValue.get().toString());

            input.setActive(isEnabled);

            return 20;
        } else if (displayMode == DisplayMode.INPUT_BAR) {
            final AtomicReference<FormTextInput> input = new AtomicReference<>();

            SwitchableFormLocalSlider slider = (SwitchableFormLocalSlider) settingsForm.addComponent(new SwitchableFormLocalSlider("settingsui", id, LEFT_MARGIN, y, newValue.get(), min, max, width - 80), 15)
                    .onChanged((e) -> {
                        newValue.set(e.from.getValue());
                        if (input.get() != null) input.get().setText(newValue.get().toString());
                    });

            input.set(
                    settingsForm.addComponent(new FormTextInput(width - 64, y + (slider.getTotalHeight() - 20) / 2, FormInputSize.SIZE_20, 64, Math.max(String.valueOf(max).length(), String.valueOf(min).length())))
            );

            input.get().setText(newValue.get().toString());

            input.get().onChange(e -> {
                FormTextInput formTextInput = (FormTextInput) e.from;
                String text = formTextInput.getText();
                try {
                    int number = Integer.parseInt(text);
                    if (number < min) {
                        number = min;
                        formTextInput.setText(String.valueOf(number));
                    }
                    if (number > max) {
                        number = max;
                        formTextInput.setText(String.valueOf(number));
                    }
                    newValue.set(number);
                    slider.setValue(number);

                } catch (RuntimeException ignored) {
                    formTextInput.setText(newValue.get().toString());
                    formTextInput.setCaretEnd();
                }
            });

            slider.setActive(isEnabled);
            input.get().setActive(isEnabled);

            return slider.getTotalHeight();
        }
        return 0;
    }

    @Override
    public void onSave() {
        changeValue(newValue.get());
    }

    public enum DisplayMode {
        INPUT,
        BAR,
        INPUT_BAR
    }
}
