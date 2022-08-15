var editable=false;

$(document).ready(function() {

    loadTimePeriodTable();
    $("#setTimePeriodBtn").click(function(e) {
        clearTimePeriod();
        $('#timePeriodPnl').collapse('show');
    });
});

function resetTimePeriod(){
    $('#timePeriodPnl').collapse('hide');
}

function saveTimePeriod() {
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    editable=false;
    var userObj={
        "id":$('#timePeriodId').val() == "" ? "" : $('#timePeriodId').val(),

        "timeTo":$('#timeToId').val(),
        "timeFrom":$('#timeFromId').val(),
        "editable":editable,
        "commonStatus":$('#timePeriodStatusId').val() == "-1" ? null : $('#timePeriodStatusId').val(),
    }

    $.ajax({
        url: "v1/time-period-management-api/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadTimePeriodTable()
                clearTimePeriod()
                toastr.success("Successfully saved.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}
function updateTimePeriod(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#timePeriodId').val(),
        "timeTo":$('#timeToId').val(),
        "timeFrom":$('#timeFromId').val(),
        "editable":editable,
        "commonStatus":$('#timePeriodStatusId').val() == "-1" ? null : $('#timePeriodStatusId').val(),
    }

    $.ajax({
        url:"v1/time-period-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadTimePeriodTable()
                clearTimePeriod()
                toastr.success("Successfully updated");
                $('#timePeriodPnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function getByIdForEditTimePeriod(data) {

    var url = "v1/time-period-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setTimePeriodData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}
function  setTimePeriodData(data) {
    if(data.status && data.payload != null) {
        editable=true;
        $('#timePeriodPnl').collapse('show');
        $('#timePeriodUpdateBtnId').prop('disabled', false);
        $('#timePeriodSaveBtnId').prop('disabled', true);
        $('#timePeriodId').val(data.payload[0].id);
        $('#timeToId').val(data.payload[0].timeTo);
        $('#timeFromId').val(data.payload[0].timeFrom);
        $('#timePeriodStatusId').val(data.payload[0].commonStatus);
    }
}
function loadTimePeriodTable(){
    $.ajax({
        url: "v1/time-period-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setTimePeriodTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}
function setTimePeriodTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#timePeriodTable').DataTable().clear();
        $('#timePeriodTable').DataTable({
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
        $("#timePeriodTable").DataTable({
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
                    "data": "timeCode"
                },
                {
                    "data": "timeTo"
                },
                {
                    "data": "timeFrom"
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
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditTimePeriod(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteTimePeriodConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}
function deleteTimePeriodConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteTimePeriod(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}
function deleteTimePeriod(data) {
    $.ajax({
        url:  "v1/time-period-management-api/"+data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadTimePeriodTable();
                clearTimePeriod();
                toastr.success("Time Period Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Time Period Deleted failed.");
        }
    });
}
function clearTimePeriod(){
    $('#timePeriodId').val('');
    $('#timeToId').val('');
    $('#timeFromId').val('')
    $('#timePeriodStatusId').val('-1');
}