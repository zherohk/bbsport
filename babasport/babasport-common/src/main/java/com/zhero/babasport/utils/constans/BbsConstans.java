package com.zhero.babasport.utils.constans;
/**
 * @description 维护常量信息
 * @author zhero
 * @date 2017年11月10日
 */
public interface BbsConstans {

	//FastDFS图片服务器的地址
	public static final String IMG_URL = "http://192.168.200.128/";
	
	//用户的令牌
	public static final String CSESSION_ID = "CSESSIONID";
	
	//redis中用户信息标识前缀
	public static final String USER_SESSION = "USER_SESSION:";
	
	//redis中购物车信息标识前缀
	public static final String BUYER_CART = "BUYER_CART:";
	
	//cookie购物车
	public static final String COOKIE_BUYER_CART = "COOKIE_BUYER_CART";
}
