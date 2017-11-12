package com.zhero.babasport.controller.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;
import com.zhero.babasport.pojo.product.Color;
import com.zhero.babasport.pojo.product.Product;
import com.zhero.babasport.service.brand.BrandService;
import com.zhero.babasport.service.color.ColorService;
import com.zhero.babasport.service.product.ProductService;

/**
 * @description 商品管理
 * @author zhero
 * @date 2017年11月12日
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	@Resource
	private ProductService productService;
	
	@Resource
	private BrandService brandService;
	
	@Resource
	private ColorService colorService;
	
	/**
	 * 商品列表查询(分页)
	 * @param name
	 * @param brandId
	 * @param isShow
	 * @param pageNo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list.do")
	public String list(String name, Long brandId, Boolean isShow, Integer pageNo, Model model) throws Exception {
		//初始化品牌下拉框
		List<Brand> brands = brandService.selectBrandListNoPage(null, 1);
		model.addAttribute("brands", brands);
		//商品列表
		Pagination pagination = productService.selectProductListByPage(name, brandId, isShow, pageNo);
		model.addAttribute("pagination", pagination);
		//查询条件回显
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		model.addAttribute("pageNo", pageNo);
		return "product/list";
	}
	
	/**
	 * 跳转添加页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/add.do")
	public String add(Model model) throws Exception {
		//初始化品牌信息
		List<Brand> brands = brandService.selectBrandListNoPage(null, 1);
		model.addAttribute("brands", brands);
		//初始化颜色信息
		List<Color> colors = colorService.selectColorsAndParentIdNotZero();
		model.addAttribute("colors", colors);
		return "product/add";
	}
	
	/**
	 * 添加商品信息
	 * @param product
	 * @return
	 */
	@RequestMapping("/save.do")
	public String save(Product product) throws Exception {
		productService.insertProduct(product);
		return "redirect:list.do";
	}
}
