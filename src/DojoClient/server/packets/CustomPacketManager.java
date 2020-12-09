package DojoClient.server.packets;

import DojoClient.server.Main;
import DojoClient.server.packets.client.CPacketComplexData;
import DojoClient.server.packets.server.SPacket;
import DojoClient.server.packets.server.SPacketTest;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Sender;
import com.comphenix.protocol.PacketType.Protocol;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldUtils;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.google.common.collect.BiMap;
import net.minecraft.server.v1_8_R3.EnumProtocol;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CustomPacketManager {

    private static final HashMap<Class <? extends DojoPacket>, PacketType> packetToType = new HashMap<Class <? extends DojoPacket>, PacketType>();

    public static void registerPackets(){
        registerPacket(SPacketTest.class, PacketIds.SPacketTest, Sender.SERVER);
        registerPacket(CPacketComplexData.class, PacketIds.CPacketComplexData, Sender.CLIENT);
    }

    private static void registerPacket(Class<? extends DojoPacket> packetClass, int packetId, Sender sender){

        final PacketType packetType = new PacketType(Protocol.PLAY, sender, packetId, packetId);

        packetToType.put(packetClass, packetType);

        final EnumProtocol protocol = EnumProtocol.PLAY;

        final EnumProtocolDirection direction = packetType.isClient() ? EnumProtocolDirection.SERVERBOUND : EnumProtocolDirection.CLIENTBOUND;

        try {

            Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> theMap = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> ) FieldUtils.readField(protocol, "j", true);

            BiMap<Integer, Class<? extends Packet<?>>> biMap = theMap.get(direction);
            biMap.put(packetId, (Class<? extends Packet<?>>) packetClass);
            theMap.put(direction, biMap);

        }catch (IllegalAccessException e){
            e.printStackTrace();
        }

        Map<Class<?>, EnumProtocol> map = (Map<Class<?>, EnumProtocol>) Accessors.getFieldAccessor(EnumProtocol.class, Map.class ,true).get(protocol);
        map.put(packetClass, protocol);

    }

    public static void sendCustomPacket(Player player, SPacket packet){

        if(!(Main.getInstance().getPeopleUsingClient().contains(player.getUniqueId()))){
            Main.getInstance().getLogger().warning("Trying to send packet to " + player.getName() + " -> Not Running Client | Packet: " + packet.getClass().getSimpleName());
            return;
        }

        PacketContainer container = new PacketContainer(packetToType.get(packet.getClass()), packet);

        try {

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);

        }catch (InvocationTargetException e){
            e.printStackTrace();
        }

    }

    public static PacketType getCustomPacketType(Class<? extends DojoPacket> clazz){
        return packetToType.get(clazz);
    }

}
