package com.hcs.controller.user;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @RestController
 *   : Spring MVC Controller에 @ResponseBody가 추가된 것이며, Json 형태로 객체 데이터를 반환함.
 *
 * @Valid :
 */

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpDto());
        return "success sign-up";
    }

    @PostMapping("/sign-up")
    public String registerUser(@Valid SignUpDto signUpDto, Errors errors) {
        if (errors.hasErrors()) {
            return "SignUp Fail: SignUpForm is not appropriate";
        }
        User user = userService.saveNewUser(signUpDto);

        return "redirect:/";


    }


}
