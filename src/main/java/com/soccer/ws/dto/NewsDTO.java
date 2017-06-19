package com.soccer.ws.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by u0090265 on 16/09/16.
 */
public class NewsDTO extends DTOBaseClass {
    private String header;
    private String content;
    private String postDate;
    private AccountDTO postedBy;
    private boolean editable;
    private List<CommentDTO> comments;
    private NewsDTOType type = NewsDTOType.POST_AND_SEND;

    public NewsDTO() {
    }

    /**
     * @param id
     * @param header
     * @param content
     * @param postDate
     * @param postedBy
     * @param editable
     * @param comments
     */
    public NewsDTO(Long id, String header, String content, String postDate, AccountDTO postedBy, boolean editable,
                   List<CommentDTO> comments) {
        super(id);
        this.header = header;
        this.content = content;
        this.postDate = postDate;
        this.postedBy = postedBy;
        this.editable = editable;
        this.comments = comments;
    }

    /**
     * @param id
     * @param header
     * @param content
     * @param postDate
     * @param postedBy
     */
    public NewsDTO(Long id, String header, String content, String postDate, AccountDTO postedBy) {
        super(id);
        this.header = header;
        this.content = content;
        this.postDate = postDate;
        this.postedBy = postedBy;
    }

    @NotNull
    @NotEmpty
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @NotNull
    @NotEmpty
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public AccountDTO getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(AccountDTO postedBy) {
        this.postedBy = postedBy;
    }

    @NotNull
    public NewsDTOType getType() {
        return type;
    }

    public void setType(NewsDTOType type) {
        this.type = type;
    }
}
