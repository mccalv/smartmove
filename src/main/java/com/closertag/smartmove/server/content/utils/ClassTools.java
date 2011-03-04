package com.closertag.smartmove.server.content.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * 
 * @author Clint Gilbert
 * 
 * Apr 9, 2007
 *
 * Childrens Hospital Informatics Program at the
 * Harvard-MIT division of Health Sciences Technology
 * @link http://www.chip.org
 *
 * All works licensed by the Lesser GPL
 * @link http://www.gnu.org/licenses/lgpl.html
 * 
 * Portions of this code donated by Ana Holzbach, David Berkowicz, Ahmad Namani, Henry Chueh and the MGH Lab of Computer Science
 * @link http://www.lcs.mgh.harvard.edu/
 *
 * Methods to make dynamic instantiation of named classes easier. 
 *
 */
public abstract class ClassTools
{
    private static final Logger log = Logger.getLogger(ClassTools.class);

    private static final boolean DEBUG = log.isDebugEnabled();

    /**
     * 
     * @param <T>
     * @param clazz
     * @return
     * @throws DynamicLoadingException
     */
    public static final <T> T createInstance(final Class<T> clazz) throws DynamicLoadingException
    {
        try
        {
            return clazz.newInstance();
        }
        catch(Exception e)
        {
            throw new DynamicLoadingException("Error instantiating class '" + clazz.getName() + "':", e);
        }
    }

    /**
     * 
     * @param <T>
     * @param className
     * @param classToCastInto
     * @return
     * @throws DynamicLoadingException
     */
    public static final <T> T createInstance(final String className, final Class<T> classToCastInto) throws DynamicLoadingException
    {
        if(DEBUG)
        {
            log.debug("Loading '" + className + "', casting into '" + classToCastInto.getSimpleName() + "'");
        }

        try
        {
            return classToCastInto.cast(Class.forName(className).newInstance());
        }
        catch(Exception e)
        {
            throw new DynamicLoadingException("Error instantiating class '" + className + "':", e);
        }
    }

    /**
     * 
     * Convenience method to make a common case less verbose.
     * 
     * @param <T>
     * @param className
     * @param classToCastInto
     * @param config
     * @return
     * @throws DynamicLoadingException
     */
    public static final <T> T createInstance(final String className, final Class<T> classToCastInto, final Node config) throws DynamicLoadingException
    {
        return createInstance(className, classToCastInto, new Class[] { Node.class }, new Object[] { config });
    }

    /**
     * Convenience method for the case where we need to specify constructor params, but don't need to cast the result. 
     * @param <T>
     * @param clazz
     * @param constructorParamTypes
     * @param constructorParams
     * @return
     * @throws DynamicLoadingException
     */
    public static final <T> T createInstance(final Class<T> clazz, final Class<?>[] constructorParamTypes, final Object[] constructorParams) throws DynamicLoadingException
    {
        return createInstance(clazz.getName(), clazz, constructorParamTypes, constructorParams);
    }
    
    /**
     * 
     * Create an instance of a class, optionally cast it to a superclass, and return the instance.
     * 
     * @param <T>
     * @param className the fully-qualified anme of the class to instantiate 
     * @param classToCastInto the type to cast the instance to.  (May be the same as the instantiated class.)
     * @param constructorParamTypes the parameters forming the signature of the constructor to invoke.
     * @param constructorParams the parameters to pass when invoking the constructor.
     * @return
     * @throws DynamicLoadingException
     */
    public static final <T> T createInstance(final String className, final Class<T> classToCastInto, final Class<?>[] constructorParamTypes, final Object[] constructorParams) throws DynamicLoadingException
    {
        if(DEBUG)
        {
            log.debug("Loading '" + className + "', casting into '" + classToCastInto.getSimpleName() + "'");
        }

        try
        {
            final Constructor<?> constructor = Class.forName(className).getConstructor(constructorParamTypes);

            return classToCastInto.cast(constructor.newInstance(constructorParams));
        }
        catch(Exception e)
        {
            throw new DynamicLoadingException("Error instantiating class '" + className + "':", e);
        }
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeArrayList()} instead
     */
    @Deprecated
    public static <T> ArrayList<T> makeArrayList()
    {
        return Util.makeArrayList();
    }
    
    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeArrayList(Collection<? extends T>)} instead
     */
    @Deprecated
    public static <T> ArrayList<T> makeArrayList(final Collection<? extends T> collection)
    {
        return Util.makeArrayList(collection);
    }
    
    /**
     * Type-inferring convenience factory method
     * Makes a mutable ArrayList; the list produced
     * my Arrays.asList() is immutable.
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeArrayList(T...)} instead
     */
    @Deprecated
    public static <T> ArrayList<T> makeArrayList(final T ... values)
    {
        return Util.makeArrayList(values);
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @param initialCapacity
     * @return
     * @deprecated Use {@link Util#makeArrayList(int)} instead
     */
    @Deprecated
    public static <T> ArrayList<T> makeArrayList(final int initialCapacity)
    {
        return Util.makeArrayList(initialCapacity);
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeLinkedList()} instead
     */
    @Deprecated
    public static <T> LinkedList<T> makeLinkedList()
    {
        return Util.makeLinkedList();
    }

    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     * @deprecated Use {@link Util#makeHashMap()} instead
     */
    @Deprecated
    public static <K, V> HashMap<K,V> makeHashMap()
    {
        return Util.makeHashMap();
    }
    
    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     * @deprecated Use {@link Util#makeEnumMap()} instead
     */
    @Deprecated
    public static <K extends Enum<K>, V> HashMap<K,V> makeEnumMap()
    {
        return Util.makeEnumMap();
    }

    /**
     * Type-inferring convenience factory method
     * @param <K>
     * @param <V>
     * @return
     * @deprecated Use {@link Util#makeTreeMap()} instead
     */
    @Deprecated
    public static <K, V> TreeMap<K,V> makeTreeMap()
    {
        return Util.makeTreeMap();
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeTreeSet()} instead
     */
    @Deprecated
    public static <T> TreeSet<T> makeTreeSet()
    {
        return Util.makeTreeSet();
    }

    /**
     * Type-inferring convenience factory method
     * @param <T>
     * @return
     * @deprecated Use {@link Util#makeHashSet()} instead
     */
    @Deprecated
    public static <T> HashSet<T> makeHashSet()
    {
        return Util.makeHashSet();
    }

    /**
     * For convenience.  Returns a variable number of inputs as a Set.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     * @deprecated Use {@link Util#asSet(T...)} instead
     */
    @Deprecated
    public static <T> Set<T> asSet(final T ... values)
    {
        return Util.asSet(values);
    }
    
    /**
     * For convenience.  Returns a variable number of inputs as a Set.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     * @deprecated Use {@link Util#asSet(Collection<T>)} instead
     */
    @Deprecated
    public static <T> Set<T> asSet(final Collection<T> values)
    {
        return Util.asSet(values);
    }
    
    /**
     * For convenience.  Returns a variable number of inputs as an array.
     * Inputs must all be of the same type.
     * @param <T>
     * @param values
     * @return
     * @deprecated Use {@link Util#asArray(T...)} instead
     */
    @Deprecated
    public static <T> T[] asArray(final T ... values)
    {
        return Util.asArray(values);
    }

    /**
     * Convenience method.  Returns the values of a Set as a List
     * @param <T>
     * @param values
     * @return
     * @deprecated Use {@link Util#asList(Set<T>)} instead
     */
    @Deprecated
    public static <T> List<T> asList(final Set<T> values)
    {
        return Util.asList(values);
    }

    /**
     * Returns true if the parameter classes are all from the same package,
     * false otherwise.
     * 
     * @param classes
     * @return
     */
    public static final boolean allSamePackage(final Class<?> ... classes)
    {
        if(classes == null || classes.length < 1)
        {
            return false;
        }

        if(classes.length == 1)
        {
            return true;
        }

        final String packageName = classes[0].getPackage().getName();

        for(int i = 1; i < classes.length; i++)
        {
            if(!classes[i].getPackage().getName().equals(packageName))
            {
                return false;
            }
        }

        return true;
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
     * @deprecated Use {@link Util#mapsAreEqual(Map<K, V>,Map<K, V>)} instead
     */
    @Deprecated
    public static <K, V> boolean mapsAreEqual(final Map<K, V> lhs, final Map<K, V> rhs)
    {
        return Util.mapsAreEqual(lhs, rhs);
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
     * @deprecated Use {@link Util#range(int)} instead
     */
    @Deprecated
    public static final Util.Range range(final int max)
    {
        return Util.range(max);
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
     * @deprecated Use {@link Util#range(int,int)} instead
     */
    @Deprecated
    public static final Util.Range range(final int min, final int max)
    {
        return Util.range(min, max);
    }
}
