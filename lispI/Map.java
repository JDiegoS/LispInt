package com.example.fuent.lispinterpreter.lispI;
import java.util.Set;
/**
 * @author Jennifer Sandoval,Andrea Paniagua
 * @Carne 18962,18733
 * @date 06/03/19
 * @name Map.java
 * <p>Interfaz de Map, con todos los metodos de un map </p>
 * */
public interface Map<K,V>
{
    /**
     * @post returns the number of entries in the map
     */
    public int size();

    /**
     * @post returns true iff this map does not contain any entries
     */
    public boolean isEmpty();

    /**
     * @pre k is non-null
     * @post returns true iff k is in the domain of the map
     */
    public boolean containsKey(K k);

    /**
     * @pre v is non-null
     * @post returns true iff v is the target of at least one map entry;
     * that is, v is in the range of the map
     */
    public boolean containsValue(V v);

    /**
     * @pre k is a key, possibly in the map
     * @post returns the value mapped to from k, or null
     */
    public V get(K k);

    /**
     * @pre k and v are non-null
     * @post inserts a mapping from k to v in the map
     */
    public V put(K k, V v);

    /**
     * @pre k is non-null
     * @post removes any mapping from k to a value, from the mapping
     */
    public V remove(K k);

    /**
     * @pre other is non-null
     * @post all the mappings of other are installed in this map,
     * overriding any conflicting maps
     */
    public void putAll(Map<K,V> other);

    /**
     * @post removes all map entries associated with this map
     */
    public void clear();

    /**
     * @post returns a set of all keys associated with this map
     */
    public Set<K> keySet();

    /**
     * @post returns a structure that contains the range of the map
     */
  /*  public Structure<V> values();
    /**
     * @post returns a set of (key-value) pairs, generated from this map
     */
   /* public Set<Association<K,V>> entrySet();
    /**
     * @pre other is non-null
     * @post returns true iff maps this and other are entry-wise equal
     */
    public boolean equals(Object other);

    /**
     * @post returns a hash code associated with this structure
     */
    public int hashCode();
}