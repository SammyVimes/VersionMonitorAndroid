package com.danilov.versionmonitor.model;

/**
 * Created by Semyon on 14.10.2015.
 */
public class Project {

    private long id;

    private String packageName;

    private String name;

    private String lastVersionString;

    private int lastVersionInt;

    private String definition;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(final String definition) {
        this.definition = definition;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLastVersionString() {
        return lastVersionString;
    }

    public void setLastVersionString(final String lastVersionString) {
        this.lastVersionString = lastVersionString;
    }

    public int getLastVersionInt() {
        return lastVersionInt;
    }

    public void setLastVersionInt(final int lastVersionInt) {
        this.lastVersionInt = lastVersionInt;
    }
}
