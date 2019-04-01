package com.iksun.event.service;

import com.iksun.event.domain.UserPoint;
import com.iksun.event.domain.UserPointHistory;
import com.iksun.event.repository.UserPointHistoryRepository;
import com.iksun.event.repository.UserPointRepository;
import com.iksun.event.reward.service.PointServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class PointServiceImplTests {
    private static final int TEST_EVENT_ID = 1;

    private String userId;
    private int rewardPoint;
    @InjectMocks
    private PointServiceImpl pointPointService;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private UserPointHistoryRepository userPointHistoryRepository;

    @Before
    public void setup() {

    }

    @Test
    public void testChangePoint_SUCCESS() {
        //given
        when(userPointRepository.save(any())).thenReturn(new Object());
        when(userPointHistoryRepository.save(any())).thenReturn(new Object());
        rewardPoint = 10;

        //when
        pointPointService.changePoint(userId, rewardPoint, TEST_EVENT_ID);

        //then
        verify(userPointRepository, times(1)).save(any());

    }

    @Test
    public void testChangePoint_FAILUE() {
        //given
        UserPoint userPoint = new UserPoint();
        userPoint.setPoint(30);
        UserPointHistory userPointHistory = new UserPointHistory();
        userPointHistory.setUserId("1");
        rewardPoint = -10;
        when(pointPointService.getUserPoint(anyString())).thenReturn(userPoint);
        when(userPointRepository.findOneByUserId(anyString())).thenReturn(userPoint);
        when(userPointRepository.save(any())).thenReturn(userPoint);

        //when
        doThrow(new RuntimeException()).when(userPointRepository).save(any());

        pointPointService.changePoint(userId, rewardPoint, TEST_EVENT_ID);
        //then
    }

    @Test
    public void testGetUserPoint_NEWUSER() {

        when(userPointRepository.findOneByUserId(anyString())).thenReturn(null);
        UserPoint p = pointPointService.getUserPoint("1");
        assertTrue("", p.getPoint() == 0);
    }

    @Test
    public void testGetUserPoint_OLDUSER() {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId("1");
        userPoint.setPoint(10);
        when(userPointRepository.findOneByUserId(anyString())).thenReturn(userPoint);
        UserPoint p = pointPointService.getUserPoint("1");
        assertTrue("", p.getPoint() == 10);
    }
}
