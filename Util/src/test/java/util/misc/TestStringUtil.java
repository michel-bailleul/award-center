package util.misc;


import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestStringUtil {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }


  @Test
  public void testNBSP() {
    assertNotSame(' ', StringUtil.NBSP);
  }


}
