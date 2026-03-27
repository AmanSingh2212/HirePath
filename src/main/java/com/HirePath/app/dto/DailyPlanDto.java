package com.HirePath.app.dto;

import com.HirePath.app.entity.DevProblem;
import com.HirePath.app.entity.DsaQuestion;
import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.SystemDesignQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanDto {

    private Long userId;
    private List<DsaQuestionDTO> dsaQuestionList;
    private List<DevProblemDTO> devQuestionList;
    private List<SystemDesignProblemDTO> sdQuestionList;

}
