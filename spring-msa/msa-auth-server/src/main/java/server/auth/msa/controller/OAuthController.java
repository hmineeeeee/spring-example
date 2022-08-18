package server.auth.msa.controller;

import kong.unirest.Unirest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.auth.msa.security.OAuthToken;

@RestController
public class OAuthController {

    // 클라이언트가 구현해야하는 코드 - 발급 받은 코드로 토큰 발행
    @RequestMapping("/callback")
    public OAuthToken.response code(@RequestParam String code){

        String cridentials = "auth_id:auth_secret";
        // base 64로 인코딩 (basic auth 의 경우 base64로 인코딩 하여 보내야한다.)
        String encodingCredentials = new String(
                Base64.encodeBase64(cridentials.getBytes())
        );
        String requestCode = code;
        OAuthToken.request.accessToken request = new OAuthToken.request.accessToken(){{
            setCode(requestCode);
            setGrant_type("authorization_code");
//            setRedirect_uri("http://localhost:8095/callback");
            setRedirect_uri("http://172.20.63.120:8095/callback");
        }};
        // oauth 서버에 http 통신으로 토큰 발행
        OAuthToken.response oauthToken = Unirest.post("http://localhost:8095/oauth/token")
                .header("Authorization","Basic "+encodingCredentials)
                .fields(request.getMapData())
                .asObject(OAuthToken.response.class).getBody();
        return oauthToken;
    }

}
