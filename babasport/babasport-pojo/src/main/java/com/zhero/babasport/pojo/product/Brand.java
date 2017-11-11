package com.zhero.babasport.pojo.product;

import java.io.Serializable;

import com.zhero.babasport.utils.constans.BbsConstans;
/**
 * @description 品牌信息
 * @author zhero
 * @date 2017年11月8日
 */
@SuppressWarnings("serial")
public class Brand implements Serializable {

	private Long id; // 品牌id,bigint,主键
	private String name; // 品牌名称,不能为空
	private String description; // 描述
	private String imgUrl; // 图片url
	// private String web_site;
	private Integer sort; // 排序,越大越靠前
	private Integer isDisplay; // 是否可用,1可用,0不可

	public String getAllUrl() {//附件完整路径
		if (null != imgUrl) {
			return BbsConstans.IMG_URL + this.imgUrl;
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
}
