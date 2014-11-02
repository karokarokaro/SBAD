<%@ page contentType="text/html;charset=UTF-8"
         language="java"
         import="core.database.*"
         import="java.sql.*"
         import="java.util.*"
         import="java.math.BigInteger"
         import="core.helpers.*"
         import="core.renderers.*"
         import="core.entity.*"%><%
    try{
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
%><!DOCTYPE HTML>
<html>
<head>
    <title>Object Editor</title>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        function go(act) {
            var id = jQuery("#"+act+"Obj option:selected").val();
            window.location.href = "/objects.jsp?act="+act+"&id="+id;
        }
        function toEdit(selectId, inputId) {
            $('#'+inputId).val($('#'+selectId+' option:selected').val());
        }
    </script>
    <style>
        td {
            vertical-align: top;
            border: 1px solid #9994FF;
        }
        tr {
            background-color: lemonchiffon;
        }
    </style>
</head>
<body>
<!--%=request.getParameterMap().toString()%-->
<table cellpadding="0" colspacing="0">
    <tr>
        <td>
            <select id="createObj">
                <%
                    ResultSet resultSet = SQLController.executeSelect("select * from object_types");
                    resultSet.beforeFirst();
                    while (resultSet.next()) {%>
                <option value="<%=resultSet.getBigDecimal("object_type_id").toBigInteger().toString()%>"><%=resultSet.getString("name")%></option>
                <%}
                %>
            </select>
        </td>
        <td>
            <input type="button" value="create" onclick="go('create');">
        </td>
    </tr>
    <tr>
        <td>
            <select id="editObj">
                <%
                    resultSet = SQLController.executeSelect("select o.object_id as id, o.name as name, ot.name as type " +
                            "from objects o " +
                            "join object_types ot on o.object_type_id=ot.object_type_id");
                    resultSet.beforeFirst();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (resultSet.next()) {
                        stringBuilder.append("<option value=\"");
                        stringBuilder.append(resultSet.getBigDecimal("id").toBigInteger().toString());
                        stringBuilder.append("\">");
                        stringBuilder.append(resultSet.getString("name")+"("+resultSet.getString("type")+")");
                        stringBuilder.append("</option>");
                    }
                %><%=stringBuilder.toString()%>
            </select>
        </td>
        <td>
            <input type="button" value="edit" onclick="go('edit');">
        </td>
    </tr>
    <tr>
        <td>
            <select id="deleteObj">
                <%=stringBuilder.toString()%>
            </select>
        </td>
        <td>
            <input type="button" value="delete" onclick="go('delete');">
        </td>
    </tr>
</table>
<%
    String reqAct = request.getParameter("act");
    String reqId = request.getParameter("id");
    if (reqAct != null && (reqAct.equals("create") || reqAct.equals("edit"))) {
%>
<form action="/objects.jsp" method="post">
    <input type="hidden" name="act" value="do<%=reqAct%>" />
    <table cellpadding="0" colspacing="0">
        <%
            if (reqAct.equals("create")) {
        %>
        <tr>
            <td>name</td><td><input type="text" name="name" /></td>
        </tr>
        <tr>
            <td>description</td><td><input type="text" name="description" /><input type="hidden" name="object_type_id" value="<%=reqId%>" /></td>
        </tr>
        <%
            resultSet = SQLController.executeSelect("select a.attr_id as id, a.name as name, vt.name as valueType\n" +
                    "from object_types ot\n" +
                    "join vals li on ot.attr_list_id=li.list_id\n" +
                    "join attributes a on li.id_v=a.attr_id\n" +
                    "join value_types vt on a.value_type_id=vt.value_type_id\n" +
                    "where ot.object_type_id="+reqId+" order by li.order");
            resultSet.beforeFirst();
            while (resultSet.next()) {%>
        <tr>
            <td>
                <%=resultSet.getString("name")%>
                <br/>
                (<%=resultSet.getString("valueType")%>)
            </td>
            <td>
                <label>double</label>
                <input type="text" name="<%="attr_"+resultSet.getString("id")+"dd"%>" />
                <label>timestamp</label>
                <input type="text" name="<%="attr_"+resultSet.getString("id")+"ts"%>" />
                <label>id</label>
                <input type="text" id="<%="attr_"+resultSet.getString("id")+"idi"%>" name="<%="attr_"+resultSet.getString("id")+"ii"%>" />
                <select id="<%="attr_"+resultSet.getString("id")+"ids"%>" onclick="toEdit('<%="attr_"+resultSet.getString("id")+"ids"%>', '<%="attr_"+resultSet.getString("id")+"idi"%>');">
                    <%=stringBuilder.toString()%>
                </select>

                <label>text</label>
                <textarea name="<%="attr_"+resultSet.getString("id")+"tt"%>"></textarea>
            </td>
        </tr>
        <%}
        } else if (reqAct.equals("edit")) {
        %>
        <%DBObject dbObject = DBHelper.getObjectById(new BigInteger(reqId));%>
        <tr>
            <td>name</td><td><input type="text" name="name" value="<%=dbObject.getName()%>"/></td>
        </tr>
        <tr>
            <td>description</td><td><input type="text" name="description" value="<%=dbObject.getDescription()%>"/><input type="hidden" name="id" value="<%=reqId%>" /></td>
        </tr>
        <%
            for (DBAttribute dbAttribute: dbObject.getAttributes()) {%>
        <tr>
            <td>
                <%=dbAttribute.getName()%>
                <br/>
                (<%=dbAttribute.getValueTypeName()%>)
            </td>
            <td>
                <label>double</label>
                <input type="text" name="<%="attr_"+dbAttribute.getId().toString()+"dd"%>" value="<%=dbAttribute.getDoubleValue()%>"/>
                <label>timestamp</label>
                <input type="text" name="<%="attr_"+dbAttribute.getId().toString()+"ts"%>" value="<%=dbAttribute.getTimestampValue() != null ? dbAttribute.getTimestampValue().toString() : ""%>"/>
                <label>id</label>
                <input type="text" id="<%="attr_"+dbAttribute.getId().toString()+"idi"%>" name="<%="attr_"+dbAttribute.getId().toString()+"ii"%>" value="<%=dbAttribute.getIdValue() != null ? dbAttribute.getIdValue().toString() : ""%>"/>
                <select id="<%="attr_"+dbAttribute.getId().toString()+"ids"%>" onclick="toEdit('<%="attr_"+dbAttribute.getId().toString()+"ids"%>', '<%="attr_"+dbAttribute.getId().toString()+"idi"%>');">
                    <%=stringBuilder.toString()%>
                </select>
                <label>text</label>
                <textarea style="width: 350px;height: 100px;" name="<%="attr_"+dbAttribute.getId().toString()+"tt"%>"><%=dbAttribute.getTextValue()%></textarea>
            </td>
        </tr>
        <%
                }  }
        %>
    </table>
    <input type="submit" value="<%=reqAct%>" />
</form>
<%
} else if (reqAct != null && reqAct.equals("docreate")) {
    int n = SQLController.executeUpdate("INSERT INTO objects (`name`, `description`, `object_type_id`) VALUES (" +
            "'"+request.getParameter("name")+"', " +
            "'"+request.getParameter("description")+"', " +
            request.getParameter("object_type_id")+");");
    if (n == 0) {
%><h2 style='color: #dd2222;'>ОШИБКА</h2><%
} else {
%>Объект "<%=request.getParameter("name")%>" <span style='color: #22dd22;'>УСПЕШНО</span> создан<%
    }
    resultSet = SQLController.executeSelect("select object_id from objects order by object_id desc;");
    resultSet.beforeFirst(); resultSet.next();
    String objId = resultSet.getBigDecimal("object_id").toBigInteger().toString();

    System.out.println("ID = " + objId);

    List<String> attrs = new ArrayList<String>();
    resultSet = SQLController.executeSelect("select a.attr_id as id\n" +
            "from object_types ot\n" +
            "join vals li on ot.attr_list_id=li.list_id\n" +
            "join attributes a on li.id_v=a.attr_id\n" +
            "where ot.object_type_id="+request.getParameter("object_type_id"));
    resultSet.beforeFirst();
    while (resultSet.next()) {
        attrs.add(resultSet.getBigDecimal("id").toBigInteger().toString());
    }
    int nAttr = 0;
    for (String attr: attrs) {
        boolean needed = false;
        if (request.getParameter("attr_"+attr+"dd") != null && request.getParameter("attr_"+attr+"dd").length() > 0) needed = true;
        if (request.getParameter("attr_"+attr+"ts") != null && request.getParameter("attr_"+attr+"ts").length() > 0) needed = true;
        if (request.getParameter("attr_"+attr+"ii") != null && request.getParameter("attr_"+attr+"ii").length() > 0) needed = true;
        if (request.getParameter("attr_"+attr+"tt") != null && request.getParameter("attr_"+attr+"tt").length() > 0) needed = true;
        if (needed) {
            StringBuilder sss = new StringBuilder();
            sss.append("INSERT INTO `vals` (`object_id`, `attr_id`");
            if (request.getParameter("attr_"+attr+"dd") != null && request.getParameter("attr_"+attr+"dd").length() > 0)
                sss.append(",`double_v`");
            if (request.getParameter("attr_"+attr+"ts") != null && request.getParameter("attr_"+attr+"ts").length() > 0)
                sss.append(",`ts_v`");
            if (request.getParameter("attr_"+attr+"ii") != null && request.getParameter("attr_"+attr+"ii").length() > 0)
                sss.append(",`id_v`");
            if (request.getParameter("attr_"+attr+"tt") != null && request.getParameter("attr_"+attr+"tt").length() > 0)
                sss.append(",`text_v`");
            sss.append(") VALUES (");
            sss.append(objId+", " + attr);
            if (request.getParameter("attr_"+attr+"dd") != null && request.getParameter("attr_"+attr+"dd").length() > 0)
                sss.append(", " + request.getParameter("attr_"+attr+"dd"));
            if (request.getParameter("attr_"+attr+"ts") != null && request.getParameter("attr_"+attr+"ts").length() > 0)
                sss.append(", '" + request.getParameter("attr_"+attr+"ts")+"'");
            if (request.getParameter("attr_"+attr+"ii") != null && request.getParameter("attr_"+attr+"ii").length() > 0)
                sss.append(", " + request.getParameter("attr_"+attr+"ii"));
            if (request.getParameter("attr_"+attr+"tt") != null && request.getParameter("attr_"+attr+"tt").length() > 0)
                sss.append(", '"+request.getParameter("attr_"+attr+"tt")+"'");
            sss.append(");");
            nAttr += SQLController.executeUpdate(sss.toString());
        }
    }
%><br/>Добавлено <%=nAttr%> аттрибутов<%
    } else if (reqAct != null && reqAct.equals("doedit")) {
        DBObject dbObject = DBHelper.getObjectById(new BigInteger(reqId));
        String reqName = request.getParameter("name");
        String reqDesc = request.getParameter("description");
        if (!dbObject.getName().equals(reqName)) {
            SQLController.executeUpdate("update objects set `name`='"+reqName+"' where object_id="+reqId);
        }
        if (!dbObject.getDescription().equals(reqDesc)) {
            SQLController.executeUpdate("update objects set `description`='"+reqDesc+"' where object_id="+reqId);
        }
        for (DBAttribute dbAttribute: dbObject.getAttributes()) {
            String postfix = "";
            String coll = "";
            if (dbAttribute.getValueTypeUsing().equals("id")) {
                postfix = "ii";
                coll = "id_v";
            } else if (dbAttribute.getValueTypeUsing().equals("double")) {
                postfix = "dd";
                coll = "double_v";
            } else if (dbAttribute.getValueTypeUsing().equals("text")) {
                postfix = "tt";
                coll = "text_v";
            } else if (dbAttribute.getValueTypeUsing().equals("ts")) {
                postfix = "ts";
                coll = "ts_v";
            }
            if (request.getParameter("attr_"+dbAttribute.getId().toString()+postfix)!=null) {
                String vall = request.getParameter("attr_"+dbAttribute.getId().toString()+postfix);
                if (vall.length() == 0) {
                    vall = "null";
                } else if(postfix.equals("tt") || postfix.equals("ts")) {
                    vall = "'" + vall + "'";
                }
                SQLController.executeUpdate("update vals set `"+coll+"`="+vall+" where object_id="+reqId + " and attr_id=" + dbAttribute.getId().toString());
            }
        }
        response.sendRedirect("/objects.jsp?act=edit&id="+reqId);
        return;
    }

%>
</body>
</html>
<%} catch (Exception e) {
    %><%="<br/>\nmessage: "+e.getMessage()+"\n<br/>-------------------------------------<br/>\ncause: "+e.getCause().getMessage()%><%

}%>