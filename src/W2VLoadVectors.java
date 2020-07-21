import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.shade.guava.collect.Iterables;
import org.nd4j.shade.guava.collect.Lists;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class W2VLoadVectors {

	private static Logger log = LoggerFactory.getLogger(W2VLoadVectors.class);
	private static OWLOntology ontology;
	private static OWLOntologyManager manager;
	private static IRI documentIRI;
	private static List<myOWLClass> allClasses;
	
	private static String stopwords1 = "<=,>=,_,=,<,>,+,%, -,- , - ,—,•,…,/,#,$,&,*,\\,^,{,},~,£,§,®,°,±,³,·,½,™";

	private static String stopwords2 = "i,me,my,myself,we,our,ours,ourselves,you,your,yours,yourself,yourselves,he,him,his,himself,she,her,hers,herself,it,its,itself,they,them,their,theirs,themselves,what,which,who,whom,never,this,that,these,those,am,is,are,was,were,be,been,being,have,has,had,having,do,does,did,doing,a,an,the,and,but,if,kung,or,because,as,until,while,of,at,by,for,with,about,against,between,into,through,during,before,after,above,below,to,from,up,down,in,out,on,off,over,under,again,further,then,once,here,there,when,where,why,how,long,all,any,both,each,few,more,delivering,most,other,some,such,no,nor,not,only,own,same,so,than,too,cry,very,s,t,can,lite,will,just,don,should,now";
	
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
		    Files.write(path, Arrays.asList(results.replace("â‰¥", "")), StandardCharsets.UTF_8,
		        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		}
		
	}
}
