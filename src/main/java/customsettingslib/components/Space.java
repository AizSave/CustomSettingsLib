package customsettingslib.components;

public class Space extends SettingsComponents {
    protected int height;

    public Space(int height) {
        this.height = height;
    }

    @Override
    public int addComponents(int y, int n) {
        return height;
    }
}
