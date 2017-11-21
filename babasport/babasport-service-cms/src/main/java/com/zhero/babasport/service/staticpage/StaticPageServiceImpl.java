package com.zhero.babasport.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

	private Configuration configuration;

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.configuration = freeMarkerConfigurer.getConfiguration();
	}

	private ServletContext servletContext;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @description 生成静态页的入口方法
	 */
	@Override
	public void index(String id, Map<String, Object> rootMap) {
		try {
			// 将路径放到配置文件中,解决硬编码,通过注入FreeMarkerConfigurer对象,获取configuration,指定模板位置
			// 获取模板
			Template template = configuration.getTemplate("product.html");
			// 准备数据
			// 模板+数据--->输出
			String pathname = "html/product/" + id + ".html";
			String realPath = servletContext.getRealPath(pathname);
			File file = new File(realPath);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {//文件件为空,创建文件夹
				parentFile.mkdirs();
			}
			Writer out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			template.process(rootMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
