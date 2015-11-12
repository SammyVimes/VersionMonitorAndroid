package com.danilov.versionmonitor.model;

import java.util.List;

/**
 * Created by semyon on 11.11.15.
 */
public class IndexResponse {

    private List<Project> projects;

    private IndexResult error;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(final List<Project> projects) {
        this.projects = projects;
    }

    public IndexResult getIndexResult() {
        return error;
    }

    public void setIndexResult(final IndexResult indexResult) {
        this.error = indexResult;
    }

    public enum IndexResult {

        NOT_AUTHORIZED(""),
        NOT_PARTICIPATING_IN_PROJECTS("Нет проектов");

        private String def;

        IndexResult(final String def) {
            this.def = def;
        }

        public String getDef() {
            return def;
        }

    }



}
