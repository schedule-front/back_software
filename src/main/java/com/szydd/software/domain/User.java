package com.szydd.software.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

//@EntityScan("User")

@Document(collection="User")
public class User implements Serializable {
    final public static String collectionName = "User";
    @Id
    private String _id;
    @Field
    private String gender;
    @Field
    private String userId;

    @Field
    private String password;

    @Field(value = "class")
    private String userClass;
    @Field
    private String major;
    @Field
    private String name;

    @Field
    private String eMail;

    @Field
    private String phone;

    @Field
    private String role;
//
//    @Field
//    public List<Long> announcements;

    @Field
    public List<Long> associations;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static String getCollectionName() {
        return collectionName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

//    public List<Long> getAnnouncements() {
//        return announcements;
//    }
//
//    public void setAnnouncements(List<Long> announcements) {
//        this.announcements = announcements;
//    }

    public List<Long> getAssociations() {
        return associations;
    }

    public void setAssociations(List<Long> associations) {
        this.associations = associations;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
