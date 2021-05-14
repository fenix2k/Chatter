package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketWriter implements Runnable {
    private static final Logger log = Logger.getLogger(SocketWriter.class);
    private Thread currentThread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ObjectOutputStream oos;

    public SocketWriter(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void start() {
        currentThread = new Thread(this);
        currentThread.start();
    }

    public void stop() {
        running.set(false);
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    @Override
    public void run() {
        log.debug("SocketWriter started");
        running.set(true);
        Packet packet = null;

        while (running.get()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String command = reader.readLine();
                if(command.equals("quit")) break;
                packet = PacketBuilder.buildFromCommand(command);
            } catch (IllegalStateException | IOException ex) {
                System.out.println(ex.getMessage());
                continue;
            }

            if (packet == null) continue;

            try {
                oos.writeObject(packet);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("SocketWriter stopped");
    }
}
