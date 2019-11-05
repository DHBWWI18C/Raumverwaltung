package com.Mosbach.Raumverwaltung.Alexa;

import java.util.HashMap;
import java.util.Map;

public class ResponseRO
{
    protected final static String TYPENAME = "ResponseRO";

    private OutputSpeechRO outputSpeech;
    private Boolean shouldEndSession;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public ResponseRO()
    {
        super();
    }

    public ResponseRO(OutputSpeechRO outputSpeech, Boolean shouldEndSession)
    {
        super();
        this.outputSpeech = outputSpeech;
        this.shouldEndSession = shouldEndSession;
    }
    
    public OutputSpeechRO getOutputSpeech() {
        return outputSpeech;
    }

    public void setOutputSpeech(OutputSpeechRO outputSpeech) {
        this.outputSpeech = outputSpeech;
    }

    public Boolean getShouldEndSession() {
        return shouldEndSession;
    }

    public void setShouldEndSession(Boolean shouldEndSession) {
        this.shouldEndSession = shouldEndSession;
    }
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
