package com.nettyrpc.test.app;

import com.nettyrpc.client.RpcProxy;
import com.nettyrpc.registry.ServiceDiscovery;
import com.nettyrpc.test.client.HelloService;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luxiaoxun on 2016-03-11.
 */
public class Benchmark {

    public static void main(String[] args) throws InterruptedException {

        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
        final RpcProxy rpcProxy = new RpcProxy(serviceDiscovery);

        int threadNum = 10;
        final int requestNum = 100;
        Thread[] threads = new Thread[threadNum];

        long startTime = System.currentTimeMillis();
        //benchmark for sync call
        for(int i = 0; i < threadNum; ++i){
            threads[i] = new Thread(new Runnable(){
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < requestNum; i++) {
                        final HelloService syncClient = rpcProxy.create(HelloService.class);
                        String result = syncClient.hello(Integer.toString(i));
                        if (!result.equals("Hello! " + i))
                            System.out.print("error = " + result);
                    }
                }
            });
            threads[i].start();
        }
        for(int i=0; i<threads.length;i++){
            threads[i].join();
        }
        long timeCost = (System.currentTimeMillis() - startTime);
        String msg = String.format("sync call total-time-cost:%sms, req/s=%s",timeCost,((double)(requestNum * threadNum)) / timeCost * 1000);
        System.out.println(msg);

    }
}
