var editable=false;

$(document).ready(function() {
    loadRepairTable()
    clearRepair()
    $("#addNewRepairBtn").click(function(e) {
        $('#repairPnl').collapse('show');
    });
});

function resetRepair(){
    $('#repairPnl').collapse('hide');
}

function saveRepair() {
    editable=false;
    var userObj={
        "id":$('#repairId').val() == "" ? "" : $('#repairId').val(),
        "repairName":$('#addRepairId').val(),
        "editable":editable,
        "commonStatus":$('#repairStatusId').val() == "-1" ? null : $('#repairStatusId').val(),
    }

    $.ajax({
        url: "v1/repair-management-api/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadRepairTable()
                clearRepair();
                toastr.success("Successfully saved.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function updateRepair(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#repairId').val(),
        "repairName":$('#addRepairId').val(),
        "editable":editable,
        "commonStatus":$('#repairStatusId').val() == "-1" ? null : $('#repairStatusId').val(),
    }

    $.ajax({
        url:"v1/repair-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadRepairTable()
                clearRepair();
                toastr.success("Successfully updated");
                $('#maintenanceTypePnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function loadRepairTable(){
    $.ajax({
        url: "v1/repair-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setRepairTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getByIdForEditRepairType(data) {

    var url = "v1/repair-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setRepairData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}

function setRepairData(data) {
    if(data.status && data.payload != null) {
        editable=true;
        $('#repairPnl').collapse('show');
        $('#repairSaveBtnId').prop('disabled', true);
        $('#repairUpdateBtnId').prop('disabled', false);
        $('#repairId').val(data.payload[0].id);
        $('#addRepairId').val(data.payload[0].repairName);
        $('#repairStatusId').val(data.payload[0].commonStatus);
    }
}
function setRepairTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#repairTable').DataTable().clear();
        $('#repairTable').DataTable({
            "bPaginate": false,
            "bLengthChange": false,
            "bFilter": false,
            "bInfo": false,
            "destroy": true,
            "language": {
                "emptyTable": "No Data Found !!!",
                search: "",
                searchPlaceholder: "Search..."
            }
        });
    } else {
        $("#repairTable").DataTable({
            dom: 'Bfrtip',
            buttons: [{
                extend: 'pageLength'
            },
                {
                    extend: 'colvis'
                }
            ],
            "destroy": true,
            "language": {
                search: "",
                searchPlaceholder: "Search..."
            },
            "data": data,
            "columns": [
                {
                    "data": "repairName"
                },
                {
                    "data": "commonStatus",
                    mRender: function(data) {
                        var classLb = ''
                        if(data == "ACTIVE")
                            classLb = 'badge bg-success'
                        if(data == "INACTIVE")
                            classLb = 'badge bg-primary'
                        var columnHtml = '<td><label class="'+classLb+'">'+data+'</label></td>';
                        return (columnHtml);
                    }
                }, {
                    "data": "id",
                    mRender: function(data) {
                        var columnHtml = '<div class="btn-group">\n' +
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditRepairType(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteRepairConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}

function deleteRepairConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteRepair(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteRepair(data) {
    $.ajax({
        url: "v1/repair-management-api/"+ data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadRepairTable();
                clearRepair();
                toastr.success("Repair Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Repair Deleted failed.");
        }
    });
}



function clearRepair(){
    $('#repairSaveBtnId').prop('disabled', false);
    $('#repairUpdateBtnId').prop('disabled', true);
    $('#addRepairId').val('');
    $('#repairStatusId').val('-1');
}