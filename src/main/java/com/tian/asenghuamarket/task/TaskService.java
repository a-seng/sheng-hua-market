package com.tian.asenghuamarket.task;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TaskService {

    private final DelayQueue<Task> delayQueue=new DelayQueue<>();

    @PostConstruct
    private void init(){
        ScheduledExecutorService executorService=new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("shchedule-pool-%d").daemon(true).build());
        executorService.execute(()->{
            while(true){
                try{
                    Task task = delayQueue.take();
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void addTask(Task task){
        if(delayQueue.contains(task)){
            return ;
        }
        delayQueue.add(task);
    }

    public void removeTask(Task task){
        delayQueue.remove(task);
    }
}
