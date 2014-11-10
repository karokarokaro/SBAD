function onErrorResponse(response, newValue) {
    return "Ошибка. ";
}
function onSuccessResponse(response, newValue) {
    if (typeof response == "undefined") {
        return "Ошибка при сохранении данных. ";
    }
    if(response.code != 0) {
        if (typeof response.message != "undefined") {
            return response.message;
        } else {
            return "Ошибка при сохранении данных. ";
        }
    } else {
//        if (response["attr-id"] == 40) {
//            var objId = response["obj-id"];
//            var $tr = $('tr[obj-id="'+objId+'"]');
//            var now = new Date();
//            var tomorrow = new Date();
//            tomorrow.setHours(23);
//            tomorrow.setMinutes(59);
//            var newDate = new Date(newValue);
//            if (newDate < now) {
//                $tr.removeClass("success");
//                $tr.addClass("danger");
//            } else if (newDate < tomorrow) {
//                $tr.removeClass("danger");
//                $tr.addClass("success");
//            } else {
//                $tr.removeClass("danger");
//                $tr.removeClass("success");
//            }
//        }
    }
}
addInitFunction(function(){
    var editableURL = "/crm/action.jsp";
    $('.editable[attr-id="45"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        params: function(params) {
            params.attrId = 45;
            params.act = "updateCamp";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $.each($('.kareditable[attr-id="40"]'), function(key, value) {
        var pk = $(value).attr("data-pk");
        var id = "kareditable-"+pk+"-40"
        $(value).attr("id", id);
        initKarEditable(id, {
            url: editableURL,
            type: "combodate",
            emptytext: 'Пусто',
            params: function(params) {
                params.pk = pk;
                params.attrId = 40;
                params.act = "updateCamp";
                return params;
            },
            title: "Введите дату и время звонка",
            success: onSuccessResponse,
            error: onErrorResponse
        });
    });
    $.each($('.kareditable[attr-id="57"]'), function(key, value) {
        var pk = $(value).attr("data-pk");
        var id = "kareditable-"+pk+"-57"
        $(value).attr("id", id);
        initKarEditable(id, {
            url: editableURL,
            type: "combodateonly",
            emptytext: 'Пусто',
            params: function(params) {
                params.pk = pk;
                params.attrId = 57;
                params.act = "updateTask";
                return params;
            },
            title: "Введите дату",
            success: onSuccessResponse,
            error: onErrorResponse
        });
    });
    $.each($('.kareditable[attr-id="50"]'), function(key, value) {
        var pk = $(value).attr("data-pk");
        var id = "kareditable-"+pk+"-50"
        $(value).attr("id", id);
        initKarEditable(id, {
            pk: pk,
            attrId: 50,
            url: editableURL,
            act: "updateCamp",
            type: "file",
            emptytext: 'Пусто',
            params: function(params) {
                params.pk = pk;
                params.attrId = 50;
                params.act = "updateCamp";
                return params;
            },
            display: function(value) {
                if (typeof value != "undefined" && value != "" && value != "Не загружено") {
                    return "Загружено";
                } else {
                    return "Не загружено";
                }
            },
            title: "Прикрепите изображение",
            success: onSuccessResponse,
            error: onErrorResponse
        });
    });
    $.each($('.kareditable[attr-id="37"]'), function(key, value) {
        var pk = $(value).attr("data-pk");
        var id = "kareditable-"+pk+"-37"
        $(value).attr("id", id);
        initKarEditable(id, {
            url: editableURL,
            type: "contacts",
            emptytext: 'Пусто',
            params: function(params) {
                params.pk = pk;
                params.attrId = 37;
                params.act = "updateTask";
                return params;
            },
            title: "Введите контактную информацию",
            display: getDisplayContacts,
            success: onSuccessResponse,
            error: onErrorResponse
        });
    });

//    $('.editable[attr-id="40"]').editable({
//        url: editableURL,
//        ajaxOptions: {
//            dataType: 'json'
//        },
//        emptytext: 'Пусто',
//        combodate: {
//            firstItem: 'name'
//        },
//        params: function(params) {
//            params.attrId = 40;
//            params.act = "updateCamp";
//            return params;
//        },
//        success: onSuccessResponse,
//        error: onErrorResponse
//    });

    $('.editable[attr-id="53"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 53;
            params.act = "updateTask";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="44"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 44;
            params.act = "updateTask";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="56"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 56;
            params.act = "updateTask";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="49"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 49;
            params.act = "updateTask";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="51"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 51;
            params.act = "updateTask";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="46"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 46;
            params.act = "updateCamp";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="42"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        showbuttons: 'bottom',
        placement: 'bottom',
        params: function(params) {
            params.attrId = 42;
            params.act = "updateCamp";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="36"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        params: function(params) {
            params.attrId = 36;
            params.act = "updateCamp";
            return params;
        },
        display: function(value) {
            $(this).html("<a target='_blank' href='http://"+value+"'>"+value+"</a>");
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="9"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        emptytext: 'Пусто',
        params: function(params) {
            params.attrId = 9;
            params.act = "updateCamp";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable[attr-id="41"]').editable({
        url: editableURL,
        ajaxOptions: {
            dataType: 'json'
        },
        inputclass: 'reasonInput',
        emptytext: 'Пусто',
        placement: "left",
        showbuttons: 'bottom',
        params: function(params) {
            params.attrId = 41;
            params.act = "updateCamp";
            return params;
        },
        success: onSuccessResponse,
        error: onErrorResponse
    });
    $('.editable').editable('toggleDisabled');
    $("#inedit-disable").click(function() {
        $("#inedit-disable").attr("disabled", "disabled");
        $("#inedit-enable").removeAttr("disabled");
        $('.editable').editable('toggleDisabled');
        stopKarEditables();
    });
    $("#inedit-enable").click(function() {
        $("#inedit-enable").attr("disabled", "disabled");
        $("#inedit-disable").removeAttr("disabled");
        $('.editable').editable('toggleDisabled');
        startKarEditables();
    });
});
addInitFunction(function(){
    startKarEditables();
    stopKarEditables();
});