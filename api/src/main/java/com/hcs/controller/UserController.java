package com.hcs.controller;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.dto.request.UserModifyDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.method.HcsDelete;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsModify;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.dto.response.user.UserInfoDto;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

/**
 * @RestController : Spring MVC Controller에 @ResponseBody가 추가된 것이며, Json 형태로 객체 데이터를 반환함.
 * @InitBinder : Spring Validator를 사용 시 @Valid 어노테이션으로 검증이 필요한 객체를 가져오기 전에 수행할 method를 지정해주는 어노테이션
 * @Valid : 유효성 검사를 진행할 파라미터에 붙여줌
 * @RequestBody : Http Request body를 읽고 HttpMessageConverter를 통해서 deserialized시켜 객체로 변환하기 위한 애노테이션
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final HcsInfo info;
    private final HcsSubmit submit;
    private final HcsModify modify;
    private final HcsDelete delete;

    // 테스트 성격의 핸들러. 서비스 후반부터는 deprecated 될것임
    @GetMapping("/sign-up")
    public String signUpForm() {
        return "hi, here is sign-up page";
    }

    @PostMapping("/submit")
    public HcsResponse registerUser(@Valid @RequestBody SignUpDto signUpDto) throws IOException {

        User newUser = userService.saveNewUser(signUpDto);

        return HcsResponse.of(submit.user(newUser.getId()));
    }

    @GetMapping("/info")
    public HcsResponse userInfo(@RequestParam("userId") long userId) {

        User user = userService.findById(userId);
        UserInfoDto userInfoDto = modelMapper.map(user, UserInfoDto.class);

        return HcsResponse.of(info.user(userInfoDto));
    }

    @PutMapping("/modify")
    public HcsResponse modifyUser(@Valid @RequestBody UserModifyDto userModifyDto, @RequestParam("userId") long userId) throws MethodArgumentNotValidException {

        // TODO : 인가 체크 (본인 확인) 후 정보 수정

        modifyUserValidation(userId, userModifyDto.getEmail(), userModifyDto.getNickname());
        userService.modifyUser(userId, userModifyDto);

        return HcsResponse.of(modify.user(userId));
    }

    @DeleteMapping("/delete")
    public HcsResponse deleteUser(@RequestParam("userId") long userId) {

        // TODO : 인가 체크 (본인 확인) 후 삭제

        userService.deleteUserById(userId);

        return HcsResponse.of(delete.user(userId));
    }

    private void modifyUserValidation(long userId, String email, String nickname) throws MethodArgumentNotValidException {

        boolean flag = false;
        User originUser = userService.findById(userId);
        Errors errors = new MapBindingResult(new HashMap<>(), "a");

        if (originUser.getEmail() != email && userService.countByEmail(email) > 0) {

            errors.rejectValue("email", "invalid.email", new Object[]{email}, "이미 사용중인 이메일입니다.");
            flag = true;
        }
        if (originUser.getNickname() != nickname && userService.countByNickname(nickname) > 0) {

            errors.rejectValue("nickname", "invalid.nickname", new Object[]{nickname}, "이미 사용중인 닉네임입니다.");
            flag = true;
        }

        if (flag) {
            throw new MethodArgumentNotValidException(null, (BindingResult) errors);
        }
    }
}
