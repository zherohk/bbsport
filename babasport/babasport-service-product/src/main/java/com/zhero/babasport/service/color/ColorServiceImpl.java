package com.zhero.babasport.service.color;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhero.babasport.mapper.product.ColorMapper;
import com.zhero.babasport.pojo.product.Color;
import com.zhero.babasport.pojo.product.ColorQuery;

/**
 * @description 颜色管理业务层接口
 * @author zhero
 * @date 2017年11月12日
 */
@Service("colorService")
public class ColorServiceImpl implements ColorService{

	@Resource
	private ColorMapper colorMapper;
	
	/*
	 * (non-Javadoc)
	 * @description 颜色信息
	 */
	@Override
	public List<Color> selectColorsAndParentIdNotZero() throws Exception {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0l);
		return colorMapper.selectByExample(colorQuery);
	}
}
