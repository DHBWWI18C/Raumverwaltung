package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;


public class ContextRO
{
    protected final static String TYPENAME = "ContextRO";

    private Map<String, Object> additionalProperties = new HashMap<>();

    public ContextRO()
    {
        super();
    }

    public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    }

}
