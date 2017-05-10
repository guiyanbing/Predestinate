package com.juxin.predestinate.bean.file;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 上传文件： 语音，图片及视频
 */
public class UpLoadResult extends BaseData {

	// ---------- 老缘分吧 -----------
	private String file_s_path;    // 短地址
	private String file_http_path; // 完整地址
	private String file_md5;

	// ---------- 小友 -----------
	/**语音**/
	private String amr;//amr的存储路径
	private String mp3;//mp3的存储路径

	/**图片**/
	private String httpPathPic; //图片http链接地址

	/**视频**/
	private String thumb; //略缩图地址
	private String video; //视频地址

	@Override
	public void parseJson(String jsonStr) {
		JSONObject jsonObject = getJsonObject(jsonStr);

		// ---------- 老缘分吧 -----------
		this.setFile_s_path(jsonObject.optString("file_s_path"));
		this.setFile_http_path(jsonObject.optString("file_http_path"));
		this.setFile_md5(jsonObject.optString("md5"));

		// ---------- 小友 -----------
		// 语音
		if(!jsonObject.isNull("amr")){
			this.setAmr(jsonObject.optString("amr"));
		}
		if(!jsonObject.isNull("mp3")){
			this.setMp3(jsonObject.optString("mp3"));
		}

		// 图片
		if(!jsonObject.isNull("pic")){
			this.setHttpPathPic(jsonObject.optString("pic"));
		}

		// 视频
		if(!jsonObject.isNull("thumb")){
			this.setThumb(jsonObject.optString("thumb"));
		}
		if(!jsonObject.isNull("video")){
			this.setVideo(jsonObject.optString("video"));
		}
	}

	public String getFile_s_path() {
		return file_s_path;
	}

	public void setFile_s_path(String file_s_path) {
		this.file_s_path = file_s_path;
	}

	public String getFile_http_path() {
		return file_http_path;
	}

	public void setFile_http_path(String file_http_path) {
		this.file_http_path = file_http_path;
	}

	public String getFile_md5() {
		return file_md5;
	}

	public void setFile_md5(String file_md5) {
		this.file_md5 = file_md5;
	}

	public String getHttpPathPic() {
		return httpPathPic;
	}

	public void setHttpPathPic(String httpPathPic) {
		this.httpPathPic = httpPathPic;
	}

	public String getAmr() {
		return amr;
	}

	public void setAmr(String amr) {
		this.amr = amr;
	}

	public String getMp3() {
		return mp3;
	}

	public void setMp3(String mp3) {
		this.mp3 = mp3;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
}