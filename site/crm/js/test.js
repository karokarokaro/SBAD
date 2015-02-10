function sectionInfoPageInit() {
    var tables = [document.getElementById('t9141779014713965001_0_t'),
        document.getElementById('t9141779227413965200_0_t')];
    var sel = '<select class="statusSelect" onchange="statusSelectChanged(this)">';
    sel += getOptionElem('Planned', '1');
    sel += getOptionElem('In Serivce', '2');
    sel += '</select>';
    for (var i = 0; i < 2; ++i) {
        var table = tables[i];
        var theadTr = table.tHead.children[0];
        var index = -1;
        var controlIndex = -1;
        for (var k = 0; k < theadTr.children.length; ++k) {
            if (theadTr.children[k].innerText.indexOf("Status") != -1) {
                index = k;
            } else if (theadTr.children[k].innerText.indexOf("Control VLAN") != -1) {
                controlIndex = k;
            }
        }
        var th = document.createElement('th');
        th.innerHTML = 'Status';
        theadTr.appendChild(th);
        for (var j = 0; j < table.children[1].children.length; ++j) {
            var tr = table.children[1].children[j];
            var td = document.createElement('td');
            td.className = 'Default';
            td.innerHTML = sel;
            tr.appendChild(td);
            if (index != -1) {
                if (tr.children[index].innerText.indexOf("In Serivce") != -1) {
                    tr.lastChild.lastChild.value = "2";
                }
            }
//            if (tr.children[controlIndex].innerText.trim().toLowerCase() == "true") {
//                tr.children[0].innerHTML = "";
//            }
        }
    }
}
function getOptionElem(title, value) {
    return '<option value="'+value+'">'+title+'</option>';
}
function statusSelectChanged(elem) {
    var tr = elem;
    while (tr.tagName.toLowerCase() != "tr" && tr.tagName.toLowerCase() != "body") {
        tr = tr.parentNode;
    }
    if (tr.tagName.toLowerCase() == "body") return;
    if (tr.children[0].getElementsBySelector('input[type="checkbox"]').length == 0) return;
    var checkBox = tr.children[0].getElementsBySelector('input[type="checkbox"]')[0];
    if (!checkBox.checked) return;
    var tbody = document.querySelector('table#t9141779014713965001_0_t.mainControl > tbody');
    for (var i = 0; i < tbody.children.length; ++i) {
        if (tbody.children[i].getElementsBySelector('input[type="checkbox"]').length == 0) continue;
        if (tbody.children[i].getElementsBySelector('input[type="checkbox"]')[0].checked) {
            tbody.children[i].getElementsBySelector(".statusSelect")[0].value = elem.value;
        }
    }
    tbody = document.querySelector('table#t9141779227413965200_0_t.mainControl > tbody');
    for (var i = 0; i < tbody.children.length; ++i) {
        if (tbody.children[i].getElementsBySelector('input[type="checkbox"]').length == 0) continue;
        if (tbody.children[i].getElementsBySelector('input[type="checkbox"]')[0].checked) {
            tbody.children[i].getElementsBySelector(".statusSelect")[0].value = elem.value;
        }
    }
}
function getSendParam() {
    var res = "";
    var tbody = document.querySelector('table#t9141779014713965001_0_t.mainControl > tbody');
    for (var i = 0; i < tbody.children.length; ++i) {
        var href = tbody.children[i].children[1].getElementsBySelector("a")[0].href;
        var objId = href.substr(href.indexOf("id=")+3);
        res += (res != "" ? "|" : "") + objId + "=" + tbody.children[i].getElementsBySelector(".statusSelect")[0].value;
    }
    tbody = document.querySelector('table#t9141779227413965200_0_t.mainControl > tbody');
    for (var i = 0; i < tbody.children.length; ++i) {
        var href = tbody.children[i].children[1].getElementsBySelector("a")[0].href;
        var objId = href.substr(href.indexOf("id=")+3);
        res += (res != "" ? "|" : "") + objId + "=" + tbody.children[i].getElementsBySelector(".statusSelect")[0].value;
    }
    return res;
}
function sendStatusInfo() {
    document.querySelector('.statusform input[name="status"]')[0].value = getSendParam();
    document.querySelector('.statusform input[name="status"]')[0].submit();
}