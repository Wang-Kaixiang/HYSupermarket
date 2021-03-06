package com.alan.hysupermarket.service.impl;

import com.alan.hysupermarket.mapper.OrdersMapper;
import com.alan.hysupermarket.pojo.Orders;
import com.alan.hysupermarket.pojo.OrdersExample;
import com.alan.hysupermarket.pojo.OrdersItem;
import com.alan.hysupermarket.pojo.Users;
import com.alan.hysupermarket.service.IOrdersItemService;
import com.alan.hysupermarket.service.IOrdersService;
import com.alan.hysupermarket.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdersServiceImpl implements IOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IOrdersItemService ordersItemService;

    @Override
    public void add(Orders Orders) {
        ordersMapper.insert(Orders);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    public float add(Orders Orders, List<OrdersItem> ois) {
        float total = 0;
        add(Orders);
        if (false) {
            throw new RuntimeException();
        }

        for (OrdersItem oi : ois) {
            oi.setOID(Orders.getID());
            ordersItemService.update(oi);

        }

        return total;
    }

    @Override
    public void delete(long id) {
        ordersMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Orders Orders) {
        ordersMapper.updateByPrimaryKeySelective(Orders);
    }

    @Override
    public Orders get(long id) {

        return ordersMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Orders> list() {
        OrdersExample example = new OrdersExample();
        example.setOrderByClause("id desc");
        return ordersMapper.selectByExample(example);
    }

    @Override
    public List<Orders> list(long uid, String excludedStatus) {
        OrdersExample example = new OrdersExample();
        example.createCriteria().andUUIDEqualTo(uid).andSTATUSNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return ordersMapper.selectByExample(example);
    }

    public void setUser(List<Orders> os) {
        for (Orders o : os)
            setUser(o);
    }

    public void setUser(Orders Orders) {
        long uid = Orders.getUUID();
        Users u = usersService.get(uid);
        Orders.setUser(u);
    }
}
