package com.trx0eth7.practise.process.example;

/**
 * @author vasilev
 */
public class ProcessHandleExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.printf("Start main thread, pid: %s\n", ProcessHandle.current().pid());
        var executionA = new Execution();
        var executionB = new Execution();
        var executionC = new Execution();

        var subProcessA = new Thread(executionA, "Thread-A");
        var subProcessB = new Thread(executionB, "Thread-B");
        var subProcessC = new Thread(executionC, "Thread-C");

        subProcessA.setDaemon(true);
        subProcessB.setDaemon(true);
        subProcessC.setDaemon(true);

        subProcessA.start();
        subProcessB.start();
        subProcessC.start();

        ProcessHandle.current().destroyForcibly();

//        var rootProcess = ProcessHandle.of(1);
//
//        System.out.println("Root: " + ;

        Thread.sleep(120000);
    }

    static class Execution implements Runnable {

        @Override
        public void run() {
            var currentThread = Thread.currentThread();
            var threadName = currentThread.getName();
            var currentProcess = ProcessHandle.current();
            var pid = currentProcess.pid();
            var ppid = currentProcess.parent().map(ProcessHandle::pid).orElseThrow();

            var info = currentProcess.info();

            System.out.printf("Thread name: %s, process id: %s, parent process id: %s\n", threadName, pid, ppid);

            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                currentThread.interrupt();
            }
        }
    }
}