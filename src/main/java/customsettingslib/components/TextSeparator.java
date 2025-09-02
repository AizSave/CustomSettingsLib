package customsettingslib.components;

import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

public class TextSeparator extends SettingsComponents {
    protected String key;

    public TextSeparator(String key) {
        this.key = key;
    }

    @Override
    public int addComponents(FormContentBox form, int y, int n) {
        int addedTop = n == 0 ? 0 : 10;
        form.addComponent(new FormLocalLabel("settingsui", key, new FontOptions(20), 0, form.getWidth() / 2, y + addedTop));
        return 26 + addedTop;
    }
}
