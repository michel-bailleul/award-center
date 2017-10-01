package util.collection;


import util.collection.IFilter;


public class Product implements IFilter<Product>, Comparable<Product> {


  // —————————————————————————————————————————————————————————— Static Variables


  private static String _filterColor;

  private static Double _filterPriceMin;

  private static Double _filterPriceMax;


  // ———————————————————————————————————————————————————————————— Static Methods


  public static void setFilterColor(String color) {
    _filterColor = color;
  }

  public static void setFilterPriceMin(Double price) {
    _filterPriceMin = price;
  }

  public static void setFilterPriceMax(Double price) {
    _filterPriceMax = price;
  }

  public static void resetFilter() {
    _filterColor = null;
    _filterPriceMin = null;
    _filterPriceMax = null;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  public Product(String name, String color, double price) {
    this.name = name;
    this.color = color;
    this.price = price;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private String name;

  private String color;

  private double price;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String toString() {
    return "[" + name + "|" + color + "|" + price + "]";
  }


  @Override
  public int compareTo(Product o) {
    if (name != null && o != null && o.getName() != null) {
      return name.compareTo(o.getName());
    }
    return 0;
  }


  @Override
  public boolean matches(Product obj) {

    if (obj == null) {
      return false;
    }

    if (_filterColor != null && obj.getColor() != null &&
        !obj.getColor().equalsIgnoreCase(_filterColor))
    {
      return false;
    }
    if (_filterPriceMin != null && _filterPriceMin > obj.getPrice()) {
      return false;
    }
    if (_filterPriceMax != null && _filterPriceMax < obj.getPrice()) {
      return false;
    }

    return true;

  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


}