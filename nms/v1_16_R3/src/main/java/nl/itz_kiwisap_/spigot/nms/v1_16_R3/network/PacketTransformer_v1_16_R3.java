package nl.itz_kiwisap_.spigot.nms.v1_16_R3.network;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.PacketTransformer;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketSpawnEntity;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.clientbound.KClientboundPacketEntityMetadata_v1_16_R3;
import nl.itz_kiwisap_.spigot.nms.v1_16_R3.network.clientbound.KClientboundPacketSpawnEntity_v1_16_R3;

import java.util.Collection;
import java.util.List;

public final class PacketTransformer_v1_16_R3 implements PacketTransformer {

    @Override
    public Collection<KPacket> transformClientboundPacket(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof PacketPlayOutSpawnEntity packetPlayOutSpawnEntity) {
            return List.of(this.transformSpawnEntity(bundlePacketObject, packetPlayOutSpawnEntity));
        }

        if (packetObject instanceof PacketPlayOutEntityMetadata packetPlayOutEntityMetadata) {
            return List.of(this.transformEntityMetadata(bundlePacketObject, packetPlayOutEntityMetadata));
        }

        return null;
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return null;
    }

    @Override
    public boolean isBundlePacket(Object packetObject) {
        return false;
    }

    @Override
    public boolean isBundleEmpty(Object bundlePacket) {
        return false;
    }

    @Override
    public void addPacketToBundle(Object bundlePacket, Object packetObject) {
    }

    @Override
    public void removePacketFromBundle(Object bundlePacket, Object packetObject) {
    }

    @Override
    public KClientboundPacketSpawnEntity transformSpawnEntity(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof PacketPlayOutSpawnEntity packet) {
            return new KClientboundPacketSpawnEntity_v1_16_R3(packet);
        }

        return null;
    }

    @Override
    public KClientboundPacketEntityMetadata transformEntityMetadata(Object bundlePacketObject, Object packetObject) {
        if (packetObject instanceof PacketPlayOutEntityMetadata packet) {
            return new KClientboundPacketEntityMetadata_v1_16_R3(packet);
        }

        return null;
    }
}