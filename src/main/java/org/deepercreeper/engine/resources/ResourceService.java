package org.deepercreeper.engine.resources;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResourceService {
    private final Map<Resource<?>, Object> cache = new HashMap<>();

    public <T> T load(Resource<T> resource) {
        Object cachedValue = cache.get(resource);
        if (cachedValue != null) {
            return resource.cast(cachedValue);
        }
        T value = resource.load();
        cache.put(resource, value);
        return value;
    }

    public <T> void save(T value, WritableResource<T> resource) {
        cache.put(resource, value);
        resource.save(value);
    }
}
