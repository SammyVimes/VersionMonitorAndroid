package com.danilov.versionmonitor.model;

/**
 * Created by Semyon on 14.10.2015.
 */
public class Version {

    private String versionInteger;

    private String versionString;

    private String changes;

    public String getChanges() {
        return changes;
    }

    public void setChanges(final String changes) {
        this.changes = changes;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(final String versionString) {
        this.versionString = versionString;
    }

    public String getVersionInteger() {
        return versionInteger;
    }

    public void setVersionInteger(final String versionInteger) {
        this.versionInteger = versionInteger;
    }

}
