package test.codec;


import static org.junit.Assert.*;

import static util.codec.Hexa.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestHexa {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String HEXA =
    "506174683A206E6577732E667265652E66722173706F6F6C657231622D312E70726F786164"
  + "2E6E657421636C65616E66656564342D612E70726F7861642E6E65742170726F7861642E6E"
  + "657421666565646572312D312E70726F7861642E6E6574216665656465722E6E6577732D73"
  + "6572766963652E636F6D216665656465722E6E6577732D736572766963652E636F6D21786C"
  + "6E65642E636F6D21666565646572312E786C6E65642E636F6D21666565646572312E63616D"
  + "627269756D7573656E65742E6E6C21666565642E747765616B6E6577732E6E6C2139342E32"
  + "33322E3131362E31332E4D49534D4154434821666565642E78736E6577732E6E6C21";

  private static final byte[] data = { 55, 72, -14, 16, -1, 112, 0, -112 };
  private static final char[] hexa = { '3', '7', '4', '8', 'F', '2', '1', '0',
                                       'F', 'F', '7', '0', '0', '0', '9', '0' };

  private static byte[] dataFile;
  private static char[] hexaFile;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testEncodeHexaToChar() {
    char[] c = encodeHexaToChar(data);
    assertArrayEquals(hexa, c);
  }


  @Test
  public void testDecodeHexaToByte() {
    byte[] b = decodeHexaToByte(hexa);
    assertArrayEquals(data, b);
  }


//  @Test
  public void testEncodeHexaToCharFile() {
    hexaFile = encodeHexaToChar(dataFile);
  }


//  @Test
  public void testDecodeHexaToByteFile() {
    byte[] b = decodeHexaToByte(hexaFile);
  }


  @Test
  public void testDecodeHexaToString() {
    System.out.println(decodeHexaToString(HEXA));
  }


  @Test
  public void testEncodeEmptyArray() {
    char[] c = encodeHexaToChar(new byte[0]);
    assertArrayEquals(new char[0], c);
  }


  @Test
  public void testDecodeEmptyArray() {
    byte[] b = decodeHexaToByte(new char[0]);
    assertArrayEquals(new byte[0], b);
  }


  @Test
  public void testEncodeNull() {
    char[] c = encodeHexaToChar((byte[])null);
    assertNull(c);
  }


  @Test
  public void testDecodeNull() {
    byte[] b = decodeHexaToByte((char[])null);
    assertNull(b);
  }


}