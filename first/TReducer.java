import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;

public class TReducer
        extends Reducer<Text, FloatWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<FloatWritable> values,
            Reducer<Text, FloatWritable, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {
        // counting for each key
        float counter = 0;
        for (FloatWritable value : values) {
            counter += value.get();
        }
        context.write(key, new IntWritable(Math.round(counter)));
    }
}
