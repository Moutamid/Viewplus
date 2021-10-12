package com.moutamid.viewplussubsbooster.models;

public class LikeTaskModel {
    private String videoUrl, thumbnailUrl, posterUid, taskKey,
            totalLikesQuantity, completedDate;
    private int currentLikesQuantity;

    public LikeTaskModel(String videoUrl, String thumbnailUrl, String posterUid, String taskKey, String totalLikesQuantity, String completedDate, int currentLikesQuantity) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.posterUid = posterUid;
        this.taskKey = taskKey;
        this.totalLikesQuantity = totalLikesQuantity;
        this.completedDate = completedDate;
        this.currentLikesQuantity = currentLikesQuantity;
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

    public String getTotalLikesQuantity() {
        return totalLikesQuantity;
    }

    public void setTotalLikesQuantity(String totalLikesQuantity) {
        this.totalLikesQuantity = totalLikesQuantity;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public int getCurrentLikesQuantity() {
        return currentLikesQuantity;
    }

    public void setCurrentLikesQuantity(int currentLikesQuantity) {
        this.currentLikesQuantity = currentLikesQuantity;
    }

    public LikeTaskModel() {
    }
}
