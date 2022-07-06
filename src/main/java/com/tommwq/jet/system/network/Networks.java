package com.tommwq.jet.system.network;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * utility functions for newwork
 */
public class Networks {

  /**
   * 非法端口号
   */
  public static int INVALID_PORT = -1;

  /**
   * get machine's host address
   */
  public static String hostAddress() throws UnknownHostException {
    return InetAddress.getLocalHost().getHostAddress();
  }

  /**
   * 将ip地址从整数转换为InetAddress
   *
   * @param ip ip address in integer
   * @return ip address
   */
  public static InetAddress inet_ntoa(int ip) throws UnknownHostException {
    return InetAddress.getByAddress(new byte[]{
        (byte) (ip & 0xFF),
        (byte) ((ip >> 8) & 0xFF),
        (byte) ((ip >> 16) & 0xFF),
        (byte) ((ip >> 24) & 0xFF)});
  }

  /**
   * 生成广播地址
   *
   * @param localAddress 本地地址
   */
  public static InetAddress localBroadcastAddress(InetAddress localAddress) {
    byte[] rawAddress = localAddress.getAddress();
    rawAddress[3] = (byte) 255;

    try {
      return InetAddress.getByAddress(rawAddress);
    } catch (Exception e) {
      // SHOULD NOT BE HERE
      return null;
    }
  }

  /**
   * 查找一个未使用的端口
   *
   * @return 一个未使用的端口。如果没有这样的端口，返回INVALID_PORT
   */
  public static int lookupUnboundPort() {
    int lower = 40000;
    int upper = 50000;

    for (int port = lower; port <= upper; port++) {
      try {
        DatagramSocket socket = new DatagramSocket(port);
        socket.close();
        return port;
      } catch (java.net.SocketException e) {
        // IGNORE
      }
    }

    return INVALID_PORT;
  }

  /**
   * 生成广播地址
   *
   * @param localAddress 本地地址
   */
  public static InetAddress getLocalBroadcastAddress(InetAddress localAddress) {
    byte[] rawAddress = localAddress.getAddress();
    rawAddress[3] = (byte) 255;

    try {
      return InetAddress.getByAddress(rawAddress);
    } catch (Exception e) {
      // SHOULD NOT BE HERE
      return null;
    }
  }
}
