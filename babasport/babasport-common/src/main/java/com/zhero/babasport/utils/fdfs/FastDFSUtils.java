package com.zhero.babasport.utils.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import com.zhero.babasport.utils.constans.BbsConstans;
/**
 * @description FastDFS工具类
 * @author zhero
 * @date 2017年11月10日
 */
public class FastDFSUtils {
	
	/**
	 * 将附件上传到FastDFS上
	 * @param file_buff
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String uploadPicToFastDFS(byte[] file_buff, String filename) throws Exception {
		//加载fastDFS的配置文件
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		//初始化配置文件
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		//创建跟踪服务器的客户端
		TrackerClient client = new TrackerClient();
		//通过跟踪服务器的客户端获取服务端
		TrackerServer trackServer = client.getConnection();
		//根据跟踪服务器创建存储服务器客户端
		StorageClient1  storageClient1 = new StorageClient1(trackServer, null);
		//附件上传,返回附件的路径
		String file_ext_name = FilenameUtils.getExtension(filename);
		//path不是完整的路径,需要拼接服务器地址.
		 String path = storageClient1.upload_appender_file1(file_buff, file_ext_name, null);
		 return path;
	}
	
	/**
	 * 删除服务器中的文件
	 * @param imgUrl
	 * @throws Exception
	 */
	public static void delPic(String imgUrl) throws Exception {
		//加载配置文件
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		//初始化配置文件
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		//创建跟踪服务器客户端
		TrackerClient client = new TrackerClient();
		//通过跟踪服务器的客户端获取服务端
		TrackerServer trackerServer = client.getConnection();
		//根据跟踪服务端获取存储服务器客户端
		StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);
		//删除指定图片
		String[] ss = imgUrl.split(",");
		for (String s : ss) {
			String file_id = StringUtils.difference(BbsConstans.IMG_URL, s);
			int row = storageClient1.delete_file1(file_id);
			if (row == 2) {
				System.out.println("文件不存在!");
			} else if (row != 0) {
				System.out.println("删除失败!");
			}
		}
	}
}
