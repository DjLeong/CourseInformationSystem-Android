package com.dehua.courseinformationsystem.data;

/**
 * Created by dehua on 16/3/8 008.
 */
public class AnnouncementBean {
    private String id;
    private String time;
    private String download;
    private String course;
    private String title;
    private String content;

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getDownload() {
        return download;
    }

    public String getCourse() {
        return course;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
