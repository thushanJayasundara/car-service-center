var editable=false;

$(document).ready(function() {
    loadUserTable();
    $("#addNewUserBtn").click(function(e) {
        clearUserField();
        $('#userPnl').collapse('show');
        $('#resetPasswordPnl').collapse('hide');
    });
    $("#passwordUpdateBtn").click(function(e) {
        clearResetPassword();
        $('#userPnl').collapse('hide');
        $('#resetPasswordPnl').collapse('show');
    });
});

function  saveUser(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var password = $('#passwordId').val();
    var confirmPassword = $('#confirmPasswordId').val();


    if (password == "" || confirmPassword == ""){
        toastr.error("Please enter password");
        return;
    }

    if(password !== confirmPassword) {
        toastr.error("Invalid password");
        return;
    }

    var userObj={
        "id":$('#userId').val() == "" ? "" : $('#userId').val(),
        "userName":$('#userNameId').val(),
        "password":confirmPassword,
        "userRole":$('#userRoleId').val() == "-1" ? null : $('#userRoleId').val(),
        "editable":editable,
        "commonStatus":$('#userStatusId').val() == "-1" ? null : $('#userStatusId').val(),
    }

    $.ajax({
        url: "v1/user-management-api/",
        type: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                clearUserField();
                loadUserTable();
                toastr.success("Successfully saved.");
                $('#userPnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}
function  updateUser(){
    if ($('#status').val() == "-1") {
        toastr.error("Please Select a Status");
        return;
    }

    var userObj={
        "id":$('#userId').val(),
        "userName":$('#userNameId').val(),
        "userRole":$('#userRoleId').val() == "-1" ? null : $('#userRoleId').val(),
        "editable":editable,
        "commonStatus":$('#userStatusId').val() == "-1" ? null : $('#userStatusId').val(),
    }

    $.ajax({
        url: "v1/user-management-api/",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                clearUserField();
                loadUserTable();
                toastr.success("Successfully saved.");
                $('#userPnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function resetUserPassword() {
    var password = $('#RPasswordId').val();
    var confirmPassword = $('#RPConfirmPasswordId').val();

    if (password == "" || confirmPassword == ""){
        toastr.error("Please enter password");
        return;
    }

    if(password !== confirmPassword) {
        toastr.error("Invalid password");
        return;
    }

    var userObj={
        "id":'',
        "userName":$('#RPUserNameId').val(),
        "password":password,
        "userRole": null,
        "editable":editable,
        "commonStatus": null,
    }

    $.ajax({
        url:"v1/user-management-api/updatePassword",
        type: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(userObj),
        success: function(data) {
            if(data.status) {
                $('#resetPasswordPnl').collapse('hide');
                loadUserTable();
                toastr.success("Successfully Update password.");
            } else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function searchUser() {
    var userName = $('#searchUserId').val()

    if(userName === "") {
        toastr.error("Enter user name");
        return;
    }

    var url ="v1/user-management-api/get-by-username/"+userName
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setUserData(data);
            } else {
                toastr.error("No Recode Found");
            }
        }
    });
}5

function getByIdForEditUser(data) {
    var url = "v1/user-management-api/"+data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setUserData(data);
            } else {
                toastr.error("No Recode Found");
            }
        }
    });
}

function setUserData(data) {
    if(data.status && data.payload != null) {
        editable=true;
        $('#userPnl').collapse('show');
        $('#updatePasswordPnl').collapse('hide');
        $('#passwordId').prop('disabled', true);
        $('#confirmPasswordId').prop('disabled', true);
        $('#updateBtnId').prop('disabled', false);
        $('#userId').val(data.payload[0].id);
        $('#userNameId').val(data.payload[0].userName);
        $('#userRoleId').val(data.payload[0].userRole);
        $('#userStatusId').val(data.payload[0].commonStatus);

    }
}

function deleteUserConfirm(data){

    var columnHtml = '<br> <br> <button value="'+data+'" onclick="deleteUser(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>'+
                        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?"+columnHtml);
}

function deleteUser(data) {

        $.ajax({
            url:'v1/user-management-api/' + data,
            type: "DELETE",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            success: function(data) {
                if (data.status) {
                    loadUserTable();
                    clearUserField();
                    toastr.success("User Deleted Successfully.");
                }else {
                    toastr.error(data.errorMessages);
                    return;
                }

            },
            error: function(xhr) {
                toastr.error("Deleting User failed.");
            }
        });

}

function loadUserTable() {
    $.ajax({
        url:"v1/user-management-api/",
        type: "GET",
        data: {},
        success: function(data) {
            if(data.status) {
                setUserTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}


function setUserTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#userTable').DataTable().clear();
        $('#userTable').DataTable({
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
        $("#userTable").DataTable({
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
                    title: 'User Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'excel',
                    title: 'User Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'print',
                    title: 'User Details',
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
                "columns": [{
                    "data": "userName"
                },
                {
                     "data": "userRole"
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
                },
                    {
                        "data": "id",
                        mRender: function(data) {
                            var columnHtml = '<div class="btn-group">\n' +
                                '    <button type="button" value="'+data+'" onclick="getByIdForEditUser(value)" ' +
                                'class="btn btn-warning"> <i class="fas fa-edit"></i> </button>' +
                                '    <button type="button" value="'+data+'" class="btn btn-danger" ' +
                                'onclick="deleteUserConfirm(value)"> <i class="fas fa-trash-alt"></i> </button></div>';
                            return (columnHtml);
                        }
                    }
                ]
        });
    }
}

function resetUser() {
    $('#userPnl').collapse('hide');
    $('#resetPasswordPnl').collapse('hide');
}

function clearUserField() {
    editable=false;
    $('#userId').val('');
    $('#userNameId').val('');
    $('#passwordId').val('');
    $('#confirmPasswordId').val('');
    $('#userRoleId').val('-1');
    $('#userStatusId').val('-1');
    $('#searchUserId').val('');
    $('#updateBtnId').prop('disabled', true);
    $('#passwordId').prop('disabled', false);
    $('#confirmPasswordId').prop('disabled', false);
}

function clearResetPassword() {
    $('#RPUserNameId').val('');
    $('#RPasswordId').val('');
    $('#RPConfirmPasswordId').val('');
}
