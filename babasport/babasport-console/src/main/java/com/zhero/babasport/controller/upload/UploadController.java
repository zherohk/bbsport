package com.zhero.babasport.controller.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.zhero.babasport.service.upload.UploadService;
import com.zhero.babasport.utils.constans.BbsConstans;

/**
 * @description 文件上传管理
 * @author zhero
 * @date 2017年11月9日
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
	
	@Resource
	private UploadService uploadService;
	
	/**
	 * 品牌管理附件上传
	 * @param pic
	 * @param request
	 * @param response
	 * @throws Exception 
	 
	@RequestMapping("/uploadPic.do")
	public void uploadPic(MultipartFile pic, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.将附件上传到upload目录下
		if (null != pic && pic.getSize() > 0) {
			//给附件改名
			String filename = pic.getOriginalFilename();
			String pre = String.valueOf(System.currentTimeMillis());
			//获取扩展名jsp
			String ext = FilenameUtils.getExtension(filename);
			String name = pre + "." + ext;
			//附件上传
			String realPath = request.getServletContext().getRealPath("/");
			String imgUrl = "/upload/" + name;//图片的相对路径
			String path = realPath + imgUrl;//绝对路径
			File file = new File(path);
			pic.transferTo(file);
			//2.响应结果
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("allUrl", imgUrl);
			jsonObject.put("imgUrl", imgUrl);
			System.out.println(jsonObject.toString());
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(jsonObject.toString());
		}
	}*/
	
	/**
	 * 品牌管理附件上传到FastDFS
	 * @param pic
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/uploadPic.do")
	public void uploadPic(MultipartFile pic, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.将附件上传到upload目录下
		if (null != pic && pic.getSize() > 0) {
			byte[] file_buff = pic.getBytes();//图片的字节码数组
			String filename = pic.getOriginalFilename();//图片名称
			String path = uploadService.uploadPicToFastDFS(file_buff, filename);
			String allUrl = BbsConstans.IMG_URL + path;//完整路径
			//2.响应结果
			JSONObject jsonObject = new JSONObject();
			//图片回显
			jsonObject.put("allUrl", allUrl);
			jsonObject.put("imgUrl", path);//保存到数据库
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(jsonObject.toString());
		}
	}
	
	/**
	 * 商品图片上传
	 * @param pics
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/uploadPics.do")
	@ResponseBody
	public List<String> uploadPics(@RequestPart MultipartFile[] pics) throws Exception {
		//将商品图片上传到fastDFS
		if (null != pics && pics.length > 0) {
			List<String> list = new ArrayList<String>();
			for (MultipartFile pic : pics) {
				String path = uploadService.uploadPicToFastDFS(pic.getBytes(), pic.getOriginalFilename());
				String url = BbsConstans.IMG_URL + path;
				list.add(url);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * Kindedit附件上传
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/uploadFck.do")
	public void uploadFck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取附件
		MultipartRequest multipartRequest = (MultipartRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		if (null != fileMap && fileMap.size() > 0) {
			Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
			for (Entry<String, MultipartFile> entry : entrySet) {
				MultipartFile pic = entry.getValue();
				//附件上传
				String path = uploadService.uploadPicToFastDFS(pic.getBytes(), pic.getOriginalFilename());
				String url = BbsConstans.IMG_URL + path;
				//响应结果
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("url", url);
				//附件上传成功
				jsonObject.put("error", 0);
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().write(jsonObject.toString());
			}	
		}
	}
}
