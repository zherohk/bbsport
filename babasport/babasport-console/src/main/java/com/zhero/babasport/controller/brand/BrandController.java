package com.zhero.babasport.controller.brand;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhero.babasport.pojo.page.Pagination;
import com.zhero.babasport.pojo.product.Brand;
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
	
	/**
	 * 品牌列表分页
	 * @param name
	 * @param isDisplay
	 * @param pageNo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list.do")
	public String list(String name,@RequestParam(defaultValue = "1") Integer isDisplay, Integer pageNo, Model model) throws Exception {
		//默认查询可用品牌
//		if (null == isDisplay) {
//			isDisplay = 1;
//		}
		//查询条件回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		model.addAttribute("pageNo", pageNo);
//		List<Brand> brands = brandService.selectBrandListNoPage(name, isDisplay);
		Pagination pagination = brandService.selectBrandListByPage(name, isDisplay, pageNo);
		model.addAttribute("pagination", pagination);
//		model.addAttribute("brands", brands);
		return "brand/list";
	}
	
	/**
	 * 跳转修改页面
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edit.do")
	public String edit(Long id, Model model) throws Exception {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	
	/**
	 * 更新品牌信息
	 * @param brand
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update.do")
	public String update(Brand brand) throws Exception {
		brandService.updateBrand(brand);
		return "redirect:list.do";
	}
	
	/**
	 * 跳转添加品牌信息页面
	 * @return
	 */
	@RequestMapping("/add.do")
	public String add() {
		return "brand/add";
	}
	
	/**
	 * 添加品牌信息
	 * @param brand
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save.do")
	public String save(Brand brand) throws Exception {
		brandService.saveBrand(brand);
		return "redirect:list.do";
	}
	
	/**
	 * 批量删除品牌
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteBatchBrand.do")
	public String deleteBatchBrand(Long[] ids) throws Exception {
		brandService.deleteBatchBrand(ids);
		//删除时需要带上条件,因此我们需要保证数据共享
		return "forward:list.do";
	}
	
	/**
	 * 单个删除指定品牌
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteById")
	public String deleteById(Long id) throws Exception {
		brandService.deleteById(id);
		return "forward:list.do";
	}
}
