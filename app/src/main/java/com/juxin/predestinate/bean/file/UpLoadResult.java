package com.juxin.predestinate.bean.file;


import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 上传文件： 语音，图片及视频
 */
public class UpLoadResult extends BaseData {

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