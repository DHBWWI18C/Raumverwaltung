package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;

public class IntentRO
{
    protected final static String TYPENAME = "IntentRO";
    
    private String name;
    
    // @JsonProperty("slots")
    // private SlotsRO slots;
    // So müsste es weitergehen, wenn der Intent Slots hat
    //
    
    private Map<String, Object> additionalProperties = new HashMap<>();

    public IntentRO()
    {
        super();
    }

    public IntentRO(String name) {
        super();
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
