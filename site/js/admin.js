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
    $('a.historyButton').click(function() {
        var objId = $(this).parent().parent().attr("obj-id");
        var $modal = $('#historyPopup');
        $('body').modalmanager('loading');

        setTimeout(function(){
            $modal.load('/history.jsp?campId='+objId, '', function(){
                $modal.modal();
                $('body').modalmanager('removeLoading');
            });
        }, 10);
    });
    $('.callButton').click(function() {
        if (prepareCallPopup($('#callPopup'))) {
            $('#callPopup').modal("show");
        }
    });
    $('.rejectButton').click(function() {
        if (prepareRejectTransferPopup($('#rejectPopup'))) {
            $('#rejectPopup').modal("show");
        }
    });
    $('.transferButton').click(function() {
        if (prepareRejectTransferPopup($('#transferPopup'))) {
            $('#transferPopup').modal("show");
        }
    });
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
    $('#callPopup, #karEditablePopupDate').find('input[name="date"]').datetimepicker({
        lang:'ru',
        format: 'Y-m-d H',
		formatTime: 'H',
        defaultSelect: false,
        allowTimes:['09', '10','11',  '12', '13', '14', '15', '16', '17', '18']
    });
});

