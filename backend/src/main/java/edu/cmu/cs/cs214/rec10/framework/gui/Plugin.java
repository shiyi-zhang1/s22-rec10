package edu.cmu.cs.cs214.rec10.framework.gui;

public class Plugin {
    private final String name;
    private final String link;

    public Plugin(String name, String link){
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "{ \"name\": \"" + this.name + "\"," +
                " \"link\": \"" + this.link + "\"}";
    }
}
