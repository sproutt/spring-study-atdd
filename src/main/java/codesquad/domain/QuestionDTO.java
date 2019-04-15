package codesquad.domain;

import codesquad.domain.Question;

import javax.validation.constraints.NotNull;

public class QuestionDTO {

    @NotNull
    private String title;
    private String content;

    public QuestionDTO(String title , String content){
        this.title = title;
        this.content= content;
    }

    public QuestionDTO(Question question){
        this.title = question.getTitle();
        this.content = question.getContents();
    }

    public QuestionDTO(){}

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }



}
