package com.zhero.babasport.service.upload;

import org.springframework.stereotype.Service;

import com.zhero.babasport.utils.fdfs.FastDFSUtils;

/**
 * @description 上传管理的service实现类
 * @author zhero
 * @date 2017年11月10日
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

	/*
	 * (non-Javadoc)
	 * @description 附件上传到FastDFS
	 */
	@Override
	public String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception {
		return FastDFSUtils.uploadPicToFastDFS(file_buff, filename);
	}

}
