package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;


public class AlexaRO
{
    protected final static String TYPENAME = "Alexa";
    
    private RequestRO request;
    private ResponseRO response;
    private String version;
    
    private SessionRO session;
    private ContextRO context;

    private Map<String, Object> additionalProperties = new HashMap<>();
    
    public AlexaRO()
    {
        super();
    }

    public AlexaRO(String version)
    {
        super();
    }
    
    public RequestRO getRequest() {
        return request;
    }

    public void setRequest(RequestRO request) {
        this.request = request;
    }
    
    public ResponseRO getResponse() {
        return response;
    }

    public void setResponse(ResponseRO response) {
        this.response = response;
    }
    
    public String getVersion() {
    return version;
    }

    public void setVersion(String version) {
    this.version = version;
    }

    public SessionRO getSession() {
    return session;
    }

    public void setSession(SessionRO session) {
    this.session = session;
    }

    public ContextRO getContext() {
    return context;
    }

    public void setContext(ContextRO context) {
    this.context = context;
    }

    public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
    }

}


// @JsonRawValue public String version;
// private String version;
// private boolean shouldEndSession;
// private String outputSpeech;



