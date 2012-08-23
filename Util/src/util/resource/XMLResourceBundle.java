package util.resource;


import static java.util.Collections.enumeration;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;


class XMLResourceBundle extends ResourceBundle {


  // —————————————————————————————————————————————————————————————— Constructors


  XMLResourceBundle(InputStream stream) throws IOException {
    props = new Properties();
    props.loadFromXML(stream);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Properties props;


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected Object handleGetObject(String key) {
    return props.getProperty(key);
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public Enumeration<String> getKeys() {
    Set<String> handleKeys = props.stringPropertyNames();
    return enumeration(handleKeys);
  }


}