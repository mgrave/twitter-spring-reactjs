package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.commons.dto.response.tweet.TweetListResponse;

public interface ListsClientService {

    TweetListResponse getTweetList(Long listId);
}
