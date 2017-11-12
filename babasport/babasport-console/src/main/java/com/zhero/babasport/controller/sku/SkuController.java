package com.zhero.babasport.controller.sku;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhero.babasport.pojo.product.Sku;
import com.zhero.babasport.service.sku.SkuService;

/**
 * 
 * @description 库存管理
 * @author zhero
 * @date 2017年11月13日
 */
@Controller
@RequestMapping("/sku")
public class SkuController {

	@Resource
	private SkuService skuService;
	
	/**
	 * 查询该商品对应的库存信息
	 * @param productId
	 * @param model
	 * @return
	 */
	@RequestMapping("/list.do")
	public String list(Long productId, Model model) {
		List<Sku> skus = skuService.selectSkusByProductId(productId);
		model.addAttribute("skus", skus);
		return "sku/list";
	}
	
	/**
	 * 更新库存
	 * @param response
	 * @param sku
	 * @throws Exception
	 */
	@RequestMapping("/update.do")
	public void update(HttpServletResponse response, Sku sku) throws Exception {
		skuService.updateSku(sku);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "保存成功");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jsonObject.toString());
	}
}
