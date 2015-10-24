package com.ru.andr.walkinggame.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by ragnar on 10/24/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable{
    public String username;
    public String password;
    public String email;
}
