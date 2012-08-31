package awardcenter;


import static util.io.FileUtil.getBytes;
import static util.swing.SwingUtil.STYLE_B;
import static util.swing.SwingUtil.STYLE_BI;
import static util.swing.SwingUtil.STYLE_BIU;
import static util.swing.SwingUtil.STYLE_BU;
import static util.swing.SwingUtil.STYLE_I;
import static util.swing.SwingUtil.STYLE_IU;
import static util.swing.SwingUtil.STYLE_U;
import static util.swing.SwingUtil.formatHTML;

import static org.junit.Assert.assertNotNull;


import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import util.misc.StringUtil;


public class AwardCenterTest {


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
/*
    FileInputStream fis = new FileInputStream(new File(FILE_NAME));
    BufferedInputStream bis = new BufferedInputStream(fis, 65535);
    ByteArrayOutputStream baos = new ByteArrayOutputStream(65535);

    byte[] buffer = new byte[1024];
    while (bis.read(buffer) != -1) {
      baos.write(buffer);
    };
    dataFile = baos.toByteArray();
    bis.close();
*/
  }


  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


//  @Test
  public void style() {

    int B = 1;
    int I = B << 1;
    int U = I << 1;

    int style = B | U;
    System.out.println(style);

    System.out.println((style & B) > 0);
    System.out.println((style & I) > 0);
    System.out.println((style & U) > 0);

  }


  /**
   * Test method for {@link common.util.util.StringUtil#formatHTML(java.lang.Object, int)}.
   */
//  @Test
  public void testFormatHTML() {

    String text = "test";

    System.out.println(formatHTML(text, 0));
    System.out.println(formatHTML(text, 0, 0));
    System.out.println(formatHTML(text, 0, 2));
    System.out.println(formatHTML(text, 0, -2));
    System.out.println(formatHTML(text, STYLE_B));
    System.out.println(formatHTML(text, STYLE_I));
    System.out.println(formatHTML(text, STYLE_U));
    System.out.println(formatHTML(text, STYLE_BI));
    System.out.println(formatHTML(text, STYLE_BU));
    System.out.println(formatHTML(text, STYLE_IU));
    System.out.println(formatHTML(text, STYLE_BIU));

  }


//  @Test
  public void testFileUtilGetBytes() {
    File file = new File("D:/tmp/img/image.png");
    byte[] b = getBytes(file);
    assertNotNull(b);
  }


//  @Test
  public void testFormatter() {
    System.out.println(StringUtil.formatString("test [%1$s]", "01"));
    System.out.println(StringUtil.formatString("test [%1$s]", "02"));
    System.out.println(StringUtil.formatString("test [%1$s]", "03"));
  }


}