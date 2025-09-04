package customsettingslib.packets;

import customsettingslib.settings.CustomModSettings;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

import java.util.HashMap;
import java.util.Map;

public class PacketReadServerSettings extends Packet {
    public Map<String, Map<String, Object>> newServerData = new HashMap<>();

    public PacketReadServerSettings(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        if (!reader.getNextBoolean()) {
            for (CustomModSettings customModSettings : CustomModSettings.customModSettingsList) {
                Map<String, Object> newModServerData = new HashMap<>();

                for (String serverSetting : customModSettings.serverSettings) {
                    newModServerData.put(serverSetting, customModSettings.settingsMap.get(serverSetting).applyPacket(reader));
                }

                newServerData.put(customModSettings.mod.id, newModServerData);
            }
        }
    }

    public PacketReadServerSettings(boolean fromClient) {
        PacketWriter writer = new PacketWriter(this);
        writer.putNextBoolean(fromClient);
        if (!fromClient) {
            for (CustomModSettings customModSettings : CustomModSettings.customModSettingsList) {
                for (String serverSetting : customModSettings.serverSettings) {
                    customModSettings.settingsMap.get(serverSetting).setupPacket(writer);
                }
            }
        }
    }

    @Override
    public void processClient(NetworkPacket packet, Client client) {
        for (CustomModSettings customModSettings : CustomModSettings.customModSettingsList) {
            customModSettings.serverDataSettings.putAll(newServerData.get(customModSettings.mod.id));
        }
    }

    @Override
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        if (client.checkHasRequestedSelf()) {
            for (CustomModSettings customModSettings : CustomModSettings.customModSettingsList) {
                for (String serverSetting : customModSettings.serverSettings) {
                    customModSettings.serverDataSettings.put(serverSetting, customModSettings.settingsMap.get(serverSetting).getValue());
                }
            }

            client.sendPacket(new PacketReadServerSettings(false));
        }
    }
}
