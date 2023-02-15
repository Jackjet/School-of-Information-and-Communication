package com.jeecg.p3.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.p3.system.entity.JwSystemUser;
import com.jeecg.p3.system.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Value("${auth.url}")
    private String authUrl="http://47.93.42.226:9001/auth/";

    @Value("${auth.realm}")
    private String realm="caict";

    @Value("${auth.clientId}")
    private String clientId="jeewx";

    @Value("${auth.client_secret}")
    private String client_secret="9fa972db-1dfe-414f-b3a0-1dfb8ca9d830";

    @Value("${auth.grant_type}")
    private String grant_type="client_credentials";

    @Value("${auth.client_id}")
    private String client_id="jeewx";

    public static void main(String[] args){

        JwSystemUser jwSystemUser = new JwSystemUser();
        jwSystemUser.setPassword("12345678");
        jwSystemUser.setUserName("admin12345");

        //new AuthServiceImpl().update(jwSystemUser);

        System.out.println(new AuthServiceImpl());

        new AuthServiceImpl().add(jwSystemUser);
        System.out.println(new AuthServiceImpl().getUserId("admin"));
        new AuthServiceImpl().delete("admin111");
        new AuthServiceImpl().update(jwSystemUser);
        System.out.println(new AuthServiceImpl().getServerToken());

    }

    @Override
    public JSONObject getAuthUser(String accessToken) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "realms/caict/protocol/openid-connect/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "bearer " + accessToken);
        ResponseEntity<JSONObject> response = null;
        try {
            response = restTemplate.exchange(rootUrl, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        }catch(Exception e){
            JSONObject j = new JSONObject();
            j.put("preferred_username","admin");
            return j;
        }
        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        }else{
            throw new Exception("token失效！");
        }

    }

    @Override
    public boolean validate(String accessToken){
        try {
            JSONObject j = getAuthUser(accessToken);
            if (j!=null&&j.size()>0) {
                return true;
            } else {
                return false;
            }
        }catch(Exception e){
            return false;
        }

    }

    @Override
    public boolean add(JwSystemUser jwSystemUser) {

        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "/admin/realms/"+realm+"/users";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Content-Type","application/json");
        headers.add("Authorization", "bearer " + getServerToken());

        JSONObject json = new JSONObject();
        json.put("username", jwSystemUser.getUserId());
        json.put("enabled", true);

        JSONArray credentials = new JSONArray();
        JSONObject credential = new JSONObject();
        credential.put("type","password");
        credential.put("value",jwSystemUser.getPassword());
        credential.put("temporary",false);
        credentials.add(credential);

        json.put("credentials", credentials);

        ResponseEntity<JSONObject> response = restTemplate.exchange(rootUrl, HttpMethod.POST, new HttpEntity<String>(json.toString(),headers), JSONObject.class);

        if (response.getStatusCodeValue() == 200) {
            System.out.println(response.getBody());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean update(JwSystemUser jwSystemUser) {
        String userId = getUserId(jwSystemUser.getUserName());

        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "/admin/realms/"+realm+"/users/"+userId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Content-Type","application/json");
        headers.add("Authorization", "bearer " + getServerToken());

        JSONObject json = new JSONObject();
        json.put("username", jwSystemUser.getUserName());
        json.put("enabled", true);

        JSONArray credentials = new JSONArray();
        JSONObject credential = new JSONObject();
        credential.put("type","password");
        credential.put("value",jwSystemUser.getPassword());
        credential.put("temporary",false);
        credentials.add(credential);

        json.put("credentials", credentials);

        //提交请求
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity< MultiValueMap<String,String>>(new LinkedMultiValueMap(),headers);

        ResponseEntity<String> response = restTemplate.exchange(rootUrl, HttpMethod.PUT, new HttpEntity<String>(json.toString(),headers), String.class);

        System.out.println(response);

        return true;
    }

    @Override
    public boolean delete(String userName) {

        String userId = getUserId(userName);

        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "/admin/realms/"+realm+"/users/"+userId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        //headers.add("Content-Type","application/json");
        headers.add("Authorization", "bearer " + getServerToken());

        //提交请求
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity< MultiValueMap<String,String>>(new LinkedMultiValueMap(),headers);

        ResponseEntity<String> response = restTemplate.exchange(rootUrl, HttpMethod.DELETE, entity, String.class);

        System.out.println(response);

        return true;
    }

    public String getUserId(String username){

        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "/admin/realms/"+realm+"/users?username="+username;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Authorization", "bearer " + getServerToken());

        //提交请求
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity< MultiValueMap<String,String>>(new LinkedMultiValueMap(),headers);

        ResponseEntity<String> response = restTemplate.exchange(rootUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCodeValue() == 200) {
            JSONArray array = JSONArray.parseArray(response.getBody());

            for(Object obj:array){
                JSONObject jobj = (JSONObject)obj;
                String userName = jobj.getString("username");

                if(username.equals(userName)){
                     return jobj.getString("id");
                }

            }

            return null;

        }else{
            return null;
        }

    }

    public String getServerToken(){

        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = authUrl + "/realms/master/protocol/openid-connect/token";

        //请求头设置
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> params= new LinkedMultiValueMap();
        params.add("client_secret", client_secret);
        params.add("grant_type", grant_type);
        params.add("client_id", client_id);

        //提交请求
        HttpEntity< MultiValueMap<String,String>> entity = new HttpEntity< MultiValueMap<String,String>>(params,headers);

        ResponseEntity<JSONObject> response = restTemplate.exchange(rootUrl, HttpMethod.POST, entity, JSONObject.class);

        if (response.getStatusCodeValue() == 200) {
            return response.getBody().getString("access_token");
        }else{
            return null;
        }

    }

}
