package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;

public class OutputSpeechRO
{
    protected final static String TYPENAME = "IntentRO";

    private String type;
    private String text;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public OutputSpeechRO()
    {
        super();
    }

    public OutputSpeechRO(String type, String text)
    {
        super();
        this.type = type;
        this.text = text;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
