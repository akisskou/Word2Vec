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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.shade.guava.collect.Iterables;
import org.nd4j.shade.guava.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class W2VLoadCriterionVectors {
	
	private static Logger log = LoggerFactory.getLogger(W2VLoadVectors.class);
	
	private static String stopwords1 = "<=,>=,_,=,<,>,+,%, -,- , - ,—,•,…,/,#,$,&,*,\\,^,{,},~,£,§,®,°,±,³,·,½,™";

	private static String stopwords2 = "i,me,my,myself,we,our,ours,ourselves,you,your,yours,yourself,yourselves,he,him,his,himself,she,her,hers,herself,it,its,itself,they,them,their,theirs,themselves,what,which,who,whom,never,this,that,these,those,am,is,are,was,were,be,been,being,have,has,had,having,do,does,did,doing,a,an,the,and,but,if,kung,or,because,as,until,while,of,at,by,for,with,about,against,between,into,through,during,before,after,above,below,to,from,up,down,in,out,on,off,over,under,again,further,then,once,here,there,when,where,why,how,long,all,any,both,each,few,more,delivering,most,other,some,such,no,nor,not,only,own,same,so,than,too,cry,very,s,t,can,lite,will,just,don,should,now";

	
	public static void main(String[] args) throws Exception {
		InputStream input = new FileInputStream("infos.properties");
		Properties prop = new Properties();
		// load a properties file
		prop.load(input);
		String criterionData = prop.getProperty("criteria");
		String[] sw1 = stopwords1.split(",");	
		String[] sw2 = stopwords2.split(",");
		String editData = criterionData;
		for(int j=0; j<sw1.length; j++) editData = editData.replace(sw1[j], " ");
		for(int j=0; j<sw2.length; j++) editData = editData.replace(" "+sw2[j]+" ", " ");
		String[] criterionWords = editData.trim().replace(" +", " ").toLowerCase().split(" ");
		List<String> myWords = new ArrayList<String>();
	    for(int j=0; j<criterionWords.length; j++) {
	    	myWords.add(criterionWords[j]);
	    }
	    Set<String> set = new HashSet<>(myWords);
	    myWords.clear();
	    myWords.addAll(set);
		List<String> wordList = null;
		log.info("Loading word vectors from zip file...");
		File vectors = new File("word_vectors.zip");
		Word2Vec vec = WordVectorSerializer.readWord2VecModel(vectors);
		boolean flag = false;
		File criteriaDir = new File("chdb0"+prop.getProperty("dbid")+"/Criteria");
		criteriaDir.mkdir();
		for(int j=0; j<myWords.size(); j++) {
			try {
				/*wordList = vec.wordsNearestSum(myWords.get(j), Integer.valueOf(prop.getProperty("count")));
				criterionData += myWords.get(j)+": "+wordList.toString()+"\n";*/
				if(j==0) wordList = (List<String>) vec.wordsNearestSum(myWords.get(j), Integer.valueOf(prop.getProperty("count")));
				else {
					List<String> myList = (List<String>) vec.wordsNearestSum(myWords.get(j), Integer.valueOf(prop.getProperty("count")));
					Iterable<String> combinedIterables = Iterables.unmodifiableIterable(
							Iterables.concat(wordList, myList));
					wordList = Lists.newArrayList(combinedIterables);
				}
				flag=true;
			}
			catch(Exception e) {
				continue;
			}
		}
		if(flag) {
			Set<String> set1 = new HashSet<>(wordList);
			wordList.clear();
			wordList.addAll(set1);
			Collections.sort(wordList);
			log.info("Words closest to '"+prop.getProperty("criteria")+"': {}", wordList);
			
			File closeDir = new File("chdb0"+prop.getProperty("dbid")+"/Closest_words");
			File[] files = closeDir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isFile();
				}
			});
			criterionData += ", Patient IDs: ";
			for(int i=0; i<files.length; i++) {
				//File myObj = new File("chdb0"+prop.getProperty("dbid")+"/Closest_words/"+files[i].getName());
			    Scanner myReader = new Scanner(files[i],"utf-8");
			    String data = "";
			    while (myReader.hasNextLine()) {
			        data = myReader.nextLine();
			        if(!data.contains(":")) break;
			        else{
			        	data = data.split(": ")[1].trim();
			        	if(data.equals("has no close words")) continue;
				        else {
				        	data = data.replace("[", "").replace("]", "");
				        	List<String> myList = new ArrayList<String>(Arrays.asList(data.split(",")));
				        	int j;
				        	for(j=0; j<myList.size(); j++) {
				        		int k;
				        		for(k=0; k<wordList.size(); k++) {
				        			if(myList.get(j).trim().equals(wordList.get(k).trim())) break;
				        		}
				        		if(k==wordList.size()) break;
				        	}
				        	if(j==myList.size()) {
				        		criterionData += files[i].getName().split("_")[2].split("\\.")[0]+" ";
				        		break;
				        	}
				        }
			        }
			    }
			    myReader.close();
			}
			log.info(criterionData);
			
			final Path path = Paths.get("chdb0"+prop.getProperty("dbid")+"/Criteria/criteria.txt");
		    Files.write(path, Arrays.asList(criterionData), StandardCharsets.UTF_8,
		        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		}
		else {
			criterionData = prop.getProperty("criteria")+" has no close words";
			log.info(criterionData);
			final Path path = Paths.get("chdb0"+prop.getProperty("dbid")+"/Criteria/criteria.txt");
		    Files.write(path, Arrays.asList(criterionData), StandardCharsets.UTF_8,
		        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		}
	}
}
