package com.lovesickness.o2o.other;

import java.util.ArrayList;
import java.util.List;

//4个CPU 求素数10000000000
public class FaceTest1 {

    public static void main(String[] args) throws InterruptedException {

        List<MyThread> myThreads = new ArrayList<>();
        myThreads.add(new MyThread(0L, 2500000000L));
        myThreads.add(new MyThread(2500000000L, 5000000000L));
        myThreads.add(new MyThread(5000000000L, 7500000000L));
        myThreads.add(new MyThread(7500000000L, 10000000000L));

        for (Thread myThread:myThreads) {
            myThread.run();
        }
        for (Thread myThread:myThreads) {
            myThread.join();
        }
        int sum = 0;
        for (MyThread myThread:myThreads) {
            sum+=myThread.getMySum();
        }
        System.out.println(sum);
    }
}

class MyThread extends Thread {
    private int mySum;
    private long startNum;
    private long endNum;

    public MyThread(long startNum, long endNum) {
        this.startNum = startNum;
        this.endNum = endNum;
    }

    @Override
    public void run() {
        for (long i = startNum; i <= endNum; i++) {
            if (judgePrimeNum(i)) {
                mySum++;
            }
        }
    }

    public boolean judgePrimeNum(long num) {
        if (num==1||num==0){
            return false;
        }
        for (long i = 2; i < num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public int getMySum() {
        return mySum;
    }

    public void setMySum(int mySum) {
        this.mySum = mySum;
    }

    public long getStartNum() {
        return startNum;
    }

    public void setStartNum(long startNum) {
        this.startNum = startNum;
    }

    public long getEndNum() {
        return endNum;
    }

    public void setEndNum(long endNum) {
        this.endNum = endNum;
    }
}
