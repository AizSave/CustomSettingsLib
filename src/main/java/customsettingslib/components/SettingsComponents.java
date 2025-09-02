package customsettingslib.components;

import necesse.gfx.forms.components.FormContentBox;

public abstract class SettingsComponents {
    public static final int LEFT_MARGIN = 4;

    public static FormContentBox settingsForm;

    public static int getRightMargin() {
        return settingsForm.getScrollBarWidth() + 4;
    }

    public static int getWidth() {
        return settingsForm.getWidth() - LEFT_MARGIN - getRightMargin();
    }

    public abstract int addComponents(int y, int n);
}
