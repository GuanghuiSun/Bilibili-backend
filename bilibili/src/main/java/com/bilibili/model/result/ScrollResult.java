package com.bilibili.model.result;

import com.bilibili.model.domain.UserMoments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 滚动分页查询结果
 *
 * @author sgh
 * @date 2022-8-4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollResult {
    private List<UserMoments> data;
    private Long offset;
    private Long minTime;
    private Long size;
}
