<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.HistoryRenderer" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new HistoryRenderer(request, response);
    thisPage.executeRequest();
%>