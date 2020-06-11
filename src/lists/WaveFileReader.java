package lists;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WaveFileReader {
    public final String levelsFile = "res/levels/waves.txt";
    public HashMap<Integer,ArrayList<String>> waveMap;
    public boolean reachedEnd = false;
    public int iterator = 0;

    public WaveFileReader()throws  Exception{
        this.waveMap = new HashMap<Integer, ArrayList<String>>();
        File file  = new File(levelsFile);
        Scanner sc = new Scanner(file);

        while (sc.hasNext()){
            String line = sc.nextLine();
            String[] lineParams = line.trim().split(",");
            if(waveMap.getOrDefault(Integer.parseInt(lineParams[0]),null)==null){
                waveMap.put(Integer.parseInt(lineParams[0]),new ArrayList<String>());
            }
            waveMap.get(Integer.parseInt(lineParams[0])).add(line);
        }
    }

    public ArrayList<String[]> next(){
        ArrayList<String[]> ret = new ArrayList<>();
        for(String dataLine : waveMap.get(iterator)){
            ret.add(dataLine.split(","));
        }
        ++iterator;
        if(waveMap.getOrDefault(iterator,null)==null){
            reachedEnd = true;
        }
        return ret;
    }

    public boolean hasNext(){
        if (reachedEnd){
            return false;
        }
        return true;
    }
}
