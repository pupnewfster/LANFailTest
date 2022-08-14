package com.example.examplemod;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

public class CustomPacket {

    private final int value;

    public CustomPacket(int value) {
        this.value = value;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(value);
    }

    public static CustomPacket decode(FriendlyByteBuf buffer) {
        return new CustomPacket(buffer.readVarInt());
    }

    public static void handle(CustomPacket message, Supplier<Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> LanPacketTest.LOGGER.info("Received packet with value: {}", message.value));
        context.setPacketHandled(true);
    }
}