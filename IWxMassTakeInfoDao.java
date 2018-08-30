package com.lenovo.dao;



import org.apache.ibatis.annotations.Param;

import com.lenovo.common.entity.cms.WxMassTaskInfo;
import com.lenovo.common.paginator.Page;
import com.lenovo.common.paginator.PageRequest;

public interface IWxMassTakeInfoDao {
	/** 分页+模糊查询 WxMassTaskInfo 列表 */
	public Page<WxMassTaskInfo> findWxMassTaskInfoPage(@Param("pageReuqest")PageRequest pageReuqest,@Param("key")String key);
    /**添加WxMassTaskInfo  参数WxMassTaskInfo*/
	public int insertWxMassTaskInfo(WxMassTaskInfo wxMassTaskInfo);
	/** 删除 WxMassTaskInfo 根据id */
	public int deleteWxMassTaskInfo(@Param("id")Integer id);
	/**修改WxMassTaskInfo状态 */
	public int updateWxMassTaskInfo(@Param("id")Integer id,@Param("status")Integer status);
	/** 根据id查询 WxMassTaskInfo 列表 */
	public WxMassTaskInfo queryWxMassTaskInfo(Integer id);
}
