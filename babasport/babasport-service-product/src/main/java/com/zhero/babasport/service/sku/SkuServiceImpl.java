package com.zhero.babasport.service.sku;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhero.babasport.mapper.product.ColorMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.pojo.product.Color;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.pojo.product.SkuQuery;
/**
 * @description 库存管理的业务层实现类
 * @author zhero
 * @date 2017年11月13日
 */
@Service("skuService")
public class SkuServiceImpl implements SkuService {

	@Resource
	private SkuMapper skuMapper;
	
	@Resource
	private ColorMapper colorMapper;
	/*
	 * (non-Javadoc)
	 * @description 查询该商品对应的库存信息
	 */
	@Override
	public List<Sku> selectSkusByProductId(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		List<Sku> list = skuMapper.selectByExample(skuQuery);
		//设置颜色信息
		if (null != list && list.size() > 0) {
			for (Sku sku : list) {
				Color color = colorMapper.selectByPrimaryKey(sku.getColorId());
				sku.setColor(color);
			}
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @description 更新库存信息
	 */
	@Transactional
	@Override
	public void updateSku(Sku sku) throws Exception {
		skuMapper.updateByPrimaryKeySelective(sku);
	}

}
