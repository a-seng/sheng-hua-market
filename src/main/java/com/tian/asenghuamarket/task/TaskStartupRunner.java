package com.tian.asenghuamarket.task;

import com.tian.asenghuamarket.Dto.Order;
import com.tian.asenghuamarket.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskStartupRunner implements ApplicationRunner
{
    public static final Long UN_PAID_ORDER_EXPIRE_TIME=30L;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TaskService taskService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("is_deleted",0);
        map.put("order_status",0);
        List<Order> orders = orderMapper.selectByMap(map);
        for (Order order : orders) {
            Date date = order.getCreateTime();
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            LocalDateTime add = instant.atZone(zoneId).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expire = add.plusMinutes(UN_PAID_ORDER_EXPIRE_TIME);
            if(expire.isBefore(now)){
                taskService.addTask(new OrderUnPaidTask(order.getOrderId(),0));
            }else{
                long delay = ChronoUnit.MILLIS.between(now, expire);
                taskService.addTask(new OrderUnPaidTask(order.getOrderId(), delay));
            }

        }
    }
}
