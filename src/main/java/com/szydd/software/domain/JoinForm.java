package com.szydd.software.domain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document("JoinForm")
public class JoinForm {
    final static String collectionName = "JoinForm";
    final static public String[] statusType = new String[]{"check","pass","reject"};
    @Id
    private String _id;
    @Field
    private Long joinFormId;
    @Field
    private Long associationId;
    @Field
    private String userId;

    @Field
    private String reason;
    @Field
    private Long date;

    @Field
    private String interests;

    @Field
    private String experiences;

    @Field
    private String status;


    private String name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }
    public Long getJoinFormId() {
        return joinFormId;
    }

    public void setJoinFormId(Long joinFormId) {
        this.joinFormId = joinFormId;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
