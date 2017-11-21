package com.zhero.babasport.utils.token;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhero.babasport.utils.constans.BbsConstans;

/**
 * @description sso单点登录,维护令牌的工具类
 * @author zhero
 * @date 2017年11月19日
 */
public class TokenUtils {

	public static String getCSESSIONID(HttpServletRequest req, HttpServletResponse resp) {
		//判断cookie中是否有令牌信息
		Cookie[] cookies = req.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (BbsConstans.CSESSION_ID.equals(cookie.getName())) {//存在
					return cookie.getValue();
				}
			}
		}
		//第一次登录,生成令牌信息,并保存到cookie中.
		String CSESSIONID = UUID.randomUUID().toString().replace("-", "");
		Cookie cookie = new Cookie(BbsConstans.CSESSION_ID, CSESSIONID);
		cookie.setMaxAge(60*60);
		cookie.setPath("/");
		resp.addCookie(cookie);
		return CSESSIONID;
	}
}
