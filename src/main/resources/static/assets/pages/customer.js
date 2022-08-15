var editable=false;


$(document).ready(function() {
    loadCustomerTable();
    $("#addNewCustomerBtn").click(function(e) {
        clearCustomer();
        $('#customerPnl').collapse('show');
    });
});

function saveCustomer() {
    editable = false;
    var obj = {
        "id": $('#customerId').val() == "" ? "" : $('#customerId').val(),
        "firstName":$('#firstNameId').val(),
        "lastName":$('#lastNameId').val(),
        "address":$('#addressId').val(),
        "email":$('#emailId').val(),
        "mobile":$('#mobileNumberId').val(),
        "vehicleNo":$('#vehicleNoId').val(),
        "nic":$('#nicId').val(),
        "editable":editable,
        "commonStatus":$('#customerStatusId').val() == "-1" ? null : $('#customerStatusId').val(),
    }

    $.ajax({
        url: "v1/customer-management-api/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(obj),
        success: function(data) {
            if(data.status) {
                loadCustomerTable();
                clearCustomer();
                toastr.success("Successfully save.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}
function  updateCustomer(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#customerId').val(),
        "firstName":$('#firstNameId').val(),
        "lastName":$('#lastNameId').val(),
        "address":$('#addressId').val(),
        "email":$('#emailId').val(),
        "mobile":$('#mobileNumberId').val(),
        "vehicleNo":$('#vehicleNoId').val(),
        "nic":$('#nicId').val(),
        "editable":editable,
        "commonStatus":$('#customerStatusId').val() == "-1" ? null : $('#customerStatusId').val(),
    }

    $.ajax({
        url: "v1/customer-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                loadCustomerTable();
                clearCustomer();
                toastr.success("Successfully updated");
                $('#customerPnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function searchCustomerByNic() {

    var personNic = $.trim($('#searchCustomerId').val());
    var url = "v1/customer-management-api/nic/" +personNic

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status && data.payload != null) {
               setCustomerData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}

function getByIdForEditCustomer(data) {

    var url = "v1/customer-management-api/"+data

    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setCustomerData(data);
            } else {
                toastr.error("No recode found");
            }
        }
    });
}

function setCustomerData(data) {
    if(data.status && data.payload != null) {
        editable=true;
        $('#customerPnl').collapse('show');
        $('#customerUpdateBtnId').prop('disabled', false);
        $('#customerSaveBtnId').prop('disabled', true);
        $('#customerId').val(data.payload[0].id);
        $('#firstNameId').val(data.payload[0].firstName);
        $('#lastNameId').val(data.payload[0].lastName);
        $('#addressId').val(data.payload[0].address);
        $('#mobileNumberId').val(data.payload[0].mobile);
        $('#emailId').val(data.payload[0].email);
        $('#vehicleNoId').val(data.payload[0].vehicleNo);
        $('#nicId').val(data.payload[0].nic);
        $('#customerStatusId').val(data.payload[0].commonStatus);
    }
}

function loadCustomerTable() {
    $.ajax({
        url: "v1/customer-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setCustomerTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setCustomerTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#customerTable').DataTable().clear();
        $('#customerTable').DataTable({
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
        $("#customerTable").DataTable({
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
                    title: 'Customer Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'excel',
                    title: 'Customer Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'print',
                    title: 'Customer Details',
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
                    "data": "firstName"
                },
                {
                    "data": "lastName"
                },
                {
                    "data": "address"
                },
                {
                    "data": "email"
                },
                {
                    "data": "mobile"
                },
                {
                    "data": "vehicleNo"
                },
                {
                    "data": "nic"
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
                            '    <button type="button" value="'+data+'" onclick="getByIdForEditCustomer(value)" ' +
                            'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                            '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                            'onclick="deleteCustomerConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                        return (columnHtml);
                    }
                }]
        });
    }
}

function deleteCustomerConfirm(data){
    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteCustomer(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteCustomer(data) {
    $.ajax({
        url:"v1/customer-management-api/"+ data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function(data) {
            if (data.status) {
                loadCustomerTable();
                clearCustomer();
                toastr.success("Customer Deleted Successfully.");
            }else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function(xhr) {
            toastr.error("Customer Deleted failed.");
        }
    });
}

function resetGNDivision() {
    $('#customerPnl').collapse('hide');
}

function clearCustomer() {
    $('#firstNameId').val('');
    $('#lastNameId').val('');
    $('#addressId').val('');
    $('#emailId').val('');
    $('#mobileNumberId').val('');
    $('#vehicleNoId').val('');
    $('#nicId').val('');
    $('#divisionCodeId').val('');
    $('#customerStatusId').val('-1');
    $('#searchDivisionId').val('');
    $('#customerUpdateBtnId').prop('disabled', true);
    $('#customerSaveBtnId').prop('disabled', false);
}

