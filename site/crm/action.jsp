<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.ActionPage2" %>
<%@ page import="core.Logger" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    Logger.debug(request.getParameter("address"));
    AbstractPage thisPage = new ActionPage2(request, response);
    thisPage.executeRequest();
%>