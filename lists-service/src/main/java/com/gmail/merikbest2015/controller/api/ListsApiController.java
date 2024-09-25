package com.gmail.merikbest2015.controller.api;

import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.commons.dto.response.tweet.TweetListResponse;
import com.gmail.merikbest2015.service.ListsClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_LISTS)
public class ListsApiController {

    private final ListsClientService listsClientService;

    @GetMapping(PathConstants.TWEET_LIST_ID)
    public TweetListResponse getTweetList(@PathVariable("listId") Long listId) {
        return listsClientService.getTweetList(listId);
    }
}
