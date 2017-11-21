package com.zhero.babasport.service.sessionprovider;

import javax.annotation.Resource;

import com.zhero.babasport.utils.constans.BbsConstans;

import redis.clients.jedis.Jedis;

public class SessionProviderImpl implements SessionProvider {

	@Resource
	private Jedis jedis;
	
	private Integer expUser = 60;//默认用户过期时间
	
	public void setExpUser(Integer expUser) {
		this.expUser = expUser;
	}

	/*
	 * (non-Javadoc)
	 * @description 用户信息保存到Redis中
	 */
	@Override
	public void setAttribute(String key, String username) {
		jedis.set(BbsConstans.USER_SESSION + key, username);
		//设置用户信息过期时间
		jedis.expire(BbsConstans.USER_SESSION + key, 60*expUser);
	}

	/*
	 * (non-Javadoc)
	 * @description 从Redis中取出用户信息
	 */
	@Override
	public String getAttribute(String key) {
		String username = jedis.get(BbsConstans.USER_SESSION + key);
		if (null != username) {
			//重置用户过期时间
			jedis.expire(BbsConstans.USER_SESSION + key, 60*expUser);
			return username;
		}
		return null;
	}

}
