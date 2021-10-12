package com.moutamid.viewplussubsbooster.models;

public class ViewTaskModel {
     String videoUrl, thumbnailUrl, posterUid, taskKey,
            totalViewsQuantity, totalViewTimeQuantity, completedDate;
     int currentViewsQuantity;

    public ViewTaskModel(String videoUrl, String thumbnailUrl, String posterUid, String taskKey, String totalViewsQuantity, String totalViewTimeQuantity, String completedDate, int currentViewsQuantity) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.posterUid = posterUid;
        this.taskKey = taskKey;
        this.totalViewsQuantity = totalViewsQuantity;
        this.totalViewTimeQuantity = totalViewTimeQuantity;
        this.completedDate = completedDate;
        this.currentViewsQuantity = currentViewsQuantity;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPosterUid() {
        return posterUid;
    }

    public void setPosterUid(String posterUid) {
        this.posterUid = posterUid;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTotalViewsQuantity() {
        return totalViewsQuantity;
    }

    public void setTotalViewsQuantity(String totalViewsQuantity) {
        this.totalViewsQuantity = totalViewsQuantity;
    }

    public String getTotalViewTimeQuantity() {
        return totalViewTimeQuantity;
    }

    public void setTotalViewTimeQuantity(String totalViewTimeQuantity) {
        this.totalViewTimeQuantity = totalViewTimeQuantity;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public int getCurrentViewsQuantity() {
        return currentViewsQuantity;
    }

    public void setCurrentViewsQuantity(int currentViewsQuantity) {
        this.currentViewsQuantity = currentViewsQuantity;
    }

    public ViewTaskModel() {
    }
}
