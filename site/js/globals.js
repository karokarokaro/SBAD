InitFuncs = [];
function addInitFunction(func) {
    var item = func;
    if (typeof func != "function") {
        item = Function(func);
    }
    InitFuncs.push(item);
}
$(document).ready(function() {
    for (var i = 0; i < InitFuncs.length; ++i) {
        InitFuncs[i]();
    }
});
function parseForm($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    return data;
}
KarEditable = {
    clickHandlers: {
        "combodate": prepareAndShowKarEditableComboDate,
        "contacts" : prepareAndShowKarEditableContacts,
        "file" : prepareAndShowKarEditableFile
    },
    items: {}
};
function startKarEditables() {
    $.each (KarEditable.items, function(id, editableItem) {
        var $karEditable = $("#"+editableItem.id);
        var type = editableItem.type;
        $karEditable.removeClass("karEditableOff");
        $karEditable.addClass("karEditableOn");
        if (editableItem.value.length == 0) {
            $karEditable.html(editableItem.emptytext);
            $karEditable.addClass("karEditableEmpty");
        } else {
            $karEditable.html(editableItem.display(editableItem.value));
            $karEditable.removeClass("karEditableEmpty");
        }
        $karEditable.click(function(e) {
            KarEditable.active = KarEditable.items[id];
            KarEditable.clickHandlers[type](editableItem);
        });
    });
}
function stopKarEditables() {
    $.each (KarEditable.items, function(id, editableItem) {
        var $karEditable = $("#"+editableItem.id);
        $karEditable.removeClass("karEditableOn");
        $karEditable.addClass("karEditableOff");
        $karEditable.removeClass("karEditableEmpty");
        $karEditable.unbind('click');
        if (editableItem.value.length == 0) {
            $karEditable.html(editableItem.display(""));
        } else {
            $karEditable.html(editableItem.display(editableItem.value));
        }
    });
}
function initKarEditableOkButton($popup, getValue) {
    $popup.find(".okButton").click(function() {
        var value;
        if (typeof getValue != "undefined") {
            value = getValue($popup);
            $popup.find("form").find('input[name="value"]').val(value);
        } else {
            value = $popup.find("form").find('input[name="value"]').val();
        }
        if (typeof KarEditable.active != "undefined") {
            var karedit = KarEditable.active;
            var params = parseForm($popup.find("form"));
            params.value = params.value || value;
            var value = params.value;
            if (typeof karedit.params != "undefined") {
                params = karedit.params(params);
            }
            $.ajax({
                type: "POST",
                url: karedit.url,
                mimeType: "multipart/form-data",
                data: params,
                dataType: "json",
                success: function(json) {
                    var mes = karedit.success(json, value);
                    if (typeof mes == "undefined") {
                        $elem = $("#"+karedit.id);
                        $elem.html(karedit.display(value));
                        karedit.value = value;
                        if (value.length == 0) {
                            $elem.html(karedit.emptytext);
                            $elem.addClass("karEditableEmpty");
                        } else {
                            $elem.removeClass("karEditableEmpty");
                        }
                        $popup.find("p.help-block").html("");
                        $popup.modal('hide');
                    } else {
                        $popup.find("p.help-block").html(mes);
                    }
                },
                error: function(json) {
                    var mes = karedit.error(json, value);
                    $popup.find("p.help-block").html(mes);
                }
            });
        }
    });
}
function initKarEditable(id, params) {
    params.id = id;
    if ($("#"+id).text().trim().length > 0) {
        if (typeof $("#"+id).attr("data-value") != "undefined") {
            params.value = $("#"+id).attr("data-value");
        } else {
            params.value = $("#"+id).text().trim();
        }
    } else {
        params.value = "";
    }
    if (typeof params.display == "undefined") {
        params.display = function(str) {return str;}
    }
    KarEditable.items[params.id] = params;
}
function prepareKarEditablePopupHeader($popup, params) {
    if (typeof params.title != "undefined") {
        $popup.find(".modal-header").find("h3").html(params.title);
    }
    $popup.find("p.help-block").html("");

}
function prepareAndShowKarEditableComboDate(params) {
    var $popup = $('#karEditablePopupDate');
    prepareKarEditablePopupHeader($popup, params);
    if (params.value.length == 16) {
        var val = params.value;
        var dateValue = val.substr(0, 13);
        var timeValue = val.substr(14, 2);
        $popup.find("form").find('input[name="date"]').val(dateValue);
        $popup.find("form").find('select[name="minute"]').val(timeValue);
    }
    $popup.modal("show");
}
function contactsRefreshHolder($holder) {
    var tr = "<tr>\n" +
        "                                        <td>\n" +
        "                                            <input class=\"form-control phone\" type=\"text\" placeholder=\"Телефон\" />\n" +
        "                                        </td>\n" +
        "                                        <td>\n" +
        "                                            <input class=\"form-control name\" type=\"text\" placeholder=\"ФИО\" />\n" +
        "                                        </td>\n" +
        "                                        <td>\n" +
        "                                            <button class=\"btn btn-danger btn-sm\" onclick=\"$(this).closest('tr').remove()\"><i class=\"fa fa-minus\"></i></button>\n" +
        "                                        </td>\n" +
        "                                    </tr>";
    $holder.find("table").html(tr);
}
function contactsAddLine($holder) {
    var tr = "<tr>\n" +
        "                                        <td>\n" +
        "                                            <input class=\"form-control phone\" type=\"text\" placeholder=\"Телефон\" />\n" +
        "                                        </td>\n" +
        "                                        <td>\n" +
        "                                            <input class=\"form-control name\" type=\"text\" placeholder=\"ФИО\" />\n" +
        "                                        </td>\n" +
        "                                        <td>\n" +
        "                                            <button class=\"btn btn-danger btn-sm\" onclick=\"$(this).closest('tr').remove()\"><i class=\"fa fa-minus\"></i></button>\n" +
        "                                        </td>\n" +
        "                                    </tr>";
    $holder.find("table").append(tr);
}
function getContactsValueFromHolder($holder) {
    var value = "";
    var $trs = $holder.find("table tr");
    for (var i = 0; i < $trs.length; ++i) {
        value += $($trs[i]).find(".phone").val() + "=" + $($trs[i]).find(".name").val() + "@@@";
    }
    return value;
}
function getDisplayContacts(value) {
    var res = "";
    var vals = value.split("@@@");
    for (var i = 0; i < vals.length; ++i) {
        if (vals[i] == "") continue;
        var phone = vals[i].split("=")[0];
        var name = vals[i].split("=")[1];
        res += phone + "("+name+")<br/>";
    }
    return res;
}
function prepareAndShowKarEditableContacts(params) {
    var $popup = $('#karEditablePopupContacts');
    prepareKarEditablePopupHeader($popup, params);
    $holder = $popup.find(".contactsHolder");
    contactsRefreshHolder($holder);
    if (params.value == "") {
        $holder.find("table").html();
    } else {
        var vals = params.value.split("@@@");
        for (var i = 0; i < vals.length; ++i) {
            if (vals[i] == "") continue;
            var phone = vals[i].split("=")[0];
            var name = vals[i].split("=")[1];
            if(i > 0) {
                contactsAddLine($holder);
            }
            $($holder.find("table").find(".phone")[$holder.find("table").find(".phone").length-1]).val(phone);
            $($holder.find("table").find(".name")[$holder.find("table").find(".name").length-1]).val(name);
        }
    }
    $popup.modal("show");
}
function prepareAndShowKarEditableFile(params) {
    var $popup = $('#karEditablePopupFile');
    prepareKarEditablePopupHeader($popup, params);
    $popup.modal("show");
}
addInitFunction(function() {
    initKarEditableOkButton($('#karEditablePopupDate'), function($popup) {
        var date = $popup.find("form").find('input[name="date"]').val();
        var minute = $popup.find("form").find('select[name="minute"]').val();
        return date+":"+minute;
    });
    initKarEditableOkButton($('#karEditablePopupContacts'), function($popup) {
        return getContactsValueFromHolder($popup.find(".contactsHolder"));
    });
    initKarEditableOkButton($('#karEditablePopupFile'));
});