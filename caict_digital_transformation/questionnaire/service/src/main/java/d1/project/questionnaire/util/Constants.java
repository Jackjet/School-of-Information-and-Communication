package d1.project.questionnaire.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * 常量
 *
 * @author Kikki 2019/8/29 8:53
 */
public class Constants {

    /* **************************通用************************** */

    public static final String SUCCESS = "成功";
    public static final String FAIL = "失败";
    public static final String DATA_IS_NULL_OR_FAIL = "数据为空或获取失败 ";
    public static final String DETAIL_WRITE_IN_NULL = "<p><br></p> ";
    public static final String DELETE_ERROR = "删除异常 ";
    public static final String REQUEST_FAIL = "请求返回信息异常";
    public static final String USER_IS_NULL = "该用户信息不存在";
    public static final String EXAM_NAME_REPEAT = "考试名已存在";

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;

    public static final String THIS_TYPE_SHOULD_NOT_EXCEED_SEVEN = "此类型最多添加7条数据";

    public static final String TYPE_ERROR = "类型错误";
    public static final String NOT_SEARCH_MSG = "请输入查询信息";

    /* **************************用户************************** */

    public static final String PASSWORD_ERROR = "密码错误";
    public static final String PASSWORD_OLD_ERROR = "原密码错误";

    public static final String REGISTER_TIME_CANNOT_UPDATE = "注册时间不能修改";
    public static final String TOKEN_CANNOT_UPDATE = "Token不能修改";
    public static final String USER_PASSWORD_CANNOT_UPDATE = "用户密码不能修改";
    public static final String USER_NO_PASSWORD = "用户尚未设置密码";

    public static final String USERNAME_CANNOT_NULL = "用户名不能为空";
    public static final String USER_PHONE_CANNOT_NULL = "用户名手机号为空";
    public static final String USER_PASSWORD_CANNOT_NULL = "用户密码不能为空";

    public static final String USERNAME_ALREADY_EXISTS_CANNOT_CREATE = "用户登录名已存在，不能创建";
    public static final String USERNAME_OR_PHONE_ALREADY_EXISTS_CANNOT_REGISTER = "用户手机号或登录名已存在，不能注册";
    public static final String USERNAME_OR_PHONE_ALREADY_EXISTS_CANNOT_UPDATE = "你要修改的用户名或手机号已经存在，不能修改";
    public static final String THIS_USER_HAS_NO_PRIVILEGES = "该用户无权限!";

    public static final String PHONE_IS_USED = "改手机号已被使用!";
    public static final String INVITE_CODE_IS_OUT = "该邀请码已失效!";
    public static final String INVITE_CODE_IS_ERROR = "邀请码错误!";
    public static final String PHONE_REGISTRATION_ID_ERROR = "该用户手机号无法推送消息";

    public static final String AUTH_CODE_IS_OUT = "该验证码已失效!";
    public static final String AUTH_CODE_TYPE_ERROR = "该验证码类别错误!";
    public static final String AUTH_CODE_ERROR = "该验证码错误!";

    public static final String BAND_ERROR = "该用户已绑定";
    public static final String DELETE_WECHAT_BEFORE = "请在绑定手机的情况下,取消绑定微信.";
    public static final String LOGIN_RECORD_ERROR = "用户登录记录添加异常";
    public static final String PHONE_AND_WECHAT_IS_USED = "该手机号已被其他微信号绑定,您可直接用该手机号登录,或输入其他手机号重新验证";
    public static final String WECHAT_AND_PHONE_IS_USED = "该微信号已被其他手机号绑定,您可直接用该微信号登录,或输入其他微信号重新验证";

    /* **************************我的收藏************************** */

    public static final String COLLECTED = "已收藏";
    public static final String COLLECT_NOT = "该用户尚未收藏";

    /* **************************评论************************** */

    public static final String COMMENTS_NOT = "不是此用户评论";
    public static final String IS_COMMENTS = "您已添加过评论";

    /* **************************记录************************** */

    public static final String RECORD_NOT = "不是此用户记录";

    /* **************************通知************************** */

    public static final String MESSAGE_TITLE_LENGTH = "标题长度不能超过30个字符";

    /* **************************卦象************************** */

    public static final String GUA_IS_FREE = "免费卦象无需购买";
    public static final String GUA_IS_NULL = "该卦象不存在";
    /* **************************点赞************************** */

    public static final String IS_THUMB_UP = "已经点赞";

    /* **************************支付记录************************** */

    public static final String TO_PAY = "请到订单页面支付!";

    public static final String IS_PAY_CAN_NOT_DELETE = "已购买无法删除";

    /* **************************轮播图************************** */

    public static final String IS_CAROUSEL_MAX = "已置底,无法向下移动";

    public static final String IS_CAROUSEL_MIN = "已置顶,无法向上移动";

    public static final String INPUT_RIGHT_MOVE = "无法移动";

    /* **************************考题相关************************** */

    public static final String EXAM_GROUP_REPEAT = "该分组已存在";

    public static final String EXAM_GROUP_IS_NOT_NULL = "存在关联内容不允许删除";

    public static final String DEFAULT_CAN_NOT_DELETE = "默认分组无法删除";

    public static final String QUESTION_ID_IS_NULL = "请选择要删除的试题";

    public static final String EXAM_QUESTION_IS_NOT_NULL = "考题已加入考试中,不可删除";

    /**
     * 状态:0未上架1未开始2招生中3开课中4已结束
     */
    public static final Map<String, String> CAMP_PERIOD_STATUS_MAP = ImmutableMap.<String, String>builder()
            .put("0", "未上架")
            .put("1", "未开始")
            .put("2", "招生中")
            .put("3", "开课中")
            .put("4", "已结束")
            .build();

    /**
     * 状态:0未上架1未开始2招生中3开课中4已结束
     */
    public static final Map<String, String> LEARN_RESULT_TYPE_MAP = ImmutableMap.<String, String>builder()
            .put("0", "图文")
            .put("1", "视频")
            .put("2", "音频")
            .put("3", "考试")
            .build();
}

