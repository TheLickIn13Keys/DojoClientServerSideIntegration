package DojoClient.server.packets;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketListener;

import java.io.IOException;

public abstract class DojoPacket implements Packet {
    @Override
    public void a(PacketDataSerializer data) throws IOException {
        readPacketData(data);
    }

    @Override
    public void b(PacketDataSerializer data) throws IOException {
        writePacketData(data);
    }

    @Override
    public void a(PacketListener listener) {
        handle(listener);
    }

    public abstract void readPacketData(PacketDataSerializer data) throws IOException;
    public abstract void writePacketData(PacketDataSerializer data) throws IOException;
    public abstract void handle(PacketListener listener);

}
