package codesquad.web.mapper;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.web.dto.QuestionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class QuestionMapperImplTest {

    private final QuestionMapper questionMapper
            = Mappers.getMapper(QuestionMapper.class);

    @Test
    @DisplayName("mapper를 통해 Entity를 Dto로 변환한다")
    void entityToDto() throws Exception {
        //given
        Question question = new Question("title", "contents");
        User user = new User("userId", "password", "name", "email");
        question.writeBy(user);

        //when
        QuestionDto questionDto = questionMapper.toDto(question);

        //then
        assertAll(
                () -> assertEquals(questionDto.getTitle(), question.getTitle()),
                () -> assertEquals(questionDto.getWriter(), question.getWriter().getName()),
                () -> assertEquals(questionDto.getContents(), question.getContents())
        );
    }

    @Test
    @DisplayName("mapper를 통해 Dto를 Entity로 변환한다")
    void DtoToEntity() throws Exception {
        //given
        QuestionDto questionDto = new QuestionDto("writer", "title", "contents");

        //when
        Question question = questionMapper.toEntity(questionDto);

        //then
        assertAll(
                () -> assertEquals(questionDto.getTitle(), question.getTitle()),
                () -> assertEquals(questionDto.getContents(), question.getContents())
        );
    }

    @Test
    @DisplayName("Dto를 이용하여 Entity를 수정할 경우 mapper가 이를 수행한다")
    void updateEntityFromDto() throws Exception {
        //given
        QuestionDto questionDto = new QuestionDto("writer", "updatedTitle", "updatedContents");
        Question question = new Question("title", "contents");

        //when
        Question updatedQuestion = questionMapper.updateFromDto(questionDto, question);

        //then
        assertAll(
                () -> assertEquals(questionDto.getTitle(), question.getTitle()),
                () -> assertEquals(questionDto.getContents(), question.getContents())
        );
    }

}