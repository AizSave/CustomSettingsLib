package customsettingslib.components.vanillaimproved;

import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.input.InputEvent;
import necesse.engine.input.controller.ControllerEvent;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.forms.components.FormSlider;
import necesse.gfx.forms.components.localComponents.FormLocalSlider;
import necesse.gfx.forms.events.FormEventListener;
import necesse.gfx.forms.events.FormInputEvent;
import necesse.gfx.gameFont.FontManager;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.GameTooltipManager;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.gfx.gameTooltips.TooltipLocation;

import java.awt.*;

public class SwitchableFormLocalSlider extends FormLocalSlider {
    protected boolean active = true;
    protected FontOptions fontOptions;
    protected int width;

    public SwitchableFormLocalSlider(String category, String key, int x, int y, int startValue, int minValue, int maxValue, int width, FontOptions fontOptions) {
        super(new LocalMessage(category, key), x, y, startValue, minValue, maxValue, width, fontOptions);
        this.fontOptions = fontOptions;
        this.width = width;
        this.allowScroll = false;
    }

    public SwitchableFormLocalSlider(String category, String key, int x, int y, int startValue, int minValue, int maxValue, int width) {
        this(category, key, x, y, startValue, minValue, maxValue, width, new FontOptions(16));
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        super.draw(tickManager, perspective, renderBox);

        Color color = Settings.UI.activeElementColor;
        GameTexture texture = Settings.UI.slider.active;

        if (!active) {
            color = Settings.UI.inactiveTextColor;
        } else if (this.isGrabbed()) {
            color = Settings.UI.highlightElementColor;
            texture = Settings.UI.slider.highlighted;
        }

        FontOptions fontOptions = this.fontOptions.defaultColor(Settings.UI.activeTextColor);

        String valueText = this.getValueText();
        int valueTextWidth = FontManager.bit.getWidthCeil(valueText, fontOptions);
        FontManager.bit.drawString((float) (this.getX() + this.width - valueTextWidth), (float) this.getY(), valueText, fontOptions);
        int maxTextWidth = this.width - valueTextWidth;
        String maxString = GameUtils.maxString(this.text, fontOptions, maxTextWidth - 10);
        if (!maxString.equals(this.text)) {
            maxString = maxString + "...";
            if (this.isControllerFocus(this)) {
                GameTooltipManager.addTooltip(new StringTooltips(this.text), TooltipLocation.FORM_FOCUS);
            }
        }

        FontManager.bit.drawString((float) this.getX(), (float) this.getY(), maxString, fontOptions);
        int textHeight = this.getTextHeight();
        drawWidthComponent(new GameSprite(texture, 0, 0, texture.getHeight()), new GameSprite(texture, 1, 0, texture.getHeight()), this.getX(), this.getY() + textHeight, this.width);
        texture.initDraw().section(texture.getHeight() * 2, texture.getWidth(), 0, texture.getHeight()).color(color).draw(this.getX() + this.getSliderPixelProgress(texture), this.getY() + this.getTextHeight());

    }

    @Override
    public boolean isGrabbed() {
        return active && super.isGrabbed();
    }

    @Override
    public boolean isMouseOverSlider(InputEvent event) {
        return active && super.isMouseOverSlider(event);
    }

    @Override
    public boolean isMouseOverBar(InputEvent event) {
        return active && super.isMouseOverBar(event);
    }

    @Override
    public boolean isMouseOverText(InputEvent event) {
        return active && super.isMouseOverText(event);
    }

    @Override
    public FormSlider onChanged(FormEventListener<FormInputEvent<FormSlider>> listener) {
        return super.onChanged(listener);
    }

    @Override
    public void handleInputEvent(InputEvent event, TickManager tickManager, PlayerMob perspective) {
        if (active) super.handleInputEvent(event, tickManager, perspective);
    }

    @Override
    public void handleControllerEvent(ControllerEvent event, TickManager tickManager, PlayerMob perspective) {
        if (active) super.handleControllerEvent(event, tickManager, perspective);
    }
}
