package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/question/list")
    public String list(Model model){ // Model 객체는 따로 생성할 필요없이 컨트롤러 메서드의 매개변수로 지정하기만 하면 스프링부트가 자동으로 Model 객체를 생성한다.
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        // Model 객체에 "questionList" 라는 이름으로 값을 저장, Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할
        return "question_list";
    }
}