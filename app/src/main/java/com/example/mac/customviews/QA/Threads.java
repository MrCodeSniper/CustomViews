package com.example.mac.customviews.QA;

import java.util.concurrent.Semaphore;

/**
 *
 * 线程1，2交互输出1～100
 * Created by mac on 2018/4/16.
 */





//    class Thread1 extends Thread {
//
//        private Object resource;
//
//
//    public Thread1(Object resource) {
//        this.resource = resource;
//    }
//
//
//        @Override
//        public void run() {
//            synchronized (resource){
//                for(int i=1;i<=100;i++){
//                    if(i%2==1){
//                        System.out.println("Thread1----"+i);
//                        try {
//                            resource.wait();//挂起线程1
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        resource.notify();//唤起线程2
//                    }
//                }
//            }
//
//        }
//    }
//
//
//    class Thread2 extends Thread {
//
//
//        private Object resource;
//
//        public Thread2(Object resource) {
//            this.resource = resource;
//        }
//
//        @Override
//        public void run() {
//            synchronized (resource){
//                for(int i=1;i<=100;i++){
//                    if(i%2==0){
//                        System.out.println("Thread2----"+i);
//                        resource.notify();//唤醒线程1
//
//                        try {
//                            resource.wait();//挂起线程2
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//        }
//    }



class Threads{

    Common_Thread[] thread_set;
    Semaphore[] mutex;//Semaphore是计数信号量 //每个acquire方法阻 //每个release方法增加一个许可证，这可能会释放一个阻塞的acquire方法



    public void init(){

        mutex = new Semaphore[10];
        mutex[0] = new Semaphore(1);
        for (int i = 1; i <= 9 ; i++) {
            mutex[i] = new Semaphore(0);
        }

        thread_set=new Common_Thread[10];

        Integer t=new Integer(0);

        for (int i=0;i<=9;i++){
            thread_set[i]=new Common_Thread(i);
        }


        for (int i = 0; i <= 9; i++)
            thread_set[i].start();
    }






    class  Common_Thread extends Thread{

        private int thread_id;


        public Common_Thread(int thread_id) {
            this.thread_id = thread_id;
        }


        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                        mutex[thread_id].acquire();
                        System.out.println("线程" + thread_id + "在执行");
                        mutex[(thread_id+1)%10].release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}











class Test {
    public static void main(String[] args) {

//        Object resource=new Object();
//
//        new Thread1(resource).start();
//        new Thread2(resource).start();



        Threads threads=new Threads();
        threads.init();
    }


}














