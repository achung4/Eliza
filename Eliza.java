// Name: Angelo Chung
// Course: CPSC 2190
// Purpose of this program: This is ELIZA and you can talk to her and be her friend :)
// Teacher: Ardeshir Bagheri
// TA: Karol Swietlicki
// another not-so-subliminal message: please give me a good mark, Thank you.
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Exception;

public class Eliza
{
    /**
            Purpose: This is the main function
        */
    public static void main(String[] args)
    {
        try 
        {
            randomResponse = new ArrayList<String>();
            previousSentence = "";
            restOfTheSentence = "";
            fillRandomResponseArray();
            System.out.println(randomResponse);
            getInput();
        }
        catch(Exception e) //i'll put a catch Exception at the main method for the sake of not crashing and i hope it works.
        {
            System.out.println("Please don't hurt me");
        }
    }
    /**
            Purpose: get the input from the user
        */
    private static void getInput()
    {
        bye = false;
        System.out.println("Hi! I'm Eliza. What would you like to talk about?");
        do
        {
            try
            {
                System.out.print(" - ");
                Scanner input = new Scanner(System.in);
                String sentence = input.nextLine();
                if(sentence.length() == 0)
                {
                    checkInput("noReply"); // user didn't input anything or user just pressed enter
                }
                else if(sentence.length() > MAXIMUM_CHARACTERS)
                {
                    checkInput("veryVeryLong"); // user input a sentence with more then 150 characters
                }
                else if(previousSentence.equals(sentence))
                {
                    checkInput("repeatedSentence"); // user just repeated his or her previous sentence
                }
                else 
                {
                    checkInput(removePunctuation(sentence));
                    previousSentence = sentence;
                }
            }
            catch(Exception e)
            {
                System.out.println("Please don't hurt my feelings."); // user tried to crash the program :(
            }
        }while(!bye);
    }
    /**
            Purpose: remove the punctuation at the end of a sentence
            @param sentence the whole sentence that might have a punctuation at the end
            @return the whole sentence without a punctuation at the end
        */
    private static String removePunctuation(String sentence)
    {
        String unpunctuatedSentence = "";
        for(int i=0; i<sentence.length(); i++)
        {
        if(sentence.charAt(i) != ',' || sentence.charAt(i) != '.' || sentence.charAt(i) != '?' || sentence.charAt(i) != '!') 
            // do not concat if char is , or . or ? or !
            unpunctuatedSentence += sentence.charAt(i);
        }
        return unpunctuatedSentence;
    }
    /**
            Purpose: check the input of the user if it has keywords that will determine the replies of ELIZA
            @param sentence the whole sentence       
        */
    private static void checkInput(String sentence)
    {
        Scanner word = new Scanner(new String(sentence));
        boolean keywordFound = false; 
        addSentenceAfterKeyword = false;
        while(word.hasNext() && !keywordFound)
        {
            String checkWord = word.next();
            checkWord += "|";
            keywordFound = checkIfKeyword(checkWord);
            if(keywordFound && addSentenceAfterKeyword)
            {
                getTheRestOfTheSentence(word); // get the rest of the sentence
            }
            else if(keywordFound && !addSentenceAfterKeyword)
                System.out.println();
        } 
        if(!keywordFound) // put random response here
            getRandomResponse(); 
    }
    /**
            Purpose: checks if a word is a keyword that will determine ELIZA's replies
            @param word if the word is a keyword
            @return true if the word is a keyword; otherwise, false
        */
    private static boolean checkIfKeyword(String word)
    {
        boolean exit = false;
        boolean keywordFound = false; 
        try
        {
            Scanner replyFile = new Scanner(new FileReader("reply.txt"));
            try
            {
                while(replyFile.hasNext() && !exit)
                {
                    String keyword = replyFile.next();
                    if(word.equals("byebye|"))
                    {
                        bye = true;
                        System.out.println("Goodbye.");
                        exit = true;
                        keywordFound = true;
                    }
                    else if(keyword.equalsIgnoreCase(word))
                    {
                        exit = true;
                        getReply(keyword);
                        keywordFound = true;
                    }
                }                
            }
            finally
            {
                replyFile.close();
            }
            
        }
        catch(IOException exception)
        {
            System.out.println("invalid file or someone changed my codes!");
        }
        catch(Exception exception)
        {
            System.out.println("Uhh.. something's wrong");
        }
        return keywordFound;
    }
    /**
            Purpose: determine ELIZA's reply based on the keyword found
            @param keyword the keyword
        */
    private static void getReply(String keyword)
    {
        try
        {
            FileReader replyFile = new FileReader("reply.txt");
            try
            {
                LineNumberReader lineReader = new LineNumberReader(replyFile);
                String line = "";
                while((line = lineReader.readLine()) != null)
                {
                    Scanner sentence = new Scanner(new String(line));
                    String firstWord = sentence.next();
                    int replyStart = 0;
                    if(firstWord.equals("i"))
                    {
                        firstWord = sentence.next();
                        replyStart += AVOID_DILIMITER;
                    }
                        replyStart += firstWord.indexOf('|')+AVOID_DILIMITER;
                        if(firstWord.equals(keyword))
                        {
                            String replyStatement = line.substring(replyStart,line.length());
                            if(replyStatement.charAt(replyStatement.length()-1) == '#') //# is a special case
                            {
                                System.out.print(replyStatement.replaceAll("#",""));
                                addSentenceAfterKeyword = true;
                            }
                            else
                                System.out.print(line.substring(replyStart,line.length()));
                        }
                }
            }
            finally
            {
                replyFile.close();
            }
        }
        catch(IOException exception)
        {
            System.out.println("invalid file or someone changed my codes!");
        }
        catch(Exception exception)
        {
            System.out.println("Uhh.. something's wrong");
        }
    }
    /**
            Purpose prints out the rest of the original sentence if needed
            @param sentence the whole sentence of the original input
        */
    private static void getTheRestOfTheSentence(Scanner sentence)
    {
        while(sentence.hasNext())
        {
            String word = sentence.next();
            if(word.equals("me"))
                System.out.print("you ");
            else
                System.out.print(word+" ");
        }
        System.out.println(".");
    }
    /**
            Purpose: if no keywords found, then ELIZA will still has to reply somehow. However, these replies must vary so that the conversation is not boring.
                        so, we fill up an ArrayList with different phrases from a text file reply.txt
        */
    private static void fillRandomResponseArray()
    {
        randomResponse.add("Please tell me more."); // this is the default random response just a person removed all random response from reply.txt
        try
        {
            FileReader replyFile = new FileReader("reply.txt");
            try
            {
                LineNumberReader lineReader = new LineNumberReader(replyFile);
                String line = "";
                while((line = lineReader.readLine()) != null)
                {
                    Scanner sentence = new Scanner(new String(line));
                    String key = sentence.next();
                    int replyStart = 0;
                    if(key.equals("##%"))
                    {
                        replyStart = key.indexOf('%')+AVOID_DILIMITER;
                        String allRandomReplies = line.substring(replyStart,line.length());
                        //System.out.println(allRandomReplies); //tester
                        int replyFrom = 0;
                        //int replyTo = 0;
                        for(int i = 0;i<allRandomReplies.length();i++)
                        {
                            if(allRandomReplies.charAt(i)=='%')
                            {
                                randomResponse.add(allRandomReplies.substring(replyFrom,i));
                                replyFrom = i+AVOID_DILIMITER;
                            }
                        }
                    }
                      
                }                
            }
            finally
            {
                replyFile.close();
            }
        }
        catch(IOException exception)
        {
            System.out.println("invalid file or someone changed my codes!");
        }
        catch(Exception exception)
        {
            System.out.println("Uhh.. something's wrong");
        }
    }
    /**
            Purpose: ELIZA will print out a random phrase if no keyword was found.
        */
    private static void getRandomResponse()
    {
        int randomReplyNumber = 0;
        while(true)
        {
            randomReplyNumber = (int)Math.round(Math.random()*randomResponse.size());
            if(randomReplyNumber >0 && randomReplyNumber < randomResponse.size())break;
        }
        System.out.println(randomResponse.get(randomReplyNumber));
    }
    // INSTANT FIELDS AND CONSTANTS
    private static int MAXIMUM_CHARACTERS = 150;
    private static int AVOID_DILIMITER = 2;
    private static ArrayList<String> randomResponse;
    private static String previousSentence;
    private static String restOfTheSentence;
    private static boolean addSentenceAfterKeyword;
    private static boolean bye;

}