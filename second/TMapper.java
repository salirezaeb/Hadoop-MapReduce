import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        String[] states = { "NewYork", "Texas", "California","Florida"};
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

        String created_at = record[0];


	String[] date_time = created_at.split(" ");
	String time = date_time[1];
	String[] splited_time = time.split(":");
 
	int h = Integer.parseInt(splited_time[0]);
	int m = Integer.parseInt(splited_time[1]);
	int s = Integer.parseInt(splited_time[2]);
/*
        System.out.println(time);
        System.out.println(h);
        System.out.println(m);
        System.out.println(s);
*/

if((h >= 9 && h < 17) || (h == 17 && m == 0 && s == 0)){	

        String state = record[18];
        if (state == null || state.isEmpty())
            return;

	if(state.equals("New York"))
	    state = "NewYork";

        state = state.toLowerCase();

        for (String st : states) {
            // check for every country list in country field
            if (state.contains(st.toLowerCase())) {
                // for calculating all tweets
                context.write(new Text(st), one);
                // for calculating each candidate tweets
                context.write(new Text(st + "_" + which_candidate), one);
            }
        }

}

    }

}