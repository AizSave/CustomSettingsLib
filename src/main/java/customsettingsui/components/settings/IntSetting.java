package customsettingsui.components.settings;

import customsettingsui.components.CustomModSetting;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalSlider;
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
        saveData.addInt(getSaveKey(), value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getInt(getSaveKey(), defaultValue);
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
    public int addComponents(FormContentBox form, int y, int n) {
        newValue.set(value);

        if (displayMode == DisplayMode.BAR) {
            boolean onlyBar = min == 0 && max == 100;
            final AtomicReference<FormLabel> preview = new AtomicReference<>();

            FormSlider slider = form.addComponent(new FormLocalSlider("settingsui", id, 10, y, newValue.get(), min, max, form.getWidth() - (onlyBar ? 0 : 60) - 10 - 20), 15)
                    .onChanged((e) -> {
                        newValue.set(e.from.getValue());
                        if (!onlyBar && preview.get() != null) preview.get().setText(newValue.get().toString());
                    });

            if(!onlyBar) preview.set(form.addComponent(new FormLabel(newValue.get().toString(), new FontOptions(16), 0, form.getWidth() - 50, y + (slider.getTotalHeight() - 16) / 2, 52)));

            return slider.getTotalHeight();
        } else if (displayMode == DisplayMode.INPUT) {
            form.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(16), -1, 108, y + 2));
            FormTextInput input = form.addComponent(new FormTextInput(4, y, FormInputSize.SIZE_20, 100, Math.max(String.valueOf(max).length(), String.valueOf(min).length())));
            input.onChange(e -> {
                FormTextInput formTextInput = (FormTextInput) e.from;
                String text = formTextInput.getText();
                try {
                    int number = Integer.parseInt(text);
                    if(number < min) {
                        number = min;
                        formTextInput.setText(String.valueOf(number));
                    }
                    if(number > max) {
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

            return 20;
        } else if (displayMode == DisplayMode.INPUT_BAR) {
            final AtomicReference<FormTextInput> input = new AtomicReference<>();

            FormSlider slider = form.addComponent(new FormLocalSlider("settingsui", id, 10, y, newValue.get(), min, max, form.getWidth() - 80 - 10), 15)
                    .onChanged((e) -> {
                        newValue.set(e.from.getValue());
                        if (input.get() != null) input.get().setText(newValue.get().toString());
                    });

            input.set(
                    form.addComponent(new FormTextInput(form.getWidth() - 76, y + (slider.getTotalHeight() - 20) / 2, FormInputSize.SIZE_20, 52, Math.max(String.valueOf(max).length(), String.valueOf(min).length())))
            );

            input.get().setText(newValue.get().toString());

            input.get().onChange(e -> {
                FormTextInput formTextInput = (FormTextInput) e.from;
                String text = formTextInput.getText();
                try {
                    int number = Integer.parseInt(text);
                    if(number < min) {
                        number = min;
                        formTextInput.setText(String.valueOf(number));
                    }
                    if(number > max) {
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
