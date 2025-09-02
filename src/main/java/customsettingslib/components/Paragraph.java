package customsettingslib.components;

import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

public class Paragraph extends SettingsComponents {
    protected String key;
    int fontSize;
    int align;
    int spaceTop;
    int spaceBottom;

    public Paragraph(String key, int fontSize, int align, int spaceTop, int spaceBottom) {
        this.key = key;
        this.fontSize = fontSize;
        this.align = align;
        this.spaceTop = spaceTop;
        this.spaceBottom = spaceBottom;
    }

    @Override
    public int addComponents(FormContentBox form, int y, int n) {
        int addedTop = n == 0 ? 0 : spaceTop;
        int startX;
        if (align == 0) {
            startX = form.getWidth() - 4;
        } else if (align == 1) {
            startX = form.getWidth() / 2;
        } else {
            startX = 4;
        }
        FormLocalLabel label = form.addComponent(new FormLocalLabel("settingsui", key, new FontOptions(fontSize), align, startX, y + addedTop, form.getWidth() - 8));
        return label.getHeight() + addedTop + spaceBottom;
    }
}
