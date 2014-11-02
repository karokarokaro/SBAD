<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.LoginPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new LoginPage(request, response);
    thisPage.executeRequest();
%>