package com.jeecg.p3.system.service;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.p3.system.entity.JwSystemUser;

public interface AuthService {

     /**
      * {"sub":"8e428d9b-f907-470d-85e1-2f75c107583a","email_verified":false,"name":"li li","preferred_username":"admin","given_name":"li","family_name":"li","email":"1521722612@qq.com"}
      * @param accessToken
      * @return
      * @throws Exception
      */
     JSONObject getAuthUser(String accessToken) throws Exception;

     boolean validate(String accessToken) throws Exception;

     boolean add(JwSystemUser jwSystemUser);

     boolean update(JwSystemUser jwSystemUser);

     boolean delete(String userName);

}
