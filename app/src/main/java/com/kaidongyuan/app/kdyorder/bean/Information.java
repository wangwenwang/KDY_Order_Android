package com.kaidongyuan.app.kdyorder.bean;

public class Information implements java.io.Serializable {
    private String IID;
    private String ITitle;
    private String url;
    private String IImage;
    private String IAddTime;
    private String IFrom;
    private String IContent;
    private String IReadCount;

    public Information() {
    }

    public String getIID() {
        return IID;
    }

    public void setIID(String IID) {
        this.IID = IID;
    }

    public String getIAddTime() {
        return IAddTime;
    }

    public void setIAddTime(String IAddTime) {
        this.IAddTime = IAddTime;
    }

    public String getIFrom() {
        return IFrom;
    }

    public void setIFrom(String IFrom) {
        this.IFrom = IFrom;
    }

    public String getIContent() {
        return IContent;
    }

    public void setIContent(String IContent) {
        this.IContent = IContent;
    }

    public String getIReadCount() {
        return IReadCount;
    }

    public void setIReadCount(String IReadCount) {
        this.IReadCount = IReadCount;
    }

    public String getITitle() {
        return ITitle;
    }

    public void setITitle(String ITitle) {
        this.ITitle = ITitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIImage() {
        return IImage;
    }

    public void setIImage(String IImage) {
        this.IImage = IImage;
    }

    @Override
    public String toString() {
        return "Information{" +
                "IID='" + IID + '\'' +
                ", ITitle='" + ITitle + '\'' +
                ", url='" + url + '\'' +
                ", IImage='" + IImage + '\'' +
                ", IAddTime='" + IAddTime + '\'' +
                ", IFrom='" + IFrom + '\'' +
                ", IContent='" + IContent + '\'' +
                ", IReadCount='" + IReadCount + '\'' +
                '}';
    }
}
