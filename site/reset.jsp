<%@ page import="core.helpers.DBHelper" %>
<%@ page import="core.helpers.TemplateHelper" %>
<%@ page import="core.cache.LocaleCache" %>
<%@ page import="core.cache.ObjectCache" %>
<%@ page import="core.cache.ObjectTypeCache" %>
<%@ page import="core.cache.SearchCache" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %>
<%
    LocaleCache.reset();
    ObjectCache.reset();
    ObjectTypeCache.reset();
    SearchCache.reset();
%>
<h1>Кеш сервера сброшен. </h1>