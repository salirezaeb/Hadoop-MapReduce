import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.ArrayList;
import java.util.List;

public class TMapper extends Mapper<Object, Text, Text, IntWritable> {

    // csv line parser
    private String[] parseCsvLine(final String sep, final String line) {
        final List<String> fields = new ArrayList<String>();

	String [] tokens = null;

        if(!line.equals(null) &&!line.equals("") ){

            tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        }

        return tokens ;
    }

    @Override
    protected void map(Object key, Text value,
            Mapper<Object, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {

        IntWritable one = new IntWritable(1);
        String record[] = parseCsvLine(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", value.toString());
        String hashtags = record[2];
        if (hashtags.equals("tweet"))
            return;
        // biden or trump or both
        boolean biden = hashtags.contains("#Biden") || hashtags.contains("#JoeBiden");
        boolean trump = hashtags.contains("#Trump") || hashtags.contains("#DonaldTrump");
        String which_candidate;

        if (biden && trump)
            which_candidate = "Common";
        else if (biden)
            which_candidate = "Biden";
        else if (trump)
            which_candidate = "Trump";
        else
            return;

        String lat = record[13];
        String _long = record[14];

        if (lat == null || lat.isEmpty() || _long == null || _long.isEmpty())
            return;

        double lat_ = Double.parseDouble(lat);
        double long_ = Double.parseDouble(_long);
        // check for America
        if (long_ < -71.7517 && long_ > -79.7624 && lat_ < 45.0153 && lat_ > 40.4772){
            //calculating all
            context.write(new Text("NewYork"), one);
            //calculating awhich_candidatell
            context.write(new Text("NewYork" + "_" +which_candidate), one);
        }
        // check for France
        if (long_ < -114.1315 && long_ > -124.6509 & lat_ < 42.0126 && lat_ > 32.5121) {
            //calculating all
            context.write(new Text("California"), one);
            //calculating awhich_candidatell
            context.write(new Text("California" + "_" +which_candidate), one);
        }
    }

}