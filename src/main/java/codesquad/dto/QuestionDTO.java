package codesquad.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDTO {
    private String title;
    private String contents;

    public QuestionDTO(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
