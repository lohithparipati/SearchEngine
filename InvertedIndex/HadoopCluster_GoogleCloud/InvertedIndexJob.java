import java.io.IOException;
import java.util.Hashtable;
/*import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;*/
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/*import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;*/

public class InvertedIndexJob 
{
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException 
	{
	    if (args.length != 2) 
	    {
	      System.err.println("Usage: InvertedIndexJob <input path> <output path>");
	      System.exit(-1);
	    }
	    
	    Job job = new Job();
	    job.setJarByClass(InvertedIndexJob.class);
	    job.setJobName("InvertedIndexJob");
	   
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	    job.setMapperClass(InvertedIndexMapper.class);
	    job.setReducerClass(InvertedIndexReducer.class);

	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
	  }
}

class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

	private Text wordText = new Text();
	private final static Text document = new Text();

	protected void map(LongWritable key, Text value, Context context)
			throws java.io.IOException, InterruptedException {
		String[] line = value.toString().split("\t");

		String documentName = line[0];
		document.set(documentName);
		String textStr = line[1];
		String[] wordArray = textStr.split(" ");
		for(int i = 0; i <  wordArray.length; i++) { 
			wordText.set(wordArray[i]);
			context.write(wordText,document);
		}
	}
}

class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {
	protected void reduce(Text key, Iterable<Text> values, Context context) 
			throws java.io.IOException, InterruptedException {
		Hashtable<String, Integer> docmap = new Hashtable<>();
		for (Text value : values)
		{
			if(docmap.containsKey(value.toString()))
			{
				int val = docmap.get(value.toString());
				docmap.put(value.toString(), val+1);
			}
			else
			{
				docmap.put(value.toString(), 1);
			}
		}
		String documents = "";
		for(String docId : docmap.keySet())
		{
		    documents = documents + docId + ":" + docmap.get(docId).toString() + "\t";		    
		}	
		//System.out.println(key + "\t" + documents); 
		Text documentList = new Text();
		documentList.set(documents);
		context.write(key, documentList);
	}
}
