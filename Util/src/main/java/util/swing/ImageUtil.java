package util.swing;


import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.Transparency.TRANSLUCENT;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import static util.resource.Logger.getLogger;
import static util.resources.LogKey.IMAGE_UTIL_ADD;
import static util.resources.LogKey.IMAGE_UTIL_DEFAULT;
import static util.resources.LogKey.IMAGE_UTIL_ERR_CREATE;
import static util.resources.LogKey.IMAGE_UTIL_ERR_NOT_FOUND;
import static util.resources.LogKey.IMAGE_UTIL_ERR_URL;
import static util.resources.LogKey.IMAGE_UTIL_GET;
import static util.resources.GuiKey.ICON_SPACER;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;

import util.collection.SoftHashMap;
import util.resource.IKey;
import util.resource.Logger;



public final class ImageUtil {


  // ————————————————————————————————————————————————————————————— Inner Classes


  private static final class Key {

    private final Object source;
    private final int width;
    private final int height;

    public Key(Object source, int height, int width) {
      if (source == null) {
        throw new IllegalArgumentException();
      }
      this.source = source;
      this.height = height;
      this.width  = width;
    }

    @Override
    public int hashCode() {
      int hash = 17;
      hash += 37*source.hashCode();
      hash += 37*height;
      hash += 37*width;
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj instanceof Key) {
        Key ce = (Key)obj;
        return (ce.height == height &&
                ce.width  == width  &&
                ce.source.equals(source));
      }
      return false;
    }

  }


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(ImageUtil.class);

  private static final Map<Key,ImageIcon> CACHE_ICON = new SoftHashMap<Key,ImageIcon>();

  private static final Image     DEFAULT_IMAGE = getImage(null, ICON_SPACER, 0, 0);
  private static final ImageIcon DEFAULT_ICON  = getImageIcon(null, ICON_SPACER, 0, 0);


  // ——————————————————————————————————————————————————————————— Private Methods


  private static ImageIcon _getImageIconFromCache(Component c, Object o, int h, int w) {

    ImageIcon img;

    if (o == null) {
      img = getImageIcon(c, DEFAULT_ICON, h, w);
      logger.debug(IMAGE_UTIL_DEFAULT);
    }
    else {
      img = CACHE_ICON.get(new Key(o, h, w));
      if (img != null && o != DEFAULT_IMAGE) {
        logger.debug(IMAGE_UTIL_GET, h, w);
      }
    }

    return img;

  }


  private static void _putImageIconIntoCache(ImageIcon imageIcon, Object o, int h, int w) {

    if (imageIcon != null) {
      CACHE_ICON.put(new Key(o, h, w), imageIcon);
      logger.debug(IMAGE_UTIL_ADD, h, w);
    }

  }


  private static ImageIcon _createImageIcon(Component c, ImageIcon imageIcon, int h, int w) {

    int sourceHeight = imageIcon.getIconHeight();
    int sourceWidth  = imageIcon.getIconWidth();

    if (sourceHeight > 0 && sourceWidth > 0) {
      int targetHeight;
      int targetWidth;
      float ratio = (float)sourceWidth / (float)sourceHeight;

      if (sourceWidth > sourceHeight) {
        targetWidth = w;
        targetHeight = (int)(targetWidth / ratio);
        if (targetHeight > h) {
          targetHeight = h;
          targetWidth = (int)(ratio * targetHeight);
        }
      }
      else {
        targetHeight = h;
        targetWidth = (int)(ratio * targetHeight);
        if (targetWidth > w) {
          targetWidth = w;
          targetHeight = (int)(targetWidth / ratio);
        }
      }

      if (targetHeight != sourceHeight || targetWidth != sourceWidth) {
        Image newImage;
        GraphicsConfiguration gc = c.getGraphicsConfiguration();
        if (c != null && gc != null) {
          newImage = gc.createCompatibleImage(targetWidth, targetHeight, TRANSLUCENT);
        }
        else {
          newImage = new BufferedImage(targetWidth, targetHeight, TYPE_INT_ARGB);
        }
        Graphics g = newImage.getGraphics();
        if (g instanceof Graphics2D) {
          ((Graphics2D)g).setRenderingHint(KEY_INTERPOLATION,
                                           VALUE_INTERPOLATION_BILINEAR);
//                                           VALUE_INTERPOLATION_BICUBIC);
        }
        g.drawImage(imageIcon.getImage(), 0, 0, targetWidth, targetHeight,
                                          0, 0, sourceWidth, sourceHeight, null);
        g.dispose();
        return new ImageIcon(newImage);
      }
      return imageIcon;
    }

    logger.error(IMAGE_UTIL_ERR_CREATE, sourceHeight, sourceWidth);
    return null;

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public static Image getImage(Component c, File img, int h, int w) {
    return getImage(c, img.toURI(), h, w);
  }


  public static ImageIcon getImageIcon(Component c, File img, int h, int w) {
    return getImageIcon(c, img.toURI(), h, w);
  }


  public static Image getImage(Component c, URI path, int h, int w) {
    ImageIcon img = getImageIcon(c, path, h, w);
    return img != null ? img.getImage() : null;
  }


  public static ImageIcon getImageIcon(Component c, URI path, int h, int w) {

    try {
      return getImageIcon(c, path.toURL(), h, w);
    }
    catch (MalformedURLException x) {
      logger.error(IMAGE_UTIL_ERR_URL, x, path.getPath());
      return null;
    }

  }


  public static Image getImage(Component c, URL path, int h, int w) {
    ImageIcon img = getImageIcon(c, path, h, w);
    return img != null ? img.getImage() : null;
  }


  public static ImageIcon getImageIcon(Component c, URL path, int h, int w) {

    ImageIcon img = _getImageIconFromCache(c, path, h, w);

    if (img == null) {
      ImageIcon icon = new ImageIcon(path);
      if (h == 0 || w == 0) {
        img = icon;
      }
      else {
        img = _createImageIcon(c, icon, h, w);
      }
      _putImageIconIntoCache(img, path, h, w);
    }

    return img;

  }


  public static Image getImage(Component c, byte[] bytes, int h, int w) {
    ImageIcon img = getImageIcon(c, bytes, h, w);
    return img != null ? img.getImage() : null;
  }


  public static ImageIcon getImageIcon(Component c, byte[] bytes, int h, int w) {

    ImageIcon img = _getImageIconFromCache(c, bytes, h, w);

    if (img == null) {
      ImageIcon icon = new ImageIcon(bytes);
      if (h == 0 || w == 0) {
        img = icon;
      }
      else {
        img = _createImageIcon(c, icon, h, w);
      }
      _putImageIconIntoCache(img, bytes, h, w);
    }

    return img;

  }


  public static Image getImage(Component c, Image image, int h, int w) {

    ImageIcon img = _getImageIconFromCache(c, image, h, w);

    if (img == null) {
      img = _createImageIcon(c, new ImageIcon(image), h, w);
      _putImageIconIntoCache(img, image, h, w);
    }

    return img.getImage();

  }


  public static ImageIcon getImageIcon(Component c, ImageIcon imageIcon, int h, int w) {

    ImageIcon img = _getImageIconFromCache(c, imageIcon, h, w);

    if (img == null) {
      img = _createImageIcon(c, imageIcon, h, w);
      _putImageIconIntoCache(img, imageIcon, h, w);
    }

    return img;

  }


  public static Image getImage(Component c, IKey key, int h, int w) {
    ImageIcon img = getImageIcon(c, key, h, w);
    return img != null ? img.getImage() : null;
  }


  public static ImageIcon getImageIcon(Component c, IKey key, int h, int w) {

    URL url = key.getClass().getResource(key.getKey());

    if (url == null) {
      logger.error(IMAGE_UTIL_ERR_NOT_FOUND, key.getKey());
    }

    return getImageIcon(c, url, h, w);

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this klass */
  private ImageUtil() { }


}