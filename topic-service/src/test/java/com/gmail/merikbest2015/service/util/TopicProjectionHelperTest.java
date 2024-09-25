package com.gmail.merikbest2015.service.util;

import com.gmail.merikbest2015.commons.util.TestConstants;
import com.gmail.merikbest2015.service.AbstractServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TopicProjectionHelperTest extends AbstractServiceTest {

    @Autowired
    private TopicProjectionHelper topicProjectionHelper;

    @Test
    public void isTopicFollowed() {
        when(topicRepository.isTopicFollowed(TestConstants.USER_ID, 3L)).thenReturn(true);
        assertTrue(topicProjectionHelper.isTopicFollowed(3L));
        verify(topicRepository, times(1)).isTopicFollowed(TestConstants.USER_ID, 3L);
    }

    @Test
    public void isTopicNotInterested() {
        when(topicRepository.isTopicNotInterested(TestConstants.USER_ID, 3L)).thenReturn(true);
        assertTrue(topicProjectionHelper.isTopicNotInterested(3L));
        verify(topicRepository, times(1)).isTopicNotInterested(TestConstants.USER_ID, 3L);
    }
}
