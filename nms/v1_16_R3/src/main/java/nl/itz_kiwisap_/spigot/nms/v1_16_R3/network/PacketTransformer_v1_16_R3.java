package nl.itz_kiwisap_.spigot.nms.v1_16_R3.network;

import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.clientbound.KClientboundPacketSpawnEntity_v1_16_R3;

import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_16_R3 implements PacketTransformer {

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        if (packetObject instanceof PacketPlayOutSpawnEntity packetPlayOutSpawnEntity) {
            return List.of(this.transformSpawnEntity(packetPlayOutSpawnEntity));
        }

        return null;
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return null;
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object packetObject) {
        if (packetObject instanceof PacketPlayOutSpawnEntity packet) {
            return new KClientboundPacketSpawnEntity_v1_16_R3(packet);
        }

        return null;
    }
}