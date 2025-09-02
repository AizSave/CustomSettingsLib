package customsettingslib.components;

import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

public class TextSeparator extends SettingsComponents {
    protected String key;

    public TextSeparator(String key) {
        this.key = key;
    }

    @Override
    public int addComponents(int y, int n) {
        int addedTop = n == 0 ? 0 : 10;
        FormLocalLabel label = settingsForm.addComponent(new FormLocalLabel("settingsui", key, new FontOptions(20), 0,  LEFT_MARGIN + getWidth() / 2, y + addedTop, getWidth()));
        return label.getHeight() + addedTop + 6;
    }
}
