package io.javabrains.springbootstarter.RetreivalServicePackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class RetrievalService {
    static float[][] a1;
    HashMap<Integer, String> doc = new HashMap<>();
    TreeMap<String, Integer> document_string_index = new TreeMap<>();
    TreeMap<String, TreeMap<Integer, Integer>> Inverted_Index = new TreeMap<>();
    TreeMap<Integer, Integer> term_frequency = new TreeMap<>();
    HashMap<Integer, Double> tr;
    HashMap<Integer, Double> rsv20;
    HashMap<Integer, String> result = new HashMap<>();
    int nonz = 0;
    Logger logger = LoggerFactory.getLogger(RetrievalService.class);
    //    @Value("${inputFile}")
    private String file_path = "/Users/sai/Documents/workspace_intelli/RESTAPI_Implementation/src/main/java/io/javabrains/springbootstarter/RetreivalServicePackage/input_res/cran.txt";

    RetrievalService() throws IOException {
        this.form_inverted_index();
        this.calculate_term_frequency();
    }

    public HashMap<Integer, String> getDoc() {
        return doc;
    }

    public TreeMap<String, TreeMap<Integer, Integer>> getInverted_Index() {
        return Inverted_Index;
    }

    public TreeMap<Integer, Integer> getTerm_frequency() {
        return term_frequency;
    }

    public HashMap<Integer, String> getResult(String query) {
        process_query(query);
        return result;
    }

    public List<String> getDemo() {
        return Arrays.asList(new String[]{"Hello", "Hi"});
    }

    public HashMap<Integer, String> getById(String id) throws IOException {
        File source = new File(file_path);
        FileReader fr = new FileReader(source);
        BufferedReader br = new BufferedReader(fr);

        String sin = "";
        boolean check = false;
        HashMap<Integer, String> res = new HashMap<>();
        String part1;
        boolean after_W = false;
        while ((part1 = br.readLine()) != null)                //tokenizing
        {
            StringTokenizer st = new StringTokenizer(part1, " ");
            String temp = st.nextToken();
            if (temp.equals(".I")) {

                if (st.nextToken().equals(id)) {
                    check = true;
                } else if (check) {
                    break;
                }

            } else if (check && part1.matches(".W")) {
                after_W = true;
            } else if (check && after_W) {
                sin += part1 + " ";
            }


        }
        res.put(Integer.parseInt(id), sin);
        return res;
    }

    public void calculate_term_frequency() {
        Set<String> keys = Inverted_Index.keySet();
        TreeMap<Integer, Integer> tt;
        for (String sy : keys) {
            tt = Inverted_Index.get(sy);
            Set<Integer> k1 = tt.keySet();
            for (Integer io : k1) {
                if (term_frequency.containsKey(io)) {
                    int mf = term_frequency.get(io);
                    int jj = tt.get(io);
                    if (jj > mf) {
                        term_frequency.replace(io, jj);
                    }
                } else {
                    int jo = tt.get(io);
                    term_frequency.put(io, jo);
                }
            }
        }
        term_frequency.put(471, 0);//no terms in this ids
        term_frequency.put(995, 0);//no term in this id
    }

    public void process_query(String query) {
        int ji = get_tokens(query);
        tr = queryEval(query, ji);
        rsv20 = sortByValues(tr);
        Set<Integer> sx = tr.keySet();
        nonz = 0;
        for (int io : sx) {
            if (tr.get(io) != 0) {
                nonz++;
            }
        }
        print_results(rsv20, nonz);
    }

    public void form_inverted_index() throws IOException {
        File source = new File(file_path);
        FileReader fr = new FileReader(source);
        BufferedReader br = new BufferedReader(fr);

        String[] stopwords = {".", ",", "+", "a", "a's", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "b", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "currently", "d", "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing", "don't", "done", "down", "downwards", "during", "e", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f", "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "g", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here", "here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept", "know", "knows", "known", "l", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "m", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "p", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "q", "que", "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t", "t's", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that's", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "uucp", "v", "value", "various", "very", "via", "viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went", "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where", "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "won't", "wonder", "would", "would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "z", "zero"};


        String part1;
        String sin = " ";
        int docid = 0;
        boolean after_W = false;
        while ((part1 = br.readLine()) != null)                //tokenizing
        {
            StringTokenizer st1 = new StringTokenizer(part1, " /(),");
            while (st1.hasMoreTokens()) {
                String stempr = st1.nextToken();
                if (stempr.contains(".I"))                        //setting document id
                {
                    after_W = false;
                    if (docid > 0) {
                        doc.put(docid, sin);
                        sin = "";
                    }
                    docid = Integer.parseInt(st1.nextToken());
                } else if (stempr.contains(".W")) {
                    after_W = true;
                } else if (after_W && isAlpha(stempr))                             //eliminating stop words
                {

                    String stemp = stempr.toLowerCase();
                    int check = 1, check1 = 1;
                    for (String stopword : stopwords) {
                        if (stemp.equals(stopword)) {
                            check = 0;
                        }
                    }
                    if (check != 0) {
                        //applying stemmer
                        Stemmer stm = new Stemmer();
                        String stemp1 = stemp;
                        char[] inchar = stemp1.toCharArray();
                        for (char temp : inchar) {
                            temp = Character.toLowerCase((char) temp);
                            stm.add(temp);

                        }

                        stm.stem();
                        sin += " " + stemp1;
                        stemp = stm.toString();
                        int docf = 1, termf = 1;
                        //inserting values into tree map to store term snd frequency

                        if (document_string_index.containsKey(stemp)) {
                            int yo = document_string_index.get(stemp);
                            yo++;
                            document_string_index.replace(stemp, yo);
                        } else {
                            document_string_index.put(stemp, docf);
                        }


                        if (Inverted_Index.containsKey(stemp)) {
                            //storing posting and term frequency
                            int tid = docid;
                            TreeMap<Integer, Integer> tval1;
                            tval1 = Inverted_Index.get(stemp);
                            if (tval1.containsKey(tid)) {
                                int ttermf = tval1.get(tid);
                                ttermf++;
                                tval1.put(tid, ttermf);
                                Inverted_Index.replace(stemp, tval1);
                            } else {
                                tval1.put(docid, termf);
                                Inverted_Index.put(stemp, tval1);
                            }
                        } else {
                            TreeMap<Integer, Integer> val = new TreeMap<>();
                            val.put(docid, termf);
                            Inverted_Index.put(stemp, val);
                        }
                    }
                }
            }
        }
        doc.put(docid, sin);

    }

    public int get_tokens(String query) {
        StringTokenizer st1 = new StringTokenizer(query, "&& || !! )(, ");
        int i = st1.countTokens();
        a1 = new float[st1.countTokens()][1401];
        int n = 0;
        while (st1.hasMoreTokens()) {
            n++;
            String tk = st1.nextToken();

//                 String key = tk;
            //stemming query
            Stemmer sx = new Stemmer();

            char[] inchar = tk.toCharArray();
            for (char temp : inchar) {
                temp = Character.toLowerCase((char) temp);
                sx.add(temp);

            }

            sx.stem();
            query = sx.toString();
            find(n, query);//function call for stemmed query for number of tokens it contains
        }
        return i;

    }

    public void find(int n, String key) {

        try {
            if (Inverted_Index.containsKey(key)) {
                TreeMap<Integer, Integer> ttm1;
                ttm1 = Inverted_Index.get(key);
                Set<Integer> st = ttm1.keySet();
                for (int i = 1; i < 1401; i++) {
                    a1[n - 1][i] = 0;
                }
                for (int f : st) {

                    int midf = term_frequency.get(f);

                    int idf = ttm1.get(f);
                    float fvalue = (float) idf / midf;
//                 System.out.println("|id="+f+"-->midf "+midf+ "-->idf="+idf+"-->fvalue = "+fvalue);
                    a1[n - 1][f - 1] = fvalue;
                }

            } else {
//                System.out.println("no word in the dictionary");
            }
        } catch (Exception e) {
//            System.out.println("exception : " + e);
        }

    }

    public HashMap<Integer, Double> queryEval(String query, int ji) {
        String tq = query;
        System.out.println("query " + tq);
        ArrayList<String> arc = new ArrayList<>();//storin elements

        StringTokenizer sr1 = new StringTokenizer(tq, "&& || !! )(, ");
        int ss = sr1.countTokens();
//         System.out.println("tokens ="+ss );
        int o = -1;
        while (sr1.hasMoreTokens()) {
            String tt = sr1.nextToken();
            o++;


            arc.add(o, tt);

        }

        StringTokenizer sr2 = new StringTokenizer(query);//OR(a,(AND(a,b))

        HashMap<Integer, Double> hm = new HashMap<>();
        for (int i = 0; i < 1401; i++) {
            String ui = query;
            for (int j = 0; j < ji; j++) {
                if (ui.contains(arc.get(j))) {
                    String hj = Float.toString(a1[j][i]);
                    ui = ui.replace(arc.get(j), hj);
                }
                if (j == ji - 1) {
                    //getting evaluated values and creating 1400 queries
                    hm.put(i, new ExtendedDoubleEvaluator().evaluate(ui));
                }
            }
        }


//         System.out.println("HashMap"+hm);
        return hm;

    }

    private HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });


        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public void print_results(HashMap<Integer, Double> rsv20, int nonz) {
        Set<Integer> se = rsv20.keySet();
        int p = 0;
        //printing rsv values and document it is present
        for (int io : se) {
            p++;
//                 System.out.println("rsv "+rsv20.get(io));
            if (p <= 20) {
                if (rsv20.get(io) > 0) {
//                    System.out.println("RSV--->  " + (rsv20.get(io)) + "   String---> " + doc.get(io));
                    result.put(io, doc.get(io));
//                    System.out.println();
                }
            }

        }
//        System.out.println("number of non zero value are " + nonz);
    }
}
