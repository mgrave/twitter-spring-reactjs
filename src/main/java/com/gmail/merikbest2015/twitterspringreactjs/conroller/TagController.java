package com.gmail.merikbest2015.twitterspringreactjs.conroller;

import com.gmail.merikbest2015.twitterspringreactjs.dto.response.TagResponse;
import com.gmail.merikbest2015.twitterspringreactjs.dto.response.TweetResponse;
import com.gmail.merikbest2015.twitterspringreactjs.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags() {
        return ResponseEntity.ok(tagMapper.getTags());
    }

    @GetMapping("/{tagName}")
    public ResponseEntity<List<TweetResponse>> getTweetsByTag(@PathVariable String tagName) {
        return ResponseEntity.ok(tagMapper.getTweetsByTag(tagName));
    }
}