package com.danilov.versionmonitor.model;

import java.util.List;

/**
 * Created by Semyon on 14.10.2015.
 */
public class ProjectDetails {

    private Project project;

    private List<Version> versions;

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(final List<Version> versions) {
        this.versions = versions;
    }

}