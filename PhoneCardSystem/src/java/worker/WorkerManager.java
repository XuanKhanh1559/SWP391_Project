package worker;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

public class WorkerManager implements ServletContextListener {
    private static final int NUM_WORKERS = 2;
    private List<Thread> workerThreads = new ArrayList<>();
    private List<JobWorker> workers = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[WorkerManager] Starting job workers...");
        
        for (int i = 0; i < NUM_WORKERS; i++) {
            JobWorker worker = new JobWorker("default");
            Thread thread = new Thread(worker, "JobWorker-" + (i + 1));
            thread.setDaemon(false);
            thread.start();
            
            workers.add(worker);
            workerThreads.add(thread);
            System.out.println("[WorkerManager] Started worker thread: " + thread.getName());
        }
        
        System.out.println("[WorkerManager] All " + NUM_WORKERS + " workers started successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[WorkerManager] Stopping job workers...");
        
        for (JobWorker worker : workers) {
            worker.stop();
        }
        
        for (Thread thread : workerThreads) {
            try {
                thread.join(5000);
                if (thread.isAlive()) {
                    System.err.println("[WorkerManager] Worker thread " + thread.getName() + " did not stop gracefully");
                }
            } catch (InterruptedException e) {
                System.err.println("[WorkerManager] Interrupted while waiting for worker thread: " + e.getMessage());
            }
        }
        
        System.out.println("[WorkerManager] All workers stopped");
    }
}
