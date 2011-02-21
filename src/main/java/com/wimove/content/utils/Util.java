package com.wimove.content.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Clint Gilbert
 * 
 * Mar 9, 2009
 *
 * Intelligent Health Lab at the
 * Childrens Hospital Informatics Program at the
 * Harvard-MIT division of Health Sciences Technology
 * @link http://www.chip.org/ihl
 * 
 */
public abstract class Util
{
    private Util()
    {
        
    }
    
    public static final <T> IterableEnumeration<T> iterable(final Enumeration<T> enumeration)
    {
        return new IterableEnumeration<T>(enumeration);
    }
    
    public static class IterableEnumeration<T> implements Iterable<T>
    {
        private final Enumeration<T> enumeration;

        public IterableEnumeration(final Enumeration<T> enumeration)
        {
            super();
        
            Guard.notNull(enumeration);
            
            this.enumeration = enumeration;
        }

        public Iterator<T> iterator()
        {
            return new Iterator<T>() {

                public boolean hasNext()
                {
                    return enumeration.hasMoreElements();
                }

                public T next()
                {
                    return enumeration.nextElement();
                }

                public void remove()
                {
                    //NB: Intentional NOOP
                }
            };
        }
    }
    
    public static final class Range implements Iterable<Integer>
    {
        private final int min;
        private final int max;
        
        public Range(final int min, final int max)
        {
            super();
            
            if(max < min)
            {
                throw new IllegalArgumentException("Max value must be greater or equal to minimum value");
            }
            
            this.min = min;
            this.max = max;
        }
    
        public int getMin()
        {
            return min;
        }

        public int getMax()
        {
            return max;
        }

        public int size()
        {
            return max - min;
        }
        
        public Range inclusive()
        {
            return new Range(min, max + 1);
        }
        
        public Iterator<Integer> iterator()
        {
            return new Iterator<Integer>() {

                private int current = min;
                
                public boolean hasNext()
                {
                    return current < max;
                }
    
                public Integer next()
                {
                    return current++;
                }
    
                public void remove()
                {
                    //Intentional NOOP; nothing to remove
                }
            };
        }

        @Override
        public String toString()
        {
            return concat("[", min, ",", max, ")");
        }
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> makeArrayList()
    {
        return new ArrayList<T>();
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> makeArrayList(final Collection<? extends T> collection)
    {
        return new ArrayList<T>(collection);
    }

    /**
     * Type-inferring convenience factory method
     * Makes a mutable ArrayList; the list produced
     * my Arrays.asList() is immutable.
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> makeArrayList(final T ... values)
    {
        return new ArrayList<T>(Arrays.asList(values));
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @param initialCapacity
     * @return
     */
    public static <T> ArrayList<T> makeArrayList(final int initialCapacity)
    {
        return new ArrayList<T>(initialCapacity);
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> LinkedList<T> makeLinkedList()
    {
        return new LinkedList<T>();
    }
    
    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> LinkedList<T> makeLinkedList(final Collection<? extends T> collection)
    {
        return new LinkedList<T>(collection);
    }

    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K,V> makeHashMap()
    {
        return new HashMap<K,V>();
    }

    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     * 
     * @deprecated Not sure what's up with this.  Doesn't return an EnumMap - EnumMap doesn't even have a no-arg constructor.
     */
    @Deprecated
    public static <K extends Enum<K>, V> HashMap<K,V> makeEnumMap()
    {
        return new HashMap<K,V>();
    }

    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> TreeMap<K,V> makeTreeMap()
    {
        return new TreeMap<K,V>();
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> TreeSet<T> makeTreeSet()
    {
        return new TreeSet<T>();
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <T> HashSet<T> makeHashSet()
    {
        return new HashSet<T>();
    }
    
    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     */
    public static <K extends Enum<K>, V> EnumMap<K,V> makeEnumMap(final Class<K> keyClass)
    {
        return new EnumMap<K,V>(keyClass);
    }

    /**
     * For convenience.  Returns a variable number of inputs as a Set.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     */
    public static <T> Set<T> asSet(final T ... values)
    {
        return Util.asSet(Arrays.asList(values));
    }

    /**
     * For convenience.  Returns a variable number of inputs as a Set.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     */
    public static <T> Set<T> asSet(final Collection<? extends T> values)
    {
        return new HashSet<T>(values);
    }

    /**
     * For convenience.  Returns a variable number of inputs as an array.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     */
    public static <T> T[] asArray(final T ... values)
    {
        return values;
    }

    /**
     * Convenience method.  Returns the values of a Set as a List
     * @param <T>
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(final Set<T> values)
    {
        return Arrays.asList((T[])values.toArray());
    }
    
    /**
     * Convenience method.  Returns the passed values as a sorted List
     * @param <T>
     * @param values
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> asSortedList(final T ... values)
    {
        return asSortedList(Arrays.asList(values));
    }
    
    /**
     * Convenience method.  Returns the values in the passed Collection
     * as a sorted List
     * @param <T>
     * @param values
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> asSortedList(final Collection<? extends T> values)
    {
        final List<T> result = makeArrayList(values);
        
        Collections.sort(result);
        
        return result;
    }

    /**
     * Tests if two Maps are equal.  The Maps being considered must have the same type params.
     * Also, their key and value types must both implement equals().
     * 
     * Returns true if:
     *  Both Maps are the same
     *  Both Maps are null
     *  Both Maps have equal key and entry sets
     *  
     * Returns false if:
     *  One Map is null but the other isn't
     *  The Maps have different sizes (shortcut for performance)
     *  The maps have unequal key and entry sets
     *  
     * @param <K>
     * @param <V>
     * @param lhs
     * @param rhs
     * @return
     */
    public static <K, V> boolean mapsAreEqual(final Map<K, V> lhs, final Map<K, V> rhs)
    {
        //maps are the same
        if(lhs == rhs)
        {
            return true;
        }
    
        //maps are both null
        if(lhs == null && rhs == null)
        {
            return true;
        }
    
        //both maps aren't null, but one is 
        if(lhs == null || rhs == null)
        {
            return false;
        }
    
        //sizes are different (short-circuit here to avoid deep comparison of key and entry sets) 
        if(lhs.size() != rhs.size())
        {
            return false;
        }
    
        //are key sets different?
        if(!lhs.keySet().equals(rhs.keySet()))
        {
            return false;
        }
    
        //
        //Make sure key -> value mappings are equal
        for(final K key : lhs.keySet())
        {
            final V lhsVal = lhs.get(key);
            final V rhsVal = rhs.get(key);
            
            //vals are the same
            if(lhsVal == rhsVal)
            {
                continue;
            }
            
            //vals are both null
            if(lhsVal == null && rhsVal == null)
            {
                continue;
            }
            
            //one is null but the other isn't
            if(lhsVal == null || rhsVal == null)
            {
                return false;
            }
            
            //vals aren't equal
            if(!lhsVal.equals(rhsVal))
            {
                return false;
            }
        }
    
        return true;
    }

    /**
     * Convenience method: allows creating iterable ranges of integers, Python-style,
     * that may be used like: 
     * 
     * for(int i : range(1, 10))
     * {
     *   System.out.println(i);
     * }
     * 
     * Creates a range from min (inclusive) to max (exclusive).
     * 
     * @param min
     * @param max
     * @return
     */
    public static final Range range(final int min, final int max)
    {
        return new Range(min, max);
    }

    /**
     * Convenience method: allows creating iterable ranges of integers, Python-style,
     * that may be used like: 
     * 
     * for(int i : range(10))
     * {
     *   System.out.println(i);
     * }
     * 
     * Creates a range from 0 (inclusive) to max (exclusive).
     * 
     * @param max
     * @return
     */
    public static final Range range(final int max)
    {
        return new Range(0, max);
    }
    
    public static final void require(final boolean condition)
    {
        require(condition, "");
    }
    
    public static final void require(final boolean condition, final String message)
    {
        if(!condition)
        {
            throw new IllegalStateException("Condition not met: " + message);
        }
    }
    
    public static final void guardNotNull(final Object o, final String message)
    {
        require(o != null, "Unexpected null value: " + message);
    }
    
    public static final void guardNotNull(final Object o)
    {
        guardNotNull(o, "");
    }
    
    public static final String concat(final Object ... components)
    {
        if(components == null)
        {
            return "";
        }
        
        return concat(Arrays.asList(components));
    }
    
    public static final String concat(final Collection<? extends Object> components)
    {
        if(components == null)
        {
            return "";
        }
        
        final StringBuilder result = new StringBuilder();
        
        for(final Object o : components)
        {
            result.append(o);
        }
        
        return result.toString();
    }
}
