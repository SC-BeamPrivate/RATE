package rate;

import utils.sg.smu.securecom.protocol.Paillier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:wbGuo
 * Date: 2023/7/14
 * Task requester: send the encrypted task location, budget and task time to cp.
 */
public class TR {
    /**
     * the paillier to encrypt
     */
    public Paillier pai = null;
    /**
     * the location of task
     */
    protected List<Double> taskLoc = new ArrayList<>();

    /**
     * the budget of task requester
     */
    public int budget;

    /**
     * the time of whole task.
     */
    public int taskTime;
    protected String pathInfo;

    /**
     * the encrypted location of task
     */
    protected BigInteger[] eTaskLoc = new BigInteger[2];

    private static final int NUMBER = 111000;

    /**
     * when you use this class, you can modify the TR to update the information, such as budget, taskTime and pathInfo.
     */
    public TR() {
        budget = 100;
        taskTime = 100;
        pathInfo = "res/T-Drive/T-Drive-4-info.csv";
    }

    /**
     * read data to form pattern
     */

    private void readData() {
        try {
            File file = new File(pathInfo);
            BufferedReader readCsv = new BufferedReader(new FileReader(file));
            readCsv.readLine();
            String[] temp = readCsv.readLine().split(",");
            taskLoc.add(Double.valueOf(temp[0].split("'")[1]));
            taskLoc.add(Double.valueOf(temp[1].split("'")[1]));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt task location
     * turn double to integer, multiply 10^5
     * because 10^5 is about equal to 111*1000
     * one longitude = 111km = 111000m
     */
    private void encryptInformation() {
        //KGC send the paillier to TR
        KGC kgc = new KGC();
        pai = kgc.getPai();

        int c = (int) Math.round(taskLoc.get(0) * NUMBER);
        int d = (int) Math.round(taskLoc.get(1) * NUMBER);
        eTaskLoc[0] = pai.encrypt(BigInteger.valueOf(c));
        eTaskLoc[1] = pai.encrypt(BigInteger.valueOf(d));
    }

    public BigInteger[] getETaskLoc() {
        readData();
        encryptInformation();
        return eTaskLoc;
    }
}
