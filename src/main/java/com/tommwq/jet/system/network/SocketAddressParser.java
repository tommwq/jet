package com.tommwq.jet.system.network;

/**
 * parse socket address string, e.g. "127.0.0.1:8080"
 */
public class SocketAddressParser {
    private final String socketAddress;
    private int port;
    private String address;

    public SocketAddressParser(String aSocketAddress) {
        socketAddress = aSocketAddress;
        parse();
    }

    public int port() {
        return port;
    }

    public String address() {
        return address;
    }

    private void parse() {
        String[] part = socketAddress.split(":");
        if (part.length != 2) {
            throw new RuntimeException("invalid socket address: " + socketAddress);
        }

        address = part[0];
        port = Integer.valueOf(part[1]);
    }
}
