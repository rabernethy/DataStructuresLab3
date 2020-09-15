/*
Author: Russell Abernethy
Date: 9/14/20
Program Name: Solitaire Encryption
Desc: Uses solitare encryption to encrypt and decrypt a passed string and prints it to the screen.
Note: The array in main represents the initial positions of the cards. 
      It can be changed as long as it still contains 1-28.
*/

import CircularLinkedList;
import java.util.Scanner;

public class SolitaireEncryption {

    public static int Step1(CircularLinkedList<Integer> A) {
        // Find joker A (27).
        int jokerA = 0; // stores the index of jokerA.
        for(int i = 0; i < A.size(); i++) {
            if(A.get(i) == 27)
                jokerA = i;
        }
        // Swap joker A with the card after it in the deck
        A.remove(jokerA);
        try {
            A.add(jokerA + 1, 27);
            jokerA += 1;
        } catch (IndexOutOfBoundsException e) {
            A.add(0, 27); //if the joker is at the end
            jokerA = 0;
        }
        return Step2(A, jokerA);
    }

    public static int Step2(CircularLinkedList<Integer> A, int jokerA) {
        // Find joker B (28).
        int jokerB = 0;
        for(int i = 0; i < A.size(); i++) {
            if (A.get(i) == 28) 
                jokerB = i;
        }
        // Move joker B down 2 positions.
        A.remove(jokerB);
        try {
            A.add(jokerB + 2, 28);
            jokerB += 2;
        } catch (IndexOutOfBoundsException e) {
            if(jokerB == 27) {
                A.add(2, 28);
                jokerB = 2;
            }
            if(jokerB == 26) {
                A.add(1, 28);
                jokerB = 1;
            }
        }
        return Step3(A, jokerA, jokerB);
    }

    public static int Step3(CircularLinkedList<Integer> A, int jokerA, int jokerB) {
        // Perform a triple cut. (take all the cards above the fist joker and swap it with all the cards below the second joker)
        if(jokerA < jokerB) {
            int numAbove = jokerA - 1;
            int numBelow = A.size() - jokerB;

            // move the top to bot
            for(int i = 0; i <= numAbove; i++) 
                A.add(A.remove(0));

            // find the joker again
            for(int i = 0; i < A.size(); i++) {
                if( A.get(i) == 28)
                    jokerB = i;
            }
            // move the bot to top
            for(int i = 0; i < numBelow - 1; i++)
                A.add(0 + i, A.remove(jokerB + 1));

        } else {
            int numAbove = jokerB - 1;
            int numBelow = A.size() - jokerA;

            // move the top to bot
            for(int i = 0; i <= numAbove; i++)
                A.add(A.remove(0));

            // find the joker agian
            for(int i = 0; i < A.size(); i++) {
                if( A.get(i) == 27)
                    jokerA = i;
            }
            // move the bot to top
            for(int i = 0; i < numBelow - 1; i++) {
                for(int j = 0; j < A.size(); j++) {
                    if( A.get(j) == 27)
                        jokerA = j;
                }
                A.add(0 + i, A.remove(jokerA + 1));
            }
        }
        return Step4(A);
    }

    public static int Step4(CircularLinkedList<Integer> A) {
        // Remove the bottom card from the deck.
        int bottom = A.remove(A.size() - 1);
        if (bottom == 28)
            bottom = 27;

        // Count down from the top equal to the value of the card previously removed and add these cards to the bottom of the deck.
            for(int i = 0; i < bottom; i++)
                A.add(A.size() - 1, A.remove(0));

        // Return the removed card to the bottom of the deck.
        A.add(A.size(), bottom);
        return Step5(A);
    }

    public static int Step5(CircularLinkedList<Integer> A) {
        // Look at the top card of the deck. (Keep it at the top of the deck)
        // Count down the deck by the value of the top card.
        int temp = A.get(0);
        int keystream = A.get(temp);

        // Start over if it was a joker.
        if(keystream >= 27) 
            return Step1(A);

        // Record the value of the next card.
        return keystream;
    }

// key generator method
    public static int genKey(CircularLinkedList<Integer> A) {
        return Step1(A);
    }

// all caps / only capitalized alphabtical characters 
    public static String allCap(String s) {
        return s.replaceAll("[^a-zA-Z]", "").toUpperCase();
    }

// encrypt method
    public static String encrypt(String s, CircularLinkedList<Integer> A) {
        String toReturn = "";
        s = allCap(s);
        for(int i = 0; i < s.length(); i++) {
            int numRep = ctoi(s.charAt(i)) + genKey(A);
            // if numRep is greater than 26, subtract 26
            if(numRep > 26)
                numRep -= 26;
            toReturn += itoc(numRep);
        }
        return toReturn;
    }

// decrypt method
    public static String decrypt(String s, CircularLinkedList<Integer> A) {
        String toReturn = "";
        for(int i = 0; i < s.length(); i++) {
            int numRep = ctoi(s.charAt(i)) - genKey(A);
            // if the numRep after subtracting the key is <= than zero, add 26
            if(numRep <= 0) 
                numRep += 26;
            toReturn += itoc(numRep);
        }
        return toReturn;
    }
// Character to Integer helper method
    public static int ctoi(char c) {
        return (int)c - (int)'A' + 1;
    }

// Integer to Character helper method    
    public static char itoc(int i) {
        return (char)(i + (int)'A' - 1);
    }

    public static void main(String[] args) {
        //Input Array, can be changed as long as 1-28 are all still there.
        int Arr[] = {1,4,7,10,13,16,19,22,25,28,3,6,9,12,15,18,21,24,27,2,5,8,11,14,17,20,23,26};
        
        //Scanner for user input
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a string to be encrypted: ");
        String userInput = input.nextLine();

        // Circular LL 1 for encryption
        CircularLinkedList<Integer> A = new CircularLinkedList<>();
        for(int i = 0; i < Arr.length; i++)
            A.add(Arr[i]);

        // Circular LL 2 for decryption
        CircularLinkedList<Integer> B = new CircularLinkedList<>();
        for(int i = 0; i < Arr.length; i++)
            B.add(Arr[i]);

        // print statements    
        System.out.println("The passed string is: " + allCap(userInput));

        String enc = encrypt(userInput, A);
        System.out.println("The encrypted string is: " + enc);

        String denc = decrypt(enc, B);
        System.out.println("The decrypted string is: " + denc);
    }    
}
