package com.idcg.idcw.model.params;

/**
 *
 * @author yiyang
 */
public class EditCommentReqParam {
    /**
     * id : 0
     * comment : string
     */

    private int id;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
