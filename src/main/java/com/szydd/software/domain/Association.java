package com.szydd.software.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(collection="Association")
public class Association implements Serializable {
    final public static String collectionName = "Association";
    @Id
    private String _id;

    @Field
    private Long associationId;

    @Field
    private String name;

    @Field
    private String introduction;

    @Field
    private Map<String,String> duties; // userId -> duityId
    @Field
    private Map<String,Set<String> > duityToUsers; // duity To Users

    @Field
    private List<String> members;

    @Field
    private List<String> announcements;

    @Field
    private List<String> accounts;

    @Field
    private List<String> awards;

    public String getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(String presidentId) {
        this.presidentId = presidentId;
    }

    private String presidentId;

    @Field
    private String logoUrl;


    @Field
    private Map<String,Double> stars;

    @Field
    private Long establishedTime;

    public Long getEstablishedTime() {
        return establishedTime;
    }

    public void setEstablishedTime(Long establishedTime) {
        this.establishedTime = establishedTime;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<String> getAnnouncements() {
        return announcements;
    }

    public List<String> getAccounts() {
        return accounts;
    }

//    public Long getPresidentId() {
//        return presidentId;
//    }
//
//    public void setPresidentId(Long presidentId) {
//        this.presidentId = presidentId;
//    }

    public Map<String, Double> getStars() {
        return stars;
    }

    public void setStars(Map<String, Double> stars) {
        this.stars = stars;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void setAnnouncements(List<String> announcements) {
        this.announcements = announcements;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }


    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getDuties() {
        return duties;
    }

    public void setDuties(Map<String, String> duties) {
        this.duties = duties;
    }

    public Map<String, Set<String> > getDuityToUsers() {
        return duityToUsers;
    }

    public void setDuityToUsers(Map<String, Set<String> > duityToUsers) {
        this.duityToUsers = duityToUsers;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
