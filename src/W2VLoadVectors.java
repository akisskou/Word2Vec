import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read TXT files with patient data --> For each term find closest terms
 * @author jason
 *
 */
public class W2VLoadVectors {

	private static Logger log = LoggerFactory.getLogger(W2VLoadVectors.class);
	
	public static void main(String[] args) throws Exception {
		InputStream input = new FileInputStream("infos.properties");
		Properties prop = new Properties();
		// load a properties file
		prop.load(input);
		//String[] criteria = prop.getProperty("criteria").split(" ");
		File dbDir = new File("chdb0"+prop.getProperty("dbid"));
		log.info("Checking for files...");
		File[] files = dbDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile();
			}
		});
		File newDir = new File("chdb0"+prop.getProperty("dbid")+"/Closest_words");
		newDir.mkdir();
		log.info("Loading word vectors from zip file...");
		File vectors = new File("word_vectors.zip");
		Word2Vec vec = WordVectorSerializer.readWord2VecModel(vectors);
		for(int i=0; i<files.length; i++) {	
			log.info("Patient "+files[i].getName().split("\\.")[0]);
			File myObj = new File("chdb0"+prop.getProperty("dbid")+"/"+files[i].getName());
		    Scanner myReader = new Scanner(myObj);
		    String data = "";
		    while (myReader.hasNextLine()) {
		        data += myReader.nextLine();
		    }
		    myReader.close();
		    String[] words = data.trim().replaceAll(" +", " ").split(" ");
		    List<String> myWords = new ArrayList<String>();
		    for(int j=0; j<words.length; j++) {
		    	myWords.add(words[j]);
		    }
		    Set<String> set = new HashSet<>(myWords);
		    myWords.clear();
		    myWords.addAll(set);
			Collection<String> wordList = null;
			String results = "";
			for(int j=0; j<myWords.size(); j++) {
				try {
					wordList = vec.wordsNearestSum(myWords.get(j), Integer.valueOf(prop.getProperty("count")));
					results += myWords.get(j)+": "+wordList.toString()+"\n";
				}
				catch(Exception e) {
					results += myWords.get(j)+": has no close words\n";
				}
				
			}
			
			log.info("Writing results in file...");
			final Path path = Paths.get("chdb0"+prop.getProperty("dbid")+"/Closest_words/closest_words_"+files[i].getName());
		    Files.write(path, Arrays.asList(results.replace("≥", "")), StandardCharsets.UTF_8,
		        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		}
		
	}
}
