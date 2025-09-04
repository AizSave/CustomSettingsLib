package customsettingslib.components;

import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.gameFont.FontOptions;

public class Paragraph extends SettingsComponents {
    protected String key;
    protected FontOptions fontOptions;
    protected int align;
    protected int spaceTop;
    protected int spaceBottom;

    public Paragraph(String key, FontOptions fontOptions, int align, int spaceTop, int spaceBottom) {
        this.key = key;
        this.fontOptions = fontOptions;
        this.align = align;
        this.spaceTop = spaceTop;
        this.spaceBottom = spaceBottom;
    }

    @Override
    public int addComponents(int y, int n) {
        int width = getWidth();

        int addedTop = n == 0 ? 0 : spaceTop;
        int startX;
        if (align == 0) {
            startX = width - getRightMargin();
        } else if (align == 1) {
            startX = width / 2;
        } else {
            startX = LEFT_MARGIN;
        }
        FormLocalLabel label = settingsForm.addComponent(new FormLocalLabel("settingsui", key, fontOptions, align, startX, y + addedTop, width));
        return label.getHeight() + addedTop + spaceBottom;
    }
}
