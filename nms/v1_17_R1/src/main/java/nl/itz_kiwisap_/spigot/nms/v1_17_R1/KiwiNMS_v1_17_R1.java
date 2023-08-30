package nl.itz_kiwisap_.spigot.nms.v1_17_R1;

import io.netty.channel.Channel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import nl.itz_kiwisap_.spigot.common.utils.JavaReflections;
import nl.itz_kiwisap_.spigot.nms.KiwiNMS;
import nl.itz_kiwisap_.spigot.nms.network.KPacket;
import nl.itz_kiwisap_.spigot.nms.network.KiwiPacketWrapper;
import nl.itz_kiwisap_.spigot.nms.network.clientbound.KClientboundPacketEntityMetadata;
import nl.itz_kiwisap_.spigot.nms.scoreboard.KScoreboardTeam;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.network.PacketTransformer_v1_17_R1;
import nl.itz_kiwisap_.spigot.nms.v1_17_R1.scoreboard.KScoreboardTeam_v1_17_R1;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class KiwiNMS_v1_17_R1 implements KiwiNMS {

    private static final JavaReflections.FieldAccessor<List<SynchedEntityData.DataItem<?>>> METADATA_PACKET_METADATA_FIELD;

    static {
        METADATA_PACKET_METADATA_FIELD = JavaReflections.getField(ClientboundSetEntityDataPacket.class, List.class, ObfuscatedNames_v1_17_R1.METADATA_PACKET_METADATA_FIELD);
    }

    private static final int ENTITY_FLAGS_INDEX = 0;

    private final PacketTransformer_v1_17_R1 packetTransformer;
    private final Scoreboard scoreboard;

    public KiwiNMS_v1_17_R1() {
        this.packetTransformer = new PacketTransformer_v1_17_R1();
        this.scoreboard = new Scoreboard();
    }

    @Override
    public void runSync(Runnable runnable) {
        MinecraftServer.getServer().executeBlocking(runnable);
    }

    @Override
    public void sendPacket(Player player, KiwiPacketWrapper packetWrapper) {
        Object packetObject = packetWrapper.packet();
        if (!(packetObject instanceof Packet<?> packet)) {
            throw new IllegalArgumentException("Packet inside packet wrapper is not an instance of a minecraft packet!");
        }

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);
    }

    @Override
    public void sendPackets(Player player, List<KiwiPacketWrapper> packetWrappers) {
        for (KiwiPacketWrapper packetWrapper : packetWrappers) {
            this.sendPacket(player, packetWrapper);
        }
    }

    @Override
    public Channel getPacketChannel(Player player) {
        return ((CraftPlayer) player).getHandle().connection.connection.channel;
    }

    @Override
    public org.bukkit.entity.Entity getEntityById(World world, int entityId) {
        Entity nmsEntity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public KScoreboardTeam createScoreboardTeam(String name) {
        PlayerTeam team = this.scoreboard.addPlayerTeam(name);
        return new KScoreboardTeam_v1_17_R1(team);
    }

    @Override
    public Collection<KPacket> transformClientboundPacket(Object packetObject) {
        return this.packetTransformer.transformClientboundPacket(null, packetObject);
    }

    @Override
    public Collection<KPacket> transformServerboundPacket(Object packetObject) {
        return this.packetTransformer.transformServerboundPacket(packetObject);
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
    public KiwiPacketWrapper createPacketEntityMetadata(int entityId, List<KClientboundPacketEntityMetadata.Entry<?>> entries) {
        List<SynchedEntityData.DataItem<?>> items = new ArrayList<>();
        for (KClientboundPacketEntityMetadata.Entry<?> entry : entries) {
            SynchedEntityData.DataItem<?> dataValue = this.createDataValue(entry);
            if (dataValue != null) {
                items.add(dataValue);
            }
        }

        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entityId, new SynchedEntityData(null), false);
        METADATA_PACKET_METADATA_FIELD.set(packet, items);
        return new KiwiPacketWrapper(packet);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamInitialize(KScoreboardTeam team) {
        ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket((PlayerTeam) team.getNMSInstance(), true);
        return new KiwiPacketWrapper(packet);
    }

    @Override
    public KiwiPacketWrapper createPacketScoreboardTeamEntityAdd(KScoreboardTeam team, String entityName) {
        ClientboundSetPlayerTeamPacket packet = ClientboundSetPlayerTeamPacket.createPlayerPacket((PlayerTeam) team.getNMSInstance(), entityName, ClientboundSetPlayerTeamPacket.Action.ADD);
        return new KiwiPacketWrapper(packet);
    }

    @SuppressWarnings("unchecked")
    private <T> SynchedEntityData.DataItem<T> createDataValue(KClientboundPacketEntityMetadata.Entry<T> entry) {
        EntityDataSerializer<T> serializer = (EntityDataSerializer<T>) EntityDataSerializers.getSerializer(entry.serializerId());
        if (serializer == null) return null;

        EntityDataAccessor<T> accessor = serializer.createAccessor(entry.index());
        return new SynchedEntityData.DataItem<>(accessor, entry.value());
    }
}