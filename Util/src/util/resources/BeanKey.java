package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("bean")
public enum BeanKey implements IKey {


  // FileUtil ------------------------------------------------------------------

  BEAN_UTIL_ERR_CLASS_NULL    ("bean.util.err.class.null"),
  BEAN_UTIL_ERR_BEAN_INFO     ("bean.util.err.bean.info"),
  BEAN_UTIL_ERR_BEAN_NULL     ("bean.util.err.bean.null"),
  BEAN_UTIL_ERR_INVOKE_METHOD ("bean.util.err.invoke.method"),
  BEAN_COMPARATOR_ERR_GETTER  ("bean.comparator.err.getter");


  // —————————————————————————————————————————————————————————————— Constructors


  private BeanKey(String key) {
    this.key = key;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final String key;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String getKey() {
    return key;
  }


}