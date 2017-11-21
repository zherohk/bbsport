package com.zhero.babasport.service.cms;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.ColorMapper;
import com.zhero.babasport.mapper.product.ProductMapper;
import com.zhero.babasport.mapper.product.SkuMapper;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.pojo.product.SkuQuery;
@Service("cmsService")
public class CmsServiceImpl implements CmsService {

	@Resource
	private ProductMapper productMapper;
	
	@Resource
	private SkuMapper skuMapper;
	
	@Resource
	private ColorMapper colorMapper;
	
	/*
	 * (non-Javadoc)
	 * @description 获取商品信息
	 */
	@Override
	public Product selectProductById(Long id) {
		return productMapper.selectByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * @description 获取商品对应的库存信息
	 */
	@Override
	public List<Sku> selectSkusByProductIdAndStockMoreThanZero(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
		List<Sku> skus = skuMapper.selectByExample(skuQuery);
		int count = skuMapper.countByExample(skuQuery);
		//填充颜色信息
		if (null != skus && count > 0) {
			for (Sku sku : skus) {
				sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));
			}
			return skus;
		}
		return null;
	}

}
