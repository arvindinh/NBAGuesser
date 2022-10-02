import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;


//NBAGuess is to be used to scan players.txt in order to play. 
public class NBAGuess {
    public static void main(String[] args) throws FileNotFoundException {
        PlayerBST t = new PlayerBST();
        Scanner scan = new Scanner(new FileInputStream(args[0]));
        int nodeCount = 0;
        ArrayList<PlayerInfo> arr = new ArrayList<PlayerInfo>();
        //adds PlayerInfo to binary search tree and an arraylsit(for selecting random player)
        while (scan.hasNext()){
            String line = scan.nextLine();
            String[] fields = line.split(",");
            String playerName = fields[0];
            String playerTeam = fields[1];
            String playerPosition = fields[2];
            String playerAge = fields[3];
            String playerHeight = fields[4];
            String playerHeight2 = fields[5];
            String playerWeight = fields[6];
            String playerCollege = fields[7];
            String playerSalary = fields[8];
            PlayerInfo info = new PlayerInfo(playerName, playerTeam, playerPosition, playerAge, playerHeight, playerHeight2, playerWeight, playerCollege, playerSalary);
            t.insert(info);
            arr.add(info);
            nodeCount++;
        }
        //JOptionPane 
        int playerChoice;
        playerChoice = JOptionPane.showConfirmDialog(null, "Ready to Play NBAGuesser?", "NBAGuesser", JOptionPane.YES_NO_OPTION);

        if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.NO_OPTION))
            System.exit(0);
        //Selects a random player from the arraylist
        String guess = "";
        Random random = new Random();
        int randomNum = random.nextInt(nodeCount + 1);
        PlayerInfo randomPlayer = arr.get(randomNum);
        //prepare a string array to add on additional hints if requested
        String[] hints = new String[]{", Height: " + randomPlayer.height2, ", Age: " + randomPlayer.age, ", Salary: " + randomPlayer.salary, ", Weight: " + randomPlayer.weight, ", Team: " + randomPlayer.team};
        //starting hints will be the player's position and college attended(nan if international or did not attend college)
        String s = "Position: " + randomPlayer.position + ", College: " + randomPlayer.college;
        Object[] options = {"GUESS", "HINT", "CANCEL"};
        //integer i keeps track of how many hints given, when reaches max index of hints array, the hint button will be unavailable.
        int i = 0;
        //case-insensitive guess checker
        while (guess.toLowerCase().equals(randomPlayer.name.toLowerCase()) != true) {
            //max index of hints array is 4, so when it increments past, no more hints can be given and only the guess feature will be available.
            if (i != 5) {
                playerChoice = JOptionPane.showOptionDialog(null, s, "Guess or Hint?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            } else {
                //setting playerChoice to 0 will only execute the else if statement below, which forces the player to make guesses after using up all the hints.
                playerChoice = 0;
            }
            if ((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.CANCEL_OPTION)) {
                System.exit(0);
            }
            else if (playerChoice == 0) {
                guess = JOptionPane.showInputDialog(null, "Make your guess!\n" + s, JOptionPane.INFORMATION_MESSAGE);
                if (guess.toLowerCase().equals(randomPlayer.name.toLowerCase()) != true) {
                    JOptionPane.showMessageDialog(null, "Your guess was wrong. Please try again!");
                } else {
                    JOptionPane.showMessageDialog(null, "Congratulations! You've guessed the correct player!");
                    //End of game, guessed correct player, player has option to play again or exit.
                    playerChoice = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "NBAGuesser", JOptionPane.YES_NO_OPTION);
                    if((playerChoice == JOptionPane.CLOSED_OPTION) || (playerChoice == JOptionPane.NO_OPTION)){
                        System.exit(0);
                    }
                    else {
                        //if player chooses to play again, it randomly selects a new player and resets all of the hints.
                        guess = "";
                        randomNum = random.nextInt(nodeCount + 1);
                        randomPlayer = arr.get(randomNum);
                        hints = new String[]{", Height: " + randomPlayer.height2, ", Age: " + randomPlayer.age, ", Salary: " + randomPlayer.salary, ", Weight: " + randomPlayer.weight, ", Team: " + randomPlayer.team};
                        s = "Position: " + randomPlayer.position + ", College: " + randomPlayer.college;
                        i = 0;
                    }
                }
            }
            else if (playerChoice == 1 && i != 6) {
                s += hints[i];
                i++;
            }
        }
    }
}

//PlayerInfo contains the main aspects of the players that will be used as hints
 class PlayerInfo {
    String name;
    String team;
    String position;
    String age;
    String height;
    String height2;
    String weight;
    String college;
    String salary;

    //constructor method for PlayerInfo
    public PlayerInfo(String name, String team, String position, String age, String height, String height2, String weight, String college, String salary) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.age = age;
        this.height = height;
        this.height2 = height2;
        this.weight = weight;
        this.college = college;
        this.salary = salary;
    }

}

//Binary search tree class to sort the players alphabetically.
//Contains nodes that have the ability to access a left and right node.
 class PlayerBST{
    class Node {
        public String key;
        public PlayerInfo data;
        public Node left, right;

        Node(String key, PlayerInfo data) {
        this.key = key;
        this.data = data;
        }

        
    }

    
    private Node root;

    //constructor method, creates empty binary tree
    public PlayerBST() {
        root = null;
    }

    public void insert(PlayerInfo data) {
        Node child = new Node(data.name, data);
        //if BST is empty, insert the node
        if(root == null) root = child;
        else {
            Node current = root;
            boolean found = false;
            //traverse the tree iteratively, using alphabetical ordering to compare keys
            while(found != true) {
                String currentName = current.key.toLowerCase();
                String childName = current.key.toLowerCase();
                int comp = currentName.compareTo(childName);
                if (comp < 0) {
                    if (current.left == null) {
                        current.left = child;
                        found = true;
                    } else {
                        current = current.left;
                    }
                } else if (comp > 0) {
                    if (current.right == null) {
                        current.right = child;
                        found = true;
                    } else {
                        current = current.right;
                    }
                } else {
                    found = true;
                }
            }
        }
    }

    public PlayerInfo findPlayer(String key) {
        Node current = root;
        while (current != null) {
            String currentName = current.key.toLowerCase();
            String childName = key.toLowerCase();
            int comp = currentName.compareTo(childName);

            if (comp < 0) {
                current = current.left;
            } else if (comp > 0) {
                current = current.right;
            } else {
                return current.data;
            }
        }
        return null;
    }


}