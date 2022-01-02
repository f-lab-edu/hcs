package com.hcs.validator;

import com.hcs.domain.TradePost;
import com.hcs.dto.request.TradePostDto;
import com.hcs.service.TradePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TradePostDtoValidator implements Validator {

    private final TradePostService tradePostService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(TradePostDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TradePostDto tradePostDto = (TradePostDto) object;

        TradePost tradePost = tradePostService.findByTitle(tradePostDto.getTitle());
        Optional<TradePost> tradePostOptional = Optional.ofNullable(tradePost);

        if (tradePostOptional.isPresent()) {
            errors.rejectValue("title", "invalid.title", new Object[]{tradePostDto.getTitle()}, "이미 사용중인 글 제목입니다.");
        }
    }

}
