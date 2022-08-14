package com.example.examplemod;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketHandler {

    public static final PacketHandler INSTANCE = new PacketHandler();
    private static final String PROTOCOL_VERSION = "1";

    private PacketHandler() {
    }

    private final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(LanPacketTest.MODID, LanPacketTest.MODID))
          .clientAcceptedVersions(PROTOCOL_VERSION::equals)
          .serverAcceptedVersions(PROTOCOL_VERSION::equals)
          .networkProtocolVersion(() -> PROTOCOL_VERSION)
          .simpleChannel();
    private int index = 0;

    public void initialize() {
        registerMessage(CustomPacket.class, CustomPacket::encode, CustomPacket::decode, CustomPacket::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    private <MSG> void registerMessage(Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder,
          BiConsumer<MSG, Supplier<Context>> messageConsumer, NetworkDirection networkDirection) {
        channel.registerMessage(index++, type, encoder, decoder, messageConsumer, Optional.of(networkDirection));
    }

    public <MSG> void sendToAll(MSG message) {
        //Works
        /*for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), message);
        }*/
        //Doesn't work
        channel.send(PacketDistributor.ALL.noArg(), message);
    }
}