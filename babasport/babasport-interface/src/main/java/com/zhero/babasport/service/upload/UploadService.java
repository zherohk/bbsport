package com.zhero.babasport.service.upload;
/**
 * @description 上传管理的service接口
 * @author zhero
 * @date 2017年11月10日
 */
public interface UploadService {

	/**
	 * 附件上传到FastDFS
	 * @param file_buff
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception;
}
