package customsettingslib.components.settings;

import customsettingslib.components.CustomModSetting;
import customsettingslib.components.vanillaimproved.SwitchableFormLocalSlider;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameMath;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.Renderer;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormButton;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.floatMenu.ColorSelectorFloatMenu;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class ColorSetting extends CustomModSetting<Integer> {

    public ColorSetting(String id, Color defaultValue) {
        super(id, defaultValue.getRGB());
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

    private final AtomicReference<Color> color = new AtomicReference<>();

    @Override
    public int addComponents(int y, int n) {
        color.set(getColor());

        Color firstShownColor = new Color(getTrueValue(), true);

        boolean isEnabled = isEnabled();

        int width = getWidth();

        settingsForm.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(12), 0, (width - 80) / 2, y + 4, width));

        SwitchableFormLocalSlider red = settingsForm.addComponent(new SwitchableFormLocalSlider("ui", "colorred", LEFT_MARGIN, y + 20, firstShownColor.getRed(), 0, 255, width - 80, new FontOptions(12)));
        red.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(e.from.getValue(), tempColor.getGreen(), tempColor.getBlue(), tempColor.getAlpha()));
        });
        SwitchableFormLocalSlider green = settingsForm.addComponent(new SwitchableFormLocalSlider("ui", "colorgreen", LEFT_MARGIN, red.getY() + red.getTotalHeight() + 4, firstShownColor.getGreen(), 0, 255, width - 80, new FontOptions(12)));
        green.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), e.from.getValue(), tempColor.getBlue(), tempColor.getAlpha()));
        });
        SwitchableFormLocalSlider blue = settingsForm.addComponent(new SwitchableFormLocalSlider("ui", "colorblue", LEFT_MARGIN, green.getY() + green.getTotalHeight() + 4, firstShownColor.getBlue(), 0, 255, width - 80, new FontOptions(12)));
        blue.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), tempColor.getGreen(), e.from.getValue(), tempColor.getAlpha()));
        });
        SwitchableFormLocalSlider alpha = settingsForm.addComponent(new SwitchableFormLocalSlider("ui", "alpha", LEFT_MARGIN, blue.getY() + blue.getTotalHeight() + 4, firstShownColor.getAlpha(), 0, 255, width - 80, new FontOptions(12)));
        alpha.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), e.from.getValue()));
        });

        int nextY = alpha.getY() + alpha.getTotalHeight() + 4;
        FormButton selectColor = settingsForm.addComponent(new FormLocalTextButton("ui", "selectcolor", LEFT_MARGIN, nextY, width, FormInputSize.SIZE_20, ButtonColor.BASE))
                .onClicked((e) -> {
                    final Color startColor = color.get();
                    e.from.getManager().openFloatMenu(new ColorSelectorFloatMenu(e.from, startColor) {
                        public void onApplied(Color newColor) {
                            if (newColor != null) {
                                newColor = new Color(GameMath.limit(newColor.getRed(), 0, 255), GameMath.limit(newColor.getGreen(), 0, 255), GameMath.limit(newColor.getBlue(), 0, 255));
                                red.setValue(newColor.getRed());
                                green.setValue(newColor.getGreen());
                                blue.setValue(newColor.getBlue());
                                color.set(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), color.get().getAlpha()));
                            }
                        }

                        public void onSelected(Color newColor) {
                            color.set(new Color(GameMath.limit(newColor.getRed(), 0, 255), GameMath.limit(newColor.getGreen(), 0, 255), GameMath.limit(newColor.getBlue(), 0, 255), color.get().getAlpha()));
                        }
                    });
                });

        settingsForm.addComponent(new ColorPreview(width - 64, y + (nextY - y) / 2 - 32));

        red.setActive(isEnabled);
        green.setActive(isEnabled);
        blue.setActive(isEnabled);
        alpha.setActive(isEnabled);
        selectColor.setActive(isEnabled);

        return (nextY - y) + 20 + 4;
    }

    @Override
    public void onSave() {
        changeValue(color.get().getRGB());
    }

    protected class ColorPreview extends Form {
        public ColorPreview(int x, int y) {
            super(64, 64);
            this.setPosition(x, y);
        }

        @Override
        public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
            super.draw(tickManager, perspective, renderBox);
            Renderer.initQuadDraw(56, 56).color(isEnabled() ? color.get() : new Color(getTrueValue())).draw(getX() + 4, getY() + 4);
        }
    }

    public Color getColor() {
        return new Color(value, true);
    }

}
