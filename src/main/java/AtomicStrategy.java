import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:  andy.xwt
 * Date:    2019-08-18 15:49
 * Description:
 * 使用原子类的好处就是，是非阻塞式的线程最终不会被阻塞，而是一直尝试CAS操作。
 * 在下面的代码中，两个线程并没有存在等待/通知的关系。只是在逻辑上处理了打印语句。
 *
 */


class AtomicStrategy implements PrintStrategy {

    AtomicInteger mAtomicInteger = new AtomicInteger(0);

    @Override
    public void print() {
        new Thread(new EvenRunnable()).start();
        new Thread(new OddRunnable()).start();
    }

    //偶数打印线程
    private class EvenRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                int number = mAtomicInteger.get();
                if (!(number <= MAX_RANGE)) break;
                if ((number & 1) == 0) {//为偶数则打印
                    System.out.println("偶数--->" + number);
                    mAtomicInteger.getAndIncrement();//自增
                }
            }
        }
    }

    //奇数打印线程
    private class OddRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                int number = mAtomicInteger.get();
                if (!(number <= MAX_RANGE)) break;
                if ((number & 1) != 0) {//为奇数则打印
                    System.out.println("奇数--->" + number);
                    mAtomicInteger.getAndIncrement();//自增
                }
            }
        }
    }
}
