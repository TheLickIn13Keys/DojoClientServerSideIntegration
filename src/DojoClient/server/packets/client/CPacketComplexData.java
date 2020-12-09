package DojoClient.server.packets.client;

import net.minecraft.server.v1_8_R3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class CPacketComplexData extends CPacket {

    int rInt;
    double rDouble;
    float rFloat;
    byte[] rByteArray;
    boolean rBoolean;
    String rString;
    UUID rUUID;
    EntityPainting.EnumArt rEnum;
    BlockPosition rBlockPos;
    ItemStack rItemStack;
    NBTTagCompound rNBT;


    @Override
    public void readPacketData(PacketDataSerializer data) throws IOException {

        rInt = data.readInt();
        rDouble = data.readDouble();
        rFloat = data.readFloat();
        rByteArray = readByteArray(data);
        rBoolean = data.readBoolean();
        rString = readString(data);
        rUUID = readUUID(data);
        rEnum = readEnum(data, EntityPainting.EnumArt.class);
        rBlockPos = readBlockPosition(data);
        rItemStack = readItemStack(data);
        rNBT = readNBTTagCompound(data);

    }

    @Override
    public void handle(PacketListener listener) {

        System.out.println(rInt);
        System.out.println(rDouble);
        System.out.println(rFloat);
        System.out.println(Arrays.toString(rByteArray));
        System.out.println(rBoolean);
        System.out.println(rString);
        System.out.println(rUUID);
        System.out.println(rEnum);
        System.out.println(rBlockPos);
        System.out.println(rItemStack);
        System.out.println(rNBT);

    }
}
