import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.*;

public class TDriver
        extends Configured implements Tool {
    public int run(String[] args) throws Exception {

        @SuppressWarnings("deprecation")
        Job job = new Job(getConf(), "First program");

        job.setMapperClass(TMapper.class);
        job.setReducerClass(TReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        for (int i = 0; i < args.length - 1; i++)
            FileInputFormat.addInputPath(job, new Path(args[i]));
        FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));

        boolean res = job.waitForCompletion(true);
        if (!res)
            return 1;

        resultPrinting(args[args.length - 1]);

        return 0;
    }

    public static void resultPrinting(String path) throws IOException {
        Configuration config = new Configuration();
        FileSystem file_system = FileSystem.get(config);
        RemoteIterator<LocatedFileStatus> fsri = file_system.listFiles(new Path(path), true);
        Path result_file = null;

        while (fsri.hasNext()) {
            LocatedFileStatus fileStatus = fsri.next();
            Path p = fileStatus.getPath();
            if (p.toString().contains("part-r-")) {
                result_file = p;
                break;
            }
        }
        FSDataInputStream stream = file_system.open(result_file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String[] countries = { "NewYork", "California" };
        Map<String, Integer> map = new HashMap<>();
        String line = reader.readLine();
        System.out.println("####################\n####################");
        while (line != null) {
            String[] keyValue = line.split("\\s+");
            String key = keyValue[0];
            int value = Integer.parseInt(keyValue[1]);
            map.put(key, value);
            line = reader.readLine();
        }
        for (String country : countries) {
            int all = 0;
            double common_p = 0;
            double trump_p = 0;
            double biden_p = 0;

            Integer all_tweets = map.get(country);

            if (all_tweets != null) {
                all = all_tweets;
                common_p = map.get(country + "_" + "Common") / (double) all;
                trump_p = map.get(country + "_" + "Trump") / (double) all;
                biden_p = map.get(country + "_" + "Biden") / (double) all;
            }

            System.out.println(country + "   " + common_p + "   " +
                    biden_p + "   " + trump_p + "    " + all);
        }
        System.out.println("####################\n####################");

    }

    public static void main(String[] args) throws Exception {
        int jobStatus = ToolRunner.run(new TDriver(), args);
        if (jobStatus == 0)
            System.out.println(jobStatus + ": Completed");
        else
            System.out.println(jobStatus + ": Something went wrong");
    }
}

/*
 * 
 * ################### Run this commands in project root.
 * 
 * hdfs dfs -rm -r /user/h-user/output.txt
 * 
 * cd build && rm * -rf
 * mkdir second ; cd second
 * javac -classpath
 * $HADOOP_HOME/share/hadoop/common/lib/commons-cli-1.2.jar:$HADOOP_HOME/share/
 * hadoop/common/hadoop-common-3.2.2.jar:$HADOOP_HOME/share/hadoop/mapreduce/
 * hadoop-mapreduce-client-core-3.2.2.jar:$HADOOP_HOME/share/hadoop/yarn/
 * timelineservice/lib/commons-csv-1.0.jar -d . ../../src/second/TMapper.java
 * ../../src/second/TReducer.java ../../src/second/TDriver.java
 * cd ..
 * jar -cvf second.jar -C second/ .
 * hadoop jar second.jar TDriver /home/h-user/hadoop/ /user/h-user/output.txt
 * 
 * 
 */