package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.BulletScreenComments;
import com.bilibili.model.request.BulletScreenCommentsRequest;
import com.bilibili.service.BulletScreenCommentsService;
import com.bilibili.support.UserSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.base.ErrorCode.PARAM_ERROR;


/**
 * 弹幕 controller
 *
 * @author sgh
 */
@RestController
@RequestMapping("/rsc")
public class BulletScreenCommentsController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private BulletScreenCommentsService bulletScreenCommentsService;

    /**
     * 获取指定时间范围内的弹幕
     *
     * @param request 请求体
     * @return 弹幕集合
     */
    @GetMapping()
    public BaseResponse<List<BulletScreenComments>> getBulletScreenComments(@RequestBody BulletScreenCommentsRequest request) {
        if (request == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long videoId = request.getVideoId();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        List<BulletScreenComments> result ;
        Long userId = userSupport.getCurrentUserId();
        if (userId == null) {
            result = bulletScreenCommentsService.getBulletScreenComments(videoId, null, null);
        } else {
            result = bulletScreenCommentsService.getBulletScreenComments(videoId, startTime, endTime);
        }
        return result == null ? ResultUtils.error(GET_SERVICE_ERROR) : ResultUtils.success(result);
    }

}
