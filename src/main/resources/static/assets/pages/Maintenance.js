$(document).ready(function() {
    loadMaintenanceTable()
    clearMaintenance();
    $("#addNewMaintenanceBtn").click(function(e) {
        clearMaintenance();
        $('#maintenancePnl').collapse('show');
    });
});

function saveMaintenance() {
    editable = false;
    var repair = [];

    $.each($("input[name='repairTypeNa']:checked"), function() {
        repair.push({
            id: $(this).val().trim()
        });
    });

    var obj = {
        "id": $('#maintenanceId').val() == "" ? "" : $('#maintenanceId').val(),
        "maintenanceType":{
            "id":  $('#setMaintenanceTypeId').val()
        },
        "vehicleNo":$('#vehicleNoId').val(),
        "otherDescription":$('#otherDescriptionId').val(),
        "repairDTOS": repair,
        "customerDTO":{
           "id":  $('#customerMaintenId').val()
        },
        "editable":editable,
        "commonStatus":$('#maintenanceStatusId').val() == "-1" ? null : $('#maintenanceStatusId').val(),
    }

    $.ajax({
        url: "v1/maintenance-management-api/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(obj),
        success: function(data) {
            if(data.status) {
                loadMaintenanceTable()
                clearMaintenance()
                toastr.success("Successfully save.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function updateMaintenance(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#maintenanceId').val(),
        "maintenanceType":$('#maintenanceTypeId').val(),
        "vehicleNo":$('#vehicleNoId').val(),
        "otherDescription":$('#otherDescriptionId').val(),
        "commonStatus":$('#maintenanceStatusId').val() == "-1" ? null : $('#maintenanceStatusId').val(),
    }

    $.ajax({
        url:"v1/maintenance-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadMaintenanceTable()
                clearMaintenance()
                toastr.success("Successfully updated");
                $('#maintenancePnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}
function loadMaintenanceTable(){
    $.ajax({
        url:"v1/maintenance-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setMaintenanceTable(data.payload[0]);
            } else {
                toastr.error("Error Getting Data");
            }
        }
    });
}
function getByIdForEditMaintenance(data) {

    var url = "v1/maintenance-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setMaintenanceData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}

function setMaintenanceData(data) {
    if(data.status && data.payload != null) {
        editable = true;
        $('#maintenanceUpdateBtn').prop('disabled', false);
        $('#maintenanceSaveBtn').prop('disabled', true);

        $('#vehicleNoId').val(data.payload[0].vehicleNo);
        $('#otherDescriptionId').val(data.payload[0].otherDescription);
        $('#maintenanceStatusId').val(data.payload[0].commonStatus);
        $('#maintenancePnl').collapse('show');
        var repair = data.payload[0].repairDTOS;
        $.each(repair, function(key, value) {
            $('input[name=repairTypeNa][value=' + value.id + ']').prop("checked", "true");
        });
        $('#setMaintenanceTypeId').val(data.payload[0].maintenanceType.id);
        $('#personAndPrivilegeId').val(data.payload[0].id);
    }
}

function  setMaintenanceTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#maintenanceTable').DataTable().clear();
        $('#maintenanceTable').DataTable({
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
        $("#maintenanceTable").DataTable({
            dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50, 100],
                [ '10', '25', '50', '100']
            ],
            buttons: [{
                extend: 'pageLength'
            },
                {
                    extend: 'pdf',
                    title: 'Maintenance Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'excel',
                    title: 'Maintenance Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'print',
                    title: 'Maintenance Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },{
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
                    "data": "maintenanceType.maintenanceName"
                },
                {
                    "data": "repairDTOS",
                    mRender: function(data) {
                        let columnHtml;
                        $.each(data, function(key, value) {
                            if (columnHtml == null)
                                columnHtml = String(value.repairName);
                            else {
                                columnHtml = columnHtml.concat(" / ", value.repairName);
                            }
                        });
                        var finalcol = '<td><label>' + columnHtml + '</label></td>'
                        return (finalcol);
                    }
                },
                {
                    "data": "vehicleNo"
                },
                {
                    "data": "otherDescription"
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
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditMaintenance(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteMaintenanceConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}
function deleteMaintenanceConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteMaintenance(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteMaintenance(data) {
    $.ajax({
        url: "v1/maintenance-management-api/"+ data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadMaintenanceTable()
                clearMaintenance()
                toastr.success("Maintenance Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Maintenance Deleted failed.");
        }
    });
}

function clearMaintenance(){
    $('#maintenanceTypeId').val('');
    $('#findCustomerNicId').val('');
    $('#vehicleNoId').val('');
    $('#maintenanceStatusId').val('-1');
    $('#customerMaintenanceId').val('-1');
    $('#otherDescriptionId').val('');
    $('#maintenanceUpdateBtn').prop('disabled', true);
    $('#maintenanceSaveBtn').prop('disabled', false);
    getAllRepairForMaintenance()
    getAllMaintenanceType();
}

function resetMaintenancePnl() {
    $('#maintenancePnl').collapse('hide');
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

function getCustomerToMapMaintenance() {
    var cusNic = $.trim($('#findCustomerNicId').val());
    cusNic = cusNic.length === 0 ? '-1' : cusNic;
    var url = 'v1/customer-management-api/advance-search/'+ cusNic;
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function (data) {
            if (data.status && data.payload != null) {
                var dataList = data.payload[0];
                $('#customerMaintenId').empty();
                $.each(dataList, function (key, value) {
                    $('#customerMaintenId').append('<option value="' + value.id + '">' + value.nic + ' - ' + value.firstName + ' ' + value.lastName + '</option>');
                    $('#customerMaintenId').val(value.id);
                });

            } else {
                toastr.error("No Recode Found");
            }
        }
    });

}

function getAllRepairForMaintenance() {
    $.ajax({
        url: "v1/repair-management-api/get-by-status/ACTIVE",
        type: "GET",
        data: {},
        success: function(data) {
            var dataList = data.payload[0];
            if (dataList != null) {
                $('#repair_div').empty();

                $.each(dataList, function(key, value) {
                    $('#repair_div').append(' <div class="col-4"> <input type="checkbox" id="repairTypeId" name="repairTypeNa" value="'+value.id +'" />  ' +
                        '<label> ' + value.repairName+ ' </label> </div>');
                });
            }
        },
        error: function(xhr) {
            toastr.error("Getting data for user role failed.");
        }
    });
}

function setVehicleNumber(data) {
    $.ajax({
        url: "v1/customer-management-api/"+data,
        type: "GET",
        data: {},
        success: function(data) {
            if (data.status && data.payload != null) {
                $('#vehicleNoId').val(data.payload[0].vehicleNo);
            }
        },
        error: function(xhr) {
            toastr.error("Getting data vehicle number failed.");
        }
    });
}