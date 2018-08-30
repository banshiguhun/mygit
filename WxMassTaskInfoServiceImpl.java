package com.lenovo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.common.entity.cms.WxMassTaskInfo;
import com.lenovo.common.paginator.Page;
import com.lenovo.common.paginator.PageRequest;
import com.lenovo.common.service.cms.IWxMassTaskInfoService;
import com.lenovo.dao.IWxMassTakeInfoDao;

@Service(value="wxMassTaskInfoServiceImpl")
public class WxMassTaskInfoServiceImpl implements IWxMassTaskInfoService {
	private static final Logger logger = Logger.getLogger(WxMassTaskInfoServiceImpl.class);

	@Autowired
	private IWxMassTakeInfoDao iWxMassTakeInfoDao;


	/** 分页+模糊查询 WxMassTaskInfo 列表 */
	@Override
	public Map<String, Object> findWxMassTaskInfo(int pageNum, int pageSize, String key) {
		logger.info("### method=findWxMassTaskInfo ### params ={ pageNum : " + pageNum + " }{ pageSize : " + pageSize+ " }{ key : " + key + " }");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", 0);
		result.put("list", new ArrayList<WxMassTaskInfo>());
		result.put("pageNum", pageNum);
		result.put("pageSize", pageSize);
		result.put("totalPage", 0);
		try {
			// 参数效验
			if (pageNum > 0 && pageSize > 0) {
				// 查询
				PageRequest pr = new PageRequest(pageNum, pageSize);
				Page<WxMassTaskInfo> page = iWxMassTakeInfoDao.findWxMassTaskInfoPage(pr, key);
				if (page != null) {
					if (pageNum > page.getTotalPages()) {
						// 查询最后一页数据
						pr = new PageRequest(page.getTotalPages(), pageSize);
						page = iWxMassTakeInfoDao.findWxMassTaskInfoPage(pr, key);
						result.put("pageNum", page.getTotalPages());
					}
					result.put("totalPage", page.getTotalPages());
					result.put("total", page.getTotalElements());
					List<WxMassTaskInfo> list = page.getResult();
					//遍历list 修改WxMassTaskInfo的openids 返回个数
					for (WxMassTaskInfo wxMassTaskInfo : list) {
						if (null != wxMassTaskInfo.getOpenids() && wxMassTaskInfo.getOpenids() != "") {
							String[] string = wxMassTaskInfo.getOpenids().split(",");
                             wxMassTaskInfo.setOpenids(Integer.toString(string.length));
						}else{
							wxMassTaskInfo.setOpenids(null);
						}
					logger.info("###openids###"+wxMassTaskInfo.getOpenids());	
					}
					//加入
					if (list != null && list.size() > 0) {
						result.put("list", list);
					}
				}
			}
		} catch (Exception e) {
			logger.error("### findWxMassTaskInfo occur error ###", e);
		}
		logger.info("### result ### " + JSON.toJSONString(result));
		return result;
	}

	/** 添加WxMassTaskInfo 参数WxMassTaskInfo */
	@Override
	public int insertWxMassTaskInfo(WxMassTaskInfo wxMassTaskInfo) {
		logger.info("### method=insertWxMassTaskInfo ### params=" + JSONObject.toJSONString(wxMassTaskInfo));
		int status = -1;
		try {
			status = iWxMassTakeInfoDao.insertWxMassTaskInfo(wxMassTaskInfo);
		} catch (Exception e) {
			logger.error("### insertWxMassTaskInfo error###", e);
		}
		return status;
	}

	/** 删除 WxMassTaskInfo 根据id */
	@Override
	public int deleteWxMassTaskInfo(Integer id) {
		logger.info(String.format("### method=deleteWxClassManage ### params[%s]", id));
		int status = -1;
		try {
			status = iWxMassTakeInfoDao.deleteWxMassTaskInfo(id);
		} catch (Exception e) {
			logger.error("### method=deleteWxClassManage error ###", e);
		}
		return status;
	}
	/**修改WxMassTaskInfo状态 */
	@Override
	public int updateWxMassTaskInfo(Integer id, Integer status) {
		logger.info("### method=updateWxMassTaskInfo ### params:{ id=" +id+" status=" +status+"}");
		int result = -1;
		try {
			result = iWxMassTakeInfoDao.updateWxMassTaskInfo(id, status);
		} catch (Exception e) {
			logger.error("### method=updateWxMassTaskInfo error ###", e);
		}
		return result;
	}
	
	/** 根据id查询 WxMassTaskInfo 列表 */
	@Override
	public WxMassTaskInfo queryWxMassTaskInfo(Integer id) {
		logger.info("### method=queryWxMassTaskInfo ###  id=" +id);
		WxMassTaskInfo wxMassTaskInfo =null;
		try {
			wxMassTaskInfo = iWxMassTakeInfoDao.queryWxMassTaskInfo(id);
		} catch (Exception e) {
			logger.error("### method=queryWxMassTaskInfo error ###", e);
		}
		return wxMassTaskInfo;
	}
}
