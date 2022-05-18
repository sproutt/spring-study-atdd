package codesquad.web.mapper;

import codesquad.domain.*;
import codesquad.web.dto.*;
import org.springframework.stereotype.*;

@Component
public class QuestionMapperImpl implements QuestionMapper{
    @Override
    public QuestionDto toDto(Question question) {
        return new QuestionDto(
                question.getWriter().getName(),
                question.getTitle(),
                question.getContents());
    }

    @Override
    public Question toEntity(QuestionDto questionDto) {
        return new Question(questionDto.getTitle(), questionDto.getContents());
    }

    @Override
    public Question updateFromDto(QuestionDto questionDto, Question question) {
        return question.update(toEntity(questionDto));
    }
}
