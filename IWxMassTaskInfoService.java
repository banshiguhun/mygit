package com.lenovo.common.service.cms;

import java.util.List;
import java.util.Map;

import com.lenovo.common.entity.cms.WxMassTaskInfo;
import com.lenovo.common.entity.cms.WxMsgInfo;

public interface IWxMassTaskInfoService {
	/** 分页+模糊查询 WxMassTaskInfo 列表 */
	public Map<String,Object> findWxMassTaskInfo(int pageNum,int pageSize,String key);
    /**添加WxMassTaskInfo  参数WxMassTaskInfo*/
	public int insertWxMassTaskInfo(WxMassTaskInfo wxMassTaskInfo);
	/**修改WxMassTaskInfo状态 */
	public int updateWxMassTaskInfo(Integer id, Integer status);
	/** 删除 WxMassTaskInfo 根据id */
	public int deleteWxMassTaskInfo(Integer id);
	/** 根据id查询 WxMassTaskInfo 列表 */
	public WxMassTaskInfo queryWxMassTaskInfo(Integer id);
}