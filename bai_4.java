
import java.io.*;
import java.util.*;

public class bai_4 {

    public static Map<String, Integer> vocab = new HashMap<>();
    public static Map<String, Integer> corpus = new HashMap<>();
    public static Map<String, Integer> pairCorpus = new HashMap<>();
    public static Double[] probs;
    public static Double[][] conditionalProbs;

    public static void readFile() {
        try {
            Vector<String> lines = new Vector<>();
            File file = new File("UIT-ViOCD.txt");
            Scanner fileScanner = new Scanner(file, "UTF-8");

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                lines.addElement(line);
            }
            fileScanner.close();

            int wordId = 0;
            for (String line : lines) {
                // Remove line breaks and extra spaces
                line = line.replace("\n", "").replace("\r", "").replace("\t", "").trim().toLowerCase();
                String[] words = line.split("\\s+");

                // Collecting words
                for (String word : words) {
                    if (corpus.containsKey(word)) {
                        corpus.put(word, corpus.get(word)
                                + 1);
                    } else {
                        vocab.put(word, wordId++);
                        corpus.put(word, 1);
                    }
                }

                // Collecting word pairs
                for (int i = 0; i < words.length - 1; i++) {
                    String words_ij = words[i] + "_" + words[i + 1];
                    pairCorpus.put(words_ij, pairCorpus.getOrDefault(words_ij, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    public static void constructSingleProb() {
        // Determine total words count
        int totalWords = corpus.values().stream().mapToInt(Integer::intValue).sum();
        probs = new Double[vocab.size()];
        Arrays.fill(probs, 1e-10); // Smoothing

        for (Map.Entry<String, Integer> item : corpus.entrySet()) {
            String word = item.getKey();
            Integer wordId = vocab.get(word);

            // Calculate P(w) = count(w) / total words
            probs[wordId] = (double) item.getValue() / totalWords;
        }
    }

    public static void constructConditionalProb() {
        int totalPairsOfWords = pairCorpus.values().stream().mapToInt(Integer::intValue).sum();
        Double[][] jointProbs = new Double[vocab.size()][vocab.size()];
        conditionalProbs = new Double[vocab.size()][vocab.size()];

        // Initialize probability arrays with a small value
        for (int i = 0; i < vocab.size(); i++) {
            Arrays.fill(jointProbs[i], 1e-20);
            Arrays.fill(conditionalProbs[i], 1e-20);
        }

        for (Map.Entry<String, Integer> item_i : corpus.entrySet()) {
            for (Map.Entry<String, Integer> item_j : corpus.entrySet()) {
                String word_i = item_i.getKey();
                String word_j = item_j.getKey();
                Integer wordId_i = vocab.get(word_i);
                Integer wordId_j = vocab.get(word_j);

                if (word_i.equals(word_j)) {
                    continue;
                }

                String wordKey_ij = word_i + "_" + word_j;
                if (pairCorpus.containsKey(wordKey_ij)) {
                    Integer wordCount_ij = pairCorpus.get(wordKey_ij);
                    jointProbs[wordId_i][wordId_j] = ((double) wordCount_ij / totalPairsOfWords);
                }

                // Calculate P(w_j | w_i) = P(w_i, w_j) / P(w_i)
                conditionalProbs[wordId_i][wordId_j] = jointProbs[wordId_i][wordId_j] / probs[wordId_i];
            }
        }
    }

    public static void training() {
        constructSingleProb();
        constructConditionalProb();
    }

    public static Vector<String> inferring(String w0) {
        Vector<String> words = new Vector<>();
        words.add(w0);

        Integer w0Idx = vocab.get(w0);
        if (w0Idx == null) {
            System.out.println("Từ không có trong từ điển!");
            return words;
        }

        for (int t = 1; t <= 5; t++) {
            String bestWord = null;
            double maxProb = 0.0;
            Integer bestIdx = null;

            for (Map.Entry<String, Integer> item : vocab.entrySet()) {
                Integer tempIdx = item.getValue();
                double prob = conditionalProbs[w0Idx][tempIdx];

                if (prob > maxProb) {
                    maxProb = prob;
                    bestWord = item.getKey();
                    bestIdx = tempIdx;
                }
            }

            if (bestWord == null) {
                break;
            }

            words.add(bestWord);
            w0Idx = bestIdx;
        }

        return words;
    }

    public static void main(String[] args) throws Exception {
        readFile();
        training();

        Scanner scanner = new Scanner(System.in, "UTF-8");
        System.out.print("Nhập từ bắt đầu: ");
        String startWord = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        Vector<String> predictedWords = inferring(startWord);
        System.out.println("Câu dự đoán: " + String.join(" ", predictedWords));
    }
}
