$(document).ready(function () {
  $("#status-available").click(function () {
    setUserAvailability("available");
  });

  $("#status-away").click(function () {
    setUserAvailability("away");
  });

  $("#status-busy").click(function () {
    setUserAvailability("busy");
  });

  function setUserAvailability(newAvailability) {
    $.ajax({
      type: "PUT",
      url: "users/availability/" + newAvailability,
      success: function (userAvailability) {
        var username = userAvailability.username;
        var availability = userAvailability.availability;

        setAvailabilityClass(username, availability);
      },
      error: function () {
        console.log("Error changing status");
      },
    });
  }

  function setAvailabilityClass(username, availability) {
    $("#" + username).attr("class", "user " + availability);

    var newClass =
      availability === "available"
        ? "fa fa-circle fa-xs"
        : availability === "away"
        ? "fa fa-circle-o fa-xs"
        : "fa fa-circle-o-notch fa-xs";

    $("#" + username + " i:first-child").attr("class", newClass);
  }
});
