package com.zhero.babasport.controller.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.Color;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.service.cms.CmsService;
import com.zhero.babasport.service.search.SearchService;

/**
 * @description 前台检索
 * @author zhero
 * @date 2017年11月13日
 */
@Controller
public class SearchController {

	@Resource
	private SearchService searchService;
	
	@Resource
	private CmsService cmsService;
	
	/**
	 * 系统首页
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	/**
	 * 根据关键字查询商品
	 * @param keyword
	 * @param pageNo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/Search")
	public String search(String keyword, Long brandId, String price, Integer pageNo, Model model) throws Exception {
		//品牌信息
		List<Brand> brands = searchService.selectBrandsFromRedis();
		model.addAttribute("brands", brands);
		//商品
		Pagination pagination = searchService.selectProductsForPortal(keyword,brandId,price,pageNo);
		model.addAttribute("pagination", pagination);
		//条件回显
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		//筛选条件展示
		Map<String,String> map = new HashMap<>();
		if (null != brandId) {
			map.put("品牌: ", searchService.selectBrandNameByIdFromRedis(brandId));
		}
		if (null != price && !"".equals(price)) {
			String[] split = price.split("-");
			if (split.length == 2) {
				map.put("价格: ", price);
			} else {
				map.put("价格: ", price + "以上");
			}
		}
		model.addAttribute("map", map);
		return "search";
	}
	
	/**
	 * 前往商品详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/product/detail")
	public String detail(Long id,Model model) {
		//商品信息
		Product product = cmsService.selectProductById(id);
		//库存信息
		List<Sku> skus = cmsService.selectSkusByProductIdAndStockMoreThanZero(id);
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		//颜色信息去重
		Set<Color> set = new HashSet<>();
		for (Sku sku : skus) {
			set.add(sku.getColor());
		}
		model.addAttribute("colors", set);
		return "product";
	}
}
