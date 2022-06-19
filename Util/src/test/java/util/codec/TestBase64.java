package util.codec;


import static org.junit.Assert.*;

import static util.codec.Base64.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestBase64 {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String STR = "You talkin' to me ?";
  private static final String B64 = "WW91IHRhbGtpbicgdG8gbWUgPw==";

  private static final byte[] data = { 55, 72, -14, 16, -1, 112, 0, -112 };
  private static final char[] base64 = { 'N', '0', 'j', 'y', 'E', 'P', '9',
                                         'w', 'A', 'J', 'A', '=' };

  private static byte[] dataFile;
  private static char[] base64File;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() throws Exception {
  }


  @After
  public void after() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testEncode64ToChar() {
    char[] c = encodeBase64ToChar(data);
    assertArrayEquals(base64, c);
  }


  @Test
  public void testDecode64ToByte() {
    byte[] b = decodeBase64ToByte(base64);
    assertArrayEquals(data, b);
  }


  @Test
  public void testEncode64ToString() {
    String s = encodeBase64ToString(STR);
    System.out.println(s);
    assertEquals(s, B64);
  }


  @Test
  public void testDecode64ToString() {
    String s = decodeBase64ToString(B64);
    System.out.println(s);
    assertEquals(s, STR);
  }


//  @Test
  public void testEncode64ToCharFile() {
    base64File = encodeBase64ToChar(dataFile);
  }


//  @Test
  public void testDecode64ToByteFile() {
    dataFile = decodeBase64ToByte(base64File);
  }


  @Test
  public void testEncodeEmptyArray() {
    char[] c = encodeBase64ToChar(new byte[0]);
    assertArrayEquals(new char[0], c);
  }


  @Test
  public void testDecodeEmptyArray() {
    byte[] b = decodeBase64ToByte(new char[0]);
    assertArrayEquals(new byte[0], b);
  }


  @Test
  public void testEncodeNull() {
    char[] c = encodeBase64ToChar((byte[])null);
    assertNull(c);
  }


  @Test
  public void testDecodeNull() {
    byte[] b = decodeBase64ToByte((char[])null);
    assertNull(b);
  }


}