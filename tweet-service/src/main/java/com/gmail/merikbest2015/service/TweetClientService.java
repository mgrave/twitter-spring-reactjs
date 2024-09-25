package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.commons.dto.request.IdsRequest;
import com.gmail.merikbest2015.model.Tweet;
import com.gmail.merikbest2015.repository.projection.ChatTweetProjection;
import com.gmail.merikbest2015.repository.projection.TweetProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TweetClientService {

    List<TweetProjection> getTweetsByIds(IdsRequest requests);

    Page<TweetProjection> getTweetsByUserIds(IdsRequest request, Pageable pageable);

    TweetProjection getTweetById(Long tweetId);

    Boolean isTweetExists(Long tweetId);

    Long getTweetCountByText(String text);

    ChatTweetProjection getChatTweet(Long tweetId);

    List<Tweet> getBatchTweets(Integer period, Integer page, Integer limit);
}
