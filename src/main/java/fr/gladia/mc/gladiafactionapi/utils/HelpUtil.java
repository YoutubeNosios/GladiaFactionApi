package fr.gladia.mc.gladiafactionapi.utils;




import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import com.google.common.collect.Lists;
public final class HelpUtil {
    private static final String NMS_PATH = "net.minecraft.server.[VERSION]";
    private static final String BUKKIT_PATH = "org.bukkit.craftbukkit.[VERSION]";
    public static final ClassPacket NMS = new ClassPacket(NMS_PATH);
    public static final ReflectBukkitPacket BUKKIT = new ReflectBukkitPacket(BUKKIT_PATH);
    public static final MethodManager METHODS = new MethodManager();
    public static final FieldManager FIELDS = new FieldManager();
    public static final ObjectManager OBJECTS = new ObjectManager();
    public static final class ReflectBukkitPacket extends ClassPacket {
        public final HelpUtil.ClassPacket BLOCK;
        public final HelpUtil.ClassPacket BOSSBAR;
        public final HelpUtil.ReflectBukkitPacketCommand COMMAND;
        public final HelpUtil.ClassPacket ENCHANT;
        public final HelpUtil.ClassPacket ENTITY;
        public final HelpUtil.ClassPacket EVENT;
        public final HelpUtil.ClassPacket GENERATOR;
        public final HelpUtil.ClassPacket HELP;
        public final HelpUtil.ClassPacket INVENTORY;
        public final HelpUtil.ClassPacket MAP;
        public final HelpUtil.ClassPacket METADATA;
        public final HelpUtil.ClassPacket POTION;
        public final HelpUtil.ClassPacket PROJECTILES;
        public final HelpUtil.ClassPacket SCHEDULER;
        public final HelpUtil.ClassPacket SCOREBOARD;
        public final HelpUtil.ReflectBukkitPacketUtil UTIL;
        ReflectBukkitPacket(final String arg0) {
            super(arg0);
            this.BLOCK = new HelpUtil.ClassPacket(this.path + ".block");
            this.BOSSBAR = new HelpUtil.ClassPacket(this.path + ".boss");
            this.COMMAND = new HelpUtil.ReflectBukkitPacketCommand(this.path + ".command");
            this.ENCHANT = new HelpUtil.ClassPacket(this.path + ".enchantments");
            this.ENTITY = new HelpUtil.ClassPacket(this.path + ".entity");
            this.EVENT = new HelpUtil.ClassPacket(this.path + ".event");
            this.GENERATOR = new HelpUtil.ClassPacket(this.path + ".generator");
            this.HELP = new HelpUtil.ClassPacket(this.path + ".help");
            this.INVENTORY = new HelpUtil.ClassPacket(this.path + ".inventory");
            this.MAP = new HelpUtil.ClassPacket(this.path + ".map");
            this.METADATA = new HelpUtil.ClassPacket(this.path + ".metadata");
            this.POTION = new HelpUtil.ClassPacket(this.path + ".potion");
            this.PROJECTILES = new HelpUtil.ClassPacket(this.path + ".projectiles");
            this.SCHEDULER = new HelpUtil.ClassPacket(this.path + ".scheduler");
            this.SCOREBOARD = new HelpUtil.ClassPacket(this.path + ".scoreboard");
            this.UTIL = new HelpUtil.ReflectBukkitPacketUtil(this.path + ".util");
        }
        @Override
        public String toString() {
            return this.path;
        }
    }
    public static String getPackageVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
    public static int getServerVersion() {
        return Integer.valueOf(String.valueOf(getPackageVersion().split("_")[1])).intValue();
    }
    public static int getReleaseVersion() {
        return Integer.valueOf(String.valueOf(getPackageVersion().split("_")[2].substring(1))).intValue();
    }
    public static boolean instanceOf(final Object arg0, final Class<?> arg1) {
        return arg1.isInstance(arg0);
    }
    public static ClassPacket getPackage(final String arg0) {
        return new ClassPacket(arg0);
    }
    public static final class ReflectBukkitPacketCommand extends ClassPacket {
        public final ClassPacket DEFAULTS;
        ReflectBukkitPacketCommand(final String arg0) {
            super(arg0);
            this.DEFAULTS = new HelpUtil.ClassPacket(this + ".defaults");
        }
    }
    public static final class ReflectBukkitPacketUtil extends ClassPacket {
        public final HelpUtil.ClassPacket PERMISSION;
        ReflectBukkitPacketUtil(final String arg0) {
            super(arg0);
            this.PERMISSION = new HelpUtil.ClassPacket(this + ".permissions");
        }
    }
    public static class ClassPacket {
        protected final String path;
        ClassPacket(final String arg0) {
            this.path = arg0.replace("[VERSION]", HelpUtil.getPackageVersion());
        }
        public synchronized boolean hasClass(final String className) {
            return this.getClass(className) != null;
        }
        public synchronized Class<?> getClass(final String className) {
            try {
                if (className.contains(".")) {
                    final String[] strings = className.replace(".", "@").split("@");
                    final String[] raw = new String[strings.length - 1];
                    for (int i = 1; i < strings.length; i++) {
                        raw[i - 1] = strings[i];
                    }
                    Class<?> target = this.getClass(strings[0]);
                    Class<?> searched = null;
                    final List<Class<?>> classes = Lists.newArrayList();
                    classes.addAll(Arrays.asList(target.getClasses()));
                    classes.addAll(Arrays.asList(target.getDeclaredClasses()));
                    if (target != null) {
                        int index = 0;
                        final Iterator<Class<?>> localIterator = classes.iterator();
                        for (; searched == null && index < raw.length; localIterator.hasNext()) {
                            if (classes.size() < 1 || index >= raw.length) {
                                break;
                            }
                            final Class<?> class1 = localIterator.next();
                            if (class1.getSimpleName().equals(raw[index])) {
                                if (index == raw.length - 1) {
                                    searched = class1;
                                } else {
                                    index++;
                                    target = class1;
                                }
                            }
                        }
                    }
                    return searched;
                }
                return Class.forName(this.path + "." + className);
            }
            catch (final Exception localException) {}
            return null;
        }
        public Object getClassEnum(final String arg0, final String arg1) {
            final Class<?> clazz = this.getClass(arg0);
            if (clazz != null && clazz.isEnum()) {
                for (final Object object : clazz.getEnumConstants()) {
                    if (object.toString().equalsIgnoreCase(arg1)) {
                        return object;
                    }
                }
            }
            return null;
        }
        @Override
        public String toString() {
            return this.path;
        }
    }
    public static final class MethodManager {
        private synchronized Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
            final Class<?>[] primitiveTypes = Data.getPrimitive(parameterTypes);
            Method method = null;
            for (final Method method2 : clazz.getDeclaredMethods()) {
                if (Data.compare(Data.getPrimitive(method2.getParameterTypes()), primitiveTypes) && method2.getName().equals(methodName)) {
                    method2.setAccessible(true);
                    return method2;
                }
            }
            if (method == null && clazz.getSuperclass() != null) {
                method = this.getMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }
            return method;
        }
        public synchronized Object invoke(final Object instance, final String methodName, final Object... parameterTypes) {
            if (instance != null) {
                final Method method = this.getMethod(instance.getClass(), methodName, Data.getPrimitive(parameterTypes));
                if (method != null) {
                    this.setMethodModifiers(method, Modifiers.PUBLIC);
                    try {
                        return method.invoke(instance, parameterTypes);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        public synchronized Object invokeStatic(final Class<?> instance, final String methodName, final Object... parameterTypes) {
            final Method method = this.getMethod(instance, methodName, Data.getPrimitive(parameterTypes));
            if (method != null && Modifier.isStatic(method.getModifiers())) {
                this.setMethodModifiers(method, Modifiers.PUBLIC, Modifiers.STATIC);
                try {
                    return method.invoke(null, parameterTypes);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        private synchronized void setMethodModifiers(final Method method, final Modifiers... modifiers) {
            try {
                final Field modifiersField = Method.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                method.setAccessible(true);
                int value = 0;
                for (final Modifiers modifier : modifiers) {
                    value += modifier.getValue();
                }
                modifiersField.setInt(method, value);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static final class FieldManager {
        private synchronized Field getField(final Class<?> clazz, final String fieldName) {
            Field field = null;
            for (final Field field2 : clazz.getDeclaredFields()) {
                if (field2.getName().equals(fieldName)) {
                    field2.setAccessible(true);
                    field = field2;
                }
            }
            if (clazz.getSuperclass() != null && field == null) {
                field = this.getField(clazz.getSuperclass(), fieldName);
            }
            return field;
        }
        private List<Field> getFields(final Class<?> clazz, final Object instance) {
            final List<Field> fields = Lists.newArrayList();
            for (final Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (Modifier.isStatic(field.getModifiers())) {
                    this.setFieldModifiers(field, Modifiers.PUBLIC, Modifiers.STATIC);
                    try {
                        fields.add(field);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.setFieldModifiers(field, Modifiers.PUBLIC);
                    try {
                        fields.add(field);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (clazz.getSuperclass() != null) {
                fields.addAll(this.getFields(clazz.getSuperclass(), instance));
            }
            return fields;
        }
        private synchronized Field getField(final Class<?> clazz, final Object instance, final Class<?> fieldType) {
            for (final Field field : this.getFields(clazz, instance)) {
                if (field.getType().equals(fieldType)) {
                    return field;
                }
            }
            return null;
        }
        public synchronized Object getValue(final Object instance, final Class<?> fieldType) {
            return this.getValue(instance.getClass(), instance, fieldType);
        }
        public synchronized Object getValue(final Class<?> clazz, final Object instance, final Class<?> fieldType) {
            final Field field = this.getField(clazz, instance, fieldType);
            if (field != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    this.setFieldModifiers(field, Modifiers.PUBLIC, Modifiers.STATIC);
                    try {
                        return field.get(null);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.setFieldModifiers(field, Modifiers.PUBLIC);
                    try {
                        return field.get(instance);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (clazz.getSuperclass() != null) {
                return this.getValue(clazz.getSuperclass(), instance, fieldType);
            }
            return null;
        }
        public synchronized void setValue(final Object instance, final Class<?> fieldType, final Object value) {
            this.setValue(instance.getClass(), instance, fieldType, value);
        }
        public synchronized void setValue(final Class<?> clazz, final Object instance, final Class<?> fieldType, final Object value) {
            final Field field = this.getField(clazz, instance, fieldType);
            if (field != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    this.setFieldModifiers(field, Modifiers.PUBLIC, Modifiers.STATIC);
                    try {
                        field.set(null, value);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.setFieldModifiers(field, Modifiers.PUBLIC);
                    try {
                        field.set(instance, value);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (clazz.getSuperclass() != null && field == null) {
                this.setValue(clazz.getSuperclass(), instance, fieldType, value);
            }
        }
        public synchronized Object getValue(final Object instance, final String fieldName) {
            return this.getValue(instance.getClass(), instance, fieldName);
        }
        public synchronized Object getValue(final Class<?> clazz, final Object instance, final String fieldName) {
            final Field field = this.getField(clazz, fieldName);
            if (field != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    this.setFieldModifiers(field, Modifiers.PUBLIC, Modifiers.STATIC);
                    try {
                        return field.get(null);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.setFieldModifiers(field, Modifiers.PUBLIC);
                    try {
                        return field.get(instance);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (clazz.getSuperclass() != null) {
                return this.getValue(clazz.getSuperclass(), instance, fieldName);
            }
            return null;
        }
        public synchronized void setValue(final Object instance, final String fieldName, final Object value) {
            this.setValue(instance.getClass(), instance, fieldName, value);
        }
        public synchronized void setValue(final Class<?> clazz, final Object instance, final String fieldName, final Object value) {
            final Field field = this.getField(clazz, fieldName);
            if (field != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    this.setFieldModifiers(field, Modifiers.PUBLIC, Modifiers.STATIC);
                    try {
                        field.set(null, value);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    this.setFieldModifiers(field, Modifiers.PUBLIC);
                    try {
                        field.set(instance, value);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (clazz.getSuperclass() != null && field == null) {
                this.setValue(clazz.getSuperclass(), instance, fieldName, value);
            }
        }
        private synchronized void setFieldModifiers(final Field field, final Modifiers... modifiers) {
            try {
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                field.setAccessible(true);
                int value = 0;
                for (final Modifiers modifier : modifiers) {
                    value += modifier.getValue();
                }
                modifiersField.setInt(field, value);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static final class ObjectManager {
        private synchronized Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes) {
            final Class<?>[] primitiveTypes = Data.getPrimitive(parameterTypes);
            for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (Data.compare(Data.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                    constructor.setAccessible(true);
                    return constructor;
                }
            }
            return null;
        }
        public synchronized Object getObject(final Class<?> clazz, final Object... arguments) {
            try {
                final Constructor<?> con = this.getConstructor(clazz, Data.getPrimitive(arguments));
                if (con != null) {
                    this.setObjectModifiers(con, Modifiers.PUBLIC);
                    return con.newInstance(arguments);
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        public synchronized Object[] getObjectArray(final Object... values) {
            try {
                if (values != null && values.length >= 1) {
                    final Object[] array = (Object[])Array.newInstance(values[0].getClass(), values.length);
                    for (int i = 0; i < values.length; i++) {
                        Array.set(array, i, values[i]);
                    }
                    return array;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        private synchronized void setObjectModifiers(final Constructor<?> constructor, final Modifiers... modifiers) {
            try {
                final Field modifiersField = Constructor.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                constructor.setAccessible(true);
                int value = 0;
                for (final Modifiers modifier : modifiers) {
                    value += modifier.getValue();
                }
                modifiersField.setInt(constructor, value);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static enum Modifiers {
        ABSTRACT(1024),
        FINAL(16),
        INTERFACE(512),
        NATIVE(256),
        PRIVATE(2),
        PROTECTED(4),
        PUBLIC(1),
        STATIC(8),
        STRICT(2048),
        SYNCHRONIZED(32),
        TRANSIENT(128),
        VOLATILE(64);
        private final int value;
        private Modifiers(final int arg0) {
            this.value = arg0;
        }
        public int getValue() {
            return this.value;
        }
        public boolean isPublic(final Field arg0) {
            return arg0.getModifiers() == PUBLIC.value;
        }
        public boolean isPrivate(final Field arg0) {
            return arg0.getModifiers() == PRIVATE.value;
        }
        public boolean isProtected(final Field arg0) {
            return arg0.getModifiers() == PROTECTED.value;
        }
        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }
    protected static enum Data {
        BYTE(Byte.TYPE, Byte.class),
        SHORT(Short.TYPE, Short.class),
        INTEGER(Integer.TYPE, Integer.class),
        LONG(Long.TYPE, Long.class),
        CHARACTER(Character.TYPE, Character.class),
        FLOAT(Float.TYPE, Float.class),
        DOUBLE(Double.TYPE, Double.class),
        BOOLEAN(Boolean.TYPE, Boolean.class);
        private static Map<Class<?>, Data> CLASS_MAP;
        private final Class<?> primitive;
        private final Class<?> reference;
        static {
            CLASS_MAP = new HashMap<Class<?>, Data>();
            for (final Data type : values()) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }
        private Data(final Class<?> primitive, final Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }
        public Class<?> getPrimitive() {
            return this.primitive;
        }
        public Class<?> getReference() {
            return this.reference;
        }
        public static Data fromClass(final Class<?> clazz) {
            return CLASS_MAP.get(clazz);
        }
        public static Class<?> getPrimitive(final Class<?> clazz) {
            final Data type = fromClass(clazz);
            return type == null ? clazz : type.getPrimitive();
        }
        public static Class<?> getReference(final Class<?> clazz) {
            final Data type = fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }
        public static Class<?>[] getPrimitive(final Class<?>[] classes) {
            final int length = classes == null ? 0 : classes.length;
            final Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }
        public static Class<?>[] getReference(final Class<?>[] classes) {
            final int length = classes == null ? 0 : classes.length;
            final Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }
        public static Class<?>[] getPrimitive(final Object[] objects) {
            final int length = objects == null ? 0 : objects.length;
            final Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }
        public static Class<?>[] getReference(final Object[] objects) {
            final int length = objects == null ? 0 : objects.length;
            final Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }
        public static boolean compare(final Class<?>[] primary, final Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0; index < primary.length; index++) {
                final Class<?> primaryClass = primary[index];
                final Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass)) {
                    return false;
                }
            }
            return true;
        }
    }
}