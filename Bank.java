
public class Bank {

}

Filter changed files
 206 changes: 206 additions & 0 deletions206  
ATM.java
@@ -0,0 +1,206 @@
import java.util.*;//Scanner
import java.lang.*;
public class ATM{
    public static void main(String[]args){
        //init Scanner 
        Scanner sc = new Scanner(System.in);
        //init bank
        Bank theBank = new Bank("State Bank Of India");
        //add user ,whichalso creates a savings account
        User aUser = theBank.addUser("Aarsh","Dhokai","1111");
        //add a checking account for our user 
        Account newAccount = new Account("Checking",aUser,theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true){
            //stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank,sc);

            //stay in main menu until user quits
            ATM.printUserMenu(curUser,sc);
        }
    }
    public static User mainMenuPrompt(Bank theBank,Scanner sc){
        //init
        String userID;
        String pin;
        User authUser;
        //prompt the user for user id and pincombo until a correct one is reached
        do{
            System.out.println("\n\nWelcome to "+theBank.getName()+"\n\n");
            System.out.print("Enter User ID : ");
            userID = sc.nextLine();
            System.out.println();
            System.out.print("Enter Pin : ");
            pin = sc.nextLine();

            //try to get the user object corresponding to th ID and pin Combo 
            authUser = theBank.userLogin(userID,pin);
            if(authUser == null){
                System.out.println("incorrect User ID/Pin, Please try again");
            }
        }while(authUser==null);//continue login until successful login
        return authUser;
    }
    public static void printUserMenu(User theUser,Scanner sc){
        //print a summary of user's account
        theUser.printAccountSummary();

        //init choice
        int choice;
        //user menu
        do{
            System.out.println("Welcome "+theUser.getFirstName()+" What would you like to do");
            System.out.println("1>Show Transaction History");
            System.out.println("2>withdraw");
            System.out.println("3>Deposite");
            System.out.println("4>Transfer");
            System.out.println("5>Quit");
            System.out.println();
            System.out.println("Enter Your Choice : ");
            choice = sc.nextInt();
            if(choice<1 || choice>5){
                System.out.println("Invalid Choice"+" Please choose between 1-5 ");  
            } 
        }while(choice<1 || choice>5);
        //process the choice
        switch(choice){
            case 1:
                ATM.showTransactionHistory(theUser,sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser,sc);
                break;
            case 3:
                ATM.depositeFunds(theUser,sc);
                break;
            case 4:
                ATM.transferFunds(theUser,sc);
                break;
            case 5:
                 //gobble up rest of previous input
                 sc.nextLine();
                 break;
        }
        //redisplay this menu unless user quit
        if(choice != 5){
            ATM.printUserMenu(theUser,sc);
        }
    }
    public static void showTransactionHistory(User theUser,Scanner sc){
        int theAct;
        //get account whose history to look at
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+" whose transaction you waant to see : ",theUser.numAccounts());
            theAct = sc.nextInt()-1;
            if(theAct < 0 || theAct >= theUser.numAccounts()){
                System.out.println("Invalid account. please try again.....");
            }
        }while(theAct < 0 || theAct >= theUser.numAccounts());
        //Print transaction history

        theUser.printActTransHistory(theAct);
    }
    public static void transferFunds(User theUser,Scanner sc){
        //intits
        int fromAct;
        int toAct;
        double amount;
        double actBal;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to transfer from:",theUser.numAccounts());
            fromAct = sc.nextInt()-1;
            if(fromAct < 0 || fromAct >= theUser.numAccounts()){
                System.out.println("Invalid account. please try again.....");
            }
        }while(fromAct < 0 || fromAct >= theUser.numAccounts());
        actBal = theUser.getAccountBalance(fromAct);

        //get the account to transfer to 
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"to transfer to:",theUser.numAccounts());
            toAct = sc.nextInt()-1;
            if(toAct < 0 || toAct >= theUser.numAccounts()){
                System.out.println("Invalid account. please try again.....");
            }
        }while(toAct < 0 || toAct >= theUser.numAccounts());
        //get the amount to traansfer
        do{
            System.out.println("Enter the amount to transfer (max than $"+actBal+") : $");
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero");
            }else if(amount > actBal){
                System.out.println("Amount must not be greater than \n"+"balance of $"+actBal);
            }
        }while(amount < 0 || amount>actBal);
        //do the transfer
        theUser.addActTransaction(fromAct,-1*amount,String.format("Transfer to account "+theUser.getActUUID(toAct)));
        theUser.addActTransaction(toAct,amount,String.format("Transfer to account "+theUser.getActUUID(fromAct)));
    }
    public static void withdrawFunds(User theUser,Scanner sc){
        int fromAct;
        String memo;
        double amount;
        double actBal;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"Where to withdraw :",theUser.numAccounts());
            fromAct = sc.nextInt()-1;
            if(fromAct < 0 || fromAct >= theUser.numAccounts()){
                System.out.println("Invalid account. please try again.....");
            }
        }while(fromAct < 0 || fromAct >= theUser.numAccounts());
        actBal = theUser.getAccountBalance(fromAct);
        //get the amount to traansfer
        do{
            System.out.println("Enter the amount to withdraw (max $ "+actBal+"): $");
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero");
            }else if(amount > actBal){
                System.out.println("Amount must not be greater than \n"+"balance of $"+actBal);
            }
        }while(amount < 0 || amount>actBal);
        //gobble up rest of previous input
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        //do withdrawal
        theUser.addActTransaction(fromAct,-1*amount,memo);        
    }
    public static void depositeFunds(User theUser,Scanner sc){
        int toAct;
        String memo;
        double amount;
        double actBal;
        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"+"Where to Deposite:",theUser.numAccounts());
            toAct = sc.nextInt()-1;
            if(toAct < 0 || toAct >= theUser.numAccounts()){
                System.out.println("Invalid account. please try again.....");
            }
        }while(toAct < 0 || toAct >= theUser.numAccounts());
        actBal = theUser.getAccountBalance(toAct);
        //get the amount to transfer
        do{
            System.out.print("Enter the amount to deposite (max than $"+actBal+") :$");
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero");
            }
        }while(amount < 0 );
        //gobble up rest of previous input
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        //do withdrawal
        theUser.addActTransaction(toAct,amount,memo);             
    }
}
 57 changes: 57 additions & 0 deletions57  
Account.java
@@ -0,0 +1,57 @@
import java.util.*;
import java.lang.*;
public class Account {
    private String name;//The name of account like saving,current etc
   // private double balance; //current balance of this account
    private String uuid;//account id number
    private User holder;//the user object owns this account 
    private ArrayList<Transaction> transactions;//list of transaction for this account
    /**
     * @param holder  the User object that holds this account 
     * @param theBank  the bank that issues the account 
     */
    public Account(String name,User holder,Bank theBank){
        //set   the account name and holder 
        this.name = name;
        this.holder = holder;

        //get new account UUID
        this.uuid = theBank.getNewAccountUUID();

        //init transactions
        this.transactions = new ArrayList<Transaction>();

    }
    public String getUUID(){
        return this.uuid;
    }
    public String getSummaryLine(){
        //get the account's balance
        double balance = this.getBalance();
        //format the summary line, depending on the wheather the balance is negative
        if(balance>=0){
            return String.format(this.uuid+" : $"+balance+" : "+this.name);
        }else{
            return String.format(this.uuid+" : $"+balance+" : "+this.name);
        }
    }
    public double getBalance(){
        double balance = 0;
        for(Transaction t : this.transactions){
            balance += t.getAmount();
        }
        return balance;
    }
    public void printTransHistory(){
        System.out.println("Transaction history for account : "+this.uuid);
        for(int t = this.transactions.size()-1;t>=0;t--){
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }
    public void addTransaction(double amount,String memo){
        //create new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount,memo,this);
        this.transactions.add(newTrans);
    }
}
 92 changes: 92 additions & 0 deletions92  
Bank.java
@@ -0,0 +1,92 @@
import java.util.*;
public class Bank{
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name){
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    public String getNewUserUUID(){
        //inits
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;
        //continue looping until we get a unique ID
        do{
            //genrate number
            uuid="";
            for(int i = 0;i<len;i++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            //check  to make sure it's unique
            nonUnique = false;
            for(User u:this.users){
                if(uuid.compareTo(u.getUUID())==0){
                    nonUnique = true;
                    break;
                }
            }
        }while(nonUnique);
        return uuid;
    }
    public String getNewAccountUUID(){
      //inits
      String uuid;
      Random rng = new Random();
      int len = 10;
      boolean nonUnique;
      //continue looping until we get a unique ID
      do{
          //genrate number
          uuid="";
          for(int i = 0;i<len;i++){
              uuid += ((Integer)rng.nextInt(10)).toString();
          }
          //check  to make sure it's unique
          nonUnique = false;
          for(Account a:this.accounts){
              if(uuid.compareTo(a.getUUID())==0){
                  nonUnique = true;
                  break;
              }
          }
      }while(nonUnique);
      return uuid;  

    }
    public void addAccount(Account anAct){
        this.accounts.add(anAct);
    }

    public User addUser(String firstName,String lastName,String pin){
        //create a new user object and add it to our list
        User newUser = new User(firstName,lastName,pin,this);
        this.users.add(newUser);
        //create a saving account for the user and add user to User and Bank account lists
        Account newAccount = new Account("Savings",newUser,this);

         newUser.addAccount(newAccount);
         this.accounts.add(newAccount);

         return newUser;
    }
    public User userLogin(String userID,String pin){
        //search through lists of user
        for(User u:this.users){
            //check usser ID is correct
            if(u.getUUID().compareTo(userID)==0 && u.validatePin(pin)){
                return u;
            }
        }
        return null;//either pin or user invalid or both invalid

    }

    public String getName(){
        return this.name;
    

 