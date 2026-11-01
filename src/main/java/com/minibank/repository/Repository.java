package com.minibank.repository;

import com.minibank.model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Repository<T> {
    private final Map<String, T> entities;

    public Repository() {
        this(new HashMap<>());
    }

    public Repository(Map<String, T> entities) {
        this.entities = Objects.requireNonNull(entities, "entities");
    }

    public T save(T entity) {
        Objects.requireNonNull(entity, "entity");
        entities.put(idOf(entity), entity);
        return entity;
    }

    @SafeVarargs
    public final ArrayList<T> saveAll(T... items) {
        ArrayList<T> saved = new ArrayList<>(items.length);
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

    public int size() {
        return entities.size();
    }

    private String idOf(T entity) {
        for (Class<?> type = entity.getClass(); type != null && type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null) {
                            throw new IllegalArgumentException("@Id field cannot be null");
                        }
                        return value.toString();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Unable to read @Id field", exception);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Entity type has no @Id field: " + entity.getClass().getName());
    }
}