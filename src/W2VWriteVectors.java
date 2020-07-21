import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read raw_text.txt --> Generate Vectors and save to zip File 
 * @author jason
 *
 */
public class W2VWriteVectors {

	private static Logger log = LoggerFactory.getLogger(W2VWriteVectors.class);
	
	public static void main(String[] args) throws Exception {

			log.info("Load & Vectorize Sentences....");
			// Strip white space before and after for each line
			
			SentenceIterator iter = new BasicLineIterator("raw_text.txt");
			// Split on white spaces in the line to get words
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new CommonPreprocessor());
				
			log.info("Building model....");
			Word2Vec vec = new Word2Vec.Builder()
						.minWordFrequency(5)
						.iterations(1)
						.layerSize(100)
						.seed(42)
						.windowSize(5)
						.iterate(iter)
						.tokenizerFactory(t)
						.build();
									
			log.info("Fitting Word2Vec model....");
			vec.fit();
					
			log.info("Writing word vectors to zip file....");
			WordVectorSerializer.writeWord2VecModel(vec, "word_vectors.zip");
			
	}
}
