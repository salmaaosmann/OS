/*
    Made by: karim ellozy, ahmed abd elaziz, salma ashraf CS-S7
 */

import java.util.*;

import static java.lang.Math.ceil;

class Process {
    int ID;
    int arrival;
    int burst;
    int remTime;
    int waitTime;

    public Process(int processId, int arrivalTime, int burstTime) {
        this.ID = processId;
        this.arrival = arrivalTime;
        this.burst = burstTime;
        this.remTime = burstTime;
        this.waitTime = 0;
    }
}

class SJF {

    static class completed {
        int time;
        int id;

        public completed(int time, int id) {
            this.time = time;
            this.id = id;
        }
    }

    public static void schedule() {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter no of process:");
        int n = sc.nextInt();
        int[] processID = new int[n];
        int[] arrivalTime = new int[n];
        int[] execTime = new int[n];
        int[] CompleteTime = new int[n];
        int[] turnAroundTime = new int[n];
        int[] waitingTime = new int[n];
        boolean[] done = new boolean[n];
        int st = 0, tot = 0;
        float avgWaitTime = 0, avgTurnAroundTime = 0;

        for (int i = 0; i < n; i++) {
            System.out.println("enter process " + (i + 1) + " arrival time:");
            arrivalTime[i] = sc.nextInt();
            System.out.println("enter process " + (i + 1) + " execution time:");
            execTime[i] = sc.nextInt();
            processID[i] = i + 1;
            done[i] = false;
        }

        while (true) {
            int c = n, min = 999;
            if (tot == n) break;
            for (int i = 0; i < n; i++) {

                if ((arrivalTime[i] <= st) && (!done[i]) && (execTime[i] < min)) {
                    min = execTime[i];
                    c = i;
                }
            }
            if (c == n)
                st++;
            else {
                CompleteTime[c] = st + execTime[c];
                st += execTime[c];
                turnAroundTime[c] = CompleteTime[c] - arrivalTime[c];
                waitingTime[c] = turnAroundTime[c] - execTime[c];
                done[c] = true;
                tot++;
            }
        }

        List<completed> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(new completed(CompleteTime[i], i + 1));
        }
        order.sort(Comparator.comparingInt(sjf -> sjf.time));
        System.out.println();
        System.out.print("Execution order : [");
        for (int i = 0; i < n; i++) {
            if (i != n - 1) System.out.print(" " + order.get(i).id + ",");
            else System.out.print(" " + order.get(i).id);
        }
        System.out.println(" ]");

        System.out.println("\nprocessID  arrival execution  complete turn waiting");
        for (int i = 0; i < n; i++) {
            avgWaitTime += waitingTime[i];
            avgTurnAroundTime += turnAroundTime[i];
            System.out.println("\t" + processID[i] + "\t\t " + arrivalTime[i] + "\t\t  " + execTime[i] +
                    "\t\t   " + CompleteTime[i] + "\t\t  " + turnAroundTime[i] + "\t\t" + waitingTime[i]);
        }
        System.out.println("\naverage total around time is " + (avgTurnAroundTime / n));
        System.out.println("average waiting time is " + (avgWaitTime / n));
        sc.close();
    }
}

class SRTF {
    public static void schedule() {
        List<Process> processes = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        System.out.println("enter how many processes to be handled: ");
        int cntIn = input.nextInt();
        for (int i = 0; i < cntIn; i++) {
            System.out.println("input process Id, arrival time and burst time separated by a space: ");
            int processId = input.nextInt();
            int arrivalTime = input.nextInt();
            int burstTime = input.nextInt();
            processes.add(new Process(processId, arrivalTime, burstTime));
        }
        int size = processes.size();
        List<Process> waitingQueue = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        int currentTime = 0;

        while (!processes.isEmpty() || !waitingQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrival <= currentTime) {
                waitingQueue.add(processes.remove(0));
            }

            waitingQueue.sort(Comparator.comparingInt(p -> p.remTime));

            if (!waitingQueue.isEmpty()) {
                Process curr = waitingQueue.get(0);
                curr.remTime--;

                if (curr.remTime == 0) {
                    curr = waitingQueue.remove(0);
                    curr.waitTime = currentTime - curr.arrival - curr.burst + 1;
                    curr.remTime = currentTime - curr.arrival + 1;
                    completed.add(curr);
                }
            }

            currentTime++;
        }

        int totalWait = 0, totalTA = 0;

        System.out.println("Processes Execution Order:");
        for (Process process : completed) {
            System.out.print("Process " + process.ID + " ");
            totalTA += process.remTime;
            totalWait += process.waitTime;
        }
        System.out.println("\n\nProcess    Waiting time    Turnaround Time:");
        for (Process process : completed) {
            System.out.println("   P" + process.ID + "          " + process.waitTime + "              " + process.remTime);
        }

        double avgWait = (double) totalWait / size;
        double avgTA = (double) totalTA / size;

        System.out.println("\nAverage Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTA);
    }
}

class Priority {

    static int[] burstTime;
    static int[] priority;
    static int[] arrivalTime;
    static String[] processId;
    static int numberOfProcess;

    static void sort(int[] at, int[] bt, int[] prt, String[] pid) {

        int temp;
        String stemp;
        for (int i = 0; i < numberOfProcess; i++) {
            for (int j = 0; j < numberOfProcess - i - 1; j++) {
                if (at[j] > at[j + 1]) {
                    temp = at[j];
                    at[j] = at[j + 1];
                    at[j + 1] = temp;
                    temp = bt[j];
                    bt[j] = bt[j + 1];
                    bt[j + 1] = temp;
                    temp = prt[j];
                    prt[j] = prt[j + 1];
                    prt[j + 1] = temp;
                    stemp = pid[j];
                    pid[j] = pid[j + 1];
                    pid[j + 1] = stemp;
                }
                if (at[j] == at[j + 1]) {
                    if (prt[j] > prt[j + 1]) {
                        temp = at[j];
                        at[j] = at[j + 1];
                        at[j + 1] = temp;

                        temp = bt[j];
                        bt[j] = bt[j + 1];
                        bt[j + 1] = temp;

                        temp = prt[j];
                        prt[j] = prt[j + 1];
                        prt[j + 1] = temp;

                        stemp = pid[j];
                        pid[j] = pid[j + 1];
                        pid[j + 1] = stemp;

                    }
                }
            }
        }
    }

    public static void schedule() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of Process for Scheduling: ");
        numberOfProcess = input.nextInt();
        burstTime = new int[numberOfProcess];
        priority = new int[numberOfProcess];
        arrivalTime = new int[numberOfProcess];
        processId = new String[numberOfProcess];
        String st = "P";
        for (int i = 0; i < numberOfProcess; i++) {
            processId[i] = st.concat(Integer.toString(i + 1));
            System.out.print("Enter the burst time   for Process - " + (i + 1) + " : ");
            burstTime[i] = input.nextInt();
            System.out.print("Enter the arrival time for Process - " + (i + 1) + " : ");
            arrivalTime[i] = input.nextInt();
            System.out.print("Enter the priority     for Process - " + (i + 1) + " : ");
            priority[i] = input.nextInt();
        }
        int[] finishTime = new int[numberOfProcess];
        int[] bt = burstTime.clone();
        int[] at = arrivalTime.clone();
        int[] prt = priority.clone();
        String[] pid = processId.clone();
        int[] waitingTime = new int[numberOfProcess];
        int[] turnAroundTime = new int[numberOfProcess];

        sort(at, bt, prt, pid);

        finishTime[0] = at[0] + bt[0];
        turnAroundTime[0] = finishTime[0] - at[0];
        waitingTime[0] = turnAroundTime[0] - bt[0];

        for (int i = 1; i < numberOfProcess; i++) {
            finishTime[i] = bt[i] + finishTime[i - 1];
            turnAroundTime[i] = finishTime[i] - at[i];
            waitingTime[i] = turnAroundTime[i] - bt[i];
        }
        float sum = 0;
        for (int n : waitingTime) {
            sum += n;
        }
        float averageWaitingTime = sum / numberOfProcess;

        sum = 0;
        for (int n : turnAroundTime) {
            sum += n;
        }
        float averageTurnAroundTime = sum / numberOfProcess;

        System.out.println();
        System.out.print("Execution order : [");
        for (int i = 0; i < numberOfProcess; i++) {
            if (i != numberOfProcess - 1) System.out.print(" " + pid[i] + ",");
            else System.out.print(" " + pid[i]);
        }
        System.out.println(" ]");
        System.out.println("ProcessId\tBurstTime\tArrivalTime\tPriority\tFinishTime\tWaitingTime\tTurnAroundTime");
        for (int i = 0; i < numberOfProcess; i++) {
            System.out.println("\t " + pid[i] + "\t\t\t" + bt[i] + "\t\t\t" + at[i] + "\t\t\t" + prt[i] + "\t\t\t" + finishTime[i] + "\t\t\t" + waitingTime[i] + "\t\t\t" + turnAroundTime[i]);
        }
        System.out.println();
        System.out.println();
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turn Around Time: " + averageTurnAroundTime);
    }
}


class agProcess {
    int pID;
    int AgFactor;
    int remBurstT;
    int priorityNum;
    int waitingTime;
    int turnaroundTime;
    int randFunction;
    int quantum;
    int arriveT;
    int burstT;
    int startT;
    int endT;

    public agProcess(int pID, int arrivalTime, int burstT, int priorityNumber, int quantumTime) {
        this.pID = pID;
        this.arriveT = arrivalTime;
        this.burstT = burstT;
        this.remBurstT = burstT;
        this.priorityNum = priorityNumber;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.quantum = quantumTime;
        this.AgFactor = calculateAgFactor();
    }

    public agProcess(int pID, int arrivalTime, int burstT, int priorityNumber, int quantumTime, int ag, int rand) {
        this.pID = pID;
        this.arriveT = arrivalTime;
        this.burstT = burstT;
        this.remBurstT = burstT;
        this.priorityNum = priorityNumber;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.quantum = quantumTime;
        this.AgFactor = ag;
        this.randFunction = rand;
    }

    private int calculateAgFactor() {
        Random rand = new Random();
//        Scanner input = new Scanner(System.in);
        int rf = rand.nextInt();
//        int rf =  input.nextInt();
        this.randFunction = rf;
        if (rf < 10)
            return rf + arriveT + burstT;
        else if (rf > 10)
            return 10 + arriveT + burstT;
        else
            return priorityNum + arriveT + burstT;
    }
}

class AG {
    ArrayList<agProcess> processes = new ArrayList<>();
    ArrayList<agProcess> processesCopy = new ArrayList<>();
    PriorityQueue<agProcess> q = new PriorityQueue<>(Comparator.comparingInt((agProcess o) -> o.AgFactor).thenComparingInt(o -> o.arriveT));
    Queue<agProcess> arrived = new LinkedList<>();
    ArrayList<agProcess> executed = new ArrayList<>();
    ArrayList<agProcess> die = new ArrayList<>();
    ArrayList<ArrayList<Integer>> quantumChanges = new ArrayList<>();
    double avgTA, avgWT;

    public AG() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();
        System.out.print("Enter Round Robin Time Quantum: ");
        int quantumTime = scanner.nextInt();
        ArrayList<agProcess> processes = new ArrayList<>();
        Vector<agProcess> processes1 = new Vector<>();
        agProcess[] processes2 = new agProcess[numProcesses];

        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("Enter details for Process " + i + ":");
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Priority Number: ");
            int priorityNumber = scanner.nextInt();
            agProcess p = new agProcess(i, arrivalTime, burstTime, priorityNumber, quantumTime);

            processes.add(p);
            processes1.add(p);
            processes2[i - 1] = p;
        }
        this.processes = processes;

        for (agProcess p : processes) {
            processesCopy.add(new agProcess(p.pID, p.arriveT, p.burstT, p.priorityNum, p.quantum, p.AgFactor, p.randFunction));
        }
    }

    public void updateQuantum() {
        ArrayList<Integer> quantumUpdate = new ArrayList<>();
        for (agProcess p : processes) {
            quantumUpdate.add(p.quantum);
        }
        quantumChanges.add(quantumUpdate);
    }

    void schedule() {
        int burst_Time = 0;
        agProcess currprocess = null;
        updateQuantum();
        int cnt = 0;
        for (int pNum = 0; !arrived.isEmpty() || pNum < processes.size() || currprocess != null; ) {

            while (pNum < processes.size() && processes.get(pNum).arriveT == cnt) {
                arrived.add(processes.get(pNum));
                q.add(processes.get(pNum));
                pNum++;
            }

            if (!arrived.isEmpty()) {
                if (currprocess == null) {
                    currprocess = arrived.poll();
                    q.remove(currprocess);
                    currprocess.startT = cnt;
                    burst_Time = currprocess.burstT;

                }

                if (ceil(0.5 * (currprocess.quantum)) <= (burst_Time - currprocess.burstT)
                        && !q.isEmpty() && q.peek().AgFactor < currprocess.AgFactor) {
                    currprocess.quantum += (currprocess.quantum - (burst_Time - currprocess.burstT));
                    updateQuantum();
                    arrived.add(currprocess);
                    q.add(currprocess);
                    currprocess.endT = cnt;
                    executed.add(currprocess);
                    currprocess = q.poll();
                    arrived.remove(currprocess);
                    assert currprocess != null;
                    currprocess.startT = cnt;
                    burst_Time = currprocess.burstT;
                }

            }
            if (currprocess != null) {
                currprocess.burstT--;
                if (currprocess.burstT == 0) {
                    currprocess.endT = cnt + 1;
                    currprocess.quantum = 0;
                    updateQuantum();
                    currprocess.turnaroundTime = currprocess.endT - currprocess.arriveT;
                    avgTA += currprocess.turnaroundTime;
                    currprocess.waitingTime = currprocess.turnaroundTime - processesCopy.get(processes.indexOf(currprocess)).burstT;
                    avgWT += currprocess.waitingTime;
                    executed.add(currprocess);
                    die.add(currprocess);
                    currprocess = null;
                }

            }
            if (currprocess != null && currprocess.quantum == burst_Time - currprocess.burstT) {
                currprocess.endT = cnt + 1;
                executed.add(currprocess);
                double sum = 0.0;
                int n = 0;
                for (agProcess p : arrived) {
                    sum += p.quantum;
                    if (p.quantum != 0)
                        n++;
                }
                currprocess.quantum += (int) ceil((sum / n) * 0.1);
                updateQuantum();
                q.add(currprocess);
                arrived.add(currprocess);
                currprocess = null;
            }
            cnt++;
        }
        printResult();
    }

    void printResult() {
        System.out.println("Quantum Update: ");
        for (ArrayList<Integer> quantumChange : quantumChanges) {
            System.out.print("( ");
            for (int j = 0; j < quantumChange.size(); j++) {
                if (j < quantumChange.size() - 1)
                    System.out.print(quantumChange.get(j) + ", ");
                else
                    System.out.print(quantumChange.get(j));
            }
            System.out.println(" )");

        }
        System.out.println();
        System.out.println("Execution Order");
        for (agProcess p : executed) {
            System.out.print("p" + p.pID + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Process ID\tArrivalTime\t  BurstTime\t  PriorityNumber\t    RandomFunction\t    AgFactor");
        die.sort(Comparator.comparingInt(p -> p.pID));
        for (agProcess p : die) {
            System.out.println("p" + p.pID + "\t\t\t\t" + p.arriveT + "\t\t\t\t" + processesCopy.get(processes.indexOf(p)).burstT + "\t\t\t\t" +
                    p.priorityNum + "\t\t\t\t" + p.randFunction + "\t\t\t\t" + p.AgFactor);
        }
        System.out.println();
        System.out.println("AverageTurnAroundTime\tAverageWaitingTime");
        avgWT /= processes.size();
        avgTA /= processes.size();
        System.out.println(avgTA + "\t\t\t\t\t\t" + avgWT);
        System.out.println();

    }
}

class Main {
    public static void main(String[] args) {
        System.out.println("Choose the scheduling algorithm:");
        System.out.println("1.Shortest- Job First (SJF)");
        System.out.println("2.Shortest- Remaining Time First (SRTF)");
        System.out.println("3.Priority ");
        System.out.println("4.AG");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 1) {
            SJF.schedule();
        } else if (choice == 2) {
            SRTF.schedule();
        } else if (choice == 3) {
            Priority.schedule();
        } else if (choice == 4) {
            AG ag = new AG();
            ag.schedule();
        }
    }
}
