window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

const setup = function (data, successAction, error = null) {
    $.ajax({
        type: "POST",
        dataType: "json",
        data: data,
        success: function (response) {
            successAction(response);
            if (response.error) {
                error.text(response.error);
            }
            if (response.redirect) {
                location.href = response.redirect;
            }
        }
    });
}
