<%@ page import="core.helpers.FileHelper" %>
<%@ page import="core.Logger" %>
<%@ page contentType="text/html;charset=UTF-8"
         language="java"
        %><%
    try {
        FileHelper.uploadImagesFromRequest(request);
    } catch (Exception e) {
        Logger.error(e);
    }
%>