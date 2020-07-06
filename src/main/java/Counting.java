public class Counting {
    public static void main(String[] args) throws InterruptedException {
        class Counter {
            private int count = 0;
            public synchronized void increment() {
                count += 1;
            }
            public int getCount() {
                return count;
            }
        }
        final Counter counter = new Counter();
        class CountingThread extends Thread {
            public void run(){
                for (int i =1; i <= 1000000; i++){
                    counter.increment();
                }
            }
        }
        CountingThread t1 = new CountingThread();
        CountingThread t2 = new CountingThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(counter.getCount());
    }
}
