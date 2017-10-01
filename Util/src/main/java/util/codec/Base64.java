package util.codec;


import static util.resources.LogKey.BASE64_ERR_CHARACTER;
import static util.resources.LogKey.BASE64_ERR_LENGTH;


import util.resource.Logger;


public final class Base64 {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = Logger.getLogger(Base64.class);


  private static final char[] BYTE_TO_BASE64 = new char[64];

  static {
    int i=0;
    for (char c='A'; c <= 'Z'; c++) BYTE_TO_BASE64[i++] = c;
    for (char c='a'; c <= 'z'; c++) BYTE_TO_BASE64[i++] = c;
    for (char c='0'; c <= '9'; c++) BYTE_TO_BASE64[i++] = c;
    BYTE_TO_BASE64[i++] = '+'; BYTE_TO_BASE64[i++] = '/';
  }

  private static byte[] BASE64_TO_BYTE = new byte[128];

  static {
    for (int i=0; i < BASE64_TO_BYTE.length; i++) BASE64_TO_BYTE[i] = -1;
    for (int i=0; i < 64; i++) BASE64_TO_BYTE[BYTE_TO_BASE64[i]] = (byte)i;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Encode --------------------------------------------------------------------


  public static char[] encodeBase64ToChar(byte[] b) {

    if (b == null) {
      return null;
    }

    final int il = b.length;
    final int ol = (il*4+2)/3;
    final char[] c = new char[((il+2)/3)*4];

    for (int i=0, o=0; i < il;) {
      int i0 = b[i++] & 0xFF;
      int i1 = (i < il) ? b[i++] & 0xFF : 0;
      int i2 = (i < il) ? b[i++] & 0xFF : 0;
      c[o++] = BYTE_TO_BASE64[i0 >>> 2];
      c[o++] = BYTE_TO_BASE64[((i0 & 0x03) << 4) | (i1 >>> 4)];
      c[o] = (o++ < ol) ? BYTE_TO_BASE64[((i1 & 0x0F) << 2) | (i2 >>> 6)] : '=';
      c[o] = (o++ < ol) ? BYTE_TO_BASE64[i2 & 0x3F]                       : '=';
    }

    return c;

  }


  public static char[] encodeBase64ToChar(String s) {
    return (s != null) ? encodeBase64ToChar(s.getBytes()) : null;
  }


  public static String encodeBase64ToString(byte[] b) {
    return (b != null) ? new String(encodeBase64ToChar(b)) : null;
  }


  public static String encodeBase64ToString(String s) {
    return (s != null) ? new String(encodeBase64ToChar(s)) : null;
  }


  // Decode --------------------------------------------------------------------


  public static byte[] decodeBase64ToByte(char[] c) {

    if (c == null) {
      return null;
    }

    if (c.length % 4 != 0) {
      throw new IllegalArgumentException(logger.error(BASE64_ERR_LENGTH));
    }

    int il = c.length;
    while (il > 0 && c[il-1] == '=') il--;

    int i = 0;
    int op = 0;
    final int ol = (il*3) / 4;
    final byte[] out = new byte[ol];

    while (i < il) {
      int i0 = c[i++];
      int i1 = c[i++];
      int i2 = (i < il) ? c[i++] : 'A';
      int i3 = (i < il) ? c[i++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
        throw new IllegalArgumentException(logger.error(BASE64_ERR_CHARACTER));
      }
      int b0 = BASE64_TO_BYTE[i0];
      int b1 = BASE64_TO_BYTE[i1];
      int b2 = BASE64_TO_BYTE[i2];
      int b3 = BASE64_TO_BYTE[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
        throw new IllegalArgumentException(logger.error(BASE64_ERR_CHARACTER));
      }
                   out[op++] = (byte) (( b0         << 2) | (b1 >>> 4));
      if (op < ol) out[op++] = (byte) (((b1 & 0x0F) << 4) | (b2 >>> 2));
      if (op < ol) out[op++] = (byte) (((b2 & 0x03) << 6) |  b3);
    }

    return out;

  }


  public static byte[] decodeBase64ToByte(String s) {
    return (s != null) ? decodeBase64ToByte(s.toCharArray()) : null;
  }


  public static String decodeBase64ToString(char[] c) {
    return (c != null) ? new String(decodeBase64ToByte(c)) : null;
  }


  public static String decodeBase64ToString(String s) {
    return (s != null) ? new String(decodeBase64ToByte(s)) : null;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this klass */
  private Base64() { }


}