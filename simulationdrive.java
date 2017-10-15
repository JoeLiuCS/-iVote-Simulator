package ivote;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class simulationdrive{
	
	final int MAX_PEOPLE_IN_CLASS = 50;
	final int MIN_PEOPLE_IN_CLASS = 30;
	
	final int BEGINNING_OF_ID = 10000000;
	final int ENDING_OF_ID = 99999999;
	
	final int MAX_QUESTIONS = 7;
	final int MIN_QUESTIONS = 2;
	
	final String single_question = "S";
	final String mutiple_question = "M";
	
	
	private Map<Integer,student> peopleInClass;
	private int key_table[];
	private Map<Integer, ArrayList<Integer>> answer;
	private questions question;
	private ivoteservice service;

	
	private void questionType_Random(){
		Random r = new Random();
		int type = MIN_QUESTIONS + (int)(Math.random() * ((MAX_QUESTIONS - MIN_QUESTIONS) + 1));
		question = new questions(type);
		if (type < 4 ){
			question.setQuestionType(single_question);
		}
		else{
			String Q_type = r.nextBoolean()? single_question : mutiple_question;
			question.setQuestionType(Q_type);
		}
	}
	
	private void peopleInClass_Random(){
		peopleInClass = new HashMap<Integer,student>();
		
		int people = MIN_PEOPLE_IN_CLASS + (int)(Math.random() * ((MAX_PEOPLE_IN_CLASS - MIN_PEOPLE_IN_CLASS) + 1));
		// # of keys = # of student 
		key_table = new int[people];
		
		// Random generate Student ID number
		for (int i=0;i<people;){
			int GenerateID = BEGINNING_OF_ID + (int)(Math.random() * ((ENDING_OF_ID - BEGINNING_OF_ID) + 1));
			if(!peopleInClass.containsKey(GenerateID)){
				student s = new student(Integer.toString(GenerateID));
				peopleInClass.put(GenerateID, s);
				key_table[i] = GenerateID;
				i++;
			}
		}
	}
	
	/*
	 * Initialize 30~50 student In the Class
	 * Random choose the question type
	 */
	public simulationdrive(){
		questionType_Random();
		peopleInClass_Random();
		service = new ivoteservice(question);
		answer = new HashMap<Integer,ArrayList<Integer>>();
	}
	
	/*
	 * The student will pick the answer for the first round
	 */
	public void firstRoundAnswers(){
		String Q_type =question.tellMeType();
		//if it is single choice
		if(Q_type == "S"){
			for(int i=0;i<key_table.length;i++){
				makeSingleAnswer(key_table[i]);	
			}
		}
		// if it is multiple choice
		if(Q_type == "M"){
			for(int i=0;i<key_table.length;){
				ArrayList<Integer> temp= makeMutipleAnswer(key_table[i]);
				if(!temp.isEmpty()){
					i++;
				}
			}
		}
	}
	
	public ArrayList<Integer> makeSingleAnswer(int key){
		ArrayList<Integer> result = new ArrayList<>();
		int len = question.questionNumberCount();
		int ans = 1 + (int)(Math.random() * ((len- 1) + 1));
		result.add(ans);
		service.addByCount(ans);
		answer.put(key,result);
		return result;
	}
	/*
	 * if the random answer is same, it will return empty
	 */
	public ArrayList<Integer> makeMutipleAnswer(int key){
		ArrayList<Integer> result = new ArrayList<>();
		int len = question.questionNumberCount();
		int ans = 1 + (int)(Math.random() * ((len- 1) + 1));
		int ans2 = 1 + (int)(Math.random() * ((len- 1) + 1));
		if(ans != ans2){
			result.add(ans);
			result.add(ans2);
			service.addByCount(ans);
			service.addByCount(ans2);
			answer.put(key,result);
		}
		return result;
	}
	
	 /*
	  * 30% student will change the answer
	  */
	public void decideMyAnswer(){
		Deque<Integer> Q =  new LinkedList<>();
		int len = question.questionNumberCount();
		
		for(int i=0;i<key_table.length;i++){
			Q.push(key_table[i]);
		}
		while(!Q.isEmpty()){
			//30 % student may change the answer
			int student_ID = Q.pop();
			int ans = 1 + (int)(Math.random() * ((10- 1) + 1));
			if(ans<4){
				System.out.println("Student ID "+Integer.toString(student_ID)+" Wants to change Answer");
				
				ArrayList<Integer> oldAnswer = answer.get(student_ID);
				ArrayList<Integer> newAnswer = new ArrayList<>();
				
				//multiple choice
				if(question.tellMeType() == "M"){
					int newAns = 0;
					int newAns2 = 0;
					do{
						newAnswer.clear();
						newAns = 1 + (int)(Math.random() * ((len- 1) + 1));
						newAns2 = 1 + (int)(Math.random() * ((len- 1) + 1));
						newAnswer.add(newAns);
						newAnswer.add(newAns2);
					}while(oldAnswer.containsAll(newAnswer));
					service.changeAnswer(oldAnswer.get(0), newAns);
					service.changeAnswer(oldAnswer.get(1), newAns2);
					answer.replace(student_ID, newAnswer);
				}
				//single choice
				if(question.tellMeType() == "S"){
					int newAns = 0;
					do{
						newAnswer.clear();
						newAns = 1 + (int)(Math.random() * ((len- 1) + 1));
						newAnswer.add(newAns);
					}while(oldAnswer.containsAll(newAnswer));
					service.changeAnswer(oldAnswer.get(0), newAns);
					answer.replace(student_ID, newAnswer);
				}
				System.out.println("The old Answer is :"+oldAnswer.toString());
				System.out.println("The new Answer is :"+newAnswer.toString());
				Q.push(student_ID);
			}
		}
		
	}
	
	
	public String[] showMeIDs(){
		String[] result = new String[key_table.length];
		for (int i =0; i<key_table.length;i++){
			result[i] = peopleInClass.get(key_table[i]).getID();
		}
		return result;
	}
	
	public int sizeOfClass(){
		return peopleInClass.size();
	}
	
	public String tellMeType(){
		return question.tellMeType();
	}
	public questions getQuestion(){
		return question;
	}
	public void showMeResult(){
		service.showMeResult();
	}
	public void studentPicked(){
		for (int i =0; i<key_table.length;i++){
			ArrayList<Integer> ans = answer.get(key_table[i]);
			System.out.println("Student ID "+ Integer.toString(key_table[i])+" Picked: "+ans.toString());
		}
	}
	
	public static void main(String[] args) {
		simulationdrive s = new simulationdrive();
	    s.firstRoundAnswers();
	    System.out.println("!!!!Game Beginning!!!!");
	    System.out.println("Random :Question Type & number of Answers & number of people ");
	    System.out.println("\n'S' means Single choice question, 'M' means Mutiple choice questions");
	    System.out.println("Question type is : " + s.tellMeType());
	    System.out.println("How many answer for the question :" + s.getQuestion().questionNumberCount());
	    System.out.println("# of the student in the class :"+s.sizeOfClass());
	    s.showMeResult();
	    s.studentPicked();
	    
	    System.out.println("\n\n======== The student want to change answer=======");
	    s.decideMyAnswer();
	    
	    System.out.println("\n\n============== Final result =====================");
	    s.showMeResult();
	    s.studentPicked();
	    System.out.println("=================================================");


	}
}
