import java.io.IOException;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.ArrayList;
import java.util.List;

public class TMapper extends Mapper<Object, Text, Text, FloatWritable> {
    
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
            Mapper<Object, Text, Text, FloatWritable>.Context context)
            throws IOException, InterruptedException {
        String record[] = parseCsvLine(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", value.toString());
        String hashtags = record[2];
        if (hashtags.equals("tweet"))
            return;
        // biden or trump or both
        boolean biden = hashtags.contains("#Biden") || hashtags.contains("#JoeBiden");
        boolean trump = hashtags.contains("#Trump") || hashtags.contains("#DonaldTrump");
        String which_candidate;

        if (biden && trump)
            which_candidate = "both";
        else if (biden)
            which_candidate = "Biden";
        else if (trump)
            which_candidate = "Trump";
        else
            return;

        Text likesRecord = new Text(which_candidate + "Like");
        Text retweetsRecord = new Text(which_candidate + "Retweet");
        Text webAppRecord = new Text(which_candidate + "WebApp");
        Text iphoneRecord = new Text(which_candidate + "Iphone");
        Text androidRecord = new Text(which_candidate + "Android");
        
        // fourth column is number Of Likes 
        float Likes_numbers = Float.parseFloat(record[3]);
        FloatWritable likes = new FloatWritable(Likes_numbers);

        // fifth column is number Of retweets 
        float retweets_numbers = Float.parseFloat(record[4]);   
        FloatWritable retweets = new FloatWritable(retweets_numbers);
    
	FloatWritable webApp = new FloatWritable(0.0f);
	FloatWritable iphone = new FloatWritable(0.0f);
	FloatWritable android = new FloatWritable(0.0f);
    
        //6th column for resource
        String resource_fields = record[5];

        if(resource_fields.contains("Twitter Web App")){
            webApp = new FloatWritable(1.0f);
        }else if(resource_fields.contains("Twitter for iPhone")){
            iphone = new FloatWritable(1.0f);
        }else if(resource_fields.contains("Twitter for Android")){
            android = new FloatWritable(1.0f);
        }

        context.write(likesRecord, likes);
        context.write(retweetsRecord, retweets);
        context.write(webAppRecord , webApp);
        context.write(iphoneRecord, iphone); 
        context.write(androidRecord, android);


    }

}