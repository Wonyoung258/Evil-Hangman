// Wonyoung Kim
// CSE 143
// Section AR
// Assignment 4: Evil Hangman

import java.util.*;

// Manages a game of evil hangman. System keeps track of multiple answers rather than one
public class HangmanManager {

   private Set<String> possibleWords;     // All the possible answers currently
   private Set<Character> guessedLetters;    // All the letters guessed so far
   private String pattern;    // The current pattern
   private int triesLeft;     // The number of tries the user has left
   
   
   // Pre: given length > 1 and max > 0 (throws IllegalArgumentException if not)
   // Post: Initializes a new game of evil hangman with the given set of words.
   // Parameters:
   //       max: the given max number of wrong guesses possible
   //       dictionary: given dictionary of words
   //       length: given length of the words
   // Assumes dictionary is all lowercase
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      guessedLetters = new TreeSet<Character>();
      possibleWords = new TreeSet<String>();
      for (String word : dictionary) {
         if (word.length() == length) {
            possibleWords.add(word);
         }
      }
      pattern = "-";
      for (int i = 0; i < length - 1; i++) {
         pattern += " -";
      }
      triesLeft = max;
   }
   
   // Returns the current set of possible answers
   public Set<String> words() {
      return possibleWords;
   }
   
   // Returns the number of remaining wrong guesses user has left
   public int guessesLeft() {
      return triesLeft;
   }
   
   // Returns the set of letters user has guessed so far
   public Set<Character> guesses() {
      return guessedLetters;
   }
   
   // pre: possibleWords must not be empty (throws IllegalStateException if not)
   // post: returns the current pattern with the correctly guessed letters if there are any
   public String pattern() {
      if (possibleWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   // pre: triesLeft > 1 and possibleWords must not be empty (throws IllegalStateException if not)
   //      Assumes guess is lowercase
   // post: records the guess made by the user and whether it was right or wrong
   //       Updates the pattern, triesLeft, and possible answers.
   //       returns the number of occurrences of the guessed letter in the new pattern
   public int record(char guess) {
      if (triesLeft < 1 || possibleWords.isEmpty()) {
         throw new IllegalStateException();
      }
      
      int occurrences = 0;
      // Map with Pattern and the corresponding set of words with the pattern
      Map<String, Set<String>> patternSet = new TreeMap<String, Set<String>>();
      // Map of Pattern with the number of occurences
      Map<String, Integer> patternOccurrences = new TreeMap<String, Integer>();
      // Set of words with the same patterns
      Set<String> wordsSet = new TreeSet<String>();
      
      guessedLetters.add(guess);
      String newPattern = "";
      // Goes through every word and checks if they contain the guessed letter.
      // Creates a new pattern for each types of pattern for each word
      for (String word : possibleWords) {
         newPattern = pattern;
         occurrences = 0;
         for (int i = 0; i < word.length(); i++) {
            if (guess == word.charAt(i)) {
               newPattern = newPattern.substring(0, 2 * i) + 
                            guess + newPattern.substring(2 * i + 1, newPattern.length());
               occurrences++;
            }
         }
         
         if (patternSet.containsKey(newPattern)) {
            wordsSet = patternSet.get(newPattern);
            wordsSet.add(word);
         } else {
            Set<String> tempWords = new TreeSet<String>();
            tempWords.add(word);
            wordsSet = tempWords;
            patternSet.put(newPattern, wordsSet);
            patternOccurrences.put(newPattern, occurrences);
         }
      }
      Set<String> largestWords = new TreeSet<String>();
      String maxPattern = "";
      int max = 0;
      occurrences = 0;
      for (String pattern : patternSet.keySet()) {
         wordsSet = patternSet.get(pattern);
         if (wordsSet.size() > max) {
            max = wordsSet.size();
            maxPattern = pattern;
            largestWords = wordsSet;
            occurrences = patternOccurrences.get(pattern);
         } else if (wordsSet.size() == max) {
            if (patternOccurrences.get(pattern) < occurrences) {
               largestWords = wordsSet;
               maxPattern = pattern;
               occurrences = patternOccurrences.get(pattern);
            }
         }
      }
      if (occurrences == 0) {
         triesLeft--;
      }
      possibleWords = largestWords;
      pattern = maxPattern;
      return occurrences;
   }
}