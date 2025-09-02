package customsettingslib.patches;

import customsettingslib.forms.CustomModSettingsForm;
import customsettingslib.forms.ModSettingsForm;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.components.FormTextButton;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.position.FormFixedPosition;
import necesse.gfx.forms.position.FormPosition;
import necesse.gfx.forms.presets.SettingsForm;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;

public class SettingsFormPatches {

    public static SettingsForm settingsForm;
    public static ModSettingsForm modSettingsForm;

    @ModConstructorPatch(target = SettingsForm.class, arguments = {Client.class,})
    public static class constructor {
        @Advice.OnMethodExit
        public static void onExit(@Advice.This SettingsForm This) {
            settingsForm = This;
            construct();
        }
    }

    public static void construct() {
        modSettingsForm = settingsForm.addComponent(new ModSettingsForm(400, 400));
        modSettingsForm.setPosMiddle(
                WindowManager.getWindow().getWidth() / 2,
                WindowManager.getWindow().getHeight() / 2
        );
    }

    @ModMethodPatch(target = SettingsForm.class, name = "setupMenuForm", arguments = {})
    public static class setupMenuForm {

        @Advice.OnMethodExit
        public static void onExit(@Advice.This SettingsForm This) {
            settingsForm = This;
            setup();
        }
    }

    public static void setup() {
        try {

            Field mainMenuField = SettingsForm.class.getDeclaredField("mainMenu");
            mainMenuField.setAccessible(true);
            Form mainMenu = (Form) mainMenuField.get(settingsForm);
            mainMenu.setHeight(mainMenu.getHeight() + 40);

            FormComponent component = null;
            for (FormComponent formComponent : mainMenu.getComponentList()) {
                component = formComponent;
            }

            assert component != null;

            FormTextButton backButton = (FormTextButton) component;
            FormPosition position = backButton.getPosition();
            backButton.setPosition(new FormFixedPosition(position.getX(), position.getY() + 40));

            FormLocalTextButton button = mainMenu.addComponent(new FormLocalTextButton("ui", "modsettings", backButton.getX(), position.getY(), mainMenu.getWidth() - 8));

            button.onClicked(event -> {
                modSettingsForm.start();
                settingsForm.makeCurrent(modSettingsForm);
            });

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @ModMethodPatch(target = SettingsForm.class, name = "onWindowResized", arguments = {GameWindow.class})
    public static class onWindowResized {

        @Advice.OnMethodExit
        public static void onExit(@Advice.This SettingsForm This) {
            windowResize();
        }
    }

    public static void windowResize() {
        if (modSettingsForm == null) return;

        modSettingsForm.setPosMiddle(
                WindowManager.getWindow().getWidth() / 2,
                WindowManager.getWindow().getHeight() / 2
        );

        for (CustomModSettingsForm customModSettingsForm : ModSettingsForm.customModSettingsForms) {
            customModSettingsForm.setPosMiddle(
                    WindowManager.getWindow().getWidth() / 2,
                    WindowManager.getWindow().getHeight() / 2
            );
        }
    }


}
