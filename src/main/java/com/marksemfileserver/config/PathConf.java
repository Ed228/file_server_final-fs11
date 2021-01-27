package com.marksemfileserver.config;

import java.nio.file.Path;

public class PathConf {

    private Path path;

    public PathConf(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
