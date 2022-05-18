package codesquad.web.mapper;


import codesquad.domain.Question;
import codesquad.web.dto.QuestionDto;
import org.mapstruct.Mapper;
import support.mapper.GenericMapper;

@Mapper
public interface QuestionMapper extends GenericMapper<QuestionDto, Question> {
}
