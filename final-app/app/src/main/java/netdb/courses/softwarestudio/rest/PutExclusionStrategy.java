package netdb.courses.softwarestudio.rest;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import netdb.courses.softwarestudio.rest.model.Putable;

public class PutExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Putable.class) == null;
    }
}

