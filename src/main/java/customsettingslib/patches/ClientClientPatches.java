package customsettingslib.patches;

import customsettingslib.packets.PacketReadServerSettings;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import necesse.entity.mobs.PlayerMob;
import net.bytebuddy.asm.Advice;

public class ClientClientPatches {

    @ModMethodPatch(target = ClientClient.class, name = "applySpawned", arguments = {int.class})
    public static class applySpawned {
        @Advice.OnMethodExit
        public static void onExit(@Advice.FieldValue(value = "client") Client client) {
            if (client.getPlayer() != null) {
                PlayerMob player = client.getPlayer();

                if (player != null) {
                    client.network.sendPacket(new PacketReadServerSettings(true));
                }
            }
        }

    }

}