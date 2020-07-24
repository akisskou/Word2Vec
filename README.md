# Word2Vec
Οδηγίες για την χρήση της εφαρμογής:
1. Αποθηκεύουμε τα xml από το clinical-trial.gov σε έναν φάκελο και καταγράφουμε το πλήρες path του φακέλου ως τιμή της παραμέτρου "pathToXML" στο αρχείο infos.properties (πχ pathToXML=C:/XML-Clinical-Trials/)
2. Τρέχουμε πρώτα το W2V.java, το οποίο ανακτά τα περιεχόμενα των πεδίων "brief_summary", "detailed_description" και "criteria" από κάθε xml και τα αποθηκεύει σε ένα νέο αρχείο raw_text.txt στον φάκελο του project μας.
3. Στη συνέχεια, τρέχουμε το W2VWriteVectors.java, το οποίο διαβάζει το raw_text.txt, δημιουργεί ένα νέο Word2Vec μοντέλο και το κάνει train και τέλος αποθηκεύει τα vectors στον φάκελο word_vectors.zip.
4. Επιλέγουμε τη βάση από την οποία θέλουμε να πάρουμε πληροφορίες για τους ασθενείς και καταγράφουμε το id της ως τιμή της παραμέτρου dbid στο αρχείο infos.properties (πχ dbid=35, dbid=08 κλπ). Έπειτα τρέχουμε το ExtractFromDB.java. Από την επιλεγμένη βάση εξάγονται για κάθε ασθενή οι πληροφορίες για τα κριτήρια που ικανοποιεί από τους πίνακες "condition_symptom", "condition_diagnosis", "intervention_medication", "laboratory_test" 
