package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
//    微信服务接口地址:
    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
//        2.判断openid是否为空，如果为空，表示登录失败（业务异常）
        String openId=getOpenId(userLoginDTO.getCode());
        if(openId==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
//        3.判断是否对于我们这个系统是新用户，
        User user=userMapper.getByOpenid(openId);
        //  4.是新用户那么自动完成注册

        if(user==null){
            user= User.builder().openid(openId).createTime(LocalDateTime.now()).build();
//            注意插入完成后要返回用户的id值因为在controller层需要使用
            userMapper.insert(user);
        }
        //  5.返回这个用户对象
        return user;

    }
    private String getOpenId(String code){
        //        微信登录与传统的账户密码登录不同，传统的账号密码登录需要在数据库中查询，但是微信登录不需要，查询的是微信登录的接口
        //1.调用微信登录接口服务，获取当前微信用户的openid
//        向外部发送请求使用HTTPClient
        HashMap<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json=HttpClientUtil.doGet(WX_LOGIN,map);

//        解析得到的json文件
        JSONObject jsonObject= JSON.parseObject(json);
        String openId=jsonObject.getString("openid");
        return openId;
    }
}
