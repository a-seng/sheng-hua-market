package com.tian.asenghuamarket.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Task implements Delayed,Runnable {
    private final String id;
    private final long start;

    public Task(String id,long delayInMilliseconds){
        this.id=id;
        this.start=System.currentTimeMillis()+delayInMilliseconds;
    }

    public String getId(){return id;}

    public long getDelay(TimeUnit unit){
        long diff=this.start-System.currentTimeMillis();
        return unit.convert(diff,TimeUnit.MICROSECONDS);
    }

    public int compareTo(Delayed o){
        return (int) (this.start-((Task)o).start);
    }

    public boolean equals(Object o){
        if(this == o )return true;
        if(o== null)return false;
        if(!(o instanceof Task t)){
            return false;
        }
        return this.id.equals(t.getId());
    }

    public int hashCode(){
        return this.id.hashCode();
    }
}
