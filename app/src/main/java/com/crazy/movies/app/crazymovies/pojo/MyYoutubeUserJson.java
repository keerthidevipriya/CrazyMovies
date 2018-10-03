package com.crazy.movies.app.crazymovies.pojo;

public class MyYoutubeUserJson {
    String id, language, country, videoKey, name, size, site, videoUrl, type;

    public MyYoutubeUserJson(String id, String language, String country, String videoKey, String name, String size, String site, String videoUrl, String type) {
        this.id = id;
        this.language = language;
        this.country = country;
        this.videoKey = videoKey;
        this.name = name;
        this.size = size;
        this.site = site;
        this.videoUrl = videoUrl;
        this.type = type;
    }

    public MyYoutubeUserJson(String sid, String svideoKey, String sname, String svideoUrl) {
        this.id = sid;
        this.videoKey = svideoKey;
        this.name = sname;
        this.videoUrl = svideoUrl;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSite() {
        return site;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getType() {
        return type;
    }
}
