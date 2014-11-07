<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.CampEditorPage" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    AbstractPage thisPage = new CampEditorPage(request, response);
    thisPage.executeRequest();
%>