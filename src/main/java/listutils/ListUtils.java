package listutils;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility class to work with lists using reflection
 * 
 * @author Tiago Cardoso dos Passos
 * @since 18/06/2020
 */
public class ListUtils {
    
    /**
     * Returns a list ordered by an object field
     * <p>
     * Example of use:</p>
     * {@code exampleList = ListSorter.sortByField(exampleList, "age")}
     *
     * @param list List to sort
     * @param fieldName Field name reference for sorting
     * @return Ordered list
     */
    public static <T> List<T> sortByField(List<T> list, String fieldName) {
        Field field = getListField(list, fieldName);
        if (field == null) return list;
        field.setAccessible(true);
        // Returns the sorted list
        return list.stream()
                .sorted(getComparator(field))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns the minimum value of a list element field
     * 
     * @param list List
     * @param fieldName Field name
     * @return Value
     */
    public static <T> T minByField(List<T> list, String fieldName) {
        Field field = getListField(list, fieldName);
        if (field == null) return null;
        field.setAccessible(true);
        // Returns the object with the lowest value for the given field
        return list.stream()
                .min(getComparator(field))
                .orElseGet(null);
    }
    
    /**
     * Returns the maximum value of a list element field
     * 
     * @param list List
     * @param fieldName Field name
     * @return Value
     */
    public static <T> T maxByField(List<T> list, String fieldName) {
        Field field = getListField(list, fieldName);
        if (field == null) return null;
        field.setAccessible(true);
        // Returns the object with the lowest value for the given field
        return list.stream()
                .max(getComparator(field))
                .orElseGet(null);
    }
    
    private static <T> Class getListGenericType(List<T> list) {
        Iterator it = list.iterator();
        if (it.hasNext()) {
            return it.next().getClass();
        }
        return null;
    }
    
    private static <T> Field getListField(List<T> list, String fieldName) {
        Class clazz = getListGenericType(list);
        if (clazz == null) return null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (!field.getType().isPrimitive() &&
                    !Comparable.class.isAssignableFrom(field.getType())) {
                throw new IllegalArgumentException("'Type '" + field.getType().getSimpleName() + "' does not implement the Comparable interface");
            }
            return field;
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(ListUtils.class.getName()).log(Level.FINE, null, ex);
            return null;
        }
    }
    
    private static <T> Comparator<T> getComparator(Field field) {
        return (T first, T second) -> {
            try {
                Comparable a = (Comparable) field.get(first);
                Comparable b = (Comparable) field.get(second);
                return a.compareTo(b);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ListUtils.class.getName()).log(Level.FINE, null, ex);
            }
            return 0;
        };
    }
    
}
