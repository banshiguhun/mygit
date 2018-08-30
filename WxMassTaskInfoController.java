package com.lenovo.eas.im_wxadmin.controller.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lenovo.common.entity.cms.WxMassTaskInfo;
import com.lenovo.common.entity.cms.WxMsgInfo;
import com.lenovo.common.service.cms.IWxMassTaskInfoService;
import com.lenovo.eas.im_wxadmin.entity.ErrorCode;
import com.lenovo.eas.im_wxadmin.entity.ResponseObj;
import com.lenovo.eas.im_wxadmin.entity.Param.PageParam;
import com.lenovo.eas.im_wxadmin.service.sendmsg.ISendMassService;
import com.lenovo.eas.im_wxadmin.util.ParamVerfyUtil;
import com.lenovo.eas.im_wxadmin.util.ResponseObjUtil;

@Controller
@RequestMapping("/wxMassTaskOpt")
public class WxMassTaskInfoController {
	private static final Logger logger = Logger.getLogger(WxMassTaskInfoController.class);

	@Autowired
	private IWxMassTaskInfoService iWxMassTaskInfoService;

	@Autowired
	private ISendMassService iSendMassService;

	/**
	 * 分页+模糊获取任务推送记录列表
	 * 
	 * @param request
	 * @param start
	 * @param num
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "/findWxMassTaskInfo", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObj findWxMassTaskInfo(HttpServletRequest request, @RequestBody PageParam pp) {
		logger.info("### method==findWxMassTaskInfo ###params =" + JSON.toJSONString(pp));
		Map<String, Object> WxMassTaskInfos = new HashMap<String, Object>();
		try {
			if (null == pp || 1 == ParamVerfyUtil.checkPageParam(pp.getStart(), pp.getNum())) {
				return ResponseObjUtil.getResponseObj(null, ErrorCode.PARAM_FORMATTE_ERR, "参数错误");
			}
			WxMassTaskInfos = iWxMassTaskInfoService.findWxMassTaskInfo(pp.getStart(), pp.getNum(), pp.getKey());
			logger.info("### method==findWxMassTaskInfo ### result=" + JSON.toJSONString(WxMassTaskInfos));
		} catch (Exception e) {
			logger.error("### method==findWxMassTaskInfo occur error ###", e);
		}
		return ResponseObjUtil.getResponseObj(WxMassTaskInfos, 200, "");
	}

	/**
	 * 添加任务推送记录
	 * 
	 * @param request
	 * @param wxMassTaskInfo
	 * @return
	 */
	@RequestMapping(value = "/insertWxMassTaskInfo", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObj addMsgInfo(HttpServletRequest request, @RequestBody WxMassTaskInfo wxMassTaskInfo) {
		logger.info("### method==insertWxMassTaskInfo ###params=" + JSON.toJSONString(wxMassTaskInfo));
		int status =-1;
		try {
			
			status=iWxMassTaskInfoService.insertWxMassTaskInfo(wxMassTaskInfo);
		} catch (Exception e) {
			logger.error("### method==insertWxMassTaskInfo occur error ###", e);
		}
		return ResponseObjUtil.getResponseObj(null, status > 0 ? 200 : -1, "");
	}

	/**
	 * 根据id删除任务推送
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteWxMassTaskInfo", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObj deleteWxMassTaskInfo(HttpServletRequest request, Integer id) {
		logger.info("### deleteWxMassTaskInfo ### id= [" + id + "]");
		int status = -1;
		try {
			if (id == null || id <= 0) {
				return ResponseObjUtil.getResponseObj(null, ErrorCode.PARAM_FORMATTE_ERR, "参数错误");
			}
			status = iWxMassTaskInfoService.deleteWxMassTaskInfo(id);
		} catch (Exception e) {
			logger.error("### deleteWxMassTaskInfo error ###", e);
		}
		return ResponseObjUtil.getResponseObj(null, status > 0 ? 200 : -1, "");
	}

	/**
	 * 修改任务推送记录状态
	 * 
	 * @param request
	 * @param wxMassTaskInfo
	 * @return
	 */
	@RequestMapping(value = "/updateWxMassTaskInfo", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseObj updateWxMassTaskInfo(HttpServletRequest request,@RequestBody WxMassTaskInfo wxMassTaskInfo){
		logger.info("### method=updateWxMassTaskInfo ### params:"+JSON.toJSONString(wxMassTaskInfo));
		int result=-1;
		WxMassTaskInfo wxMassTask = null;
		try { 
			// 对传过来的数据进行校验
			if (wxMassTaskInfo.getId() == null || wxMassTaskInfo.getId() <= 0 || wxMassTaskInfo.getStatus()==null || wxMassTaskInfo.getStatus()<0 ) {
				return ResponseObjUtil.getResponseObj(null, ErrorCode.PARAM_FORMATTE_ERR, "参数错误");
			}
			//对id进行校验
			wxMassTask = iWxMassTaskInfoService.queryWxMassTaskInfo(wxMassTaskInfo.getId());
			if (wxMassTask == null) {
				 return ResponseObjUtil.getResponseObj(null, ErrorCode.PARAM_FORMATTE_ERR, "参数错误");
			}
			//对当前状态进行校验
			if (wxMassTask.getStatus() == 2) {
				return ResponseObjUtil.getResponseObj(null, ErrorCode.CLASS_MANAGE_REPEAT, "任务已结束");
			}
			final WxMassTaskInfo newWxMassTask = wxMassTask;
			if (wxMassTask.getStatus() == 0) {
				//修改状态为开始
				result = iWxMassTaskInfoService.updateWxMassTaskInfo(wxMassTaskInfo.getId(),wxMassTaskInfo.getStatus());
				// 创建一个线程
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							iSendMassService.sendGroupMsg(newWxMassTask);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} catch (Exception e) {
			logger.error("### updateWxMassTaskInfo error ###", e);
		}
		return ResponseObjUtil.getResponseObj(null, result > 0 ? 200 : -1, "");
	}

	
}
