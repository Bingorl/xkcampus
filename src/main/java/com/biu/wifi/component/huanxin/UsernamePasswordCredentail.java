package com.biu.wifi.component.huanxin;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * UsernamePasswordCredentail
 *
 * @author HJD
 */
public class UsernamePasswordCredentail extends Credentail {
    @Autowired
    private static HuanXinService huanXinService;

    public UsernamePasswordCredentail(String tokenKey1, String tokenKey2) {
        super(tokenKey1, tokenKey2);
        // TODO Auto-generated constructor stub
    }

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(UsernamePasswordCredentail.class);

    private static URL USERNAMEPASSWORD_TOKEN_URL = null;

    @Override
    protected URL getUrl() {
        return USERNAMEPASSWORD_TOKEN_URL;
    }

    @Override
    protected GrantType getGrantType() {
        return GrantType.PASSWORD;
    }

    @Override
    public Token getToken() {
        if (null == token || token.isExpired()) {
            token = new Token(huanXinService.getToken(), null);
        }

        return token;
    }
}
