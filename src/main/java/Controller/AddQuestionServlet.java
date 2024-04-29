package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import Model.Question;

@WebServlet(urlPatterns = {"/addQuestions","/submitAnswers"})
public class AddQuestionServlet extends HttpServlet {
	
	private static List<Question> questionList = new LinkedList<>();
	
  	private Connection connection;
  	
  	static List<Question> displayList = null;
	
	@Override
	public void init() throws ServletException {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/advancejava-m3","root","tiger");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String path = req.getServletPath();
		
		switch(path) {
		
		case "/addQuestions":
			String button = req.getParameter("operation");
			
			if(button.equals("ADD QUESTIONS")) {
				addQuestions(req,resp);
				
	      }else if(questionList.size()>0) {
	    		  addQuestionToTheDataBase(req,resp);
	    		  questionList.removeAll(questionList);
	    	  }else {
	    		    
	    		  displayData(req,resp);
	    	  }
			
			
			
			break;
			
		case "/submitAnswers":	
			checkAnswer(req,resp);
		
		}
		
		
      }
	

	private void checkAnswer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int count = 0;
		
		ArrayList<String> userAnswerList = new ArrayList<String>();
		for(Question q:displayList) {
			String userAnswer = req.getParameter(q.getQuestionId()+"");//convert object value into string value
			
			userAnswerList.add(userAnswer);
			
			if(userAnswer.equals(q.getAnswerKey())) {
				count++;
			}
		}
//		System.out.println(count);
		req.setAttribute("userAnswerList", userAnswerList);
		req.setAttribute("questionAnswerList",displayList);
		
		RequestDispatcher rd = req.getRequestDispatcher("checkAnswer.jsp");
		rd.forward(req, resp);
		
	}

	private void displayData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String query = "select * from question";
		
		try {
			Statement statement = connection.createStatement();
		ResultSet	rs=statement.executeQuery(query);
			
			Question question = null;
			
			displayList = new ArrayList<Question>();
			
			
			while(rs.next()) {
				question = new Question();
				
				question.setQuestionId(rs.getInt(1));
				question.setQuestionPaperTitle(rs.getString(2));
				question.setQuestion(rs.getString(3));
				question.setOption1(rs.getString(4));
				question.setOption2(rs.getString(5));
				question.setOption3(rs.getString(6));
				question.setOption4(rs.getString(7));
				question.setDifficultyLevel(rs.getString(8));
				question.setAnswerKey(rs.getString(9));
				
				displayList.add(question);
			}
			
			RequestDispatcher rd = req.getRequestDispatcher("display.jsp");
			req.setAttribute("DISPLAYDATA",displayList);
			rd.forward(req, resp);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void addQuestionToTheDataBase(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String insertQuery = "insert into question values(?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(insertQuery);
			
			for(Question temp:questionList) {
				pstmt.setInt(1, 0);
				pstmt.setString(2,temp.getQuestionPaperTitle());
				pstmt.setString(3,temp.getQuestion());
				pstmt.setString(4,temp.getOption1());
				pstmt.setString(5,temp.getOption2());
				pstmt.setString(6,temp.getOption3());
				pstmt.setString(7,temp.getOption4());
				pstmt.setString(8,temp.getDifficultyLevel());
				pstmt.setString(9,temp.getAnswerKey());
				
				pstmt.addBatch();
			}
			
			int[] count = pstmt.executeBatch();
			
			resp.setContentType("text/html");
			PrintWriter pw = resp.getWriter();
			RequestDispatcher rd = req.getRequestDispatcher("AddQuestions.html");
			
			pw.print("<div align='center'>");
			pw.print("<h5 style='color:green;'>"+count.length+" QUESTION ADDED SUCCESSFULLY</h5>");
			pw.print("</div>");
			
			rd.include(req, resp);
			System.out.println(questionList.size());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void addQuestions(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String questionPaperTitle = req.getParameter("QuestionPaperTitle");
		String question = req.getParameter("Question");
		String option1 = req.getParameter("Option1");
		String option2 = req.getParameter("Option2");
		String option3 = req.getParameter("Option3");
		String option4 = req.getParameter("Option4");
		String difficultyLevel = req.getParameter("difficulty-level");
		String answerKey = req.getParameter("AnswerKey");
		
		questionList.add(new Question(questionPaperTitle,question,option1,option2,option3,option4,difficultyLevel,answerKey));
		
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		RequestDispatcher rd = req.getRequestDispatcher("AddQuestions.html");
		
		pw.print("<div align='center'>");
		pw.print("<h5 style='color:green;'>QUESTIONS ADDED SUCESSFULLY</h5>");
		pw.print("</div>");
		
		rd.include(req, resp);
		
		System.out.println(questionList.size());
		
	}

}
