<%@ page import="core.pages.AbstractPage" %>
<%@ page import="core.pages.RouterPage" %>
<%@ page import="core.database.DBObject" %>
<%@ page import="core.helpers.TempHelper" %>
<%@ page import="core.entity.User" %>
<%@ page import="core.database.DBAttribute" %>
<%@ page import="java.util.List" %>
<%@ page import="core.database.Attributes" %>
<%@ page import="core.helpers.DBHelper" %>
<%@ page import="java.math.BigInteger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
        %><%
    AbstractPage thisPage = new RouterPage(request, response);
    thisPage.executeRequest();
%>