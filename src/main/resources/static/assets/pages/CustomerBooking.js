$(document).ready(function() {
    getAvailableSlot();
});

function getAvailableSlot(){
    $.ajax({
        url: "v1/slot-management-api/get-by-availability/AVAILABLE",
        type: "GET",
        data: {},
        success: function(data) {
            var slots = data.payload[0]
            if(data.status) {
                $('#timeSlotId_div').empty();
                $.each(slots, function(key, value) {
                    var timePeriod =  value.timePeriod !== null ? value.timePeriod.timeCode : "Any time"
                    $('#timeSlotId_div').append('<div class="col-lg-4">\n' +
                        '                        <div class="info-box">\n' +
                        '                            <span class="info-box-icon bg-success"><i class="far fa-flag"></i></span>\n' +
                        '\n' +
                        '                            <div class="info-box-content">\n' +
                        '                                <span class="info-box-text">Slot Number : '+value.slotName+'</span>\n' +
                        '                                <span class="info-box-number">Available Time : '+timePeriod+'</span>\n' +
                        '                                <span class="info-box-number">Service Type : '+value.maintenanceType.maintenanceName+'</span>\n' +
                        ' <button class="btn btn-primary btn-xs" id="customerSaveBtnId" onclick="reserveSlot('+value.id+')" type="button">\n' +
                        '                                    Reserve\n' +
                        '                                </button>\n'+
                        '                            </div>\n' +
                        '                            <!-- /.info-box-content -->\n' +
                        '                        </div>\n' +
                        '                        <!-- /.info-box -->\n' +
                        '                    </div>');
                });
            }
        },
        error: function(xhr) {
            toastr.error("Getting data slot failed.");
        }
    });
}

function reserveSlot(data){

    var reserveObj = {
        "id": "",
        "slotDTO": {
            "id": data
        },
        "customerDTO": {
            "nic": $('#customerNicId').val()
        }
    }

    $.ajax({
        url: "v1/booking-management-api/",
        type: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        dataType:"json",
        data: JSON.stringify(reserveObj),
        success: function(data) {
            if(data.status) {
                getAvailableSlot();
                toastr.success("Successfully reserved.");
            }else {
                $.each(data.errorMessages, function(key, value) {
                    toastr.error(value);
                });
            }
        }
    });
}