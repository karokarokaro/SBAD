<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.ActionPage2" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new ActionPage2(request, response);
    thisPage.executeRequest();
%>