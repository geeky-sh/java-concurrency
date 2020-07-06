public class Uninterruptible {
    public static void main(String[] args) throws InterruptedException {
        final Object o1 = new Object();
        final Object o2 = new Object();
        Thread t1 = new Thread() {
            public void run() {
                System.out.println("t1 started");
                try {
                    synchronized (o1) {
                        System.out.println("acquired o1; t1 waiting for o2");
                        Thread.sleep(1000);
                        synchronized (o2) {
                            System.out.println("t1; acquired o1 and o2");
                        }
                    }
                }
                catch (InterruptedException e) {
                    System.out.println("t1 interrupted");
                }
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                System.out.println("t2 started");
                try {
                    synchronized (o2) {
                        System.out.println("acquired o2; t2 waiting for o1");
                        Thread.sleep(1000);
                        synchronized (o1) {
                            System.out.println("t2; acquired o1 and o2");
                        }
                    }
                }
                catch (InterruptedException e){
                    System.out.println("t2 interrupted");
                }
            }
        };
        t1.start(); t2.start();
        Thread.sleep(2000);
        t1.interrupt(); t2.interrupt();
        t1.join(); t2.join();
        System.out.println("I am ending the program now");
    }
}
