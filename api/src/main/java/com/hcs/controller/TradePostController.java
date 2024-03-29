package com.hcs.controller;

import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.TradePostDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.method.HcsDelete;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsModify;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.dto.response.tradePost.TradePostInfoDto;
import com.hcs.dto.response.tradePost.TradePostListDto;
import com.hcs.dto.response.user.UserInfoDto;
import com.hcs.service.TradePostService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post/tradePost")
@RequiredArgsConstructor
public class TradePostController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TradePostService tradePostService;
    private final HcsInfo info;
    private final HcsList list;
    private final HcsSubmit submit;
    private final HcsModify modify;
    private final HcsDelete delete;

    @GetMapping("/")
    public HcsResponse tradePost(@RequestParam("tradePostId") long tradePostId) {

        long authorId = tradePostService.findAuthorIdById(tradePostId);

        TradePost tradePost = tradePostService.findById(tradePostId);
        User author = userService.findById(authorId);

        TradePostInfoDto tradePostInfoDto = modelMapper.map(tradePost, TradePostInfoDto.class);
        UserInfoDto userInfoDto = modelMapper.map(author, UserInfoDto.class);

        tradePostInfoDto.setAuthor(userInfoDto);

        return HcsResponse.of(info.tradePost(tradePostInfoDto));
    }

    @GetMapping("/list")
    public HcsResponse tradePosts(@RequestParam("page") int page, @RequestParam("category") String category, @RequestParam("salesStatus") boolean salesStatus) {

        List<TradePost> tradePosts = tradePostService.findTradePostsWithPaging(page, category, salesStatus);
        List<TradePostInfoDto> tradePostInfoDtos = new ArrayList<>();

        for (TradePost tradePost : tradePosts) {
            TradePostInfoDto info = modelMapper.map(tradePost, TradePostInfoDto.class);
            tradePostInfoDtos.add(info);
        }

        TradePostListDto tradePostListDto = TradePostListDto.builder()
                .page(page)
                .count(tradePosts.size())
                .salesStatus(salesStatus)
                .category(category)
                .tradePosts(tradePostInfoDtos)
                .build();

        return HcsResponse.of(list.tradePost(tradePostListDto));
    }

    @PostMapping("/")
    public HcsResponse saveTradePost(@Valid @RequestBody TradePostDto tradePostDto, @RequestParam("authorId") long authorId) {

        TradePost newTradePost = tradePostService.saveTradePost(authorId, tradePostDto);
        long tradePostId = newTradePost.getId();

        return HcsResponse.of(submit.tradePost(authorId, tradePostId));
    }

    @PutMapping("/")
    public HcsResponse modifyTradePost(@Valid @RequestBody TradePostDto tradePostDto, @RequestParam("tradePostId") long tradePostId) throws MethodArgumentNotValidException {

        modifyTradePostValidation(tradePostId, tradePostDto.getTitle());
        tradePostService.modifyTradePost(tradePostId, tradePostDto);

        return HcsResponse.of(modify.tradePost(tradePostId));
    }

    @DeleteMapping("/")
    public HcsResponse deleteTradePost(@RequestParam("tradePostId") long tradePostId) {

        tradePostService.deleteTradePostById(tradePostId);

        return HcsResponse.of(delete.tradePost(tradePostId));
    }

    private void modifyTradePostValidation(long tradePostId, String title) throws MethodArgumentNotValidException {

        TradePost originTradePost = tradePostService.findById(tradePostId);

        if (originTradePost.getTitle() != title && tradePostService.countByTitle(title) > 0) {

            Errors errors = new MapBindingResult(new HashMap<>(), "a");

            errors.rejectValue("title", "invalid.title", new Object[]{title}, "이미 사용중인 제목 입니다.");
            throw new MethodArgumentNotValidException(null, (BindingResult) errors);
        }
    }
}
