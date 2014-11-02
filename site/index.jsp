<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.MainPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new MainPage(request, response);
    thisPage.executeRequest();
%>