/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-20 11:35 创建
 */
package org.antframework.configcenter.web.manager.biz.service;

import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.web.manager.dal.dao.ManagerAppDao;
import org.antframework.configcenter.web.manager.dal.dao.ManagerDao;
import org.antframework.configcenter.web.manager.dal.entity.Manager;
import org.antframework.configcenter.web.manager.dal.entity.ManagerApp;
import org.antframework.configcenter.web.manager.facade.order.AddManagerAppOrder;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 添加管理员与应用关联服务
 */
@Service(enableTx = true)
public class AddManagerAppService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private ManagerAppDao managerAppDao;

    @ServiceExecute
    public void execute(ServiceContext<AddManagerAppOrder, EmptyResult> context) {
        AddManagerAppOrder order = context.getOrder();

        Manager manager = managerDao.findLockByManagerCode(order.getManagerCode());
        if (manager == null) {
            throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), String.format("管理员[%s]不存在", order.getManagerCode()));
        }
        ManagerApp managerApp = managerAppDao.findLockByManagerCodeAndAppCode(order.getManagerCode(), order.getAppCode());
        if (managerApp == null) {
            managerApp = buildManagerApp(order);
            managerAppDao.save(managerApp);
        }
    }

    // 构建管理员与应用关联
    private ManagerApp buildManagerApp(AddManagerAppOrder addManagerAppOrder) {
        ManagerApp managerApp = new ManagerApp();
        BeanUtils.copyProperties(addManagerAppOrder, managerApp);
        return managerApp;
    }
}
