package worker;

import dal.*;
import model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JobWorker implements Runnable {
    private static final int MAX_ATTEMPTS = 3;
    private static final long POLL_INTERVAL_MS = 1000;
    private volatile boolean running = true;
    private String queue;
    private Gson gson = new Gson();

    public JobWorker(String queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        JobDao jobDao = new JobDao();
        System.out.println("[JobWorker] Started worker for queue: " + queue);

        while (running) {
            try {
                Job job = jobDao.getNextJob(queue);
                
                if (job != null) {
                    System.out.println("[JobWorker] Processing job #" + job.getId());
                    processJob(job);
                } else {
                    Thread.sleep(POLL_INTERVAL_MS);
                }
            } catch (Exception e) {
                System.err.println("[JobWorker] Error in worker loop: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("[JobWorker] Stopped worker for queue: " + queue);
    }

    private void processJob(Job job) {
        JobDao jobDao = new JobDao();
        OrderIntentDao intentDao = new OrderIntentDao();
        
        try {
            JsonObject payloadJson = gson.fromJson(job.getPayload(), JsonObject.class);
            String type = payloadJson.get("type").getAsString();

            if ("CREATE_ORDER".equals(type)) {
                CreateOrderPayload payload = gson.fromJson(payloadJson.get("data").getAsString(), CreateOrderPayload.class);
                OrderDao orderDao = new OrderDao();
                
                int orderId = orderDao.createOrderFromJob(payload);
                
                OrderIntent intent = intentDao.getIntentByJobId(job.getId());
                if (intent != null) {
                    intentDao.updateStatus(intent.getId(), "COMPLETED", orderId, null);
                }
                
                jobDao.markCompleted(job.getId());
                System.out.println("[JobWorker] Job #" + job.getId() + " completed. Order #" + orderId + " created.");
                
            } else {
                throw new Exception("Unknown job type: " + type);
            }

        } catch (Exception e) {
            System.err.println("[JobWorker] Job #" + job.getId() + " failed: " + e.getMessage());
            e.printStackTrace();

            OrderIntent intent = intentDao.getIntentByJobId(job.getId());
            if (intent != null) {
                intentDao.updateStatus(intent.getId(), "FAILED", null, e.getMessage());
            }

            if (job.getAttempts() < MAX_ATTEMPTS) {
                jobDao.retryJob(job.getId());
                System.out.println("[JobWorker] Job #" + job.getId() + " will retry (attempt " + (job.getAttempts() + 1) + "/" + MAX_ATTEMPTS + ")");
            } else {
                jobDao.markFailed(job.getId(), e.getMessage());
                System.out.println("[JobWorker] Job #" + job.getId() + " failed permanently after " + MAX_ATTEMPTS + " attempts");
            }
        }
    }

    public void stop() {
        running = false;
    }
}
