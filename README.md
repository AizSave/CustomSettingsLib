# Custom Settings Lib

This library provides an easy way to create custom mod settings in Necesse.

It was originally built by modifying and expanding the base game files. While parts of the original game code remain, all modifications and additional functionality were created by **Save**.

You're free to use this library for:
- Learning purposes
- Creating Necesse-related mods and content

**Author**: [Save's Steam profile](https://steamcommunity.com/id/Aiz_Save/)  
**License**: [CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/)

---

## Index

1. [Adding as a Dependency](#adding-as-a-dependency)
2. [Implementation](#implementation)
3. [Accessing Settings](#accessing-settings)
4. [Customizing Your Mod Settings](#customizing-your-mod-settings)
   - [Custom Components](#custom-components)
   - [Text Separators](#text-separators)
   - [Custom Settings](#custom-settings)
   - [Boolean Settings](#boolean-settings)
   - [String Settings](#string-settings)
   - [Integer Settings](#integer-settings)
   - [Selection Settings](#selection-settings)
   - [Color Settings](#color-settings)

---

# Adding as a Dependency

Since this library isn't published on Maven Central, you'll need to add it manually:

1. Download the latest `.jar` from [GitHub Packages](https://github.com/AizSave/CustomSettingsLib/packages/).
2. Place the `.jar` file in a folder inside your mod project (e.g., `libs/`).
3. Add the following to the `repositories` section in `build.gradle`:
   ```groovy
   flatDir {
       dirs 'libs' // Replace 'libs' with your chosen folder
   }
   ```
4. Add the dependency in the dependencies section:
   ```groovy
   compileOnly files('libs/CustomSettingsLib.jar') // Replace with actual jar path
   ```
5. Add the .jar as a library in your IDE.
6. In `build.gradle`, include the dependency:
   ```groovy
   project.ext.modDependencies = ["aizsave.customsettingsui"]
   ```
7. When publishing your mod, don't forget to also add the dependency to Steam.

[Return to Index](#index)

---

# Implementation

1. In your mod entry class, add `initSettings()` returning a new instance of `ModSettings`:
   
   Example:
   ```java
    @ModEntry
    public class SettingsModEntry {
        public ModSettings initSettings() {
            CustomModSettings customModSettings = new CustomModSettings();
            return customModSettings;
        }
   }
   ```

2. Add a static `CustomModSettingsGetter` variable to the entry object and assign it via `getGetter()`:
   
   Example:
   ```java
    @ModEntry
    public class SettingsModEntry {
        public static CustomModSettingsGetter settingsGetter;

        public ModSettings initSettings() {
            CustomModSettings customModSettings = new CustomModSettings();
            settingsGetter = customModSettings.getGetter();
            return customModSettings;
        }
    }
   ```

3. Customize the settings to suit your mod:

   Example:
   ```java
   @ModEntry
    public class SettingsModEntry { 
        public static CustomModSettingsGetter settingsGetter;

        public void init() {
            // ...
        }
    
        public void postInit() {
            // ...
        }
    
        public void initResources() {
            // ...
        }
        
        public ModSettings initSettings() {
            CustomModSettings customModSettings = new CustomModSettings()
                .addTextSeparator("section1")
                .addBooleanSetting("boolean1", false)
                .addBooleanSetting("boolean2", false)
                .addBooleanSetting("boolean3", false)
                .addBooleanSetting("boolean4", false)
                .addStringSetting("string", "", 0)
                
                .addTextSeparator("section2")
                .addSelectionSetting("selection", 0,
                        new SelectionSetting.Option("option1"),
                        new SelectionSetting.Option("option2"),
                        new SelectionSetting.Option("option3")
                )
                .addIntSetting("int_bar1", 0, 0, 100, IntSetting.DisplayMode.BAR)
                .addIntSetting("int_bar2", 0, 0, 200, IntSetting.DisplayMode.BAR)
                .addIntSetting("int_input", 0, -10000, 10000, IntSetting.DisplayMode.INPUT)
                .addIntSetting("int_input-bar", 0, -100, 100, IntSetting.DisplayMode.INPUT_BAR)
                
                .addTextSeparator("section3")
                .addBooleanSetting("boolean5", false)
                .addColorSetting("color", new Color(255, 255, 255));

            settingsGetter = customModSettings.getGetter();
            return customModSettings;
        }
    }
   ```

[Return to Index](#index)

---

# Accessing Settings

The `public static CustomModSettingsGetter settingsGetter` gives access to your defined settings

Incorrect usage will throw errors, so ensure you use the correct getter method for the setting type

```java
public void method() {
   // "settingID" must be a valid ID of a custom setting
   SettingsModEntry.settingsGetter.get(settingID);          // Returns Object
   SettingsModEntry.settingsGetter.getBoolean(settingID);   // Boolean only
   SettingsModEntry.settingsGetter.getString(settingID);    // String only
   SettingsModEntry.settingsGetter.getInt(settingID);       // Int only
   SettingsModEntry.settingsGetter.getFloat(settingID, 2);  // Float (from int) only
   SettingsModEntry.settingsGetter.getSelection(settingID); // Selected option (Object)
   SettingsModEntry.settingsGetter.getColor(settingID);     // Color only
}
```

To access **other mods' custom settings**, use:
```java
public void method() {
   SettingsModEntry.getModSetting(modID, settingID);
}
```

[Return to Index](#index)

---

# Customizing Your Mod Settings

This mod provides helper methods to add different kinds of settings and UI components to your mod.

Each method modifies the internal `CustomModSettings` object by registering a new component or setting.  
You can **chain these methods** to build your settings screen in a clear, declarative style.

**Important:** Do not use the `settingID` itself as the displayed name.  
Instead, provide translations in your `.lang` file(s) under the key `settingsui.settingID`  
Example:
```
[settingsui]
customhealthbarsection=Custom Health Bar
customhealthbar=Enabled
healthbartitle=Title
healthbarsize=Size
healthbardisplay=Tyle
healthbarcolor=Background Color
```

![Result Example](https://lh3.googleusercontent.com/fife/ALs6j_E8xF-juZKNBpmWXk0dqbZ4iS0EsCIH4oADvB-yE0qAv5baKEvmfCSg-IllgvHcQqjDJJ5itT8kSvm70CDPuUN6jpLAzUwSPEJLdG19Px90Yat-vBkv4OIa9Fs9vSYDf8im22mzTHb7VOl1ic_PhmVIDt-cgh1vrTsI323kVVDdomi2Wy-9NQpNXInGKttEi6i5_hgXwAPZ1tLegB4WyXTM2guXZLCUZwGblWA55IJZ_ForTFBwuyzIsLkzd1_94DAfNVwcLyS1f7tfAXh5FvooR_c6DGW4DJw9a7v3mN-HyLy5_atUpgM0zLVgCaVYBHUnkRFzFrJ4Wc_bS82Vvtp6dZSnIPNm6sVCYYq8TFDtf9om0GHwVGFbTafTx55L6TI86kf6twnGIQiCjs6TmJPy5HvPdUD4smdsEwAf5kFBz_8Xa3fcMd1RRwVP0pWdWTeBTQYQ9EWoKmEvyWTkwWbmdBk0_ZdXSsiWsgUw_yzefViQFf1ZKbudUQNuhhQN5ZsH3CelG6F2oMgx7UCDohoTcSbHDpQ8TfJaoB_s7Zv4Zb27-mhX0WkQoTeORQ5V0AvZ-PdqjoM3fl2TmhUxl4ptwf5Fyd-WxVOXLyXTOZ06p3gCxb_pVsHseZxN9FTb-tuUa56RUzC0dtuAF0Lb0bfpxJK9nAdsu8Kx73CYDlr2snAgvNFhK-4WhKc0te9bLFT1WXCX4UE7iu-O3bVKkfBjMEDYr8kvjhFWBh_DgTJCQlNp1B5wDBwEa-mbVjNskY5nmwlN_8R2ICKqqN0EXUSI7EiG_xyLuqTTwDfYkPU1aCsnR5hCUgnIWjcI9afSZq-F28ZWPOlAfMyPDJJ8AtX2HezUHA0uE5gOcRGdSXawsbv9_8ydvLOzGWEaLmHB_0yfJAdXjKwJcfT_9Gd2hep53wl-i360sLOXhXutrU3WSw_4p4udXEOj4egVdOKHWpUmFwtMDINSeWHOvNV_gNqiTQ=w1842-h959)

[Return to Index](#index)

---

### Custom Components

```java
public CustomModSettings addCustomComponents(SettingsComponents settingsComponents) {
    settingsDisplay.add(settingsComponents);
    return this;
}
```

Use this when you create your own custom components.

[Return to Index](#index)

---

### Text Separators

```java
public CustomModSettings addTextSeparator(String key) {
   addCustomComponents(new TextSeparator(key));
   return this;
}
```

Adds a visual **section divider** in the settings UI. Useful for grouping related settings into categories.

Example:
```
.addTextSeparator("customhealthbarsection")
```

[Return to Index](#index)

---

### Custom Settings

```java
public CustomModSettings addCustomSetting(CustomModSetting<?> customModSetting) {
   settingsDisplay.add(customModSetting);
   settingsList.add(customModSetting);
   settingsMap.put(customModSetting.id, customModSetting);
   return this;
}
```

Use this when you create your own custom settings components.

[Return to Index](#index)

---

### Boolean Settings

```java
public CustomModSettings addBooleanSetting(String id, boolean defaultValue) {
   addCustomSetting(new BooleanSetting(id, defaultValue));
   return this;
}
```

Creates a simple **toggle (checkbox)**

```
.addBooleanSetting("customhealthbar", false)
```

[Return to Index](#index)

---

### String Settings

```java
public CustomModSettings addStringSetting(String id, String defaultValue, int maxLength) {
   addCustomSetting(new StringSetting(id, defaultValue, maxLength));
   return this;
}
```

Creates a **text input field** with a maximum length

Example:
```
.addStringSetting("healthbartitle", "Health Bar", 20)
```

[Return to Index](#index)

---

### Integer Settings

```java
public CustomModSettings addIntSetting(String id, int defaultValue, int min, int max, IntSetting.DisplayMode displayMode) {
   addCustomSetting(new IntSetting(id, defaultValue, min, max, displayMode));
   return this;
}
```

Adds an **integer-based setting** with a configurable range and display style

Display modes:
- `INPUT` → text input field
- `BAR` → slide bar
- `INPUT_BAR` → text input field + slide bar

Example:
```
.addIntSetting("healthbarsize", 50, 0, 100, IntSetting.DisplayMode.BAR)
```

[Return to Index](#index)

---

### Selection Settings

```java
public CustomModSettings addSelectionSetting(String id, int defaultValue, SelectionSetting.Option... options) {
   addCustomSetting(new SelectionSetting(id, defaultValue, options));
   return this;
}
```

Adds a **dropdown** with predefined options

Example:
```
.addSelectionSetting("healthbardisplay", 0,
    new SelectionSetting.Option("Bar", CustomHealthBar.Display.BAR),
    new SelectionSetting.Option("Compact Bar", ModDifficulty.COMPACT),
    new SelectionSetting.Option("Circular", CustomHealthBar.Display.CIRCULAR)
)
```

Each `Option` has three values:
- `name` → The text displayed in the settings menu
- `object` → The actual underlying value of the option (the `name` string is used by default if not provided)
- `staticMessage` → True will use `settingsui` localization (`false` by default)

[Return to Index](#index)

---

### Color Settings

```java
public CustomModSettings addColorSetting(String id, Color defaultValue) {
   addCustomSetting(new ColorSetting(id, defaultValue));
   return this;
}
```

Adds a **color picker** to let the player choose an RGBA color

Example:
```
.addColorSetting("healthbarcolor", new Color(255, 51, 0, 153))
```

[Return to Index](#index)