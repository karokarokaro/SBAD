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
            $karEditable.html(editableItem.value);
            $karEditable.removeClass("karEditableEmpty");
        }
        if (type == "combodate") {
            $karEditable.click(function(){
                KarEditable.active = KarEditable.items[$(this).attr("id")];
                prepareAndShowKarEditableComboDate(editableItem);
            });
        }
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
            $karEditable.html("");
        } else {
            $karEditable.html(editableItem.display(editableItem.value));
        }
    });
}
addInitFunction(function() {
    var $popup = $('#karEditablePopupDate');
    $popup.find(".okButton").click(function() {
        var date = $popup.find("form").find('input[name="date"]').val();
        var minute = $popup.find("form").find('select[name="minute"]').val();
        $popup.find("form").find('input[name="value"]').val(date+":"+minute);
        if (typeof KarEditable.active != "undefined") {
            var karedit = KarEditable.active;
            var params = parseForm($popup.find("form"));
            var value = params.value;
            if (typeof karedit.params != "undefined") {
                params = karedit.params(params);
            }
            $.ajax({
                type: "POST",
                url: karedit.url,
                data: params,
                dataType: "json",
                success: function(json) {
                    var mes = karedit.success(json, value);
                    if (typeof mes == "undefined") {
                        $elem = $("#"+karedit.id);
                        $elem.html(value);
                        karedit.value = value;
                        $('#karEditablePopupDate').find("p.help-block").html("");
                        $('#karEditablePopupDate').modal('hide');
                    } else {
                        $('#karEditablePopupDate').find("p.help-block").html(mes);
                    }
                },
                error: function(json) {
                    var mes = karedit.error(json, value);
                    $('#karEditablePopupDate').find("p.help-block").html(mes);
                }
            });
        }
    });
});
function prepareAndShowKarEditableComboDate(params) {
    var $popup = $('#karEditablePopupDate');
    if (typeof params.title != "undefined") {
        $popup.find(".modal-header").find("h3").html(params.title);
    }
    $('#karEditablePopupDate').find("p.help-block").html("");
    if (params.value.length == 16) {
        var val = params.value;
        var dateValue = val.substr(0, 13);
        var timeValue = val.substr(14, 2);
        $popup.find("form").find('input[name="date"]').val(dateValue);
        $popup.find("form").find('select[name="minute"]').val(timeValue);
    }
    $popup.modal("show");
}
function initKarEditable(id, params) {
    params.id = id;
    if ($("#"+id).text().trim().length > 0) {
        params.value = $("#"+id).text().trim();
    } else {
        params.value = "";
    }
    if (typeof params.display == "undefined") {
        params.display = function(str) {return str;}
    }
    KarEditable.items[params.id] = params;
}