package com.moutamid.viewplussubsbooster.models;

public class SubscribeTaskModel {
    private String videoUrl, thumbnailUrl, posterUid, taskKey,
            totalSubscribesQuantity, completedDate;
    private int currentSubscribesQuantity;

    public SubscribeTaskModel(String videoUrl, String thumbnailUrl, String posterUid, String taskKey, String totalSubscribesQuantity, String completedDate, int currentSubscribesQuantity) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.posterUid = posterUid;
        this.taskKey = taskKey;
        this.totalSubscribesQuantity = totalSubscribesQuantity;
        this.completedDate = completedDate;
        this.currentSubscribesQuantity = currentSubscribesQuantity;
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

    public String getTotalSubscribesQuantity() {
        return totalSubscribesQuantity;
    }

    public void setTotalSubscribesQuantity(String totalSubscribesQuantity) {
        this.totalSubscribesQuantity = totalSubscribesQuantity;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public int getCurrentSubscribesQuantity() {
        return currentSubscribesQuantity;
    }

    public void setCurrentSubscribesQuantity(int currentSubscribesQuantity) {
        this.currentSubscribesQuantity = currentSubscribesQuantity;
    }

    public SubscribeTaskModel() {
    }
}
