package net.slipp.web;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/loginForm")
    public String loginForm() { return "/user/login"; }

    @PostMapping("/login") // 로그인
    public String login(String userId, String password, HttpSession session) {
        User user = userRepository.findByUserId(userId);
        if(userId == null || userId.equals("") || password.equals("")) {
            System.out.println("아이디 혹은 비밀번호 빈 칸");
            return "redirect:/users/loginForm";
        }
        if(!password.equals(user.getPassword())) {
            System.out.println("비밀번호 불일치");
            return "redirect:/users/loginForm";
        }
        System.out.println("로그인 성공");
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
        return "redirect:/";
    }

    @GetMapping("/logout") // 로그아웃
    public String logout(HttpSession session) {
        System.out.println  ("로그아웃");
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/form")
    public String form() {
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user) {
        System.out.println("user : " + user);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        if (HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }
        User sessionedUser = HttpSessionUtils.getUserFromSession(session);
        if(!id.equals(sessionedUser.getId())) {
            throw new   IllegalStateException("자신의 정보만 수정할 수 있습니다.");
        }

        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
        if (HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }
        User sessionedUser = HttpSessionUtils.getUserFromSession(session);
        if(!id.equals(sessionedUser.getId())) {
            throw new   IllegalStateException("자신의 정보만 수정할 수 있습니다.");
        }

        User user = userRepository.findById(id).get();
        user.update(updatedUser);
        userRepository.save(user);
        System.out.println("user : " + user);
        return "redirect:/users";
    }
}
