package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page){ // Model 객체는 따로 생성할 필요없이 컨트롤러 메서드의 매개변수로 지정하기만 하면 스프링부트가 자동으로 Model 객체를 생성한다.
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        //  Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        // QuestionForm의 객체로 변경, 폼이 전송되면 속성이 자동으로 바인딩
        // @Valid 어노테이션을 적용하면 QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작
        if(bindingResult.hasErrors()){ // 오류가 있는 경우에는 폼 작성 화면을 렌더링
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }


}
