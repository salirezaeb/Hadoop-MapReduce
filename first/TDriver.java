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
        job.setMapOutputValueClass(FloatWritable.class);
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

/*
        System.out.println("---------------------1----------------------");
        System.out.println(reader.readLine());
        System.out.println("---------------------2----------------------");
        System.out.println(reader.readLine());
        System.out.println("---------------------3----------------------");
        System.out.println(reader.readLine());
        System.out.println("---------------------4----------------------");
        System.out.println(reader.readLine());
        System.out.println("---------------------5----------------------");
        System.out.println(reader.readLine());
*/

        String likesLine = reader.readLine();
        System.out.println("####################\n####################");
        while (likesLine != null) {
		
	
	String like = null;
	String android = null;
	String iphone = null;
	String webApp = null;
	String retweet = null;
    
            String[] first = likesLine.split("\\s+");
            String key = first[0];
            String value = first[1];

            if (key.contains("Android"))
                android = value;
            else if (key.contains("Iphone"))
                iphone = value;
            else if (key.contains("Like"))
                like = value;
            else if (key.contains("Retweet"))
                retweet = value;
            else if (key.contains("WebApp"))
                webApp = value;
	    


//            retweet = reader.readLine().split("\\s+")[1];
  
            String[] second = reader.readLine().split("\\s+");
            String key2 = second[0];
            String value2 = second[1];

            if (key2.contains("Android"))
                android = value2;
            else if (key2.contains("Iphone"))
                iphone = value2;
            else if (key2.contains("Like"))
                like = value2;
            else if (key2.contains("Retweet"))
                retweet = value2;
            else if (key2.contains("WebApp"))
                webApp = value2;

//            webApp = reader.readLine().split("\\s+")[1];

            String[] third = reader.readLine().split("\\s+");
            String key3 = third[0];
            String value3 = third[1];

            if (key3.contains("Android"))
                android = value3;
            else if (key3.contains("Iphone"))
                iphone = value3;
            else if (key3.contains("Like"))
                like = value3;
            else if (key3.contains("Retweet"))
                retweet = value3;
            else if (key3.contains("WebApp"))
                webApp = value3;

//            android = reader.readLine().split("\\s+")[1];

            String[] forth = reader.readLine().split("\\s+");
            String key4 = forth[0];
            String value4 = forth[1];

            if (key4.contains("Android"))
                android = value4;
            else if (key4.contains("Iphone"))
                iphone = value4;
            else if (key4.contains("Like"))
                like = value4;
            else if (key4.contains("Retweet"))
                retweet = value4;
            else if (key4.contains("WebApp"))
                webApp = value4;


//            iphone = reader.readLine().split("\\s+")[1];

            String[] fifth = reader.readLine().split("\\s+");
            String key5 = fifth[0];
            String value5 = fifth[1];

            if (key5.contains("Android"))
                android = value5;
            else if (key5.contains("Iphone"))
                iphone = value5;
            else if (key5.contains("Like"))
                like = value5;
            else if (key5.contains("Retweet"))
                retweet = value5;
            else if (key5.contains("WebApp"))
                webApp = value5;



            if (key.contains("Biden"))
                key = "Biden";
            else if (key.contains("Trump"))
                key = "Trump";
            else
                key = "Common";


            System.out.println(key + " " + like + " " + retweet + " " + webApp + " " + iphone + " " + android);

            likesLine = reader.readLine();

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

################### Run this commands in project root.

hdfs dfs -rm -r /user/h-user/output.txt

cd build && rm * -rf
mkdir first ; cd first
javac -classpath $HADOOP_HOME/share/hadoop/common/lib/commons-cli-1.2.jar:$HADOOP_HOME/share/hadoop/common/hadoop-common-3.2.2.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-3.2.2.jar:$HADOOP_HOME/share/hadoop/yarn/timelineservice/lib/commons-csv-1.0.jar -d . ../../src/first/TMapper.java ../../src/first/TReducer.java ../../src/first/TDriver.java
cd ..
jar -cvf first.jar -C first/ .
hadoop jar first.jar TDriver /home/h-user/hadoop/ /user/h-user/output.txt


*/