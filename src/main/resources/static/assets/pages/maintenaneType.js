var editable=false;

$(document).ready(function() {
    loadMaintenanceTypeTable()
    $("#addNewMaintenanceTypeBtn").click(function(e) {
        clearMaintenanceType()
        $('#maintenanceTypePnl').collapse('show');
    });
});

function resetMaintenanceType(){
    $('#maintenanceTypePnl').collapse('hide');
}

function saveMaintenanceType() {
    editable=false;
    var userObj={
        "id":$('#maintenanceTypeId').val() == "" ? "" : $('#maintenanceTypeId').val(),
        "maintenanceName":$('#addMaintenanceTypeId').val(),
        "editable":editable,
        "commonStatus":$('#maintenanceTypeStatusId').val() == "-1" ? null : $('#maintenanceTypeStatusId').val(),
    }

    $.ajax({
        url: "v1/maintenance-type-management-api/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadMaintenanceTypeTable()
                clearMaintenanceType();
                toastr.success("Successfully saved.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function updateMaintenanceType(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#maintenanceTypeId').val(),
        "maintenanceName":$('#addMaintenanceTypeId').val(),
        "editable":editable,
        "commonStatus":$('#maintenanceTypeStatusId').val() == "-1" ? null : $('#maintenanceTypeStatusId').val(),
    }

    $.ajax({
        url:"v1/maintenance-type-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadMaintenanceTypeTable()
                clearMaintenanceType();
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

function loadMaintenanceTypeTable(){
    $.ajax({
        url: "v1/maintenance-type-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setMaintenanceTypeTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}


function getByIdForEditMaintenanceType(data) {

    var url = "v1/maintenance-type-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setMaintenanceTypeData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}

function setMaintenanceTypeData(data) {
    if(data.status && data.payload != null) {
        editable=true;
        $('#maintenanceTypePnl').collapse('show');
        $('#maintenanceTypeSaveBtnId').prop('disabled', true);
        $('#maintenanceTypeUpdateBtnId').prop('disabled', false);
        $('#maintenanceTypeId').val(data.payload[0].id);
        $('#addMaintenanceTypeId').val(data.payload[0].maintenanceName);
        $('#maintenanceTypeStatusId').val(data.payload[0].commonStatus);
    }
}

function setMaintenanceTypeTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#maintenanceTypeTable').DataTable().clear();
        $('#maintenanceTypeTable').DataTable({
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
        $("#maintenanceTypeTable").DataTable({
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
                    "data": "maintenanceName"
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
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditMaintenanceType(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteMaintenanceTypeConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}
function deleteMaintenanceTypeConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteMaintenanceType(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteMaintenanceType(data) {
    $.ajax({
        url: "v1/maintenance-type-management-api/"+ data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadMaintenanceTypeTable();
                clearMaintenanceType();
                toastr.success("Maintenance Type Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Maintenance Type Deleted failed.");
        }
    });
}

function clearMaintenanceType(){
    $('#maintenanceTypeUpdateBtnId').prop('disabled', true);
    $('#maintenanceTypeSaveBtnId').prop('disabled', false);
    $('#addMaintenanceTypeId').val('');
    $('#maintenanceTypeStatusId').val('-1');
}
