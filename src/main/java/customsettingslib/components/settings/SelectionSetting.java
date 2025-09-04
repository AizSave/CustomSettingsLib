package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.FormDropdownSelectionButton;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.ui.ButtonColor;

import java.util.concurrent.atomic.AtomicReference;

public class SelectionSetting extends CustomModSetting<Integer> {
    public Option[] options;

    public SelectionSetting(String id, int defaultValue, Option... options) {
        super(id, defaultValue);
        this.options = options;
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
    public void setupPacket(PacketWriter writer) {
        writer.putNextInt(value);
    }

    @Override
    public Integer applyPacket(PacketReader reader) {
        return reader.getNextInt();
    }

    @Override
    protected boolean isValidValue(Object value) {
        return super.isValidValue(value) && inBounds((Integer) value);
    }

    protected boolean inBounds(int value) {
        return 0 <= value && value <= options.length;
    }

    private final AtomicReference<Integer> newValue = new AtomicReference<>();

    @Override
    public int addComponents(int y, int n) {
        newValue.set(value);

        int width = getWidth();

        FormDropdownSelectionButton<Integer> selectionForm = settingsForm.addComponent(new FormDropdownSelectionButton<>(LEFT_MARGIN, y, FormInputSize.SIZE_20, ButtonColor.BASE, width));
        for (int i = 0; i < options.length; i++) {
            selectionForm.options.add(i, options[i].getDisplayName());
        }
        selectionForm.setSelected(getTrueValue(), options[getTrueValue()].getDisplayName());
        selectionForm.onSelected((e) -> newValue.set(e.value));

        selectionForm.setActive(isEnabled());

        return 20;
    }

    @Override
    public void onSave() {
        changeValue(newValue.get());
    }

    public static class Option {
        public String name;
        public Object value;
        public boolean staticMessage;

        public Option(String name, Object value, boolean staticMessage) {
            this.name = name;
            this.value = value;
            this.staticMessage = staticMessage;
        }

        public Option(String name, Object value) {
            this(name, value, false);
        }

        public Option(String name) {
            this(name, name, false);
        }

        public GameMessage getDisplayName() {
            return staticMessage ? new StaticMessage(name) : new LocalMessage("settingsui", name);
        }
    }
}
