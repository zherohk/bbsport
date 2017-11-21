package com.zhero.babasport.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhero.babasport.pojo.user.Buyer;
import com.zhero.babasport.service.buyer.BuyerService;
import com.zhero.babasport.service.sessionprovider.SessionProvider;
import com.zhero.babasport.utils.token.TokenUtils;

/**
 * 
 * @description 用户登录
 * @author zhero
 * @date 2017年11月19日
 */
@Controller
public class LoginController {

	@Resource
	private SessionProvider sessionProvider;

	@Resource
	private BuyerService buyerService;

	/**
	 * 跳转登录页面
	 */
	@RequestMapping(value = "/login.aspx", method = { RequestMethod.GET })
	public String login() {
		return "login";
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @param ReturnUrl
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.aspx", method = { RequestMethod.POST })
	public String login(String username, String password, String ReturnUrl, HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		if ((null == username || "".equals(username)) && (null == password || "".equals(password))) {
			model.addAttribute("error", "请输入账户名和密码");
			return "login";
		}
		// 用户名不能为空
		if (null != username && !"".equals(username)) {
			// 密码不能为空
			if (null != password && !"".equals(password)) {
				// 用户名必须正确
				Buyer buyer = buyerService.selectBuyerByUserName(username);
				if (null != buyer) {
					// 密码必须正确
					if (encodePassword(password).equals(buyer.getPassword())) {
						// 验证通过,保存用户信息
						sessionProvider.setAttribute(TokenUtils.getCSESSIONID(req, resp), username);
						return "redirect:" + ReturnUrl;
					} else {
						model.addAttribute("error", "账户名与密码不匹配，请重新输入");
					}
				} else {
					model.addAttribute("error", "账户名与密码不匹配，请重新输入");
				}
			} else {
				model.addAttribute("error", "请输入密码");
			}
		} else {
			model.addAttribute("error", "请输入账户名");
		}
		return "login";
	}
	
	/**
	 * 判断用户是否登录
	 * @param callback
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//判断用户是否登录
		String flag = "0";
		String username = sessionProvider.getAttribute(TokenUtils.getCSESSIONID(req, resp));
		if (null != username) {//已登录
			flag = "1";
		}
		//springmvc中封装回调函数的对象
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(flag);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
	/**
	 * 密码加密。规则： MD5 + 十六进制 spring：盐值
	 * 
	 * @param password
	 * @return
	 */
	public String encodePassword(String password) {

		String algorithm = "MD5";
		char[] encodeHex = null;
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			// MD5加密后密文
			byte[] digest = instance.digest(password.getBytes());
			// 十六进制再加密一次
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(encodeHex);
	}
}
