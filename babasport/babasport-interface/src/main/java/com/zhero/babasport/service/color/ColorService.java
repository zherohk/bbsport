package com.zhero.babasport.service.color;

import java.util.List;

import com.zhero.babasport.pojo.product.Color;

/**
 * @description 颜色管理业务层接口
 * @author zhero
 * @date 2017年11月12日
 */
public interface ColorService {

	/**
	 * 颜色信息
	 * @return
	 * @throws Exception
	 */
	public List<Color> selectColorsAndParentIdNotZero() throws Exception;
}
