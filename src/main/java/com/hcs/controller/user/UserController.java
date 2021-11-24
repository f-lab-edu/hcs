package com.hcs.controller.user;


import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.service.UserService;

import com.hcs.validator.SignUpDtoValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @RestController
 *   : Spring MVC Controller에 @ResponseBody가 추가된 것이며, Json 형태로 객체 데이터를 반환함.
 *
 * @InitBinder : Spring Validator를 사용 시 @Valid 어노테이션으로 검증이 필요한 객체를 가져오기 전에 수행할 method를 지정해주는 어노테이션
 * @Valid : 유효성 검사를 진행할 파라미터에 붙여줌
 * @RequestBody : Http Request body를 읽고 HttpMessageConverter를 통해서 deserialized시켜 객체로 변환하기 위한 애노테이션
 */

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SignUpDtoValidator signUpDtoValidator;

    @InitBinder("SignUpDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpDtoValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "hi, here is sign-up page";
    }

    @PostMapping("/sign-up")
    public void registerUser(@Valid @RequestBody SignUpDto signUpDto, HttpServletResponse response,
                                         Model model) throws IOException {

        User newUser = userService.saveNewUser(signUpDto);

        response.sendRedirect("/");
    }

}
