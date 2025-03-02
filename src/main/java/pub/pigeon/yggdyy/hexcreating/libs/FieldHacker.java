package pub.pigeon.yggdyy.hexcreating.libs;

import java.lang.reflect.Field;

public class FieldHacker {
    public static void hackFinalField(Object instance, String fieldName, Object newValue)
            throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        //Field modifiersField = Field.class.getDeclaredField("modifiers");
        //modifiersField.setAccessible(true);
        //modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(instance, newValue);
    }
}
