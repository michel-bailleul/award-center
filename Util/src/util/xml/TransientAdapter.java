package util.xml;


import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Adapter class for transient properties
 *
 * @author Michel BAILLEUL
 */
public class TransientAdapter extends XmlAdapter<Object, Object> {

  @Override
  public Object unmarshal(Object v) throws Exception {
    return null;
  }

  @Override
  public Object marshal(Object v) throws Exception {
    return null;
  }

}
