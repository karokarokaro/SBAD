<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.RouterPage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
        %><%
    AbstractPage thisPage = new RouterPage(request, response);
    thisPage.executeRequest();
%>