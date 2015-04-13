package com.hasgeek.zalebi.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
@Parcel
public class Session {

    @Expose
    public String description;
    @SerializedName("description_text")
    @Expose
    public String descriptionText;
    @Expose
    public String end;
    @SerializedName("feedback_url")
    @Expose
    public String feedbackUrl;
    @Expose
    public Integer id;
    @SerializedName("is_break")
    @Expose
    public Boolean isBreak;
    @SerializedName("json_url")
    @Expose
    public String jsonUrl;
    @Expose
    public Integer proposal;
    @Expose
    public String room;
    @SerializedName("section_name")
    @Expose
    public String sectionName;
    @SerializedName("section_title")
    @Expose
    public String sectionTitle;
    @Expose
    public String speaker;
    @SerializedName("speaker_bio")
    @Expose
    public String speakerBio;
    @SerializedName("speaker_bio_text")
    @Expose
    public String speakerBioText;
    @Expose
    public String start;
    @SerializedName("technical_level")
    @Expose
    public String technicalLevel;
    @Expose
    public String title;
    @Expose
    public String url;

    public String space_id;

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsBreak() {
        return isBreak;
    }

    public void setIsBreak(Boolean isBreak) {
        this.isBreak = isBreak;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public Integer getProposal() {
        return proposal;
    }

    public void setProposal(Integer proposal) {
        this.proposal = proposal;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSpeakerBio() {
        return speakerBio;
    }

    public void setSpeakerBio(String speakerBio) {
        this.speakerBio = speakerBio;
    }

    public String getSpeakerBioText() {
        return speakerBioText;
    }

    public void setSpeakerBioText(String speakerBioText) {
        this.speakerBioText = speakerBioText;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTechnicalLevel() {
        return technicalLevel;
    }

    public void setTechnicalLevel(String technicalLevel) {
        this.technicalLevel = technicalLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
