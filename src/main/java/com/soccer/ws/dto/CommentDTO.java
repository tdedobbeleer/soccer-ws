package com.soccer.ws.dto;

import com.soccer.ws.utils.GeneralUtils;
import lombok.*;

import java.util.UUID;

/**
 * Created by u0090265 on 16/09/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends BaseClassDTO implements Comparable<CommentDTO> {
    private AccountDTO postedBy;
    private String content;
    private String postDate;
    private boolean editable;

    public CommentDTO(UUID id, AccountDTO postedBy, String content, String postDate, boolean editable) {
        super(id);
        this.postedBy = postedBy;
        this.content = content;
        this.postDate = postDate;
        this.editable = editable;
    }

    @Override
    public int compareTo(CommentDTO o) {
        return GeneralUtils.convertToDate(this.postDate).compareTo(GeneralUtils.convertToDate(o.postDate));
    }
}
