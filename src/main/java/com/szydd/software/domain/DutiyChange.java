package com.szydd.software.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("DutiyChange")
public class DutiyChange {
    final static public String collectionName = "DutiyChange";
    @Id
    private String _id;
    @Field
    private Long dutiyChangeId;
    @Field
    private Long associationId;
    @Field
    private String beforeUserId;
    @Field
    private String afterUserId;
    @Field
    private String reason;
    @Field
    private String status;

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

    public String getBeforeUserId() {
        return beforeUserId;
    }

    public void setBeforeUserId(String beforeUserId) {
        this.beforeUserId = beforeUserId;
    }

    public String getAfterUserId() {
        return afterUserId;
    }

    public void setAfterUserId(String afterUserId) {
        this.afterUserId = afterUserId;
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

    public Long getDutiyChangeId() {
        return dutiyChangeId;
    }

    public void setDutiyChangeId(Long dutiyChangeId) {
        this.dutiyChangeId = dutiyChangeId;
    }
}
