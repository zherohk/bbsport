package com.zhero.babasport.service.buyer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.ColorMapper;
import com.zhero.babasport.mapper.product.ProductMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.mapper.user.BuyerMapper;
import com.zhero.babasport.pojo.cart.BuyerCart;
import com.zhero.babasport.pojo.cart.BuyerItem;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.pojo.user.Buyer;
import com.zhero.babasport.pojo.user.BuyerQuery;
import com.zhero.babasport.utils.constans.BbsConstans;

import redis.clients.jedis.Jedis;
/**
 * @description 用户管理实现类
 * @author zhero
 * @date 2017年11月19日
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Resource
	private BuyerMapper buyerMapper;
	
	@Resource
	private SkuMapper skuMapper;
	
	@Resource
	private ProductMapper productMapper;
	
	@Resource
	private ColorMapper colorMapper;
	
	@Resource
	private Jedis jedis;
	/*
	 * (non-Javadoc)
	 * @description 通过用户名获取用户信息
	 */
	@Override
	public Buyer selectBuyerByUserName(String username) {
		BuyerQuery buyerQuery = new BuyerQuery();
		buyerQuery.createCriteria().andUsernameEqualTo(username);
		List<Buyer> buyers = buyerMapper.selectByExample(buyerQuery);
		if (null != buyers && buyers.size() >0) {
			return buyers.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @description 获取库存信息
	 */
	@Override
	public Sku selectSkuById(Long id) {
		Sku sku = skuMapper.selectByPrimaryKey(id);
		//填充商品信息,颜色信息
		sku.setProduct(productMapper.selectByPrimaryKey(sku.getProductId()));
		sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));
		return sku;
	}

	/*
	 * (non-Javadoc)
	 * @description 购物车保存到Redis
	 */
	@Override
	public void insertBuyerCartToRedis(String username, BuyerCart buyerCart) {
		List<BuyerItem> buyerItems = buyerCart.getBuyerItems();
		if (null != buyerItems && buyerItems.size() > 0) {
			//skuId,amount
			for (BuyerItem buyerItem : buyerItems) {
				Long skuId = buyerItem.getSku().getId();
				Integer amount = buyerItem.getAmount();
				//判断是否是同一款商品:key是否存在
				if (jedis.hexists(BbsConstans.BUYER_CART + username, String.valueOf(skuId))) {
					//在原有的基础上追加商品数量
					jedis.hincrBy(BbsConstans.BUYER_CART + username, String.valueOf(skuId), amount);
				} else {
					jedis.hset(BbsConstans.BUYER_CART + username, String.valueOf(skuId), String.valueOf(amount));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @description 从Redis中取购物车
	 */
	@Override
	public BuyerCart getBuyerCartFromRedis(String username) {
		Map<String, String> map = jedis.hgetAll(BbsConstans.BUYER_CART + username);
		Set<Entry<String, String>> entrySet = map.entrySet();
		BuyerCart buyerCart = new BuyerCart();
		for (Entry<String, String> entry : entrySet) {
			//创建购物项
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setAmount(Integer.valueOf(entry.getValue()));
			Sku sku = new Sku();
			sku.setId(Long.parseLong(entry.getKey()));
			buyerItem.setSku(sku);
			//将购物项装车
			buyerCart.addBuyerItem(buyerItem);
		}
		return buyerCart;
	}

}
