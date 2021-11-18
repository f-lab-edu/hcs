package com.hcs.controller.user;

import com.hcs.form.SignUpForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController
 *   : Spring MVC Controller에 @ResponseBody가 추가된 것이며, Json 형태로 객체 데이터를 반환함.
 */

@RestController
public class UserController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "success sign-up";
    }
}
