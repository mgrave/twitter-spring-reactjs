package com.gmail.merikbest2015.repository;

import com.gmail.merikbest2015.model.Tag;
import com.gmail.merikbest2015.repository.projection.TagProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Page<TagProjection> findByOrderByTweetsQuantityDesc(Pageable pageable);

    List<TagProjection> findTop5ByOrderByTweetsQuantityDesc();

    Tag findByTagName(String tagName);

    @Query("SELECT tag FROM Tag tag WHERE tag.id IN :tagIds")
    List<Tag> getTagsBuIds(@Param("tagIds") List<Long> tagIds);

    @Modifying
    @Query("UPDATE Tag tag SET tag.tweetsQuantity = " +
            "CASE WHEN :increaseCount = true THEN (tag.tweetsQuantity + 1) " +
            "ELSE (tag.tweetsQuantity - 1) END " +
            "WHERE tag.id = :tagId")
    void updateTagQuantity(@Param("tagId") Long tagId, @Param("increaseCount") boolean increaseCount);
}