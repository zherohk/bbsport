package com.zhero.babasport.utils.converter;

import org.springframework.core.convert.converter.Converter;
/**
 * @description 自定义类型转换器
 * @author zhero
 * @date 2017年11月9日
 */
public class CustomParamsConverter implements Converter<String, String> {

	/*
	 * (non-Javadoc)
	 * @description 去空格
	 */
	@Override
	public String convert(String source) {
		if (null != source && !"".equals(source)) {
			source = source.trim();
			//如果输入的是一组空格.
			if (!"".equals(source)) {
				return source;
			}
		}
		return null;
	}

}
