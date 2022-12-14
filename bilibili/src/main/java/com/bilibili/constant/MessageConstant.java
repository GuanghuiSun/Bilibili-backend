package com.bilibili.constant;

/**
 * 错误信息
 *
 * @author sgh
 * @date 2022-8-1
 */
public interface MessageConstant {
    /**
     * 状态码
     *
     */
    public static final String PARAM_ERROR_CODE = "4001";
    public static final String SYSTEM_ERROR_CODE = "5000";
    public static final String SUCCESS_RESPONSE_CODE = "200";
    public static final String GET_MESSAGE_ERROR_CODE = "5001";
    public static final String UPDATE_SERVICE_ERROR_CODE = "5002";
    public static final String DELETE_SERVICE_ERROR_CODE = "5003";
    public static final String PUT_SERVICE_ERROR_CODE = "5004";
    public static final String REQUEST_SERVICE_ERROR_CODE = "5005";
    public static final String TOKEN_ERROR_CODE = "5006";
    public static final String AUTH_ERROR_CODE = "5010";
    public static final String FILE_ERROR_CODE = "5020";
    public static final String UPLOAD_TIMEOUT_ERROR_CODE = "5021";
    /**
     * 错误信息 message
     */
    public static final String REQUEST_PARAM_ERROR = "请求参数错误";
    public static final String SERVER_INTERNAL_ERROR = "服务器异常";
    public static final String GET_MESSAGE_ERROR = "获取信息失败";
    public static final String UPDATE_ERROR = "更新业务失败";
    public static final String DELETE_ERROR = "删除业务失败";
    public static final String PUT_ERROR = "新增业务失败";
    public static final String REQUEST_ERROR = "请求业务失败";
    public static final String USER_STATUS_ERROR = "用户状态异常";
    public static final String FILE_ERROR = "文件异常";

    /**
     * 成功信息
     */
    public static final String SUCCESS = "成功!";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGOUT_SUCCESS = "退出登录成功";
    public static final String FOLLOW_SUCCESS = "关注成功";
    public static final String UNFOLLOW_SUCCESS = "取消关注成功";


    /**
     * 错误详细信息
     */
    public static final String GET_KEY_ERROR = "获取key失败";
    public static final String PARAM_EMPTY_ERROR = "参数不能为空";
    public static final String PHONE_EXIST_ERROR = "该手机号已被注册";
    public static final String PHONE_NOT_EXIST_ERROR = "手机号不存，请先注册";
    public static final String PHONE_PATTERN_ERROR = "手机号格式错误";
    public static final String RSA_DECODE_FAIL = "密码解密失败";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String GENERATE_TOKEN_ERROR = "token生成错误";
    public static final String TOKEN_EXPIRE_ERROR = "token已过期";
    public static final String TOKEN_DECODE_ERROR = "token不合法";
    public static final String GET_USER_ERROR = "获取用户信息失败";
    public static final String GROUP_TYPE_ID_ERROR = "关注分组类型id错误";
    public static final String GROUP_NOT_EXIST_ERROR = "关注分组不存在";
    public static final String GROUP_ID_ERROR = "关注分组不存在";
    public static final String FOLLOWING_USER_NOT_EXIST_ERROR = "关注用户不存在";
    public static final String USER_ID_ERROR = "用户ID错误";
    public static final String ADD_FOLLOWING_REPEAT_ERROR = "请勿重复关注";
    public static final String FOLLOWING_NOT_EXIST_ERROR = "还未关注";
    public static final String MESSAGE_ERROR = "消息传递错误";
    public static final String PUT_MOMENTS_ERROR = "发布动态失败";
    public static final String REQUEST_AUTH_ERROR = "用户权限不足";
    public static final String MOMENTS_AUTH_ERROR = "没有权限发送该类型动态";
    public static final String ROLE_CODE_ERROR = "角色code不存在";
    public static final String FILE_TYPE_ERROR = "文件类型错误";
    public static final String FILE_NAME_ERROR = "文件名称错误";
    public static final String FILE_UPLOAD_TIMEOUT_ERROR = "文件上传超时，请重试!";
    public static final String FILE_UPLOAD_ERROR = "文件分片上传失败，请重试!";
    public static final String FILE_NOT_EXIST_ERROR = "文件还未上传";
    public static final String FILE_MD5_ERROR = "生成md5错误";
    public static final String VIDEO_NOT_EXIST_ERROR = "视频不存在!";
    public static final String VIDEO_LIKE_REPEAT_ERROR = "请勿重复点赞";
    public static final String VIDEO_LIKE_NOT_EXIST_ERROR = "点赞不存在";
    public static final String VIDEO_COLLECT_REPEAT_ERROR = "请勿重复收藏";
    public static final String VIDEO_COLLECT_NOT_EXIST_ERROR = "收藏不存在";
    public static final String COLLECT_GROUP_NOT_EXIST_ERROR = "收藏分组不存在";
    public static final String COINS_INSUFFICIENT_ERROR = "硬币数量不足";




}
