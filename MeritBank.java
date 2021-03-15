package com.meritamerica.assignment3;


import java.io.*;
import java.text.*;
import java.util.*;

/* Fix error with first and 2nd best CD val, add combined bals and next Acct num */

public class MeritBank 
{
	 private static final String FILE_NAME = "src/test/testMeritBank_testing";
	private static CDOffering[] listOfCDOffers;
	private static AccountHolder[] listOfAccountHolders;
	private static long nextAccountNumber = 0;
		
	static void addAccountHolder(AccountHolder accountHolder)
	{
		for(int i = 0; i < listOfAccountHolders.length; i++)
		{
			if(listOfAccountHolders[i] == null)
			{
				listOfAccountHolders[i] = accountHolder;
				break;
			}
		}
	}
	
	static AccountHolder[] getAccountHolders(){return listOfAccountHolders;}
	
	static CDOffering[] getCDOfferings(){return listOfCDOffers;}
	
	static CDOffering getBestCDOffering(double depositAmount)
	{	
		if(listOfCDOffers == null) return null;
		double stored = futureValue(depositAmount, listOfCDOffers[0].getInterestRate(), listOfCDOffers[0].getTerm());
		int indexBiggest = 0;
		for(int i = 1; i < listOfCDOffers.length; i++)
		{
			double tempStored = futureValue(depositAmount, listOfCDOffers[i].getInterestRate(), listOfCDOffers[i].getTerm());
			if(tempStored > stored)
			{
				stored = tempStored;
				indexBiggest = i;
			}				
		}		
		return listOfCDOffers[indexBiggest];
	}
	
	static CDOffering getSecondBestCDOffering(double depositAmount)
	{
		if(listOfCDOffers == null) return null;
		double biggest = futureValue(depositAmount, listOfCDOffers[0].getInterestRate(), listOfCDOffers[0].getTerm());
		double secondBiggest = futureValue(depositAmount, listOfCDOffers[0].getInterestRate(), listOfCDOffers[0].getTerm());
		int indexBiggest = 0;
		int indexSecondBiggest = 0;
		for(int i = 1; i < listOfCDOffers.length; i++)
		{
			double tempStored = futureValue(depositAmount, listOfCDOffers[i].getInterestRate(), listOfCDOffers[i].getTerm());								
			if(tempStored > biggest)
			{
				indexSecondBiggest = indexBiggest;
				indexBiggest = i;			
			}
			if(tempStored > secondBiggest && tempStored != biggest)
			{
				indexSecondBiggest = i;
			}
		}		
		return listOfCDOffers[indexSecondBiggest];
	}
	
	static void clearCDOfferings(){listOfCDOffers = null;}
	
	static void setCDOfferings(CDOffering[] offerings){listOfCDOffers = offerings;}
	
	static long getNextAccountNumber(){return nextAccountNumber++;}
	
	static double totalBalances()
	{
		double total = 0;
		for(AccountHolder ah: listOfAccountHolders) 
		{
			if(ah != null)
			{
				total += ah.getCombinedBalance();
			}		
		}
		return total;
	 }
	
	public static double futureValue(double presentValue, double interestRate, int term)
	{
		return presentValue * Math.pow((1 + interestRate), term);
	}

	
	//finish//
	static BankAccount readFromFile(String accountData) throws ParseException
	{		
		try(Scanner sc = new Scanner(new FileReader(accountData));)
		{																																	 
			setNextAccountNumber(Long.parseLong(sc.next()));										
			//  CD OFFERS  //
			CDOffering[] newCDarr = new CDOffering[sc.nextInt()];								
			for(int i = 0; i < newCDarr.length; i++)
			{
				newCDarr[i] = CDOffering.readFromString(sc.next());									 				 
			}
			setCDOfferings(newCDarr);
			// number of account holders// 
			int newNumOfAcctHolder = sc.nextInt();													
			// -- GET ACCT HOLDER INFO AND ADD --//
			for(int i = 0; i < newNumOfAcctHolder; i++)
			{
				
				//temp stores the number of account holders // 
				AccountHolder tempAcct = AccountHolder.readFromString(sc.next());					
				int numOfCheckAccts = sc.nextInt();													
					for(int j = 0; j < numOfCheckAccts; j++)	
					{
						tempAcct.addCheckingAccount(CheckingAccount.readFromString(sc.next()));		
				int numOfSavAccts = sc.nextInt();
					for(int j = 0; j < numOfSavAccts; j++)
					{
						tempAcct.addSavingsAccount(SavingsAccount.readFromString(sc.next()));		
					}
				int numOfCDAccts = sc.nextInt();
					for(int j = 0; j < numOfCDAccts; j++)
					{
						tempAcct.addCDAccount(CDAccount.readFromString(sc.next()));					
					}
					
				listOfAccountHolders[i] = tempAcct;													
			}				
			}	}catch(Exception e)
		{
			e.printStackTrace();
		}		
		return null;
	}
	
	String writeToFile()
	{	
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/test/testMeritBank_testing")))
		{
			bw.write(String.valueOf(nextAccountNumber)); bw.newLine();						// next acct num
			
			bw.write(String.valueOf(listOfCDOffers.length)); bw.newLine();					// num of CD Offers
				for(int i = 0; i < listOfCDOffers.length; i++)
				{
					bw.write(listOfCDOffers[i].writeToString()); bw.newLine();				// list CD offers
				}
				
			bw.write(String.valueOf(listOfAccountHolders.length)); bw.newLine();			
			sortAccountHolders();								
				for(int i = 0; i < listOfAccountHolders.length; i++) 
				{									
					bw.write(listOfAccountHolders[i].getLastName() + "," +
							listOfAccountHolders[i].getMiddleName() + "," +
							listOfAccountHolders[i].getFirstName() + "," +
							listOfAccountHolders[i].getSSN()
							); bw.newLine();
					
					bw.write(String.valueOf(listOfAccountHolders[i].getNumberOfCheckingAccounts())); bw.newLine();
						for(int j = 0; j < listOfAccountHolders[i].getNumberOfCheckingAccounts(); i++)
						{
							bw.write(j); bw.newLine(); 
						}
					bw.write(String.valueOf(listOfAccountHolders[i].getNumberOfSavingsAccounts())); bw.newLine();
						for(int j = 0; j < listOfAccountHolders[i].getNumberOfSavingsAccounts(); i++)
						{
							bw.write(listOfAccountHolders[i].compareTo(null)); bw.newLine(); 
						}
					bw.write(String.valueOf(listOfAccountHolders[i].getNumberOfCDAccounts())); bw.newLine();
						for(int j = 0; j < listOfAccountHolders[i].getNumberOfCDAccounts(); i++)
						{
							bw.write(listOfAccountHolders[i].compareTo(null)); bw.newLine(); 
						}
				}				
		}		
		catch(IOException e) 
		{
			System.out.println("File not found"); 
		}
		return null; 
	}
	
	static AccountHolder[] sortAccountHolders()
	{		
		Arrays.sort(listOfAccountHolders, Collections.reverseOrder());
		return listOfAccountHolders;											
	}
	
	private static void setNextAccountNumber(long nextAccountNumber){MeritBank.nextAccountNumber = nextAccountNumber;}

}