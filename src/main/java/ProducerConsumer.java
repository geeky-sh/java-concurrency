import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {
    private String contents;
    private boolean available = false;
    private Lock alock = new ReentrantLock();
    private Condition condVar = alock.newCondition();

    public String get() {
        try {
            alock.lock();
            while (!available) {
                try {
                    condVar.await();
                } catch (InterruptedException e) {

                }
            }
            available = false;
            System.out.println("GET Contents are " + contents);
            condVar.signalAll();
        } finally {
            alock.unlock();
            return contents;
        }
    }
    public void put(String value) {
        try {
            alock.lock();
            while (available) {
                try {
                    condVar.await();
                } catch (InterruptedException e) {

                }
            }
            available = true;
            contents = value;
            System.out.println("PUT contents are " + contents);
            condVar.signalAll();
        } finally {
            alock.unlock();
        }
    }

    public static void main (String[] args) throws InterruptedException {
        ProducerConsumer pc = new ProducerConsumer();
        int limit = 10;
        Thread producer_1 = new Thread() {
            public void run(){
                for(int i = 1; i< limit; i++){
                    String content = String.format("producer 1 : %d", i);
                    pc.put(content);
                }
            }
        };
        Thread consumer_1 = new Thread() {
            public void run() {
                for(int i = 1; i< limit; i++) {
                    String content = pc.get();
                    System.out.println("consumer 1 + " + content);
                }
            }
        };

        Thread producer_2 = new Thread() {
            public void run(){
                for(int i = 1; i<limit; i++){
                    String content = String.format("producer 2 : %d", i);
                    pc.put(content);
                }
            }
        };
        Thread consumer_2 = new Thread() {
            public void run() {
                for(int i = 1; i<limit; i++) {
                    String content = pc.get();
                    System.out.println("consumer 2 + " + content);
                }
            }
        };

        producer_1.start(); consumer_1.start();
        producer_2.start(); consumer_2.start();

        producer_1.join(); consumer_1.join();
        producer_2.join(); consumer_2.join();
    }
}
