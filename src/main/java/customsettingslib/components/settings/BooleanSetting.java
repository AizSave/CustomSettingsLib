package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.gfx.forms.components.FormCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;

import java.util.concurrent.atomic.AtomicReference;

public class BooleanSetting extends CustomModSetting<Boolean> {
    public BooleanSetting(String id, Boolean defaultValue) {
        super(id, defaultValue);
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addBoolean(id, value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getBoolean(id, loadData.getBoolean(getOldSaveKey(), defaultValue));
    }

    @Override
    public void setupPacket(PacketWriter writer) {
        writer.putNextBoolean(value);
    }

    @Override
    public Boolean applyPacket(PacketReader reader) {
        return reader.getNextBoolean();
    }

    private final AtomicReference<Boolean> newValue = new AtomicReference<>();

    @Override
    public int addComponents(int y, int n) {
        newValue.set(value);

        int width = getWidth();

        FormCheckBox component = settingsForm.addComponent(new FormLocalCheckBox("settingsui", id, LEFT_MARGIN, y, width), 8)
                .onClicked((e) -> {
                    newValue.set(!newValue.get());
                });
        component.checked = getTrueValue();
        component.setActive(isEnabled());

        return component.getHitboxes().get(0).height;
    }

    @Override
    public void onSave() {
        changeValue(newValue.get());
    }
}
