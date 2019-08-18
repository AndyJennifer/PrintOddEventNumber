/**
 * Author:  andy.xwt
 * Date:    2019-08-18 15:45
 * Description:
 */


class PrintDemo {

    public static void main(String[] args) {
//        PrintStrategy printStrategy = new LockStrategy();
//        PrintStrategy printStrategy = new SyncPrintStrategy();
        PrintStrategy printStrategy = new AtomicStrategy();
        printStrategy.print();
    }
}
