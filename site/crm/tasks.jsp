<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.TaskEditorPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new TaskEditorPage(request, response);
    thisPage.executeRequest();
%>