/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:02 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.common.PasswordUtils;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.facade.order.ModifyManagerPasswordOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 修改管理员密码服务
 */
@Service(enableTx = true)
public class ModifyManagerPasswordService {
    @Autowired
    private ManagerDao managerDao;

    @ServiceExecute
    public void execute(ServiceContext<ModifyManagerPasswordOrder, EmptyResult> context) {
        ModifyManagerPasswordOrder order = context.getOrder();

        Manager manager = managerDao.findLockByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
        manager.setPassword(PasswordUtils.digest(order.getNewPassword()));
        managerDao.save(manager);
    }
}
