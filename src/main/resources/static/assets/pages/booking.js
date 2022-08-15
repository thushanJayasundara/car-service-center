$(document).ready(function () {
    clearPersonField();
    $("#addNewPersonBtn").click(function (e) {
        clearPersonField();
        $('#personPnl').collapse('show');
    });

});

function savePerson() {
    var gender;

    if (document.getElementById('personMaleId').checked) {
        gender = $("input[name='personMale']:checked").val();
    } else if (document.getElementById('personFemaleId').checked) {
        gender = $("input[name='personFemale']:checked").val();
    }

    var personDTO = {
        "id": $('#personId').val() == "" ? "" : $('#personId').val(),
        "fullName": $('#personFullNameId').val(),
        "initials": $('#personInitialId').val(),
        "firstName": $('#personFirstNameId').val(),
        "lastName": $('#personLastNameId').val(),
        "dob": $('#personDOBId').val() + " 00:00",
        "nic": $('#personNicId').val(),
        "address": $('#personAddressId').val(),
        "gramaNiladhariDivision": {"id": $('#personGNDivisionId').val()},
        "gender": gender,
        "contactNumber": $('#personMobileId').val(),
        "commonStatus": $('#personStatusId').val() == "-1" ? null : $('#personStatusId').val(),
    }

    $.ajax({
        url: "/api/erp/person-management/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType: "json",
        data: JSON.stringify(personDTO),
        success: function (data) {
            if (data.status) {
                toastr.success("Successfully save.");
                loadPersonTable();
                clearPersonField();
                $('#personPnl').collapse('hide');
            } else {
                $.each(data.errorMessages, function (key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}

function getPersonByAdvancedSearch() {
    $('#personPnl').collapse('hide');
    var divisionId = $.trim($('#searchPersonDivisionId').val());
    var search_key = $.trim($('#searchPersonId').val());
    search_key = search_key.length == 0 ? '-1' : search_key;
    var url = '/api/erp/person-management/get-by-search/' + divisionId + '/' + search_key;
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function (data) {
            if (data.status && data.payload != null) {
                setPersonTable(data.payload[0])
            } else {
                toastr.error("No Recode Found");
            }
        }
    });

}

function searchPerson() {
    $('#personPnl').collapse('show');
    var search_person = $('#searchPersonId').val()
    var url = "/api/erp/person-management/get-by-nic/" + search_person
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function (data) {
            if (data.status && data.payload != null) {
                setPersonData(data);
            } else {
                toastr.error("No Recode Found");
            }
        }
    });
}

function getByIdForEditPerson(data) {
    $('#personPnl').collapse('show');
    var url = "/api/erp/person-management/" + data
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        success: function (data) {
            if (data.status && data.payload != null) {
                setPersonData(data)
            } else {
                toastr.error("No Recode Found");
            }
        }
    });
}

function loadPersonTable() {
    $.ajax({
        url: "v1/booking-management-api/",
        type: "GET",
        data: {},
        success: function (data) {
            if (data.status) {
                setPersonTable(data.payload[0]);
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function setPersonTable(data) {
    console.log(data)
    if ($.isEmptyObject(data)) {
        $('#personTable').DataTable().clear();
        $('#personTable').DataTable({
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
        $("#personTable").DataTable({
            dom: 'Bfrtip',
            lengthMenu: [
                [10, 25, 50, 100],
                ['10', '25', '50', '100']
            ],
            buttons: [{
                extend: 'pageLength'
            },
                {
                    extend: 'pdf',
                    title: 'Person Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'excel',
                    title: 'Person Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                },
                {
                    extend: 'print',
                    title: 'Person Details',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':not(:last-child)'
                    }
                }, {
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
                "data": "slotDTO.slotName"
            },
                {
                    "data": "slotDTO.timePeriod.timeCode",
                    mRender: function (data) {
                        var classLb = ''
                        if (data == null)
                            classLb = 'Not Select'
                        else
                            classLb = data
                        var columnHtml = '<td>' + classLb + '</td>';
                        return (columnHtml);
                    }
                },
                {
                    "data": "slotDTO.maintenanceType.maintenanceName"
                },
                {
                    "data": "slotDTO.commonStatus",
                    mRender: function (data) {
                        var classLb = ''
                        if (data == "ACTIVE")
                            classLb = 'badge bg-success'
                        if (data == "INACTIVE")
                            classLb = 'badge bg-primary'
                        var columnHtml = '<td><label class="' + classLb + '">' + data + '</label></td>';
                        return (columnHtml);
                    }
                }
            ]
        });
    }
}

function setPersonData(data) {
    if (data.status) {
        $('#personId').val(data.payload[0].id);
        $('#personFullNameId').val(data.payload[0].fullName);
        $('#personInitialId').val(data.payload[0].initials);
        $('#personFirstNameId').val(data.payload[0].firstName);
        $('#personLastNameId').val(data.payload[0].lastName);
        $('#personDOBId').val(data.payload[0].dob);
        $('#personNicId').val(data.payload[0].nic);
        $('#personAddressId').val(data.payload[0].address);
        $('#personGNDivisionId').val(data.payload[0].gramaNiladhariDivision.id);
        if (data.payload[0].gender === "MALE")
            $("#personMaleId").prop("checked", true);
        else
            $("#personFemaleId").prop("checked", true);

        $('#personMobileId').val(data.payload[0].contactNumber);
        $('#personStatusId').val(data.payload[0].commonStatus);
    }
}

function deletePersonConfirm(data) {
    var columnHtml = '<br> <br> <button value="' + data + '" onclick="deletePerson(value)" type="button" class="btn btn-gradient-warning btn-sm">Yes</button>' +
        '&#160&#160&#160&#160&#160&#160&#160<button type="button" class="btn btn-gradient-warning btn-sm">No</button>'
    toastr["warning"]("Are you sure you want to delete this item?" + columnHtml);
}

function deletePerson(data) {
    $.ajax({
        url: '/api/erp/person-management/' + data,
        type: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        success: function (data) {
            if (data.status) {
                loadPersonTable();
                clearPersonField();
                toastr.success("Person Deleted Successfully.");
            } else {
                toastr.error(data.errorMessages);
                return;
            }
        },
        error: function (xhr) {
            toastr.error("Deleting Privilege failed.");
        }
    });
}

function getAllG_NDivisionForPerson() {
    $.ajax({
        url: "/api/erp/grama-niladhari-division-management/get-active-division",
        type: "GET",
        data: {},
        success: function (data) {
            var dataList = data.payload[0];
            if (dataList != null) {
                $('.personDivision').empty();
                $('.personDivision').append('<option value="-1">--Select Division--</option>');
                $.each(dataList, function (key, value) {
                    $('.personDivision').append(' <option value="' + value.id + '">' + value.gramaNiladhariDivision + '</option>');
                });
                $('.personDivision').val('-1');
            } else {
                toastr.error(data.errorMessages);
            }
        }
    });
}

function maleRadio() {
    if (document.getElementById('personMaleId').checked) {
        $("#personFemaleId").prop("checked", false);
    }
}

function femaleRadio() {
    if (document.getElementById('personFemaleId').checked) {
        $("#personMaleId").prop("checked", false);
    }
}

function resetPerson() {
    clearPersonField();
    $('#personPnl').collapse('hide');
}

function clearPersonField() {
    $('#personInitialId').val('');
    $('#personFullNameId').val('');
    $('#personFirstNameId').val('');
    $('#personLastNameId').val('');
    $('#personDOBId').val('');
    $('#personNicId').val('');
    $('#personAddressId').val('');
    $('#personFemaleId').prop("checked", false);
    $('#personMaleId').prop("checked", false);
    $('#personStatusId').val('-1');
    $('#search_person').val('');
    $('#personMobileId').val('');
    getAllG_NDivisionForPerson();
    loadPersonTable();
}
