package org.example.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {
    private final Map<String, Object> data = new HashMap<>();

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(data.get(key));
    }

    public void clear() {
        data.clear();
    }
}