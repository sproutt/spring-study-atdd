package codesquad.web.dto;

import codesquad.domain.Question;

public class QuestionDto {

    private String title;
    private String content;

    public QuestionDto(String title , String content){
       this.title = title;
       this.content= content;
    }

    public QuestionDto(Question question){
        this.title = question.getTitle();
        this.content = question.getContents();
    }

    public QuestionDto(){}

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
