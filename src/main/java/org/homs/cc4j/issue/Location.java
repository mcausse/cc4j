package org.homs.cc4j.issue;

import java.util.Stack;

public class Location {

    final String javaFileName;
    final Stack<String> path;

    public Location(Location location) {
        this.javaFileName = location.javaFileName;
        this.path = new Stack<>();
        this.path.addAll(location.path);
    }

    public Location(String javaFileName) {
        this.javaFileName = javaFileName;
        this.path = new Stack<>();
    }

    public void push(String s) {
        this.path.push(s);
    }

    public void pop() {
        this.path.pop();
    }

    public String getJavaFileName() {
        return javaFileName;
    }

    public Stack<String> getPath() {
        return path;
    }

    @Override
    public String toString() {
        if (path.isEmpty()) {
            return "[" + javaFileName + "]";
        } else {
            return "[" + javaFileName + "]: " + String.join(".", path);
        }
    }
}