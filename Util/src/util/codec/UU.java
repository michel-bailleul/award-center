package util.codec;


import static util.resource.ResourceUtil.getMsg;
import static util.resources.LogKey.BASE64_ERR_CHARACTER;
import static util.resources.LogKey.BASE64_ERR_LENGTH;


public final class UU {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(Coder.class);

  /*

32     33 !   34 "   35 #   36 $   37 %   38 &   39 '
40 (   41 )   42 *   43 +   44 ,   45 -   46 .   47 /
48 0   49 1   50 2   51 3   52 4   53 5   54 6   55 7
56 8   57 9   58 :   59 ;   60 <   61 =   62 >   63 ?
64 @   65 A   66 B   67 C   68 D   69 E   70 F   71 G
72 H   73 I   74 J   75 K   76 L   77 M   78 N   79 O
80 P   81 Q   82 R   83 S   84 T   85 U   86 V   87 W
88 X   89 Y   90 Z   91 [   92 \   93 ]   94 ^   95 _

   * Where the bits of two octets are combined, the least significant bits of the first octet are shifted left and combined with the most significant bits of the second octet shifted right. Thus the three octets A, B, C are converted into the four octets:


0x20 + (( A >> 2                    ) & 0x3F)
0x20 + (((A << 4) ' |' ((B >> 4) & 0xF)) & 0x3F)
0x20 + (((B << 2) ' |' ((C >> 6) & 0x3)) & 0x3F)
0x20 + (( C                         ) & 0x3F)

These octets are then translated into the local character set.


   */

  private static final char[] BYTE_TO_B64 = new char[64];

  static {
    int i=0;
    for (char c='A'; c <= 'Z'; c++) BYTE_TO_B64[i++] = c;
    for (char c='a'; c <= 'z'; c++) BYTE_TO_B64[i++] = c;
    for (char c='0'; c <= '9'; c++) BYTE_TO_B64[i++] = c;
    BYTE_TO_B64[i++] = '+'; BYTE_TO_B64[i++] = '/';
  }

  private static byte[] B64_TO_BYTE = new byte[128];

  static {
    for (int i=0; i < B64_TO_BYTE.length; i++) B64_TO_BYTE[i] = -1;
    for (int i=0; i < 64; i++) B64_TO_BYTE[BYTE_TO_B64[i]] = (byte)i;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Encode --------------------------------------------------------------------


  public static char[] encode64ToChar(byte[] b) {

    if (b == null) {
      return null;
    }

    final int il = b.length;
    final int ol = (il*4+2)/3;
    final char[] c = new char[((il+2)/3)*4];

    for (int i=0, o=0; i < il;) {
      int i0 = b[i++] & 0xFF;
      int i1 = i < il ? b[i++] & 0xFF : 0;
      int i2 = i < il ? b[i++] & 0xFF : 0;
      c[o++] = BYTE_TO_B64[i0 >>> 2];
      c[o++] = BYTE_TO_B64[((i0 & 0x03) << 4) | (i1 >>> 4)];
      c[o] = o++ < ol ? BYTE_TO_B64[((i1 & 0x0F) << 2) | (i2 >>> 6)] : '=';
      c[o] = o++ < ol ? BYTE_TO_B64[i2 & 0x3F]                       : '=';
    }

    return c;

  }


  public static char[] encode64ToChar(String s) {
    return (s != null) ? encode64ToChar(s.getBytes()) : null;
  }


  public static String encode64ToString(byte[] b) {
    return (b != null) ? new String(encode64ToChar(b)) : null;
  }


  public static String encode64ToString(String s) {
    return (s != null) ? new String(encode64ToChar(s)) : null;
  }


  // Decode --------------------------------------------------------------------


  public static byte[] decode64ToByte(char[] c) {

    if (c == null) {
      return null;
    }

    if (c.length % 4 != 0) {
      throw new IllegalArgumentException(getMsg(BASE64_ERR_LENGTH));
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
      int i2 = i < il ? c[i++] : 'A';
      int i3 = i < il ? c[i++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
        throw new IllegalArgumentException(getMsg(BASE64_ERR_CHARACTER));
      }
      int b0 = B64_TO_BYTE[i0];
      int b1 = B64_TO_BYTE[i1];
      int b2 = B64_TO_BYTE[i2];
      int b3 = B64_TO_BYTE[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
        throw new IllegalArgumentException(getMsg(BASE64_ERR_CHARACTER));
      }
                   out[op++] = (byte)(( b0         << 2) | (b1 >>> 4));
      if (op < ol) out[op++] = (byte)(((b1 & 0x0F) << 4) | (b2 >>> 2));
      if (op < ol) out[op++] = (byte)(((b2 & 0x03) << 6) |  b3);
    }

    return out;

  }


  public static byte[] decode64ToByte(String s) {
    return (s != null) ? decode64ToByte(s.toCharArray()) : null;
  }


  public static String decode64ToString(char[] c) {
    return (c != null) ? new String(decode64ToByte(c)) : null;
  }


  public static String decode64ToString(String s) {
    return (s != null) ? new String(decode64ToByte(s)) : null;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private UU() { }


}