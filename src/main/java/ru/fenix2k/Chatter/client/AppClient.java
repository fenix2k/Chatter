package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;

import java.io.IOException;

public class AppClient {
    private static final Logger log = Logger.getLogger(AppClient.class);

    public static void main(String[] args) {
        Client client = new Client("localhost", 9090);
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
