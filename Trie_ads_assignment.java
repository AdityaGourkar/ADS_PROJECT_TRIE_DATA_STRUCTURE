/**
 * AIM :- [ROLL NO.: - 42] 
*   A data set consisting of set of words is to be stored in the form of Tries. 
*   Demonstrate create, insert and delete operations.
*/

package ads_assignment;
/**
 *
 * @author aditya
 */
import static java.lang.System.exit;
import java.util.*;
import java.util.Iterator;

public class Trie_ads_assignment 
{
    final static int ALPHABET_SIZE = 26;
    final static int NON_VALUE = -1;
     
    class TrieNode
    {
        int value; //constant value for a word
        boolean isLeafCharNode; //end of string
        String leafNodeStr; 
        TrieNode children[];
         
        TrieNode(boolean isLeafNode, int value)
        {
            this.value = value;
            this.isLeafCharNode = isLeafNode;
            children = new TrieNode[ALPHABET_SIZE];
        }
         
        public void markAsLeaf(int value)
        {
            this.isLeafCharNode = true;
            this.value = value;
        }
         
        public void unMarkAsLeaf()
        {
            this.isLeafCharNode = false;
            this.value = NON_VALUE;
        }
 
    }
 
    TrieNode root;
    static int nodeCount = 0; 
    static HashMap<Integer, String> hmap = new HashMap<Integer, String>();

    Trie_ads_assignment() 
    {
        this.root = new TrieNode(false, NON_VALUE);
    }
 
    private int getIndex(char ch)
    {
        return ch - 'a';
    }
 
    public int search(String key)
    {
        // null keys are not allowed
        if (key == null)
        {
            return NON_VALUE;
        }
         
        TrieNode currentNode = this.root;
        int charIndex = 0;
         
        while ((currentNode != null) && (charIndex < key.length()))
        {
            int childIndex = getIndex(key.charAt(charIndex));
             
            if (childIndex < 0 || childIndex >= ALPHABET_SIZE)
            {
                return NON_VALUE;
            }
            currentNode = currentNode.children[childIndex];
             
            charIndex += 1;
             
        }
         
        if (currentNode != null)
        {
            return currentNode.value;
        }
         
        return NON_VALUE;
    }
 
    public void insert(String key, int value)
    {
        // null keys are not allowed
        if (key == null) 
            return;
         
        key = key.toLowerCase();
         
        TrieNode currentNode = this.root;
        int charIndex = 0;
         
        while (charIndex < key.length())
        {
            int childIndex = getIndex(key.charAt(charIndex));
            int indice_in_node = childIndex+1;
            System.out.println("Child Index : " + indice_in_node);
 
            if (childIndex < 0 || childIndex >= ALPHABET_SIZE)
            {
                System.out.println("Invalid Key");
                return;
            }
             
            if (currentNode.children[childIndex] == null)
            {
                currentNode.children[childIndex] = new TrieNode(false, NON_VALUE);
            }
             
            currentNode = currentNode.children[childIndex];
            
            String str1 = Character.toString(key.charAt(charIndex));
            System.out.println(" " + str1);
            charIndex  += 1;
        }
        // mark currentNode as leaf
        currentNode.markAsLeaf(value);
        currentNode.leafNodeStr = key.toLowerCase();
        hmap.put(value, key);
        nodeCount++;
    }
     
    private boolean hasNoChildren(TrieNode currentNode)
    {
        for (int i = 0; i < currentNode.children.length; i++)
        {
            if ((currentNode.children[i]) != null)
                return false;
        }
        return true;
    }
     
    private boolean deleteHelper(String key, TrieNode currentNode, int length, int level)
    {
         
        boolean deletedSelf = false;
         
        if (currentNode == null)
        {
            System.out.println("Key does not exist");
            return deletedSelf;
        }
         
        // base case: if we have reached at the node which points to the alphabet at the end of the key.
        if (level == length)
        {
            // if there are no nodes ahead of this node in this path
            // then we can delete this node
            if (hasNoChildren(currentNode))
            {
                currentNode = null;
                deletedSelf = true;
            }
            // if there are nodes ahead of this node in this path 
            // then we cannot delete this node. We simply unmark this as leaf
            else
            {
                currentNode.unMarkAsLeaf();
                deletedSelf = false;
            }
        }
        else
        {
            TrieNode childNode = currentNode.children[getIndex(key.charAt(level))];
            boolean childDeleted = deleteHelper(key, childNode, length, level + 1);
             
            if (childDeleted)
            {   
                // making children pointer also null: since child is deleted
                currentNode.children[getIndex(key.charAt(level))] = null;
                 
                // if this is leaf node that means this is part of another key
                // and hence we can not delete this node and it's parent path nodes
                if (currentNode.isLeafCharNode)
                {
                    deletedSelf = false;
                }
                // if childNode is deleted but if this node has more children then this node must be part of another key.
                // we cannot delete this node
                //multiple children exists.
                else if (!hasNoChildren(currentNode))
                {
                    deletedSelf = false;
                }
                // else safely delete this node
                else
                {
                    currentNode = null;
                    deletedSelf = true;
                }
            }
            else
            {
                deletedSelf = false;
            }
        }
         
        return deletedSelf;
    }
 
    public void delete(String key)
    {
        if ((root == null) || (key == null))
        {
            System.out.println("Null key or Empty trie error");
            return;
        }
 
        deleteHelper(key, root, key.length(), 0);
        //hmap.remove(key);
        nodeCount--;
    }
    
    public static void printTrie()
    {
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) 
        {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("Keyword is: "+ mentry.getValue() + " & Value is: ");
            System.out.println(mentry.getKey());
        }
        System.out.println("\nNo. of words stored is : " + hmap.size());
        
    }
   /**
    public static void printAllWordsInTrie(TrieNode root,String s)
    {
        TrieNode currentNode = root;
        
        if(root.children == null || root.children.length == 0)
            return;
        //Set childSet = currentNode.children[i];
        Iterator iter = currentNode.children.iterator();
        
        while(iter.hasNext())
        {
            TrieNode node = (TrieNode) iter.next();
            
            s += node.leafNodeStr;
            printAllWordsInTrie(node,s);
            
            if(node.isLeafCharNode == true)
            { 
             System.out.print(" "+s);
             s = s.substring(0,s.length()-1);
            }
            else
            {
                s = s.substring(0,s.length()-1);
            }
        }
    }*/
    
    
    
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        
        Trie_ads_assignment trie = new Trie_ads_assignment();
        int choice;
        
        System.out.println("Implementing Trie as a Data Structure.\n");
        System.out.println("Creation of Trie is done by Object intialization.\n");
        try{
        do{
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Trie Operations to be performed are :");
        System.out.println("1.Insertion \t 2.Searching \t 3.Deletion \t 4.Print_Trie\t 0.Exit");
        System.out.println("--------------------------------------------------------------------------");
        choice = sc.nextInt();
        switch(choice)
        {
            case 0 : System.out.println("Exiting..!!");
                     exit(0);
                     break;
                     
            case 1 :System.out.println("Enter the no. of words u wish to insert :\t");
                    int n = sc.nextInt();
                    for(int i=1;i<=n;i++)
                    {
                        System.out.println("Now enter the word " + (i) + " to be inserted as string,value : ");
                        String word = sc.next();
                        int val = sc.nextInt();
                        trie.insert(word, val);
                    }
                    break;
                     
            case 2 : System.out.println("Enter the word to be searched : \t");
                     String srch_str = sc.next();
                     int value = trie.search(srch_str);
 
                    if (value != NON_VALUE)
                    {
                        System.out.println("Word Found.!!");
                        System.out.println("Key-value pair retrieved : [ " + "\"" + srch_str + "\"," + value + "]");
                        System.out.println(" "+ srch_str + " Presence in the trie : "+ "TRUE");
                    }
                    else
                    {
                        System.out.println("Word not found!!");
                        System.out.println(" "+ srch_str + " Presence in the trie : "+ "FALSE");
                        
                    }
                    break;
            
            case 3 : System.out.println("Enter the word to be deleted with its value :\t");
                     String del_str = sc.next();
                     int del_val = sc.nextInt();
                     trie.delete(del_str);
                     hmap.remove(del_val);
                     break;
                    
            case 4 : System.out.println("Contents in Trie : \n");
                     printTrie();
                     //System.out.println("\nNo. of words stored is : " + nodeCount);
                     break;
            default : System.out.println("Invalid Input!");
                        break;
            
        }
    }while(choice!=0);
        }
        catch(Exception e)
        {   
            System.out.println("Invalid input exception occured..");
        }
    }
}