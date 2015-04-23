package testing.utils;

import java.lang.reflect.Field;

/**
 * Created on 4/23/2015.
 */
public class ReflectionUtilities {
    /**
     * Returns an accessible field from a class.
     * @param propertyName Property/Field name of class.
     * @param targetClass Class from which to acquire a field.
     * @return A field which was made accessible.
     * @throws NoSuchFieldException
     */
    public static Field makeFieldAccessible(String propertyName, Class<?> targetClass) throws NoSuchFieldException {
        Field commandDefault;
        commandDefault = targetClass.getDeclaredField(propertyName);
        commandDefault.setAccessible(true);
        return commandDefault;
    }
}
