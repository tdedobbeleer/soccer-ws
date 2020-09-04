package com.soccer.ws.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 16/09/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NewsDTO extends BaseClassDTO {
    @NotNull
    @NotEmpty
    private String header;
    @NotNull
    @NotEmpty
    private String content;
    private String postDate;
    private AccountDTO postedBy;
    private boolean editable;
    private List<CommentDTO> comments;
    @NotNull
    private NewsDTOType type = NewsDTOType.POST_AND_SEND;

    /**
     * @param id
     * @param header
     * @param content
     * @param postDate
     * @param postedBy
     * @param editable
     * @param comments
     */
    public NewsDTO(UUID id, String header, String content, String postDate, AccountDTO postedBy, boolean editable,
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
    public NewsDTO(UUID id, String header, String content, String postDate, AccountDTO postedBy) {
        super(id);
        this.header = header;
        this.content = content;
        this.postDate = postDate;
        this.postedBy = postedBy;
    }
}
