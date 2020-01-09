package net.slipp.web;

import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String form(HttpSession session) {
        if(!HttpSessionUtils.isLoginUser(session)) { // 로그이 안 된 경우 로그인폼으로 이동
            return "/users/loginForm";
        }
        return "qna/form";
    }

    @PostMapping("")
    public String create(HttpSession session, String title, String contents) {
        if(!HttpSessionUtils.isLoginUser(session)) { // 로그이 안 된 경우 로그인폼으로 이동
            System.out.println("로그인해주세요."); // 오류잡기
            return "../users/loginForm";
        }
        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Question newQuestion = new Question(sessionUser.getUserId(), title, contents);
        questionRepository.save(newQuestion);
        return "redirect:/";
    }
}
