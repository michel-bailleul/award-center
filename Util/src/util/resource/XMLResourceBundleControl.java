package util.resource;


import static java.util.Collections.singletonList;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;


class XMLResourceBundleControl extends Control {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String XML = "xml";


  // —————————————————————————————————————————————————————————————— Constructors


  /** default constructor */
  XMLResourceBundleControl() { }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public List<String> getFormats(String baseName) {
    return singletonList(XML);
  }


  @Override
  public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                  ClassLoader loader, boolean reload)
  throws IllegalAccessException, InstantiationException, IOException
  {
    if ((baseName == null) || (locale == null) ||
        (format   == null) || (loader == null))
    {
      throw new IllegalArgumentException();
    }

    if (!format.equals(XML)) {
      return null;
    }

    String bundleName = toBundleName(baseName, locale);
    String resourceName = toResourceName(bundleName, format);

    URL url = loader.getResource(resourceName);
    if (url == null) {
      return null;
    }

    URLConnection connection = url.openConnection();
    if (connection == null) {
      return null;
    }
    else if (reload) {
      connection.setUseCaches(false);
    }

    InputStream stream = connection.getInputStream();
    if (stream == null) {
      return null;
    }

    BufferedInputStream bis = new BufferedInputStream(stream);
    ResourceBundle bundle = new XMLResourceBundle(bis);
    bis.close();

    return bundle;
  }


}