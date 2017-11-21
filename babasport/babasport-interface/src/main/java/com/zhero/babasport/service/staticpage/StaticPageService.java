package com.zhero.babasport.service.staticpage;

import java.util.Map;

public interface StaticPageService {

	/**
	 * 生成静态页的入口
	 * @param id: 作为静态页的名称
	 * @param rootMap: 静态页需要的数据
	 */
	public void index(String id, Map<String,Object> rootMap);
}
