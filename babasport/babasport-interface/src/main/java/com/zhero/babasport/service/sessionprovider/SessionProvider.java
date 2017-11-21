package com.zhero.babasport.service.sessionprovider;
/**
 * @description 维护用户信息
 * @author zhero
 * @date 2017年11月19日
 */
public interface SessionProvider {

	/**
	 * 将用户信息保存到Redis
	 * @param key
	 * @param username
	 */
	public void setAttribute(String key, String username);
	
	/**
	 * 从Redis中取出用户信息
	 * @param key
	 * @return
	 */
	public String getAttribute(String key);
}
