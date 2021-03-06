package com.gambarra.money.api.token;

import com.gambarra.money.api.config.property.MoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    @Autowired
    private MoneyApiProperty moneyApiProperty;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken,
                                             MethodParameter methodParameter,
                                             MediaType mediaType,
                                             Class<? extends HttpMessageConverter<?>> aClass,
                                             ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        HttpServletRequest req = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        HttpServletResponse res = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();

        addRefreshTokenOnCookie(refreshToken, req, res);

        removeBodyRefreshToken(token);

        return oAuth2AccessToken;
    }

    private void removeBodyRefreshToken(DefaultOAuth2AccessToken token) {
        token.setRefreshToken(null);
    }

    private void addRefreshTokenOnCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(moneyApiProperty.getSecurity().isEnableHttps());
        refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
        refreshTokenCookie.setMaxAge(5000000);
        res.addCookie(refreshTokenCookie);
    }


}
