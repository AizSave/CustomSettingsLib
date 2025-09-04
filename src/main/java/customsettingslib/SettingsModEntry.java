package customsettingslib;

import customsettingslib.packets.PacketReadServerSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.PacketRegistry;

@ModEntry
public class SettingsModEntry {

    public void init() {
        PacketRegistry.registerPacket(PacketReadServerSettings.class);
    }

}
