package ivote;

public class ivoteservice {
	
	private int [] answer;
	
	public ivoteservice(questions q){
		answer = new int [q.questionNumberCount()];
	}
	
	public void addByCount(int number){
		answer[number-1] = answer[number-1] + 1;
	}
	
	public void removeByCount(int number){
		answer[number-1] = answer[number -1] - 1;
	}
	/*
	 *  Delete the old selected, add the new selected
	 */
	public void changeAnswer(int before,int after){
		answer[before-1] = answer[before-1] - 1;
		answer[after-1] = answer[after-1] + 1;
	}
	
	public void showMeResult(){
		char a = 'A';
		System.out.println("======================================");
		for(int i=0;i<answer.length;i++){
			System.out.println((char) (a+i)+" : "+Integer.toString(answer[i]));
		}
		System.out.println("======================================");
	}
}
