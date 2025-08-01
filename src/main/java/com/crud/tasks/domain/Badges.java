package com.crud.tasks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Badges {

    @JsonProperty("attachmentsByType")
    private AttachmentsByType attachmentsByType;

    @JsonProperty("location")
    private boolean location;

    @JsonProperty("votes")
    private int votes;

    @JsonProperty("viewingMemberVoted")
    private boolean viewingMemberVoted;

    @JsonProperty("subscribed")
    private boolean subscribed;

    @JsonProperty("fogbugz")
    private String fogbugz;

    @JsonProperty("checkItems")
    private int checkItems;

    @JsonProperty("checkItemsChecked")
    private int checkItemsChecked;

    @JsonProperty("comments")
    private int comments;

    @JsonProperty("attachments")
    private int attachments;

    @JsonProperty("description")
    private boolean description;

    @JsonProperty("due")
    private String due;

    @JsonProperty("start")
    private String start;

    @JsonProperty("dueComplete")
    private boolean dueComplete;
}
