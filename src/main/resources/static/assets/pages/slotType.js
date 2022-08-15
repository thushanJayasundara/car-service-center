var editable=false;

$(document).ready(function() {
    loadSlotTypeTable()
    $("#addNewSlotBtn").click(function(e) {
        clearSlotType()
        $('#addNewSlotPnl').collapse('show');
    });
    getAllMaintenanceType();
    getAllTimePeriodType();
});


function saveSlotType() {
    editable=false;
    var userObj={
        "id":$('#slotTypeId').val() == "" ? "" : $('#slotTypeId').val(),
        "slotName":$('#addSlotTypeId').val(),
        "maintenanceType":{
            "id":  $('#setMaintenanceTypeId').val()
        },
        "timePeriod":{
            "id":  $('#setTimePeriodId').val()
        },
        "editable":editable,
        "commonStatus":$('#slotTypeStatusId').val() == "-1" ? null : $('#slotTypeStatusId').val(),
    }

    $.ajax({
        url: "v1/slot-management-api/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadSlotTypeTable()
                clearSlotType()
                toastr.success("Successfully saved.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function  UpdateSlotType(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#slotTypeId').val(),
        "slotName":$('#addSlotTypeId').val(),
        "editable":editable,
        "maintenanceType":{
            "id":  $('#setMaintenanceTypeId').val()
        },
        "timePeriod":{
            "id":  $('#setTimePeriodId').val()
        },
        "commonStatus":$('#slotTypeStatusId').val() == "-1" ? null : $('#slotTypeStatusId').val(),
    }

    $.ajax({
        url:"v1/slot-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadSlotTypeTable()
                clearSlotType();
                toastr.success("Successfully updated");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function loadSlotTypeTable() {
    $.ajax({
        url: "v1/slot-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setSlotTypeTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getByIdForEditSlotType(data) {

    var url = "v1/slot-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setSlotTypeData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}



function setSlotTypeData(data) {
    if(data.status && data.payload != null) {
        editable=true;

        $('#addNewSlotPnl').collapse('show');
        $('#slotTypeUpdateBtnId').prop('disabled', false);
        $('#slotTypeSaveBtnId').prop('disabled', true);
        $('#slotTypeId').val(data.payload[0].id);
        $('#setMaintenanceTypeId').val(data.payload[0].maintenanceType.id);
        if(data.payload[0].timePeriod !== null)
             $('#setTimePeriodId').val(data.payload[0].timePeriod.id);
        else
            $('#setTimePeriodId').val('-1');
        $('#addSlotTypeId').val(data.payload[0].slotName);
        $('#slotTypeStatusId').val(data.payload[0].commonStatus);
    }
}

function setSlotTypeTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#slotTypeTable').DataTable().clear();
        $('#slotTypeTable').DataTable({
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
        $("#slotTypeTable").DataTable({
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
                    "data": "slotName"
                },
                {
                    "data" : "maintenanceType.maintenanceName"
                },
                {
                    "data" : "timePeriod.timeCode",
                    mRender: function(data) {
                        var classLb = ''
                        if(data == null)
                            classLb = 'Not Select'
                        else
                            classLb = data
                        var columnHtml = '<td>'+classLb+'</td>';
                        return (columnHtml);
                    }
                },
                {
                    "data" : "availability"
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
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditSlotType(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteSlotTypeConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}
function deleteSlotTypeConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteSlotType(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteSlotType(data) {
    $.ajax({
        url:"v1/slot-management-api/"+ data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadSlotTypeTable();
                clearSlotType();
                toastr.success("Slot Type Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Slot Type Deleted failed.");
        }
    });
}


function getAllMaintenanceType() {
    $.ajax({
        url: "v1/maintenance-type-management-api/get-by-status/ACTIVE",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if(dataList != null) {
                $('#setMaintenanceTypeId').empty();
                $('#setMaintenanceTypeId').append('<option value="-1">--Select a Type--</option>');
                $.each(dataList, function(key, value) {
                    $('#setMaintenanceTypeId').append(' <option value="'+value.id+'">'+value.maintenanceName+'</option>');
                });

            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function getAllTimePeriodType() {
    $.ajax({
        url: "v1/time-period-management-api/get-by-status/ACTIVE",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if(dataList != null) {
                $('#setTimePeriodId').empty();
                $('#setTimePeriodId').append('<option value="-1">--Select Time Period--</option>');
                $.each(dataList, function(key, value) {
                    $('#setTimePeriodId').append(' <option value="'+value.id+'">'+value.timeCode+'</option>');
                });

            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function resetSlotPnl() {
    $('#addNewSlotPnl').collapse('hide');
}

function clearSlotType(){
    getAllMaintenanceType();
    getAllTimePeriodType();
    $('#slotTypeUpdateBtnId').prop('disabled', true);
    $('#slotTypeSaveBtnId').prop('disabled', false);
    $('#addSlotTypeId').val('');
    $('#slotTypeStatusId').val('-1');
    $('#setTimePeriodId').val('-1');
}
