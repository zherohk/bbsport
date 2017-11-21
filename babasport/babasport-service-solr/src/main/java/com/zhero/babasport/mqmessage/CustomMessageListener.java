package com.zhero.babasport.mqmessage;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.zhero.babasport.service.search.SearchService;
/**
 * @description 自定义消息监听器
 * @author zhero
 * @date 2017年11月18日
 */
public class CustomMessageListener implements MessageListener {

	@Resource
	private SearchService searchService;
	
	/*
	 * (non-Javadoc)
	 * @description 商品信息保存到索引库中
	 */
	@Override
	public void onMessage(Message msg) {
		try {
			ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) msg;
			//取出消息
			String id = activeMQTextMessage.getText();
			System.out.println("solr----id: " + id);
			//消费消息
			searchService.insertProductToSolr(Long.parseLong(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
