<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.DriverPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new DriverPage(request, response);
    thisPage.executeRequest();
%>