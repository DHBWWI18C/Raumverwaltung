package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;

public class RequestRO
{
    protected final static String TYPENAME = "RequestRO";

    private String type;
    private String requestId;
    private IntentRO intent;
    private String locale;
    private String timestamp;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public RequestRO()
    {
        super();
    }

    public RequestRO(String type, String requestId, IntentRO intent, String locale, String timestamp)
    {
        super();
        this.type = type;
        this.requestId = requestId;
        this.intent = intent;
        this.locale = locale;
        this.timestamp = timestamp;
    }
 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public IntentRO getIntent() {
        return intent;
    }

    public void setIntent(IntentRO intent) {
        this.intent = intent;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
