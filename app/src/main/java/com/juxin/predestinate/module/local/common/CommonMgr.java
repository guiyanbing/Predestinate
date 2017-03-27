package com.juxin.predestinate.module.local.common;

import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.request.RequestParam;
import com.juxin.predestinate.module.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用，新版本
 * Created by Xy on 17/3/22.
 */
public class CommonMgr implements ModuleBase {

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

    /**
     * 游戏交互-请求转发。接口回调data为String类型。[注意]：加密请求添加了新的header，若服务器无法处理，谨慎调用该方法
     *
     * @param requestType   请求方式（GET/POST）
     * @param requestUrl    请求完整地址，不会进行再次拼接
     * @param isEnc         是否为加密请求
     * @param requestObject 请求体参数
     * @param complete      请求回调
     */
    public void CMDRequest(String requestType, boolean isEnc, String requestUrl, Map<String, Object> requestObject, RequestComplete complete) {
        RequestParam requestParam = new RequestParam();
        if ("POST".equalsIgnoreCase(requestType)) {
            requestParam.setRequestType(RequestParam.RequestType.POST);
            if (requestObject != null) requestParam.setPost_param(requestObject);
        } else if ("GET".equalsIgnoreCase(requestType)) {
            requestParam.setRequestType(RequestParam.RequestType.GET);
            if (requestObject != null) requestParam.setGet_param(requestObject);
        }
        requestParam.setNeedEncrypt(isEnc);
        if (isEnc) requestParam.setHead_param(getEncHeaderMap());

        UrlParam urlParam = UrlParam.CMDRequest;
        urlParam.resetHost(requestUrl);
        requestParam.setUrlParam(urlParam);

        requestParam.setRequestCallback(complete);
        ModuleMgr.getHttpMgr().request(requestParam);
    }

    /**
     * @return 获取游戏调用时自定义的请求头：safe-request
     */
    public Map<String, String> getEncHeaderMap() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("Accept", "application/jx-json");
        headerParams.put("Content-Type", "application/jx-json");
        return headerParams;
    }

    /**
     * 判断是否到达第二天,并存储
     *
     * @return true 达到第二天 并存储
     */
    public boolean checkDateAndSave(String key) {
        String recommendDate = PSP.getInstance().getString(key, "-1");
        if (recommendDate != null) {
            if (!recommendDate.equals(TimeUtil.getCurrentData())) {
                PSP.getInstance().put(key, TimeUtil.getCurrentData() + "");
                return true;
            }
        }
        return false;
    }

}