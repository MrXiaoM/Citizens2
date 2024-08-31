package net.citizensnpcs.nms.v1_20_R1.network;

import java.io.IOException;
import java.net.SocketAddress;

import net.citizensnpcs.nms.v1_20_R1.util.NMSImpl;
import net.citizensnpcs.util.EmptyChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

public class EmptyNetworkManager extends Connection {
    public EmptyNetworkManager(PacketFlow flag) throws IOException {
        super(flag);
        channel = new EmptyChannel(null);
        address = new SocketAddress() {
            private static final long serialVersionUID = 8207338859896320185L;
        };
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(Packet packet) {
    }

    @Override
    public void send(Packet packet, PacketSendListener genericfuturelistener) {
    }

    @Override
    public void setListener(PacketListener pl) {
        try {
            NMSImpl.CONNECTION_PACKET_LISTENER.invoke(this, pl);
            NMSImpl.CONNECTION_DISCONNECT_LISTENER.invoke(this, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}