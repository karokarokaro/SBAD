function prepareAddPopup($popup) {
    $popup.find('select[name="campType"]').val(1);
    $popup.find('input[name="name"]').val("");
    $popup.find('input[name="site"]').val("");
    $popup.find('input[name="phone"]').val("+7 ");
    $popup.find('input[name="addNumber"]').val("");
    $popup.find('input[name="contacts"]').val("");
    $popup.find('input[name="source"]').val("");
    $popup.find('p.help-block').html("");
    return true;
}
function prepareAddTaskPopup($popup) {
    //$popup.find('select[name="campId"]').val();
    $popup.find('input[name="dateText"]').val("");
    $popup.find('input[name="comment"]').val("Без комментариев");
    $popup.find('input[name="billNbr"]').val("");
    $popup.find('p.help-block').html("");
    return true;
}
function prepareCallPopup($popup) {
    var $checkboxes = $('.campTable .checkCol input[type="checkbox"]:checked');
    if ($checkboxes.size() != 1) {
        return false;
    }
    var $ids = $("#callForm .ids");
    $ids.html("");
    for (var i = 0; i < $checkboxes.size(); ++i) {
        var check = $checkboxes[i];
        var $tr = $(check).parent().parent();
        var objId = $tr.attr("obj-id");
        var check = "<input type='hidden' name='campId' value='"+ objId +"'/>"
        $ids.append(check);
    }
    $popup.find('textarea[name="reason"]').val("");
    $popup.find('textarea[name="comment"]').val("");
    $popup.find('input[name="date"]').val("");
    return true;
}
function prepareDeletePopup($popup) {
    var $checkboxes = $('.campTable .checkCol input[type="checkbox"]:checked');
    if ($checkboxes.size() == 0) {
        return false;
    }
    var $tbody = $("table.deleteForm > tbody");
    $tbody.html("");
    for (var i = 0; i < $checkboxes.size(); ++i) {
        var check = $checkboxes[i];
        var $tr = $(check).parent().parent();
        var objId = $tr.attr("obj-id");
        var name = $tr.find("td.nameCol").html();
        var tr = "<tr><td class='checkCol'><input type='checkbox' checked name='campId' value='" + objId + "'/></td>";
        tr += "<td><label>"+name+"</label></td></tr>";
        $tbody.append(tr);
    }
    return true;
}
function prepareDeleteTaskPopup($popup) {
    var $checkboxes = $('.taskTable .checkCol input[type="checkbox"]:checked');
    if ($checkboxes.size() == 0) {
        return false;
    }
    var $tbody = $("table.deleteForm > tbody");
    $tbody.html("");
    for (var i = 0; i < $checkboxes.size(); ++i) {
        var check = $checkboxes[i];
        var $tr = $(check).parent().parent();
        var objId = $tr.attr("obj-id");
        var name = $tr.find("td.nameCol").html();
        var tr = "<tr><td class='checkCol'><input type='checkbox' checked name='taskId' value='" + objId + "'/></td>";
        tr += "<td><label>"+name+"</label></td></tr>";
        $tbody.append(tr);
    }
    return true;
}

function prepareRejectTransferPopup($popup) {
    var $checkboxes = $('.campTable .checkCol input[type="checkbox"]:checked');
    if ($checkboxes.size() == 0) {
        return false;
    }
    var $tbody = $popup.find("table.rejectTransferForm > tbody");
    $tbody.html("");
    for (var i = 0; i < $checkboxes.size(); ++i) {
        var check = $checkboxes[i];
        var $tr = $(check).parent().parent();
        var objId = $tr.attr("obj-id");
        var name = $tr.find("td.nameCol").html();
        var tr = "<tr><td class='checkCol'><input type='checkbox' checked name='campId' value='" + objId + "'/></td>";
        tr += "<td><label>"+name+"</label></td></tr>";
        $tbody.append(tr);
    }
    return true;
}
addInitFunction(function() {
    $("input,select,textarea").not("[type=submit]").jqBootstrapValidation();
    $('.addButton').click(function() {
        if (prepareAddPopup($('#addPopup'))) {
            $('#addPopup').modal("show");
        }
    });
    $('.deleteButton').click(function() {
        if (prepareDeletePopup($('#deletePopup'))) {
            $('#deletePopup').modal("show");
        }
    });
    $('.deleteTaskButton').click(function() {
        if (prepareDeleteTaskPopup($('#deleteTaskPopup'))) {
            $('#deleteTaskPopup').modal("show");
        }
    });
    $('.addTaskButton').click(function() {
        if (prepareAddTaskPopup($('#addTaskPopup'))) {
            $('#addTaskPopup').modal("show");
        }
    });
    $(".schemeNeeded").change(function() {
        var isChecked = $(this).is(':checked');
        var campId = $(this).closest("tr").attr("camp-id");
        if (isChecked) {
            $('.taskTable tr[camp-id="'+campId+'"] .schemeNeeded').prop('checked', true);
            $('.schemes .half[camp-id="'+campId+'"]').show();
        } else {
            $('.taskTable tr[camp-id="'+campId+'"] .schemeNeeded').prop('checked', false);
            $('.schemes .half[camp-id="'+campId+'"]').hide();
        }
    });
    $('#callPopup, #karEditablePopupDate').find('input[name="date"]').datetimepicker({
        lang:'ru',
        format: 'Y-m-d H',
		formatTime: 'H',
        defaultSelect: false,
        allowTimes:['09', '10','11',  '12', '13', '14', '15', '16', '17', '18']
    });
    $('#karEditablePopupDateOnly').find('input[name="date"]').datetimepicker({
        lang:'ru',
        format: 'd/m/Y',
        timepicker:false,
        defaultSelect: false
    });
    $('#addTaskPopup').find('input[name="dateOnly"]').datetimepicker({
        lang:'ru',
        timepicker:false,
        format: 'd/m/Y',
        defaultSelect: false
    });
    $('#dateSelectPopup').find('input[name="date"]').datetimepicker({
        lang:'ru',
        timepicker:false,
        format: 'd/m/Y',
        defaultSelect: false
    });
});

