package util.codec;


public final class Hexa {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(Coder.class);


  private static final char[] HEXA = { '0', '1', '2', '3', '4', '5', '6', '7',
                                       '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  private static final int[] BYTE0 = new int['f' + 1];

  private static final int[] BYTE1 = new int['f' + 1];

  static {
    for (char c='0'; c <= '9'; c++) BYTE0[c] =  c - '0';
    for (char c='A'; c <= 'F'; c++) BYTE0[c] = (c - 'A') + 10;
    for (char c='a'; c <= 'f'; c++) BYTE0[c] = (c - 'a') + 10;
    for (int i=0; i < BYTE0.length; i++) BYTE1[i] = BYTE0[i] << 4;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Encode --------------------------------------------------------------------


  public static char[] encodeHexaToChar(byte[] b) {

    if (b == null) {
      return null;
    }

    final char[] c = new char[2*b.length];

    for (int i=b.length; i-- > 0;) {
      c[ i << 1     ] = HEXA[0x0F & b[i] >>> 4];
      c[(i << 1) + 1] = HEXA[0x0F & b[i]];
    }

    return c;

  }


  public static char[] encodeHexaToChar(String s) {
    return (s != null) ? encodeHexaToChar(s.getBytes()) : null;
  }


  public static String encodeHexaToString(byte[] b) {
    return new String(encodeHexaToChar(b));
  }


  public static String encodeHexaToString(String s) {
    return (s != null) ? new String(encodeHexaToChar(s)) : null;
  }


  // Decode --------------------------------------------------------------------


  public static byte[] decodeHexaToByte(char[] c) {

    if (c == null) {
      return null;
    }

    final byte[] b = new byte[c.length/2];

    for (int i=c.length; i-- > 0;) {
      b[i >>> 1] = (byte)(BYTE0[c[i]] | BYTE1[c[--i]]);
    }

    return b;

  }


  public static byte[] decodeHexaToByte(String s) {
    return decodeHexaToByte(s.toCharArray());
  }


  public static String decodeHexaToString(char[] c) {
    return (c != null) ? new String(decodeHexaToByte(c)) : null;
  }


  public static String decodeHexaToString(String s) {
    return (s != null) ? new String(decodeHexaToByte(s)) : null;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private Hexa() { }


}