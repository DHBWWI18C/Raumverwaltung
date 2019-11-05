package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;


public class SlotsRO
{
    protected final static String TYPENAME = "SlotRO";

    private Map<String, Object> additionalProperties = new HashMap<>();

    public SlotsRO()
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
