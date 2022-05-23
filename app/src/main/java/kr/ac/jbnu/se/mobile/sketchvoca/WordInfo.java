package kr.ac.jbnu.se.mobile.sketchvoca;

import java.util.ArrayList;

public class WordInfo {
    private String engWord;
    private String wordMeaning;
    private String wordImagePath;
    private String publisher;
    ArrayList<String> wordContentsList;

    public WordInfo(){}

    public WordInfo(String engWord, String wordMeaning, String publisher){
        this.engWord = engWord;
        this.wordMeaning = wordMeaning;
        this.publisher = publisher;
    }

    public WordInfo(String engWord, String wordMeaning, String wordImagePath, String publisher){
        this.engWord = engWord;
        this.wordMeaning = wordMeaning;
        this.wordImagePath = wordImagePath;
        this.publisher = publisher;
    }


    public String getEngWord() {
        return engWord;
    }
    public String getWordImagePath() { return wordImagePath; }
    public String getWordMeaning() {
        return wordMeaning;
    }
    public String getPublisher() {
        return publisher;
    }

    public ArrayList<String> getWordContentsList() {
        return wordContentsList;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }
    public void setWordImagePath(String wordImagePath) {
        this.wordImagePath = wordImagePath;
    }
    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setWordContentsList(ArrayList<String> wordContentsList) {
        this.wordContentsList = wordContentsList;
    }
}
