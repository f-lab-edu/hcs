package com.hcs.repository;

import com.hcs.domain.TradePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TradePostRepository extends JpaRepository<TradePost, Long> {

    @EntityGraph(value = "TradePost.withAuthor", type = EntityGraph.EntityGraphType.LOAD)
    Page<TradePost> findListsByCategoryAndSalesStatus(String category, boolean salesStatus, Pageable pageable);
}
