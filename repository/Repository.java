package repository;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Generic in-memory repository that discovers entity IDs using @Id. */
public class Repository<T> {
    private final HashMap<String, T> entities = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String id = extractId(entity);
        entities.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final ArrayList<T> saveAll(T... items) {
        ArrayList<T> saved = new ArrayList<>();
        if (items == null) {
            return saved;
        }
        for (T item : items) {
            saved.add(save(item));
        }
        return saved;
    }

    public T findById(String id) {
        return entities.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean delete(String id) {
        return entities.remove(id) != null;
    }

    private String extractId(T entity) {
        Class<?> type = entity.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null || value.toString().trim().isEmpty()) {
                            throw new IllegalArgumentException("@Id field cannot be null or empty");
                        }
                        return value.toString();
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot read @Id field: " + field.getName(), e);
                    }
                }
            }
            type = type.getSuperclass();
        }
        throw new IllegalArgumentException("No field marked with @Id found on " + entity.getClass().getName());
    }

    /** Calculates the arithmetic mean of a numeric array. */
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be null or empty");
        }
        double total = 0.0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }
}
