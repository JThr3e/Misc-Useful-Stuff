import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

public class ZRSimuConvert {
    public static void main(String[] args) throws Exception {

    	String url =  JOptionPane.showInputDialog
        		("Enter url of tourny leaderboard:");
    	
    	String date =  JOptionPane.showInputDialog
        		("Enter Date: YYYY-MM-DD(format)");
    	
    	String team =  JOptionPane.showInputDialog
        		("Enter Team Name:");
    	
    	
    	
        URL simuList = new URL(getTeamURL(url, team));
        
        BufferedReader in = new BufferedReader(
        new InputStreamReader(simuList.openStream()));
        
        String inputLine;
        String output = "";
        String badGuis = "";
        String row = team.replace(" ", "_") + " EnemyTeam Rank Win/Loss ScoreDiffrence Notes Simu\n";
        String extraInfo = "";
        int count = 1;
        float scoreDiffrence = 0;
        int wins = 0;
        int loss = 0;
        int rank = 0;
        String outcome = "";
        
        while ((inputLine = in.readLine()) != null)
        {
        	if(inputLine.indexOf(date) != -1)
        	{
        		float temp = 0;
        		for(int i = 6; i>0; i--)
        		{
        			inputLine = in.readLine();
        			if(i == 5 || i == 3)
        			{
        				if(temp == 0)
        				{
        					temp = Float.parseFloat(extractString(inputLine));
        				}
        				else scoreDiffrence = Math.abs(temp - Float.parseFloat(extractString(inputLine)));
        			}
        			
        			
        			else if(inputLine.indexOf("<td>") != -1 && 
        					inputLine.indexOf("</td>") != -1)
                	{
                    	String s = extractString(inputLine);
                    	if(s.equals("W"))
            			{
            				outcome = "W";
            				wins++;
            			}
            			
                    	else if(s.equals("L"))
            			{
            				outcome = "L";
            				loss++;
            			}
                    	else if(!s.equals(team))
                    	{
                    		rank = getTeamRank(url, s);
                    		badGuis = s.replace(" ", "_");
                    	}
                    	
                	}
        			
        			else if(inputLine.indexOf("<p><a") != -1)
        			{
        				output += count + " " + badGuis + " " +rank+ " " + outcome + " " + scoreDiffrence + " " + "Notes: " +
                				"http://zerorobotics.mit.edu" + inputLine.substring(32,inputLine.length()-26)+
                				"\n";
        				count++;
        			}
        			else i++;
        		}
        	}
        	
        	
        }
        extraInfo = "ExtraInfo: Date(UTC):_" + date + " Wins:_" + wins + " Loss:_" + loss;
        System.out.println(row+output+extraInfo);
        in.close();
    }
    
    public static String extractString(String inputLine)
    {
    	int start = inputLine.indexOf("<td>")+4;
    	int end = inputLine.indexOf("</td>");
    	return inputLine.substring(start, end);
    }
    
    public static int getTeamRank(String leaderBoardURL, String team) throws Exception
    {
    	URL leaderboard = new URL(leaderBoardURL);
        
        BufferedReader in = new BufferedReader(
        new InputStreamReader(leaderboard.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
        	if(inputLine.contains(team))
        	{
        		for(int i = 0; i<7; i++)
        		{
        			in.readLine();
        		}
        		return Integer.parseInt(extractString(in.readLine()));
        	}
        }
        return -1;
    	
    }
    
    public static String getTeamURL(String leaderBoardURL, String team) throws Exception
    {
    	URL leaderboard = new URL(leaderBoardURL);
        
        BufferedReader in = new BufferedReader(
        new InputStreamReader(leaderboard.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
        {
        	if(inputLine.contains(team))
        	{
        		//System.out.println("http://zerorobotics.mit.edu" + inputLine.substring(19,inputLine.length()-30));
        		return "http://zerorobotics.mit.edu" + inputLine.substring(19,48);
        	}
        }
        return "invalidTeamName";
    }
}
