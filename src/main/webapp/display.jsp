<%@page import="Model.Question"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<% Object data = request.getAttribute("DISPLAYDATA"); %>
<% ArrayList<Question> questionData = (ArrayList<Question>)data;  %>
<%int i=1; %>
<form action="submitAnswers" method="post">
<%for(Question q:questionData){ %>
 <h4>Question No.<%= i %>:<%= q.getQuestion() %></h4>
  <input type="radio" name="<%=q.getQuestionId() %>" value="1" required="required"> <label style="font-size: 20px;"><%= q.getOption1() %></label> <br>
  <input type="radio" name="<%=q.getQuestionId() %>" value="2"><label style="font-size: 20px;"><%= q.getOption2() %></label>  <br>
   <input type="radio" name="<%=q.getQuestionId() %>" value="3"><label style="font-size: 20px;"><%= q.getOption3() %></label>  <br>
   <input type="radio" name="<%=q.getQuestionId() %>" value="4"> <label style="font-size: 20px;"><%= q.getOption4() %></label>
<%i++;
   } %>
<br>
<input type="submit" value="submit-answer">

</form>
</body>
</html>