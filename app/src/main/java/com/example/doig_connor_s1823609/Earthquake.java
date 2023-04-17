package com.example.doig_connor_s1823609;
// Connor Doig S1823609
public class Earthquake {
    private String location;
    private String magnitude;
    private String depth;
    private String pubDate,link,title;

    public Earthquake(String title,String pubDate,String link,String location, String magnitude, String depth) {

        this.location = location;
        this.magnitude = magnitude;
        this.depth = depth;
        this.title=title;
        this.link=link;
        this.pubDate=pubDate;
    }
    public Earthquake(String location, String magnitude, String depth) {
        this.location = location;
        this.magnitude = magnitude;
        this.depth = depth;
    }

    public Earthquake( String depth,String magnitude) {

        this.magnitude = magnitude;
        this.depth = depth;
    }
    public String getLocation() {
        return location;
    }

    public String getDepth() {
        return depth;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }
}
