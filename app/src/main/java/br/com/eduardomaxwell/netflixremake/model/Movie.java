package br.com.eduardomaxwell.netflixremake.model;

public class Movie {

    private int id;
    private String coverUrl;
    private String title;
    private String desc;
    private String cast;

    public Movie(int id, String desc, String coverUrl, String title, String cast) {
        this.id = id;
        this.desc = desc;
        this.coverUrl = coverUrl;
        this.title = title;
        this.cast = cast;
    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
