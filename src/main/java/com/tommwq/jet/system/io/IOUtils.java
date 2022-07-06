package com.tommwq.jet.system.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOUtils {

  // 对于GZIPInputStream等输入流，读取时必须按固定大小的块。
  public static void read(InputStream inputStream, byte[] outputBuffer, int offset, int length, int blockSize) throws IOException {
    byte[] localBuffer = new byte[blockSize];
    int readLength = 0;
    int remainLength = length - readLength;
    int dataLength = blockSize - (offset % blockSize);
    if (dataLength > remainLength) {
      dataLength = remainLength;
    }

    inputStream.skip(offset);
    while (readLength < length) {
      int n = inputStream.read(localBuffer, 0, dataLength);
      if (n == -1) {
        throw new IOException("end of stream");
      }

      System.arraycopy(localBuffer, 0, outputBuffer, readLength, n);

      readLength += n;
      remainLength -= n;
      dataLength = Math.min(blockSize, remainLength);
    }
  }

  public static <T> void print(Stream<T> stream) {
    System.out.println(String.join(", ", stream.map(x -> x == null ? "null" : x.toString()).collect(Collectors.toList())));
  }

  public static <T> void print(T[] array) {
    print(Arrays.stream(array));
  }

  public static void print(long[] array) {
    print(Arrays.stream(array).boxed());
  }
}
