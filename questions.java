package ivote;

public class questions {
		private String questionList[];
		private String questionType;
		
		
		public questions(int questionsCount){
			questionList = new String[questionsCount];
			questionType = "";
		}
		
		public boolean setQuestionType(String M_or_S){
			if(M_or_S == "S" || M_or_S == "M"){
				questionType = M_or_S;
				return true;
			}
			else{
				return false;
			}
		}
		
		public String tellMeType(){
			return questionType;
		}
		
		public int questionNumberCount(){
			return questionList.length;
		}
}
