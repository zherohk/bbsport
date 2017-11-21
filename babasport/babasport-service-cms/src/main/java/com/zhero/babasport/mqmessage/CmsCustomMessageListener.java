package com.zhero.babasport.mqmessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.zhero.babasport.pojo.product.Color;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.service.cms.CmsService;
import com.zhero.babasport.service.staticpage.StaticPageService;

/**
 * @description 自定义监听器
 * @author zhero
 * @date 2017年11月18日
 */
public class CmsCustomMessageListener implements MessageListener {

	@Resource
	private StaticPageService staticPageService;
	
	@Resource
	private CmsService cmsService;
	
	/*
	 * (non-Javadoc)
	 * @description 生成商品详情静态页
	 */
	@Override
	public void onMessage(Message message) {
		try {
			//获取消息
			ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
			String id = activeMQTextMessage.getText();
			System.out.println("cms productId: " + id);
			//处理业务
			Map<String,Object> rootMap = new HashMap<String,Object>();
			//商品信息,库存信息,颜色信息
			Product product = cmsService.selectProductById(Long.parseLong(id));
			rootMap.put("product", product);
			List<Sku> skus = cmsService.selectSkusByProductIdAndStockMoreThanZero(Long.parseLong(id));
			rootMap.put("skus", skus);
			Set<Color> colors = new HashSet<>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			rootMap.put("colors", colors);
			staticPageService.index(id, rootMap);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
