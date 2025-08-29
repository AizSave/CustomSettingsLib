package customsettingsui.components.settings;

import customsettingsui.components.CustomModSetting;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameMath;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.Renderer;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormContentBox;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalSlider;
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
        saveData.addInt(getSaveKey(), value);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        value = loadData.getInt(getSaveKey(), defaultValue);
    }

    @Override
    public Object getValue() {
        return getColor();
    }

    private final AtomicReference<Color> color = new AtomicReference<>();

    @Override
    public int addComponents(FormContentBox form, int y, int n) {
        color.set(getColor());

        form.addComponent(new FormLocalLabel("settingsui", id, new FontOptions(12), 0, (form.getWidth() - 100) / 2, y + 4, form.getWidth() - 16));

        FormLocalSlider red = form.addComponent(new FormLocalSlider("ui", "colorred", 8, y + 20, color.get().getRed(), 0, 255, form.getWidth() - 100, new FontOptions(12)));
        red.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(e.from.getValue(), tempColor.getGreen(), tempColor.getBlue(), tempColor.getAlpha()));
        });
        FormLocalSlider green = form.addComponent(new FormLocalSlider("ui", "colorgreen", 8, red.getY() + red.getTotalHeight() + 4, color.get().getGreen(), 0, 255, form.getWidth() - 100, new FontOptions(12)));
        green.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), e.from.getValue(), tempColor.getBlue(), tempColor.getAlpha()));
        });
        FormLocalSlider blue = form.addComponent(new FormLocalSlider("ui", "colorblue", 8, green.getY() + green.getTotalHeight() + 4, color.get().getBlue(), 0, 255, form.getWidth() - 100, new FontOptions(12)));
        blue.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), tempColor.getGreen(), e.from.getValue(), tempColor.getAlpha()));
        });
        FormLocalSlider alpha = form.addComponent(new FormLocalSlider("ui", "alpha", 8, blue.getY() + blue.getTotalHeight() + 4, color.get().getAlpha(), 0, 255, form.getWidth() - 100, new FontOptions(12)));
        alpha.onChanged((e) -> {
            Color tempColor = color.get();
            color.set(new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), e.from.getValue()));
        });

        int nextY = alpha.getY() + alpha.getTotalHeight() + 4;
        form.addComponent(new FormLocalTextButton("ui", "selectcolor", 8, nextY, form.getWidth() - 32, FormInputSize.SIZE_20, ButtonColor.BASE))
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

        form.addComponent(new ColorPreview(form.getWidth() - 82, y + (nextY - y) / 2 - 32));

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
            Renderer.initQuadDraw(56, 56).color(color.get()).draw(getX() + 4, getY() + 4);
        }
    }

    public Color getColor() {
        return new Color(value, true);
    }

}
