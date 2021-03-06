/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-03 20:38 创建
 */
package org.antframework.configcenter.test.manage;

import org.antframework.common.util.facade.EmptyResult;
import org.antframework.common.util.facade.Status;
import org.antframework.configcenter.facade.api.manage.PropertyValueManageService;
import org.antframework.configcenter.facade.order.manage.DeletePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.FindAppProfilePropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.QueryPropertyValueOrder;
import org.antframework.configcenter.facade.order.manage.SetPropertyValuesOrder;
import org.antframework.configcenter.facade.result.manage.FindAppProfilePropertyValueResult;
import org.antframework.configcenter.facade.result.manage.QueryPropertyValueResult;
import org.antframework.configcenter.test.AbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Ignore
public class PropertyValueManageServiceTest extends AbstractTest {
    @Autowired
    private PropertyValueManageService propertyValueManageService;

    @Test
    public void testSetPropertyValue() {
        SetPropertyValuesOrder order = new SetPropertyValuesOrder();
        order.setAppCode("scbfund");
        order.setProfileCode("dev");
        SetPropertyValuesOrder.KeyValue keyValue1 = new SetPropertyValuesOrder.KeyValue();
        keyValue1.setKey("collection.accNo");
        keyValue1.setValue("20170903200000000001");
        order.addKeyValue(keyValue1);
        SetPropertyValuesOrder.KeyValue keyValue2 = new SetPropertyValuesOrder.KeyValue();
        keyValue2.setKey("cashier.url");
        keyValue2.setValue("http://localhost:8080/cashier");
        order.addKeyValue(keyValue2);
        EmptyResult result = propertyValueManageService.setPropertyValues(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void tesDeletePropertyValue() {
        DeletePropertyValueOrder order = new DeletePropertyValueOrder();
        order.setAppCode("scbfund");
        order.setKey("collection.accNo");
        order.setProfileCode("dev");
        EmptyResult result = propertyValueManageService.deletePropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testFindAppProfilePropertyValue() {
        FindAppProfilePropertyValueOrder order = new FindAppProfilePropertyValueOrder();
        order.setAppCode("scbfund");
        order.setProfileCode("dev");
        FindAppProfilePropertyValueResult result = propertyValueManageService.findAppProfilePropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }

    @Test
    public void testQueryPropertyValue() {
        QueryPropertyValueOrder order = new QueryPropertyValueOrder();
        order.setPageNo(1);
        order.setPageSize(10);
        QueryPropertyValueResult result = propertyValueManageService.queryPropertyValue(order);
        checkResult(result, Status.SUCCESS);
    }
}
