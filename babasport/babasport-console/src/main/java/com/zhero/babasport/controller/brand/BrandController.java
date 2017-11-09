package com.zhero.babasport.controller.brand;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhero.babasport.pojo.product.Pagination;
import com.zhero.babasport.service.brand.BrandService;

/**
 * @description 品牌管理
 * @author zhero
 * @date 2017年11月8日
 */
@Controller
@RequestMapping("/brand")
public class BrandController {

	@Resource
	private BrandService brandService;
	
	@RequestMapping("/list.do")
	public String list(String name,@RequestParam(defaultValue = "1") Integer isDisplay, Integer pageNo, Model model) throws Exception {
		//默认查询可用品牌
//		if (null == isDisplay) {
//			isDisplay = 1;
//		}
		//查询条件回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
//		List<Brand> brands = brandService.selectBrandListNoPage(name, isDisplay);
		Pagination pagination = brandService.selectBrandListByPage(name, isDisplay, pageNo);
		model.addAttribute("pagination", pagination);
//		model.addAttribute("brands", brands);
		return "brand/list";
	}
}
