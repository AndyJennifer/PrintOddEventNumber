/**
 * Author:  andy.xwt
 * Date:    2019-08-18 15:48
 * Description:
 * synchronized的使用有如下弊端：
 * 1. synchronized中一个锁对应一个同步队列与等待队列。所以在处理多个条件时，需要创建多个object。而concurrent包下的lock接口，一个锁可以对应多个等待队列。
 * 2. 因为要创建多个object来处理不同条件，所以在实际代码的处理过程中，代码的阅读性并没有lock那么直观。一不要小心也有可能就会造成死锁。
 *
 * synchronized的使用优势：
 * 作为java的亲儿子，Java SE 1.6为了减少synchronized使用中，获得锁和释放锁带来的性能消耗，引入了“偏向锁”和“轻量级锁”等一系列操作。
 *
 * 所以处理多线程时，看各位小伙伴喜欢使用哪种方式了。效率和阅读性由大家选择。
 */


class SyncPrintStrategy implements PrintStrategy {


    private final Object evenLock = new Object();
    private final Object oddLock = new Object();
    private int number;

    @Override
    public void print() {
        new Thread(new EvenRunnable()).start();
        new Thread(new OddRunnable()).start();
    }

    //偶数打印线程
    private class EvenRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (evenLock) {
                try {
                    while (number < MAX_RANGE) {
                        while ((number & 1) != 0) {//为奇数则阻塞当前线程
                            evenLock.wait();
                        }
                        System.out.println("偶数--->" + number);
                        number++;

                        /**
                         * 唤醒奇数线程，需要注意的是必须拥有了锁后，才能唤醒
                         */
                        synchronized (oddLock) {
                            oddLock.notifyAll();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //奇数打印线程
    private class OddRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (oddLock) {
                try {
                    while (number < MAX_RANGE) {
                        while ((number & 1) == 0) {//为偶数则阻塞当前线程
                            oddLock.wait();
                        }
                        System.out.println("奇数--->" + number);
                        number++;

                        /**
                         * 唤醒偶数线程，需要注意的是必须拥有了锁后，才能唤醒
                         */
                        synchronized (evenLock) {
                            evenLock.notifyAll();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
