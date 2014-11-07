<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.LoginPage2" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new LoginPage2(request, response);
    thisPage.executeRequest();
%>