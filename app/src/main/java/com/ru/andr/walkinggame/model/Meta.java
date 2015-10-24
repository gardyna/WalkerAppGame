package com.ru.andr.walkinggame.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by ragnar on 10/24/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta implements Serializable{
    public String status;
    public String message;
}
