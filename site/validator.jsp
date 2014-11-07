<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.ValidatorPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new ValidatorPage(request, response);
    thisPage.executeRequest();
%>