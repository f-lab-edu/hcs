package com.hcs.service;

import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.TradePostDto;
import com.hcs.mapper.TradePostMapper;
import com.hcs.repository.TradePostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradePostService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TradePostMapper tradePostMapper;
    private final TradePostRepository tradePostRepository;

    public TradePost saveTradePost(long authorId, TradePostDto tradePostDto) {

        TradePost tradePost = modelMapper.map(tradePostDto, TradePost.class);

        User author = userService.findById(authorId);
        LocalDateTime registrationTime = LocalDateTime.now();

        tradePost.setAuthor(author);
        tradePost.setSalesStatus(false);
        tradePost.setRegisterationTime(registrationTime);

        tradePostMapper.insertTradePost(tradePost);

        return tradePost;
    }

    public void clickTradePost(long Id) {
        tradePostMapper.updateTradePostForView(Id);
    }

    public long modifyTradePost(long Id, TradePostDto tradePostDto) {

        TradePost tradePost = findById(Id);

        tradePost = modelMapper.map(tradePostDto, TradePost.class);
        tradePost.setId(Id);

        tradePostMapper.updateTradePost(tradePost);

        return tradePost.getId();
    }

    public TradePost findByTitle(String title) {
        return tradePostMapper.findByTitle(title);
    }

    public TradePost findById(long Id) {
        return tradePostMapper.findById(Id);
    }

    public Long findAuthorIdById(long Id) {
        return tradePostMapper.findAuthorIdById(Id);
    }

    public List<TradePost> findTradePostsWithPaging(int page, String category, boolean salesStatus) {

        int pagePerCount = 10;

        PageRequest pageRequest = PageRequest.of(page - 1, pagePerCount, Sort.by("registerationTime").descending());

        List<TradePost> result = tradePostRepository.findListsByCategoryAndSalesStatus(category, salesStatus, pageRequest).getContent();

        return result;
    }

    public int countByTitle(String title) {
        return tradePostMapper.countByTitle(title);
    }

    public long deleteTradePostById(long Id) {
        return tradePostMapper.deleteTradePostById(Id);
    }
}
