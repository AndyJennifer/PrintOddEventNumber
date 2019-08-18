import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author:  andy.xwt
 * Date:    2019-08-18 15:49
 * Description: 使用concurrent下的lock接口有个好处就是逻辑清晰。支持公平锁与非公平锁。
 */


class LockStrategy implements PrintStrategy {

    private int number;
    private Lock lock = new ReentrantLock();
    private Condition evenCondition = lock.newCondition();
    private Condition oddCondition = lock.newCondition();


    @Override
    public void print() {
        new Thread(new EvenRunnable()).start();
        new Thread(new OddRunnable()).start();
    }

    //偶数打印线程
    private class EvenRunnable implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                while (number < MAX_RANGE) {
                    while ((number & 1) != 0) {//为奇数则阻塞当前线程
                        oddCondition.await();
                    }
                    System.out.println("偶数--->" + number);
                    number++;
                    evenCondition.signalAll();//唤醒奇数线程
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    //奇数打印线程
    private class OddRunnable implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                while (number < MAX_RANGE) {
                    while ((number & 1) == 0) {//为偶数则阻塞当前线程
                        evenCondition.await();
                    }
                    System.out.println("奇数--->" + number);
                    number++;
                    oddCondition.signalAll();//唤醒偶数线程
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
