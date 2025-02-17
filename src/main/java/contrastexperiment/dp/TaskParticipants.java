package contrastexperiment.dp;

import rate.SecureTaskParticipants;
import utils.sg.smu.securecom.protocol.Paillier;
import utils.sg.smu.securecom.utils.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:wbGuo
 * Date: 2023/7/14
 * task participants: send the current locations and vel to cp
 */
public class TaskParticipants {
    /**
     * the location of enter system
     */
    protected List<List<Integer>> startLocs = new ArrayList<>();

    public List<Integer> vel = new ArrayList<>();
    private static final int NUMBER = 111000;
    String pathItem = null;

    List<Integer> costs = new ArrayList<>();
    List<Integer> payments = new ArrayList<>();


    public TaskParticipants() {

    }

    public TaskParticipants(String p) throws ExecutionException, InterruptedException {
        pathItem = p;
        readData();
    }

    private void readData() {
        try {
            File file = new File(pathItem);
            BufferedReader readCsv = new BufferedReader(new FileReader(file));
            readCsv.readLine();
            String temp;
            while ((temp = readCsv.readLine()) != null) {
                List<Integer> startLoc = new ArrayList<>();
                String[] line = temp.split(",");
                startLoc.add((int) (Double.valueOf(line[1].split("'")[1]) * NUMBER));
                startLoc.add((int) (Double.valueOf(line[2].split("'")[1])* NUMBER));
//                System.out.println(startLoc.get(0) + " " + startLoc.get(1));
                startLocs.add(startLoc);
                costs.add(Integer.valueOf(line[3]));
                payments.add(Integer.valueOf(line[4]));
                vel.add(Integer.valueOf(line[5]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
