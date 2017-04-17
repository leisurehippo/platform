package com.bisaibang.monojwt.domain.util;

/**
 * Created by Gsy on 2016/7/14.
 * bsb v2
 */
public class NotifyContent {
    /**
     *
     * @author gsy
     *
     */
        public static final String SUCCESS="success";
        public static final String FAIL="failure";
        public static final String UNDEFINE="未定义数据";
        public static final String PASS="pass";
        public static final String REGISTER_SUCCESS="注册成功";
        public static final String NOT_LOGIN="未登录";
        public static final String REGISTER_FAIL="注册失败";
        public static final String LOGIN_PARAMS_NULL="用户名或密码或验证码不能为空";
        public static final String OPERATION_EXCEPTION="操作异常";
        public static final String OPERATION_SUCCESS="操作成功";
        public static final String UPDATE_SMSCODE="验证码已重新发送";
        public static final String USER_REGISTERED="该用户已经注册";
        public static final String FLUENT_REGISTERED="注册太频繁";
        public static final String ALIDAYU_PHONE_ERROR="未知原因,可能是号码格式错误";
        public static final String NICKNAME_REGISTERED="昵称已经被注册";
        public static final String NICKNAME_UNREGISTERED="昵称没有被注册";
        public static final String PHONE_REGISTERED="手机号已经注册";
        public static final String PHONE_UNREGISTERED="手机号没被注册";
        public static final String USERNAME_PASSWORD_WRONG="用户名密码错误";
        public static final String PHONE_PASSWORD_WRONG="手机号或密码错误";
        public static final String PHONE_NOT_RIGHT="请输入正确手机号";
        public static final String PHONE_FORMAT="请输入正确的手机号";
        public static final String PASSWORD_FORMAT="密码输入请大于6位小于10位的字母数字组合";
        public static final String NICKNAME_FORMAT="昵称应为4-16个字符,中英文及数字组合";
        public static final String RESET_PASSWORD_FAIL="修改密码失败";
        public static final String VERTIFY_CODE_WRONG="验证码输入不正确";
        public static final String SMS_CODE_NOTRIGHT="请输入正确的短信验证码";
        public static final String SMS_CODE_OVERTIME="输入短信验证码超时";
        public static final String PASSWORD_NOT_CONSISTENT="两次输入的密码不一致";
        public static final String SMS_CODE_SENDED="短信验证码刚刚发送，请您及时输入";
        public static final String SMS_NOTIFY_PREFIX="您的验证码为：";
        public static final String TOURNAMENT_EXIST="该赛事已存在,请修改：";
        public static final String ORGANIZER_REGIST="组织者已经存在";
        public static final String CREATE_ORGANIZER ="已经创建新组织者";
        public static final String CREATE_TOURNAMENT ="已经创建新赛事";
        public static final String GAME_ERROR_TOURNAMENT="游戏名不存在";
        public static final String USERID_ERROR_TOURNAMENT="用户ID不存在";
        public static final String MATCHGAME_CREATE_ERROR="创建MatchGames失败";
        public static final String PARTICIPANT_CREATE_ERROR="用户名不存在";
        public static final String USER_OVER_AUTHORITY="修改失败,该用户越权!";
        public static final String PLAYERS_OVERFLOW="选手溢出!";
        public static final String ADD_PLAYERS_SUCCESS="选手成功加入";
        public static final String WRONG_MATCH_GAME="比分提交错误";

        public static final String MATCH_REGISTERED="该赛事名已经注册";

        public static final String GET_MATCHDATA_FAIL= "获取赛事信息错误";

        public static final String USER_MATCH_REGISTERED="该用户已报名该赛事";
        public static final String USER_MATCH_REGISTER_SUCCESS = "报名成功";
        public static final String USER_MATCH_REGISTER_FAIL = "报名失败";
        public static final String USER_MATCH_REGISTER_FULL = "报名已满";
        public static final String UPDATE_SUCCESS = "修改成功";
        public static final String USER_MATCH_REGISTER_EMPTY = "无人报名";

        public static final String REGISTER_WIP = "对阵表还未生成，请耐心等候";
        public static final String DAO_INJ_DANGER="您输入了不合法字符，请重试";
        public static final String DAO_SAFE="字符合法";
        public static final String MATCHGAME_CANNOT_CHANGE="当前不可提交比分!";
        public static final String DROP_EMPTY_GAMES="成功去除僵尸赛事!";
        public static final String NO_MATCH_GAMES="未生成赛事树!";
        public static final Integer INT_UNDEFINE = -1;
        public static final String NO_ORGANIZER="无组织者!";
        public static final String NICKNAME_ERROR="用户名不存在";
}

