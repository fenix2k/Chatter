package ru.fenix2k.Chatter.server;


public class AppServer {

    public static final int PORT = 9090;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        new Thread(server).start();

        /*try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stopping Server");
        server.stop();*/
    }

}
