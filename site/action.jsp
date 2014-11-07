<%@ page import="core.pages.LoginPage" %>
<%@ page import="core.pages.ActionPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    ActionPage thisPage = new ActionPage(request, response);
    thisPage.executeRequest();
%>