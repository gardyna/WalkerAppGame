package com.ru.andr.walkinggame.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ragnar on 10/24/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result implements Serializable{
    public Meta meta;
    public Map<String, String> content;
}
